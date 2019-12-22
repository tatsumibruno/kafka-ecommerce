package tatsumibruno.samples.kafka.ecommerce.http;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class KafkaService<T> {
    private String groupName;
    private String topic;
    private Pattern topicPattern;
    private Consumer<ConsumerRecord<String, T>> consumer;
    private Class<T> type;

    public KafkaService(String groupName, String topic, Consumer<ConsumerRecord<String, T>> consumer, Class<T> type) {
        this.topic = topic;
        this.consumer = consumer;
        this.groupName = groupName;
        this.type = type;
    }

    public KafkaService(String groupName, Pattern topicPattern, Consumer<ConsumerRecord<String, T>> consumer, Class<T> type) {
        this.topicPattern = topicPattern;
        this.consumer = consumer;
        this.groupName = groupName;
        this.type = type;
    }

    public void run() {
        var kafkaConsumer = new KafkaConsumer<String, T>(properties());
        if (Objects.nonNull(topicPattern)) {
            kafkaConsumer.subscribe(topicPattern);
        } else {
            kafkaConsumer.subscribe(Collections.singletonList(topic));
        }
        while (true) {
            var records = kafkaConsumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Encontrados novos registros, processando...");
                for (var record : records) {
                    consumer.accept(record);
                }
            }
        }
    }

    private Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupName);
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, this.type.getName());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }
}
