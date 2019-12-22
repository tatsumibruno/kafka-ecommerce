package tatsumibruno.samples.kafka.ecommerce.fraud_detector;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import tatsumibruno.samples.kafka.ecommerce.kafka.KafkaService;

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
            System.out.println("Nova mensagem:");
            System.out.println(record.key());
            System.out.println(record.value().toString());
            System.out.println(record.partition());
            System.out.println(record.offset());
            System.out.println("-------------------------------------");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Order processed.");
        };
    }


}
