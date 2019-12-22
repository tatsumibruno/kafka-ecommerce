package tatsumibruno.samples.kafka.ecommerce.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {

    private static final Gson gson = new GsonBuilder().create();

    @Override
    public byte[] serialize(String s, T t) {
        String toJson = gson.toJson(t);
        return toJson.getBytes();
    }
}
