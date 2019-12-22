package tatsumibruno.samples.kafka.ecommerce.logging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import tatsumibruno.samples.kafka.ecommerce.http.KafkaService;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {
        LogService logService = new LogService();
        KafkaService kafkaService = new KafkaService(LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"),
                logService.consumer(),
                Object.class);
        kafkaService.run();
    }

    public Consumer<ConsumerRecord<String, Object>> consumer() {
        return record -> {
            System.out.println("Logging");
            System.out.println(record.key());
            System.out.println(record.value().toString());
            System.out.println(record.partition());
            System.out.println(record.offset());
            System.out.println("-------------------------------------");
        };
    }
}
