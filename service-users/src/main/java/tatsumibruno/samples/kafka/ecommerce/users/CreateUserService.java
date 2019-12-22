package tatsumibruno.samples.kafka.ecommerce.users;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import tatsumibruno.samples.kafka.ecommerce.http.KafkaService;

import java.sql.*;
import java.util.UUID;
import java.util.function.Consumer;

public class CreateUserService {

    private Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:users_database.db";
        connection = DriverManager.getConnection(url);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS USERS (UUID VARCHAR(200) PRIMARY KEY, EMAIL VARCHAR(200))");
        }
    }

    public static void main(String[] args) throws SQLException {
        CreateUserService createUserService = new CreateUserService();
        var kafkaService = new KafkaService<>(CreateUserService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                createUserService.consumer(),
                Order.class);
        kafkaService.run();
    }

    private Consumer<ConsumerRecord<String, Order>> consumer() {
        return record -> {
            Order order = record.value();
            try {
                String userEmail = order.getUserEmail();
                if (!userExists(userEmail)) {
                    System.out.println("Usuário " + userEmail + " não existente, inserindo no banco de dados...");
                    insertUser(userEmail);
                } else {
                    System.out.println("Usuário " + userEmail + " já existente.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
    }

    private void insertUser(String userEmail) throws SQLException {
        String sql = "INSERT INTO USERS (UUID, EMAIL) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, userEmail);
            statement.executeUpdate();
        }
    }

    private boolean userExists(String userEmail) throws SQLException {
        String sql = "SELECT EMAIL FROM USERS WHERE EMAIL = ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userEmail);
            ResultSet resultset = statement.executeQuery();
            return resultset.next();
        }
    }
}
