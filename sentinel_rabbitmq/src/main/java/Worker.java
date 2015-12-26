import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by hzzhengxianrui on 2015/12/26.
 */
public class Worker {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException,TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        /**
         * true:
         * 这样设置之后，服务器收到消息后就会立刻将消息写入到硬盘，就可以防止突然服务器挂掉，而引起数据丢失了，但是服务器如果刚收到消息，还没有来得写入硬盘，就挂掉了，这样
         * 无法避免消息得丢失。
         */
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("waiting for message.To exit press CTRL+C");
        //channel.basicQos(1);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        /**
         * false：设置确认消息，true表示接收到消息之后，将返回给服务端确定消息
         */
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("Received:[" + message + "] from Task");
            doWork(message);
            System.out.println("Done!");
            //设置消息确认机制，如将如下代码注释掉，则
            //一旦将autoAck关闭之后，一定要记得处理完消息之后，向服务器确认消息。否则服务器将会一直转发该消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    private static void doWork(String message) throws InterruptedException {
        for (char ch : message.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
