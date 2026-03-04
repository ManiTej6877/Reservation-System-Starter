package flight.reservation;

import flight.reservation.observer.PassengerObserver;
import flight.reservation.flight.ScheduledFlight;
import java.util.ArrayList;
import java.util.List;

public class Passenger {

    private final String name;
    private final List<PassengerObserver> observers = new ArrayList<>();

    public Passenger(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Adds an observer to be notified about this passenger's flights.
     */
    public void addObserver(PassengerObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the notification list.
     */
    public void removeObserver(PassengerObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers that a flight has been scheduled.
     */
    public void notifyFlightScheduled(ScheduledFlight flight) {
        for (PassengerObserver observer : observers) {
            observer.onFlightScheduled(this, flight);
        }
    }

    /**
     * Notifies all observers that the booking has been confirmed.
     */
    public void notifyBookingConfirmed(ScheduledFlight flight, double price) {
        for (PassengerObserver observer : observers) {
            observer.onBookingConfirmed(this, flight, price);
        }
    }

}
