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
         * ��������֮�󣬷������յ���Ϣ��ͻ����̽���Ϣд�뵽Ӳ�̣��Ϳ��Է�ֹͻȻ�������ҵ������������ݶ�ʧ�ˣ����Ƿ�����������յ���Ϣ����û������д��Ӳ�̣��͹ҵ��ˣ�����
         * �޷�������Ϣ�ö�ʧ��
         */
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("waiting for message.To exit press CTRL+C");
        //channel.basicQos(1);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        /**
         * false������ȷ����Ϣ��true��ʾ���յ���Ϣ֮�󣬽����ظ������ȷ����Ϣ
         */
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("Received:[" + message + "] from Task");
            doWork(message);
            System.out.println("Done!");
            //������Ϣȷ�ϻ��ƣ��罫���´���ע�͵�����
            //һ����autoAck�ر�֮��һ��Ҫ�ǵô�������Ϣ֮���������ȷ����Ϣ���������������һֱת������Ϣ
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    private static void doWork(String message) throws InterruptedException {
        for (char ch : message.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
