package flight.reservation.observer;

import flight.reservation.Passenger;
import flight.reservation.flight.ScheduledFlight;

/**
 * Observer interface for passenger notifications.
 * Notifies passengers about flight schedule and confirmation events.
 */
public interface PassengerObserver {
    
    /**
     * Called when a passenger's flight is scheduled.
     */
    void onFlightScheduled(Passenger passenger, ScheduledFlight flight);
    
    /**
     * Called when a passenger's booking is confirmed.
     */
    void onBookingConfirmed(Passenger passenger, ScheduledFlight flight, double price);
}
