package tatsumibruno.samples.kafka.ecommerce.http;

import tatsumibruno.samples.kafka.ecommerce.email.Email;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher newOrderDispatcher = new KafkaDispatcher("ECOMMERCE_NEW_ORDER");
    private final KafkaDispatcher sendEmailDispatcher = new KafkaDispatcher("ECOMMERCE_SEND_EMAIL");

    @Override
    public void destroy() {
        newOrderDispatcher.close();
        sendEmailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        String userEmail = req.getParameter("email");
        Order order = new Order(userEmail, orderId, amount);
        Email email = new Email("New Order", "Welcome, we are processing your order.");
        try {
            newOrderDispatcher.send(userEmail, order);
            sendEmailDispatcher.send(userEmail, email);
        } catch (ExecutionException | InterruptedException e) {
            throw new ServerException(e.getMessage());
        }
        System.out.println("New order sent succesfully.");
        resp.setStatus(200);
        resp.getWriter().println(order.toString());
    }
}
