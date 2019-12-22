package tatsumibruno.samples.kafka.ecommerce.order;

import tatsumibruno.samples.kafka.ecommerce.email.Email;
import tatsumibruno.samples.kafka.ecommerce.http.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        for (var i = 0; i < 10; i++) {
            try (KafkaDispatcher newOrderDispatcher = new KafkaDispatcher("ECOMMERCE_NEW_ORDER")) {
                String orderId = String.valueOf(i) + " - " + UUID.randomUUID().toString();
                BigDecimal amount = BigDecimal.valueOf(Math.random() * 5000);
                String userEmail = String.format("bruno.yokio_%s@gmail.com", BigDecimal.valueOf(Math.random() * 10).intValue());
                newOrderDispatcher.send(userEmail, new Order(userEmail, orderId, amount));
                try (KafkaDispatcher sendEmailDispatcher = new KafkaDispatcher("ECOMMERCE_SEND_EMAIL")) {
                    Email email = new Email("New Order", "Welcome, we are processing your order.");
                    sendEmailDispatcher.send(userEmail, email);
                }
            }
        }
    }
}
