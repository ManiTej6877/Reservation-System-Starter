# Strategy Pattern

## Applicability & Reasoning
- Context: `FlightOrder` previously handled payment validation and payment execution details for each payment method directly, creating duplicated logic and tighter coupling.
- Problem addressed: payment flow changes required modifying `FlightOrder`, and adding a new payment option risked code duplication and regressions.
- Why Strategy: the Strategy pattern encapsulates each payment algorithm behind a shared contract (`PaymentStrategy`), so `FlightOrder` only coordinates order state.
- This reduced payment-processing complexity in `FlightOrder` and moved payment-specific rules into `CreditCard` and `Paypal`.

## Before & After Code Snippets
**Before (duplicated payment processing paths in `FlightOrder`)**
```java
public boolean processOrderWithCreditCard(CreditCard creditCard) throws IllegalStateException {
    if (isClosed()) return true;
    if (!cardIsPresentAndValid(creditCard)) {
        throw new IllegalStateException("Payment information is not set or not valid.");
    }
    boolean isPaid = payWithCreditCard(creditCard, this.getPrice());
    if (isPaid) this.setClosed();
    return isPaid;
}

public boolean processOrderWithPayPal(String email, String password) throws IllegalStateException {
    if (isClosed()) return true;
    if (email == null || password == null || !email.equals(Paypal.DATA_BASE.get(password))) {
        throw new IllegalStateException("Payment information is not set or not valid.");
    }
    boolean isPaid = payWithPayPal(email, password, this.getPrice());
    if (isPaid) this.setClosed();
    return isPaid;
}
```

**After (single strategy-based flow + strategy interface)**
```java
public boolean processOrder(PaymentStrategy paymentStrategy) throws IllegalStateException {
    if (isClosed()) return true;
    if (paymentStrategy == null || !paymentStrategy.validate()) {
        throw new IllegalStateException("Payment information is not set or not valid.");
    }
    boolean isPaid = paymentStrategy.pay(this.getPrice());
    if (isPaid) this.setClosed();
    return isPaid;
}

public interface PaymentStrategy {
    boolean pay(double amount) throws IllegalStateException;
    boolean validate();
}
```

## Benefits
- Reduced duplication: one shared processing path in `FlightOrder.processOrder(PaymentStrategy)`.
- Extensibility: new payment methods only need to implement `PaymentStrategy`.
- Better separation of concerns: payment validation/payment execution live in payment classes.
- OCP alignment: `FlightOrder` is open for extension through new strategies without modification.
- Improved maintainability: changes in a payment provider are localized to one strategy class.

## Drawbacks & Mitigations
- More classes/interfaces to navigate (`PaymentStrategy`, concrete strategies) can increase initial onboarding time.
- Inconsistent behavior across strategies is possible; mitigate with contract-based tests for `validate()` and `pay()`.
- Runtime misconfiguration (null/invalid strategy) can fail late; mitigate with explicit guards already present in `processOrder`.

## class diagrams

<div style="display:flex;gap:10px;align-items:flex-start;">
    <div style="width:48%;text-align:center;">
        <div style="margin-top:6px;font-weight:600;">Before</div>
        <img src="before_strategy_pattern.jpeg" alt="before-strategy" style="width:100%;height:auto;object-fit:contain;" />
    </div>
    <div style="width:48%;text-align:center;">
        <div style="margin-bottom:6px;font-weight:600;">After</div>
        <img src="after_strategy_pattern.jpeg" alt="after-strategy" style="width:100%;height:auto;object-fit:contain;" />
    </div>
</div>

## Code Changes
Refactoring moved payment-specific logic out of `FlightOrder` into strategy implementations and kept only orchestration in the order class.

- `PaymentStrategy.java`:

```java
public interface PaymentStrategy {
    boolean pay(double amount) throws IllegalStateException;
    boolean validate();
}
```

- `FlightOrder.java` (core strategy-based flow):

```java
public boolean processOrder(PaymentStrategy paymentStrategy) throws IllegalStateException {
    if (isClosed()) return true;

    if (paymentStrategy == null || !paymentStrategy.validate()) {
        throw new IllegalStateException("Payment information is not set or not valid.");
    }

    boolean isPaid = paymentStrategy.pay(this.getPrice());
    if (isPaid) {
        this.setClosed();
        notifyPassengersBookingConfirmed();
    }
    return isPaid;
}
```

- Example strategy implementations:

```java
public class CreditCard implements PaymentStrategy {
    @Override
    public boolean validate() { return this.valid && this.number != null; }

    @Override
    public boolean pay(double amount) {
        if (!validate()) return false;
        // credit-card specific checks and balance handling
        return true;
    }
}

public class Paypal implements PaymentStrategy {
    @Override
    public boolean validate() {
        return email != null && password != null && email.equals(DATA_BASE.get(password));
    }

    @Override
    public boolean pay(double amount) {
        if (!validate()) return false;
        // PayPal-specific processing
        return true;
    }
}
```