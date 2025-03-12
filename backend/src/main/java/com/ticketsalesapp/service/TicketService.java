//package com.ticketsalesapp.service;
//
//import lombok.RequiredArgsConstructor;
//import com.ticketsalesapp.exception.BusinessLogicException;
//import com.ticketsalesapp.exception.EntityNotFoundException;
//import com.ticketsalesapp.exception.ValidationException;
//import com.ticketsalesapp.model.event.Event;
//import com.ticketsalesapp.model.ticket.Ticket;
//import com.ticketsalesapp.model.ticket.TicketType;
//import com.ticketsalesapp.model.user.Customer;
//import com.ticketsalesapp.model.venue.Seat;
//import com.ticketsalesapp.model.venue.Venue;
//import com.ticketsalesapp.repository.Repository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * Service class for managing ticket-related operations.
// */
//@Service
//@RequiredArgsConstructor
//public class TicketService {
//
//    private final Repository<Ticket> ticketRepository;
//    private final VenueService venueService;
//
//    /**
//     * Generates tickets for an event based on its available seats or venue capacity.
//     *
//     * @param event        the event for which tickets are generated.
//     * @param basePrice    the base price for EARLY_BIRD tickets.
//     * @param earlyBirdCount the number of EARLY_BIRD tickets.
//     * @param vipCount     the number of VIP tickets.
//     * @param standardCount the number of STANDARD tickets.
//     * @return the list of generated tickets.
//     */
//    public List<Ticket> generateTicketsForEvent(Event event, double basePrice, int earlyBirdCount, int vipCount, int standardCount) {
//        Optional<Venue> venue = venueService.findVenueByID(event.getVenueID());
//        if (venue.isEmpty()) {
//            throw new EntityNotFoundException("Venue not found for the event.");
//        }
//
//        List<Ticket> allTickets = new ArrayList<>();
//        if (venue.get().isHasSeats()) {
//            // Generate tickets for venues with seats
//            List<Seat> availableSeats = venueService.getAvailableSeatsInVenue(event.getVenueID(), event.getId());
//            if (availableSeats.size() < (earlyBirdCount + vipCount + standardCount)) {
//                throw new BusinessLogicException("Not enough available seats to generate tickets.");
//            }
//
//            allTickets.addAll(generateTicketsWithSeats(availableSeats.subList(0, earlyBirdCount), event, basePrice, TicketType.EARLY_BIRD));
//            allTickets.addAll(generateTicketsWithSeats(availableSeats.subList(earlyBirdCount, earlyBirdCount + vipCount), event, basePrice * 1.5, TicketType.VIP));
//            allTickets.addAll(generateTicketsWithSeats(availableSeats.subList(earlyBirdCount + vipCount, earlyBirdCount + vipCount + standardCount), event, calculateDynamicStandardPrice(basePrice, event.getStartDateTime()), TicketType.STANDARD));
//        } else {
//            // Generate tickets for venues without seats
//            int totalCapacity = venue.get().getVenueCapacity();
//            if (totalCapacity < (earlyBirdCount + vipCount + standardCount)) {
//                throw new BusinessLogicException("Not enough capacity in the venue to generate tickets.");
//            }
//
//            allTickets.addAll(generateTicketsWithoutSeats(event, basePrice, earlyBirdCount, TicketType.EARLY_BIRD));
//            allTickets.addAll(generateTicketsWithoutSeats(event, basePrice * 1.5, vipCount, TicketType.VIP));
//            allTickets.addAll(generateTicketsWithoutSeats(event, calculateDynamicStandardPrice(basePrice, event.getStartDateTime()), standardCount, TicketType.STANDARD));
//        }
//
//        // Save tickets to repository
//        for (Ticket ticket : allTickets) {
//            ticketRepository.create(ticket);
//        }
//
//        return allTickets;
//    }
//
//    /**
//     * Generates tickets with specific seats for an event.
//     *
//     * @param seats     the list of seats to associate with the tickets.
//     * @param event     the event for which tickets are generated.
//     * @param price     the price of the tickets.
//     * @param ticketType the type of the tickets (e.g., EARLY_BIRD, VIP, STANDARD).
//     * @return the list of generated tickets.
//     */
//    private List<Ticket> generateTicketsWithSeats(List<Seat> seats, Event event, double price, TicketType ticketType) {
//        return seats.stream()
//                .map(seat -> {
//                    Ticket ticket = new Ticket(0, event, seat, null, price, ticketType);
//                    seat.setTicket(ticket); // Associate ticket with seat
//                    venueService.updateSeat(seat); // Persist the seat update
//                    return ticket;
//                })
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Generates tickets without specific seats for an event.
//     *
//     * @param event     the event for which tickets are generated.
//     * @param price     the price of the tickets.
//     * @param count     the number of tickets to generate.
//     * @param ticketType the type of the tickets (e.g., EARLY_BIRD, VIP, STANDARD).
//     * @return the list of generated tickets.
//     */
//    private List<Ticket> generateTicketsWithoutSeats(Event event, double price, int count, TicketType ticketType) {
//        List<Ticket> tickets = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            tickets.add(new Ticket(0, event, null, null, price, ticketType));
//        }
//        return tickets;
//    }
//
//    /**
//     * Calculates the dynamic price for STANDARD tickets based on the event date.
//     *
//     * @param basePrice the base price of the ticket.
//     * @param eventDate the date of the event.
//     * @return the dynamically calculated price.
//     */
//    private double calculateDynamicStandardPrice(double basePrice, LocalDateTime eventDate) {
//        LocalDateTime now = LocalDateTime.now();
//        long daysToEvent = java.time.Duration.between(now, eventDate).toDays();
//
//        if (daysToEvent > 30) {
//            return basePrice * 1.1; // +10%
//        } else if (daysToEvent <= 30 && daysToEvent > 7) {
//            return basePrice * 1.2; // +20%
//        } else if (daysToEvent <= 7 && daysToEvent > 1) {
//            return basePrice * 1.5; // +50%
//        } else {
//            return basePrice * 0.8; // -20% for last-minute
//        }
//    }
//
//    /**
//     * Reserves a ticket for a customer.
//     *
//     * @param ticket   the ticket to be reserved.
//     * @param customer the customer reserving the ticket.
//     */
//    public void reserveTicket(Ticket ticket, Customer customer) {
//        if (ticket.getSeat() != null) {
//            venueService.reserveSeat(
//                    ticket.getSeat().getId(),
//                    ticket.getEvent(),
//                    customer,
//                    ticket.getPrice(),
//                    ticket.getTicketType()
//            );
//        }
//
//        ticket.setSold(true);
//        ticket.setCustomer(customer);
//        ticket.setPurchaseDate(LocalDateTime.now());
//        updateTicket(ticket);
//    }
//
//    /**
//     * Releases a reserved ticket.
//     *
//     * @param ticket the ticket to be released.
//     */
//    public void releaseTicket(Ticket ticket) {
//        if (ticket.getSeat() != null) {
//            venueService.unreserveSeat(ticket.getSeat().getId());
//        }
//        ticket.setSold(false);
//        ticket.setCustomer(null);
//        ticket.setPurchaseDate(null);
//        updateTicket(ticket);
//    }
//
//    /**
//     * Retrieves tickets associated with a specific event.
//     *
//     * @param event the event for which tickets are retrieved.
//     * @return a list of tickets for the specified event.
//     */
//    public List<Ticket> getTicketsByEvent(Event event) {
//        return ticketRepository.getAll().stream()
//                .filter(ticket -> ticket.getEvent().equals(event))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Retrieves available (unsold) tickets for a specific event.
//     *
//     * @param event the event for which available tickets are retrieved.
//     * @return a list of available tickets for the specified event.
//     */
//    public List<Ticket> getAvailableTicketsForEvent(Event event) {
//        return getTicketsByEvent(event).stream()
//                .filter(ticket -> !ticket.isSold())
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Finds tickets associated with a specific cart ID.
//     *
//     * @param cartID the ID of the cart.
//     * @return a list of tickets associated with the specified cart ID.
//     */
//    public List<Ticket> findTicketsByCartID(int cartID) {
//        return ticketRepository.getAll().stream()
//                .filter(ticket -> ticket.getCart() != null && ticket.getCart().getId() == cartID)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Updates a ticket in the repository.
//     *
//     * @param ticket the ticket to update.
//     */
//    public void updateTicket(Ticket ticket) {
//        ticket.setCustomer(ticket.getCustomer());
//        ticketRepository.update(ticket);
//    }
//
//    /**
//     * Finds a ticket by its ID.
//     *
//     * @param ticketID the ID of the ticket to find.
//     * @return the ticket with the specified ID, or null if not found.
//     */
//    public Optional<Ticket> findTicketByID(int ticketID) {
//        return ticketRepository.read(ticketID);
//    }
//
//    /**
//     * Deletes a ticket by its ID.
//     *
//     * @param ticketID the ID of the ticket to delete.
//     */
//    public void deleteTicket(int ticketID) {
//        Optional<Ticket> ticket = findTicketByID(ticketID);
//        if (ticket.isPresent()) {
//            ticketRepository.delete(ticketID);
//        }
//    }
//
//    /**
//     * Calculates the total price of a list of tickets.
//     *
//     * @param tickets the list of tickets.
//     * @return the total price of the tickets.
//     */
//    public double calculateTotalPrice(List<Ticket> tickets) {
//        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
//    }
//
//    /**
//     * Retrieves ticket availability information by type for a specific event.
//     *
//     * @param event the event for which ticket availability is retrieved.
//     * @return a list of strings describing the availability of each ticket type.
//     */
//    public List<String> getTicketAvailabilityByType(Event event) {
//        List<Ticket> tickets = getTicketsByEvent(event);
//
//        long earlyBirdAvailable = tickets.stream()
//                .filter(ticket -> ticket.getTicketType() == TicketType.EARLY_BIRD && !ticket.isSold())
//                .count();
//
//        long vipAvailable = tickets.stream()
//                .filter(ticket -> ticket.getTicketType() == TicketType.VIP && !ticket.isSold())
//                .count();
//
//        long standardAvailable = tickets.stream()
//                .filter(ticket -> ticket.getTicketType() == TicketType.STANDARD && !ticket.isSold())
//                .count();
//
//        List<String> availability = new ArrayList<>();
//        availability.add(earlyBirdAvailable > 0
//                ? earlyBirdAvailable + " early bird tickets available"
//                : "early bird tickets sold out");
//
//        availability.add(vipAvailable > 0
//                ? vipAvailable + " VIP tickets available"
//                : "VIP tickets sold out");
//
//        availability.add(standardAvailable > 0
//                ? standardAvailable + " standard tickets available"
//                : "standard tickets sold out");
//
//        return availability;
//    }
//
//    /**
//     * Retrieves available tickets of a specific type for an event.
//     *
//     * @param event      the event for which tickets are retrieved.
//     * @param ticketType the type of tickets to retrieve.
//     * @return a list of available tickets of the specified type for the event.
//     */
//    public List<Ticket> getAvailableTicketsByType(Event event, TicketType ticketType) {
//        return getTicketsByEvent(event).stream()
//                .filter(ticket -> ticket.getTicketType() == ticketType && !ticket.isSold())
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Retrieves tickets associated with a specific customer.
//     *
//     * @param customer the customer whose tickets are retrieved.
//     * @return a list of tickets associated with the customer.
//     * @throws ValidationException if the customer is null.
//     */
//    public List<Ticket> getTicketsByCustomer(Customer customer) {
//        if (customer == null) {
//            throw new ValidationException("Customer cannot be null.");
//        }
//        return ticketRepository.getAll().stream()
//                .filter(ticket -> customer.equals(ticket.getCustomer()))
//                .collect(Collectors.toList());
//    }
//
//}