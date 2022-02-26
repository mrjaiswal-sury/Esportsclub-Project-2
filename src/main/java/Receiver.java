import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Receiver {
    private static final String QUEUE_NAME="hello";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection=factory.newConnection();
        Channel channel= connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println("[*] Waiting for message. To exit press Ctrl+C");

        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            String message=new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("\n\n[x] Message received :"+message);
            try {
                DetectExplicit.detectSafeSearch("gs://project-gv-folder/"+message,message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag->{});
    }
}
