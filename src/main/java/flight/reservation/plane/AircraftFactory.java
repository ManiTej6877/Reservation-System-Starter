package flight.reservation.plane;

public class AircraftFactory {
    public static Aircraft createAircraft(String type, String model) {
        switch (type) {
            case "Passenger Plane":
                return new PassengerPlane(model);
            case "Helicopter":
                return new Helicopter(model);
            case "Passenger Drone":
                return new PassengerDrone(model);
            default:
                throw new IllegalArgumentException(String.format("Aircraft type '%s' is not recognized", type));
        }
    }
}
