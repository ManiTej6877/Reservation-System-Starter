package flight.reservation.validation;

import flight.reservation.Customer;
import flight.reservation.flight.ScheduledFlight;

import java.util.List;

public class FlightCapacityHandler extends ValidationHandler {

    @Override
    public boolean validate(Customer customer, List<String> passengerNames, List<ScheduledFlight> flights) {
        boolean allFlightsHaveCapacity = flights.stream().allMatch(scheduledFlight -> {
            try {
                return scheduledFlight.getAvailableCapacity() >= passengerNames.size();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return false;
            }
        });
        
        if (!allFlightsHaveCapacity) {
            return false;
        }
        
        if (next != null) {
            return next.validate(customer, passengerNames, flights);
        }
        
        return true;
    }
}
