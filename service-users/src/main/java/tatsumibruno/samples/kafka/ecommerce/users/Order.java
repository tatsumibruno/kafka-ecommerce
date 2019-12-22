package tatsumibruno.samples.kafka.ecommerce.users;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class Order {
    private final String userEmail;
    private final String orderId;
    private final BigDecimal amount;

    public Order(String userEmail, String orderId, BigDecimal amount) {
        this.userEmail = userEmail;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Order.class)
                .add("userEmail", userEmail)
                .add("orderId", orderId)
                .add("amount", amount)
                .toString();
    }
}
