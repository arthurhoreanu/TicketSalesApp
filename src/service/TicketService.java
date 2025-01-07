package service;

import exception.BusinessLogicException;
import exception.EntityNotFoundException;
import exception.ValidationException;
import model.*;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing ticket-related operations.
 */
public class TicketService {

    private final IRepository<Ticket> ticketRepository;
    private final VenueService venueService;

    public TicketService(RepositoryFactory repositoryFactory, VenueService venueService) {
        this.ticketRepository = repositoryFactory.createTicketRepository();
        this.venueService = venueService;
    }

    /**
     * Generates tickets for an event based on its available seats or venue capacity.
     *
     * @param event        the event for which tickets are generated.
     * @param basePrice    the base price for EARLY_BIRD tickets.
     * @param earlyBirdCount the number of EARLY_BIRD tickets.
     * @param vipCount     the number of VIP tickets.
     * @param standardCount the number of STANDARD tickets.
     * @return the list of generated tickets.
     */
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice, int earlyBirdCount, int vipCount, int standardCount) {
        Venue venue = venueService.findVenueByID(event.getVenueID());
        if (venue == null) {
            throw new EntityNotFoundException("Venue not found for the event.");
        }

        List<Ticket> allTickets = new ArrayList<>();
        if (venue.isHasSeats()) {
            // Generate tickets for venues with seats
            List<Seat> availableSeats = venueService.getAvailableSeatsInVenue(event.getVenueID(), event.getID());
            if (availableSeats.size() < (earlyBirdCount + vipCount + standardCount)) {
                throw new BusinessLogicException("Not enough available seats to generate tickets.");
            }

            allTickets.addAll(generateTicketsWithSeats(availableSeats.subList(0, earlyBirdCount), event, basePrice, TicketType.EARLY_BIRD));
            allTickets.addAll(generateTicketsWithSeats(availableSeats.subList(earlyBirdCount, earlyBirdCount + vipCount), event, basePrice * 1.5, TicketType.VIP));
            allTickets.addAll(generateTicketsWithSeats(availableSeats.subList(earlyBirdCount + vipCount, earlyBirdCount + vipCount + standardCount), event, calculateDynamicStandardPrice(basePrice, event.getStartDateTime()), TicketType.STANDARD));
        } else {
            // Generate tickets for venues without seats
            int totalCapacity = venue.getVenueCapacity();
            if (totalCapacity < (earlyBirdCount + vipCount + standardCount)) {
                throw new BusinessLogicException("Not enough capacity in the venue to generate tickets.");
            }

            allTickets.addAll(generateTicketsWithoutSeats(event, basePrice, earlyBirdCount, TicketType.EARLY_BIRD));
            allTickets.addAll(generateTicketsWithoutSeats(event, basePrice * 1.5, vipCount, TicketType.VIP));
            allTickets.addAll(generateTicketsWithoutSeats(event, calculateDynamicStandardPrice(basePrice, event.getStartDateTime()), standardCount, TicketType.STANDARD));
        }

        // Save tickets to repository
        for (Ticket ticket : allTickets) {
            ticketRepository.create(ticket);
        }

        return allTickets;
    }

    private List<Ticket> generateTicketsWithSeats(List<Seat> seats, Event event, double price, TicketType ticketType) {
        return seats.stream()
                .map(seat -> {
                    Ticket ticket = new Ticket(0, event, seat, null, price, ticketType);
                    seat.setTicket(ticket); // Associate ticket with seat
                    venueService.updateSeat(seat); // Persist the seat update
                    return ticket;
                })
                .collect(Collectors.toList());
    }

    private List<Ticket> generateTicketsWithoutSeats(Event event, double price, int count, TicketType ticketType) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tickets.add(new Ticket(0, event, null, null, price, ticketType));
        }
        return tickets;
    }

    private double calculateDynamicStandardPrice(double basePrice, LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();
        long daysToEvent = java.time.Duration.between(now, eventDate).toDays();

        if (daysToEvent > 30) {
            return basePrice * 1.1; // +10%
        } else if (daysToEvent <= 30 && daysToEvent > 7) {
            return basePrice * 1.2; // +20%
        } else if (daysToEvent <= 7 && daysToEvent > 1) {
            return basePrice * 1.5; // +50%
        } else {
            return basePrice * 0.8; // -20% for last-minute
        }
    }

    public void reserveTicket(Ticket ticket, Customer customer) {
        if (ticket.getSeat() != null) {
            venueService.reserveSeat(
                    ticket.getSeat().getID(),
                    ticket.getEvent(),
                    customer,
                    ticket.getPrice(),
                    ticket.getTicketType()
            );
        }

        ticket.setSold(true);
        ticket.setCustomer(customer);
        ticket.setPurchaseDate(LocalDateTime.now());
        updateTicket(ticket);
    }


    public void releaseTicket(Ticket ticket) {
        if (ticket.getSeat() != null) {
            venueService.unreserveSeat(ticket.getSeat().getID());
        }
        ticket.setSold(false);
        ticket.setCustomer(null);
        ticket.setPurchaseDate(null);
        updateTicket(ticket);
    }

    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketRepository.getAll().stream()
                .filter(ticket -> ticket.getEvent().equals(event))
                .collect(Collectors.toList());
    }

    public List<Ticket> getAvailableTicketsForEvent(Event event) {
        return getTicketsByEvent(event).stream()
                .filter(ticket -> !ticket.isSold())
                .collect(Collectors.toList());
    }

    public List<Ticket> findTicketsByCartID(int cartID) {
        return ticketRepository.getAll().stream()
                .filter(ticket -> ticket.getCart() != null && ticket.getCart().getID() == cartID)
                .collect(Collectors.toList());
    }

    public void updateTicket(Ticket ticket) {
        ticket.setCustomer(ticket.getCustomer());
        ticketRepository.update(ticket);
    }

    public Ticket findTicketByID(int ticketID) {
        return ticketRepository.read(ticketID);
    }

    public void deleteTicket(int ticketID) {
        Ticket ticket = findTicketByID(ticketID);
        if (ticket != null) {
            ticketRepository.delete(ticketID);
        }
    }

    public double calculateTotalPrice(List<Ticket> tickets) {
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }

    public List<String> getTicketAvailabilityByType(Event event) {
        List<Ticket> tickets = getTicketsByEvent(event);

        long earlyBirdAvailable = tickets.stream()
                .filter(ticket -> ticket.getTicketType() == TicketType.EARLY_BIRD && !ticket.isSold())
                .count();

        long vipAvailable = tickets.stream()
                .filter(ticket -> ticket.getTicketType() == TicketType.VIP && !ticket.isSold())
                .count();

        long standardAvailable = tickets.stream()
                .filter(ticket -> ticket.getTicketType() == TicketType.STANDARD && !ticket.isSold())
                .count();

        List<String> availability = new ArrayList<>();
        availability.add(earlyBirdAvailable > 0
                ? earlyBirdAvailable + " early bird tickets available"
                : "early bird tickets sold out");

        availability.add(vipAvailable > 0
                ? vipAvailable + " VIP tickets available"
                : "VIP tickets sold out");

        availability.add(standardAvailable > 0
                ? standardAvailable + " standard tickets available"
                : "standard tickets sold out");

        return availability;
    }

    public List<Ticket> getAvailableTicketsByType(Event event, TicketType ticketType) {
        return getTicketsByEvent(event).stream()
                .filter(ticket -> ticket.getTicketType() == ticketType && !ticket.isSold())
                .collect(Collectors.toList());
    }

    public List<Ticket> getTicketsByCustomer(Customer customer) {
        if (customer == null) {
            throw new ValidationException("Customer cannot be null.");
        }
        return ticketRepository.getAll().stream()
                .filter(ticket -> customer.equals(ticket.getCustomer()))
                .collect(Collectors.toList());
    }

}