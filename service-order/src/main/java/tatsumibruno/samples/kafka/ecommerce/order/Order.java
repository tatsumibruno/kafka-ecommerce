package tatsumibruno.samples.kafka.ecommerce.order;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class Order {
    private final String userId;
    private final String orderId;
    private final BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal amount) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Order.class)
                .add("userId", userId)
                .add("orderId", orderId)
                .add("amount", amount)
                .toString();
    }
}
