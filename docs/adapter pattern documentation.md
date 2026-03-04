# Adapter Pattern in the Reservation System

## Design Pattern Application, Reasoning, Benefits, and Drawbacks

### Application

The Adapter Pattern is applied in the payment integration boundary by adding [src/main/java/flight/reservation/payment/ExternalPaymentAdapter.java](src/main/java/flight/reservation/payment/ExternalPaymentAdapter.java).
The existing order flow already expects the target interface `PaymentStrategy` in [src/main/java/flight/reservation/order/FlightOrder.java](src/main/java/flight/reservation/order/FlightOrder.java#L27).
`ExternalPaymentAdapter` implements `PaymentStrategy` and wraps `ExternalPaymentGateway`, translating internal payment calls (`pay`, `validate`) into an external-style gateway API (`makePayment`, `canProcess`).

### Reasoning

- Decouples order-processing logic from provider-specific payment APIs.
- Ensures compatibility with external gateways that use different method signatures and data formats.
- Introduces a clear translation layer (for example, converting `double` amount to `long` cents) so core business logic remains unchanged.

### Benefits

- Preserves existing `FlightOrder.processOrder(PaymentStrategy)` behavior with no modification to booking flow.
- Improves extensibility by allowing new gateways to be integrated through the adapter contract.
- Improves maintainability by localizing protocol/format conversion and credential checks in one class.
- Supports interface-driven design and polymorphism (`CreditCard`, `Paypal`, `ExternalPaymentAdapter` all usable via `PaymentStrategy`).

### Drawbacks

- Adds an additional abstraction layer, which can feel heavy for a small codebase.
- Requires defining/maintaining an adapter contract (`ExternalPaymentGateway`) and mapping logic.

---

## Enabling an external gateway using Adapter Pattern

The following diagram shows the initial system which supports only Paypal and Credit Card.
![Adapter Pattern Class Diagram Before](adapter_pattern_class_diagram_before.png)

The following diagram shows how the Adapter pattern enables the addition of an external gateway to the reservation system.  

![Adapter Pattern Class Diagram After](adapter_pattern_class_diagram.png)

## Key Code Snippets

### 1. External Payment Adapter Class

```java
package flight.reservation.payment;

public class ExternalPaymentAdapter implements PaymentStrategy {

    private final ExternalPaymentGateway gateway;
    private final String accountId;
    private final String secret;

    public ExternalPaymentAdapter(ExternalPaymentGateway gateway, String accountId, String secret) {
        this.gateway = gateway;
        this.accountId = accountId;
        this.secret = secret;
    }

    @Override
    public boolean pay(double amount) throws IllegalStateException {
        if (!validate()) {
            return false;
        }
        long amountInCents = Math.round(amount * 100);
        return gateway.makePayment(accountId, secret, amountInCents);
    }

    @Override
    public boolean validate() {
        return gateway != null
                && accountId != null
                && !accountId.isBlank()
                && secret != null
                && !secret.isBlank()
                && gateway.canProcess(accountId, secret);
    }

    public interface ExternalPaymentGateway {
        boolean canProcess(String accountId, String secret);

        boolean makePayment(String accountId, String secret, long amountInCents);
    }
}

```

