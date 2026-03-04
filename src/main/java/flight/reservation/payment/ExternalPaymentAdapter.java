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