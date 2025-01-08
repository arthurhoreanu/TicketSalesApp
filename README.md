# TicketSalesCompany

## Introduction
The TicketSalesCompany application is your ultimate platform for managing and purchasing event tickets. Whether you're a music enthusiast or a sports fan, our app ensures a seamless and personalized experience, offering features for both admins and customers. From creating events to purchasing tickets, the app covers all aspects of event management.

## Features

### 1. User Management
- **Admin Accounts**: Created automatically when the email domain is corporate (e.g., `admin@tsc.com`).
  - **Methods**: `createAccount(role, username, email, password)`
- **Customer Accounts**: Default account type for non-corporate domains.
- **Account Features**:
  - **Create Account**:
    - **Method**: `handleCreateAccount(scanner, controller)`
  - **Login/Logout**:
    - **Methods**: 
      - `handleLogin(scanner, controller)`
      - `logout()`
  - **Delete Account**:
    - **Method**: `handleDeleteUserAccount(scanner, controller)`

---

### 2. Event Management
Admins can manage two types of events:
- **Concerts**: Include artists as performers.
- **Sports Events**: Feature athletes as participants.

**Event Features**:
- **Statuses**: Events can be `SCHEDULED`, `CANCELLED`, or `COMPLETED`.
  - **Method**: `updateEventStatus(eventId, newStatus)`
- **Venue Assignment**: Events are assigned to specific venues.
  - **Method**: `assignVenueToEvent(eventId, venueId)`
- **Ticket Management**: Generate and manage tickets for each event.
  - **Methods**:
    - `generateTicketsForEvent(event, basePrice, earlyBirdCount, vipCount, standardCount)`
    - `viewTicketsByEvent(eventId)`
    - `deleteTicket(ticketId)`

---

### 3. Venue Management
Admins can:
- Create venues with capacity, location, and seating details.
  - **Method**: `createVenue(name, location, capacity, hasSeats)`
- Add sections, rows, and seats to venues.
  - **Methods**:
    - `addSectionToVenue(venueId, numberOfSections, sectionCapacity, sectionNamePrefix)`
    - `addRowsAndSeatsToSection(sectionId, numberOfRows, seatsPerRow)`
- View and manage full venue structures.
  - **Method**: `viewFullVenueStructure(venueId)`

---

### 4. Tickets
- **Types**: `EARLY_BIRD`, `VIP`, `STANDARD`.
- **Customer Features**:
  - View ticket availability.
    - **Method**: `getAvailableTicketsByType(event, ticketType)`
  - Add tickets to a shopping cart.
    - **Method**: `addTicketToCart(cart, ticket)`
  - Get optional seat recommendations based on the seat/s previously selected.
    - **Method**: `recommendClosestSeat(sectionId, rowId, selectedSeats)`
  - Proceed to checkout and finalize purchase.
    - **Method**: `processPayment(cart, cardNumber, cardholderName, expiryMonth, expiryYear, cvv)`
- **Admin Features**:
  - Generate tickets for events.
    - **Method**: `generateTicketsForEvent(event, basePrice, earlyBirdCount, vipCount, standardCount)`
  - Manage ticket inventory.
    - **Method**: `deleteTicket(ticketId)`
  - View ticket details.
    - **Method**: `viewTicketDetails(ticketId)`

---

### 5. Shopping Cart
Customers can:
- Add or remove tickets.
  - **Methods**:
    - `addTicketToCart(cart, ticket)`
    - `removeTicketFromCart(cart, ticket)`
- View cart summary and total price.
  - **Methods**:
    - `getTicketsInCart(cart)`
    - `calculateTotalPrice(cart)`
- Checkout and process payments securely.
  - **Method**: `processPayment(cart, cardNumber, cardholderName, expiryMonth, expiryYear, cvv)`

---

### 6. Order History
Customers can view:
- Previous orders with event details.
  - **Method**: `getTicketsByCustomer(customer)`
- Ticket specifics, including seat numbers and purchase dates.

---

### 7. Favourites and Recommendations
- Customers can mark artists, athletes, and events as favourites.
  - **Methods**:
    - `addFavourite(entity)`
    - `removeFavourite(entity)`
- **Recommendations** are generated based on favourites:
  - Related artists (same genre).
    - **Method**: `findArtistsByGenre(genre)`
  - Related athletes (same sport).
    - **Method**: `findAthletesBySport(sport)`
  - Upcoming events for favourites.
    - **Method**: `getUpcomingEventsForEntity(entityId)`

---

### 8. Search Functionality
Customers can search for events by:
- **Performer** (artist/athlete).
  - **Methods**:
    - `getUpcomingEventsForArtist(artistId)`
    - `getUpcomingEventsForAthlete(athleteId)`
- **Venue or location**.
  - **Method**: `getEventsByLocation(location)`

---

## Technical Overview

### Architecture
- **Presentation Layer**: Console-based menus for interaction.
- **Controller Layer**: Manages business logic and bridges the user interface with data handling.
- **Repository Layer**: Data persistence handled via `InMemory`, `File`, or `Database` repositories.
- **Service Layer**: Encapsulates application logic for user, event, ticket, and payment management.

### Data Storage Options
- **InMemory**: For lightweight testing and temporary storage.
- **File**: Uses CSV files for persistent storage.
- **Database**: Supports PostgreSQL with Hibernate ORM for scalable data management.

---

## UML
![TicketSalesCompany](https://github.com/user-attachments/assets/4cd99ddd-c14a-4168-ad74-998a19f9391f)
