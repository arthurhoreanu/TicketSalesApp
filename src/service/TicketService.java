package service;

import controller.Controller;
import model.*;
import repository.FileRepository;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Service class for managing ticket-related operations.
 */
public class TicketService {

    private final IRepository<Ticket> ticketRepository;
    private final VenueService venueService;

    private static final double VIP_PERCENTAGE_INCREASE = 50.0; // +50% for VIP tickets
    private static final double LAST_MINUTE_DISCOUNT = -20.0; // -20% for last-minute tickets

    public TicketService(RepositoryFactory repositoryFactory, VenueService venueService) {
        this.ticketRepository = repositoryFactory.createTicketRepository();
        this.venueService = venueService;
    }

    /**
     * Generates tickets for an event based on its available seats.
     *
     * @param event     the event for which tickets are generated.
     * @param basePrice the base price for EARLY_BIRD tickets.
     * @return the list of generated tickets.
     */
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice, int earlyBirdCount, int vipCount, int standardCount) {
        Venue venue = venueService.findVenueByID(event.getVenueID());
        if (venue == null) {
            throw new IllegalArgumentException("Venue not found for the event.");
        }

        List<Ticket> allTickets = new ArrayList<>();

        if (venue.isHasSeats()) {
            // Venue with seats
            List<Seat> availableSeats = venueService.getAvailableSeatsInVenue(event.getVenueID(), event.getID());
            if (availableSeats.size() < (earlyBirdCount + vipCount + standardCount)) {
                throw new IllegalArgumentException("Not enough available seats to generate tickets.");
            }

            allTickets.addAll(generateEarlyBirdTickets(availableSeats.subList(0, earlyBirdCount), event, basePrice));
            allTickets.addAll(generateVipTickets(availableSeats.subList(earlyBirdCount, earlyBirdCount + vipCount), event, basePrice));
            allTickets.addAll(generateStandardTickets(availableSeats.subList(earlyBirdCount + vipCount, availableSeats.size()), event, basePrice));
        } else {
            // Venue without seats
            int totalCapacity = venue.getVenueCapacity();
            if (totalCapacity < (earlyBirdCount + vipCount + standardCount)) {
                throw new IllegalArgumentException("Not enough capacity in the venue to generate tickets.");
            }

            allTickets.addAll(generateTicketsWithoutSeats(event, basePrice, earlyBirdCount, TicketType.EARLY_BIRD));
            allTickets.addAll(generateTicketsWithoutSeats(event, basePrice * 1.5, vipCount, TicketType.VIP));
            allTickets.addAll(generateTicketsWithoutSeats(event, calculateDynamicStandardPrice(basePrice, event.getStartDateTime()), standardCount, TicketType.STANDARD));
        }

        // Save all tickets
        for (Ticket ticket : allTickets) {
            ticketRepository.create(ticket);
        }

        return allTickets;
    }

    private List<Ticket> generateTicketsWithoutSeats(Event event, double price, int count, TicketType ticketType) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tickets.add(new Ticket(0, event, null, null, price, ticketType));
        }
        return tickets;
    }

    private List<Ticket> generateEarlyBirdTickets(List<Seat> seats, Event event, double basePrice) {
        return seats.stream()
                .map(seat -> new Ticket(0, event, seat, null, basePrice, TicketType.EARLY_BIRD))
                .collect(Collectors.toList());
    }


    private List<Ticket> generateVipTickets(List<Seat> seats, Event event, double basePrice) {
        double vipPrice = basePrice * (1 + VIP_PERCENTAGE_INCREASE / 100);
        return seats.stream()
                .map(seat -> new Ticket(0, event, seat, null, vipPrice, TicketType.VIP))
                .collect(Collectors.toList());
    }

    private List<Ticket> generateStandardTickets(List<Seat> seats, Event event, double basePrice) {
        double dynamicPrice = calculateDynamicStandardPrice(basePrice, event.getStartDateTime());
        return seats.stream()
                .map(seat -> new Ticket(0, event, seat, null, dynamicPrice, TicketType.STANDARD))
                .collect(Collectors.toList());
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
            return basePrice * (1 + LAST_MINUTE_DISCOUNT / 100); // -20%
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

    private void updateTicket(Ticket ticket) {
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
}