package tatsumibruno.samples.kafka.ecommerce.email;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import tatsumibruno.samples.kafka.ecommerce.http.KafkaService;

import java.util.function.Consumer;

public class SendEmailService {

    public static void main(String[] args) {
        SendEmailService emailService = new SendEmailService();
        KafkaService kafkaService = new KafkaService(SendEmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                emailService.consumer(),
                Email.class);
        kafkaService.run();
    }

    public Consumer<ConsumerRecord<String, Email>> consumer() {
        return record -> {
            System.out.println("Novo email:");
            System.out.println(record.key());
            System.out.println(record.value().toString());
            System.out.println(record.partition());
            System.out.println(record.offset());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Email enviado.");
            System.out.println("-------------------------------------");
        };
    }
}
