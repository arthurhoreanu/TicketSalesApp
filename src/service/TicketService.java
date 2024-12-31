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
    private static final int EARLY_BIRD_STOCK = 100; // Number of EARLY_BIRD tickets
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
    public List<Ticket> generateTicketsForEvent(Event event, double basePrice) {
        List<Seat> availableSeats = venueService.getAvailableSeatsInVenue(event.getVenueID(), event.getID());
        List<Ticket> earlyBirdTickets = generateEarlyBirdTickets(availableSeats, event, basePrice);
        List<Ticket> vipTickets = generateVipTickets(availableSeats, event, basePrice);
        List<Ticket> standardTickets = generateStandardTickets(availableSeats, event, basePrice);

        List<Ticket> allTickets = new ArrayList<>();
        allTickets.addAll(earlyBirdTickets);
        allTickets.addAll(vipTickets);
        allTickets.addAll(standardTickets);

        for (Ticket ticket : allTickets) {
            ticketRepository.create(ticket);
        }
        return allTickets;
    }

    private List<Ticket> generateEarlyBirdTickets(List<Seat> availableSeats, Event event, double basePrice) {
        return availableSeats.stream()
                .limit(EARLY_BIRD_STOCK)
                .map(seat -> new Ticket(0, event, seat, null, basePrice, TicketType.EARLY_BIRD))
                .collect(Collectors.toList());
    }

    private List<Ticket> generateVipTickets(List<Seat> availableSeats, Event event, double basePrice) {
        double vipPrice = basePrice * (1 + VIP_PERCENTAGE_INCREASE / 100);
        return availableSeats.stream()
                .filter(seat -> seat.getRow().getRowCapacity() <= 5) // VIP logic
                .map(seat -> new Ticket(0, event, seat, null, vipPrice, TicketType.VIP))
                .collect(Collectors.toList());
    }

    private List<Ticket> generateStandardTickets(List<Seat> availableSeats, Event event, double basePrice) {
        double dynamicPrice = calculateDynamicStandardPrice(basePrice, event.getStartDateTime());
        return availableSeats.stream()
                .skip(EARLY_BIRD_STOCK)
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