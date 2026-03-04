import flight.reservation.Airport;
import flight.reservation.flight.Schedule;
import flight.reservation.flight.Flight;
import flight.reservation.flight.FlightBuilder;
import flight.reservation.plane.Aircraft;
import flight.reservation.plane.AircraftFactory;

import java.util.Arrays;
import java.util.List;

public class Runner {
    static List<Airport> airports = Arrays.asList(
            new Airport("Berlin Airport", "BER", "Berlin, Berlin"),
            new Airport("Frankfurt Airport", "FRA", "Frankfurt, Hesse"),
            new Airport("Madrid Barajas Airport", "MAD", "Barajas, Madrid"),
            new Airport("Guarulhos International Airport", "GRU", "Guarulhos (São Paulo)"),
            new Airport("John F. Kennedy International Airport", "JFK", "Queens, New York, New York"),
            new Airport("Istanbul Airport", "IST", "Arnavutköy, Istanbul"),
            new Airport("Dubai International Airport", "DXB", "Garhoud, Dubai"),
            new Airport("Chengdu Shuangliu International Airport", "CTU", "Shuangliu-Wuhou, Chengdu, Sichuan")
    );

    static List<Aircraft> aircrafts = Arrays.asList(
        AircraftFactory.createAircraft("Passenger Plane", "A380"),
        AircraftFactory.createAircraft("Passenger Plane", "A350"),
        AircraftFactory.createAircraft("Passenger Plane", "Embraer 190"),
        AircraftFactory.createAircraft("Passenger Plane", "Antonov AN2"),
        AircraftFactory.createAircraft("Helicopter", "H1"),
        AircraftFactory.createAircraft("Passenger Drone", "HypaHype")
    );

    static List<Flight> flights = Arrays.asList(
            new FlightBuilder().number(1).departure(airports.get(0)).arrival(airports.get(1)).aircraft(aircrafts.get(0)).build(),
            new FlightBuilder().number(2).departure(airports.get(1)).arrival(airports.get(2)).aircraft(aircrafts.get(1)).build(),
            new FlightBuilder().number(3).departure(airports.get(2)).arrival(airports.get(4)).aircraft(aircrafts.get(2)).build(),
            new FlightBuilder().number(4).departure(airports.get(3)).arrival(airports.get(2)).aircraft(aircrafts.get(3)).build(),
            new FlightBuilder().number(5).departure(airports.get(4)).arrival(airports.get(2)).aircraft(aircrafts.get(4)).build(),
            new FlightBuilder().number(6).departure(airports.get(5)).arrival(airports.get(7)).aircraft(aircrafts.get(5)).build()
    );

    static Schedule schedule;

    public static void main(String[] args) {
    }
}
