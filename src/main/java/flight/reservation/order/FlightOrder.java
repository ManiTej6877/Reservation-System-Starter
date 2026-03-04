package flight.reservation.order;

import flight.reservation.flight.ScheduledFlight;
import flight.reservation.payment.CreditCard;
import flight.reservation.payment.Paypal;
import flight.reservation.payment.PaymentStrategy;

import java.util.Arrays;
import java.util.List;

public class FlightOrder extends Order {
    private final List<ScheduledFlight> flights;
    static List<String> noFlyList = Arrays.asList("Peter", "Johannes");

    public FlightOrder(List<ScheduledFlight> flights) {
        this.flights = flights;
    }

    public static List<String> getNoFlyList() {
        return noFlyList;
    }

    public List<ScheduledFlight> getScheduledFlights() {
        return flights;
    }

    public boolean processOrder(PaymentStrategy paymentStrategy) throws IllegalStateException {
        if (isClosed()) {
            // Payment is already proceeded
            return true;
        }
        // validate payment information
        if (paymentStrategy == null || !paymentStrategy.validate()) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        boolean isPaid = paymentStrategy.pay(this.getPrice());
        if (isPaid) {
            this.setClosed();
            // Notify all passengers about booking confirmation
            notifyPassengersBookingConfirmed();
        }
        return isPaid;
    }

    public boolean processOrderWithCreditCard(CreditCard creditCard) throws IllegalStateException {
        return processOrder(creditCard);
    }

    public boolean processOrderWithPayPal(String email, String password) throws IllegalStateException {
        Paypal paypal = new Paypal(email, password);
        return processOrder(paypal);
    }

    /**
     * Notifies all passengers about their booking confirmation.
     */
    private void notifyPassengersBookingConfirmed() {
        for (var passenger : this.getPassengers()) {
            for (var flight : this.flights) {
                passenger.notifyBookingConfirmed(flight, this.getPrice());
            }
        }
    }

}
