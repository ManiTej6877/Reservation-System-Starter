package flight.reservation.flight;

import flight.reservation.Airport;
import flight.reservation.plane.Aircraft;

/**
 * Builder pattern implementation for Flight class
 * Provides a fluent API for constructing Flight objects step-by-step
 */
public class FlightBuilder {
    private int number;
    private Airport departure;
    private Airport arrival;
    private Aircraft aircraft;

    /**
     * Sets the flight number
     * @param number the flight number
     * @return this builder instance for method chaining
     */
    public FlightBuilder number(int number) {
        this.number = number;
        return this;
    }

    /**
     * Sets the departure airport
     * @param departure the departure airport
     * @return this builder instance for method chaining
     */
    public FlightBuilder departure(Airport departure) {
        this.departure = departure;
        return this;
    }

    /**
     * Sets the arrival airport
     * @param arrival the arrival airport
     * @return this builder instance for method chaining
     */
    public FlightBuilder arrival(Airport arrival) {
        this.arrival = arrival;
        return this;
    }

    /**
     * Sets the aircraft
     * @param aircraft the aircraft for this flight
     * @return this builder instance for method chaining
     */
    public FlightBuilder aircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
        return this;
    }

    /**
     * Builds and returns a Flight object with the configured values
     * @return a new Flight instance
     * @throws IllegalArgumentException if the aircraft is not valid for the selected route
     */
    public Flight build() throws IllegalArgumentException {
        return new Flight(this);
    }

    // Package-private getters for Flight class
    int getNumber() {
        return number;
    }

    Airport getDeparture() {
        return departure;
    }

    Airport getArrival() {
        return arrival;
    }

    Aircraft getAircraft() {
        return aircraft;
    }
}
