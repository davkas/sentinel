import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by hzzhengxianrui on 2015/12/26.
 */
public class Receive {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws  Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String message = new String(body,"UTF-8");
                System.out.println("[x] Receiver '"+message+"'");
            }
        };
        channel.basicConsume(QUEUE_NAME,true,consumer);

    }
}
