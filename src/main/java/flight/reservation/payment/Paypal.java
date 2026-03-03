package flight.reservation.payment;

import java.util.HashMap;
import java.util.Map;

/**
 * PayPal payment class implementing PaymentStrategy.
 */
public class Paypal implements PaymentStrategy {
    public static final Map<String, String> DATA_BASE = new HashMap<>();

    static {
        DATA_BASE.put("amanda1985", "amanda@ya.com");
        DATA_BASE.put("qwerty", "john@amazon.eu");
    }

    private String email;
    private String password;

    public Paypal(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean pay(double amount) throws IllegalStateException {
        if (!validate()) {
            return false;
        }
        System.out.println("Paying " + amount + " using PayPal.");
        return true;
    }

    @Override
    public boolean validate() {
        return email != null && password != null && email.equals(DATA_BASE.get(password));
    }
}
