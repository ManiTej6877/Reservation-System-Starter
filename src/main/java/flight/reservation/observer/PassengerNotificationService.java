package flight.reservation.observer;

import flight.reservation.Passenger;
import flight.reservation.flight.ScheduledFlight;
import java.text.SimpleDateFormat;

/**
 * Concrete observer that sends notifications to passengers.
 * Simple implementation for flight schedule and booking confirmation.
 */
public class PassengerNotificationService implements PassengerObserver {
    
    @Override
    public void onFlightScheduled(Passenger passenger, ScheduledFlight flight) {
        String departureTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(flight.getDepartureTime());
        
        System.out.println("✈️  FLIGHT SCHEDULED NOTIFICATION");
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Flight: " + flight.getNumber());
        System.out.println("From: " + flight.getDeparture().getCode() + " (" + flight.getDeparture().getLocationName() + ")");
        System.out.println("To: " + flight.getArrival().getCode() + " (" + flight.getArrival().getLocationName() + ")");
        System.out.println("Departure: " + departureTime);
        System.out.println("Aircraft: " + flight.getAircraft().getType());
        System.out.println("---");
    }
    
    @Override
    public void onBookingConfirmed(Passenger passenger, ScheduledFlight flight, double price) {
        String departureTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(flight.getDepartureTime());
        
        System.out.println("✅ BOOKING CONFIRMATION");
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Flight: " + flight.getNumber());
        System.out.println("From: " + flight.getDeparture().getCode() + " to " + flight.getArrival().getCode());
        System.out.println("Departure: " + departureTime);
        System.out.println("Booking Price: $" + price);
        System.out.println("Status: CONFIRMED");
        System.out.println("---");
    }
}
