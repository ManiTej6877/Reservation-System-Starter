package flight.reservation;

import flight.reservation.flight.ScheduledFlight;
import flight.reservation.observer.PassengerNotificationService;
import flight.reservation.order.FlightOrder;
import flight.reservation.order.Order;
import flight.reservation.validation.CustomerNoFlyListHandler;
import flight.reservation.validation.FlightCapacityHandler;
import flight.reservation.validation.PassengerNoFlyListHandler;
import flight.reservation.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Customer {

    private String email;
    private String name;
    private List<Order> orders;
    private ValidationHandler validationChain;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        this.orders = new ArrayList<>();
        this.validationChain = buildValidationChain();
    }

    private ValidationHandler buildValidationChain() {
        ValidationHandler customerHandler = new CustomerNoFlyListHandler();
        ValidationHandler passengerHandler = new PassengerNoFlyListHandler();
        ValidationHandler capacityHandler = new FlightCapacityHandler();
        
        customerHandler.setNext(passengerHandler).setNext(capacityHandler);
        
        return customerHandler;
    }

    public FlightOrder createOrder(List<String> passengerNames, List<ScheduledFlight> flights, double price) {
        if (!isOrderValid(passengerNames, flights)) {
            throw new IllegalStateException("Order is not valid");
        }
        FlightOrder order = new FlightOrder(flights);
        order.setCustomer(this);
        order.setPrice(price);
        List<Passenger> passengers = passengerNames
                .stream()
                .map(Passenger::new)
                .collect(Collectors.toList());
        order.setPassengers(passengers);
        order.getScheduledFlights().forEach(scheduledFlight -> scheduledFlight.addPassengers(passengers));
        
        // Notify passengers about their flights and add observer
        PassengerNotificationService notificationService = new PassengerNotificationService();
        for (Passenger passenger : passengers) {
            passenger.addObserver(notificationService);
            for (ScheduledFlight flight : flights) {
                passenger.notifyFlightScheduled(flight);
            }
        }
        
        orders.add(order);
        return order;
    }

    private boolean isOrderValid(List<String> passengerNames, List<ScheduledFlight> flights) {
        return validationChain.validate(this, passengerNames, flights);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
