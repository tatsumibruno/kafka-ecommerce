package tatsumibruno.samples.kafka.ecommerce.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {

    private String topic;
    private KafkaProducer<String, T> producer;

    public KafkaDispatcher(String topic) {
        this.topic = topic;
        this.producer = new KafkaProducer<>(properties());
    }

    public void send(String key, T value) throws ExecutionException, InterruptedException {
        Callback callback = (metadata, exception) -> {
            System.out.println(metadata);
            System.out.println(exception);
        };

        var record = new ProducerRecord<>(topic, key, value);
        producer.send(record, callback).get();
        producer.close();
    }

    private Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        return properties;
    }

    @Override
    public void close() {
        producer.close();
    }
}
