# Chain of Responsibility Pattern in the Reservation System

## Design Pattern Application, Reasoning, Benefits, and Drawbacks

### Application

The Chain of Responsibility Pattern is applied to order validation by moving validation logic out of [src/main/java/flight/reservation/Customer.java](src/main/java/flight/reservation/Customer.java) into dedicated handlers under [src/main/java/flight/reservation/validation](src/main/java/flight/reservation/validation).
Instead of keeping all validation checks in one method inside `Customer`, `Customer` now builds a validation chain using [CustomerNoFlyListHandler.java](src/main/java/flight/reservation/validation/CustomerNoFlyListHandler.java), [PassengerNoFlyListHandler.java](src/main/java/flight/reservation/validation/PassengerNoFlyListHandler.java), and [FlightCapacityHandler.java](src/main/java/flight/reservation/validation/FlightCapacityHandler.java), coordinated through [ValidationHandler.java](src/main/java/flight/reservation/validation/ValidationHandler.java).
The previously inlined block:

- customer no-fly check
- passenger no-fly check
- capacity check across flights

was removed from `Customer` and distributed into specialized handlers in the chain.

### Reasoning

- Splits a large, mixed-responsibility validation block into small, focused validators.
- Allows validation steps to run sequentially and short-circuit naturally when a rule fails.
- Makes adding/reordering/removing rules possible without editing core customer/order creation logic.

### Benefits

- **Loose coupling:** `Customer` does not need to know rule internals.
- **Easy extensibility:** New validation handlers can be added with minimal changes.
- **Separation of concerns:** Each handler owns one validation rule.
- **Improved testability:** Rules can be tested independently.

### Drawbacks

- **Debugging complexity:** Validation flow spans multiple classes.
- **Execution order sensitivity:** Chain order affects behavior and outcomes.
- **Additional indirection:** More classes are introduced for a simple validation flow.

---

## Applying Validation with Chain of Responsibility

The following structure shows how validation requests flow through handlers until one fails or the chain completes.

![chain of responsibilities class diagram](chain_of_responsibilities_class_diagram.png)

---

## Key Code Snippets

### 1. Handler Base Interface/Abstraction

```java
// ValidationHandler.java
public abstract class ValidationHandler {
	protected ValidationHandler next;

	public ValidationHandler setNext(ValidationHandler next) {
		this.next = next;
		return next;
	}

	public abstract boolean validate(Customer customer, List<String> passengerNames, List<ScheduledFlight> flights);
}
```

---

### 2. Customer No-Fly Validation Handler

```java
// CustomerNoFlyListHandler.java
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
```

---

### 3. Passenger No-Fly Validation Handler

```java
// PassengerNoFlyListHandler.java
public class PassengerNoFlyListHandler extends ValidationHandler {

	@Override
	public boolean validate(Customer customer, List<String> passengerNames, List<ScheduledFlight> flights) {
		boolean anyPassengerOnNoFlyList = passengerNames.stream()
				.anyMatch(passenger -> FlightOrder.getNoFlyList().contains(passenger));

		if (anyPassengerOnNoFlyList) {
			return false;
		}

		if (next != null) {
			return next.validate(customer, passengerNames, flights);
		}

		return true;
	}
}
```

---

### 4. Flight Capacity Validation Handler

```java
// FlightCapacityHandler.java
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
```

---

### 5. Building and Triggering the Chain

```java
// In Customer.java
private ValidationHandler buildValidationChain() {
	ValidationHandler customerHandler = new CustomerNoFlyListHandler();
	ValidationHandler passengerHandler = new PassengerNoFlyListHandler();
	ValidationHandler capacityHandler = new FlightCapacityHandler();

	customerHandler.setNext(passengerHandler).setNext(capacityHandler);

	return customerHandler;
}

private boolean isOrderValid(List<String> passengerNames, List<ScheduledFlight> flights) {
	return validationChain.validate(this, passengerNames, flights);
}
```

---
