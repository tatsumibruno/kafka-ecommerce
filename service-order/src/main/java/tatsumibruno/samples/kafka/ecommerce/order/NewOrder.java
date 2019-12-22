package tatsumibruno.samples.kafka.ecommerce.order;

import tatsumibruno.samples.kafka.ecommerce.email.Email;
import tatsumibruno.samples.kafka.ecommerce.kafka.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        for (var i = 0; i < 10; i++) {
            try (KafkaDispatcher newOrderDispatcher = new KafkaDispatcher("ECOMMERCE_NEW_ORDER")) {
                var key = UUID.randomUUID().toString();
                String userId = UUID.randomUUID().toString();
                String orderId = String.valueOf(i);
                BigDecimal amount = BigDecimal.valueOf(Math.random() * 5000);
                Order order = new Order(userId, orderId, amount);
                newOrderDispatcher.send(key, order);
                try (KafkaDispatcher sendEmailDispatcher = new KafkaDispatcher("ECOMMERCE_SEND_EMAIL")) {
                    Email email = new Email("New Order", "Welcome, we are processing your order.");
                    sendEmailDispatcher.send(key, email);
                }
            }
        }
    }
}
