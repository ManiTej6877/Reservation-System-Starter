package flight.reservation.validation;

import flight.reservation.Customer;
import flight.reservation.flight.ScheduledFlight;
import flight.reservation.order.FlightOrder;

import java.util.List;

public class CustomerNoFlyListHandler extends ValidationHandler {

    @Override
    public boolean validate(Customer customer, List<String> passengerNames, List<ScheduledFlight> flights) {
        if (FlightOrder.getNoFlyList().contains(customer.getName())) {
            return false;
        }
        
        if (next != null) {
            return next.validate(customer, passengerNames, flights);
        }
        
        return true;
    }
}
