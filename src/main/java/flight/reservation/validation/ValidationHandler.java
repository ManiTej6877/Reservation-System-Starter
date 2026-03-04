package flight.reservation.validation;

import flight.reservation.Customer;
import flight.reservation.flight.ScheduledFlight;

import java.util.List;

public abstract class ValidationHandler {
    protected ValidationHandler next;

    public ValidationHandler setNext(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public abstract boolean validate(Customer customer, List<String> passengerNames, List<ScheduledFlight> flights);
}
