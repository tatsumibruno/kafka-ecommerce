package tatsumibruno.samples.kafka.ecommerce.email;

import com.google.common.base.MoreObjects;

public class Email {
    private final String subject;
    private final String body;

    public Email(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Email.class)
                .add("subject", subject)
                .add("body", body)
                .toString();
    }
}
