package tatsumibruno.samples.kafka.ecommerce.fraud_detector;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import tatsumibruno.samples.kafka.ecommerce.http.KafkaDispatcher;
import tatsumibruno.samples.kafka.ecommerce.http.KafkaService;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class FraudDetectorService {

    public static void main(String[] args) {
        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        KafkaService kafkaService = new KafkaService(
                FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudDetectorService.consumer(),
                Order.class);
        kafkaService.run();
    }

    public Consumer<ConsumerRecord<String, Order>> consumer() {
        return record -> {
            Order order = record.value();
            System.out.println("Nova mensagem:");
            System.out.println(record.key());
            System.out.println(order.toString());
            System.out.println(record.partition());
            System.out.println(record.offset());
            if (isFraud(order)) {
                System.out.println("Order is a fraud!");
                try (KafkaDispatcher fraudOrderDispatcher = new KafkaDispatcher("ECOMMERCE_ORDER_REJECTED")) {
                    fraudOrderDispatcher.send(order.getUserEmail(), String.format("Order %s is a fraud!", order.getOrderId()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Order processed.");
            }
            System.out.println("-------------------------------------");
        };
    }

    private boolean isFraud(Order order) {
        processMachineLearning();
        return order.getAmount().compareTo(BigDecimal.valueOf(4500)) >= 0;
    }

    private void processMachineLearning() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
