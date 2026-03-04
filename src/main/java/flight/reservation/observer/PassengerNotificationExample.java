package flight.reservation.observer;

import flight.reservation.Airport;
import flight.reservation.Customer;
import flight.reservation.flight.Flight;
import flight.reservation.flight.Schedule;
import flight.reservation.flight.ScheduledFlight;
import flight.reservation.payment.CreditCard;
import flight.reservation.plane.PassengerPlane;

import java.util.Arrays;
import java.util.Date;

/**
 * Example demonstrating the Observer Pattern for passenger notifications.
 * Shows how passengers receive notifications for flight scheduling and booking confirmation.
 */
public class PassengerNotificationExample {

    public static void main(String[] args) throws Exception {
        System.out.println("=== PASSENGER NOTIFICATION WITH OBSERVER PATTERN ===\n");

        // Setup airports and flight
        Airport berlin = new Airport("Berlin", "BER", "Berlin");
        Airport frankfurt = new Airport("Frankfurt", "FRA", "Frankfurt");
        
        Flight flight = new Flight(
            101, 
            berlin, 
            frankfurt, 
            new PassengerPlane("Airbus A320")
        );

        // Schedule the flight
        Schedule schedule = new Schedule();
        schedule.scheduleFlight(flight, new Date());
        ScheduledFlight scheduledFlight = schedule.searchScheduledFlight(101);

        // Create customer and order with passengers
        Customer customer = new Customer("John Smith", "john@example.com");
        
        System.out.println(">>> Creating booking for passengers...\n");
        
        var order = customer.createOrder(
            Arrays.asList("Alice Johnson", "Bob Williams"), 
            Arrays.asList(scheduledFlight),
            200.0
        );

        // Process payment - this triggers booking confirmation notifications
        System.out.println("\n>>> Processing payment...\n");
        CreditCard card = new CreditCard("1234567890123456", new Date(), "123");
        card.setAmount(200.0);
        
        order.processOrderWithCreditCard(card);

        System.out.println("\n=== NOTIFICATION DEMONSTRATION COMPLETE ===");
        System.out.println("✓ Passengers received flight schedule notifications when order was created");
        System.out.println("✓ Passengers received booking confirmation when payment was processed");
    }
}
