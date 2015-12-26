import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by hzzhengxianrui on 2015/12/26.
 */
public class NewTask {
    private static final String TASK_QUEUE_NAME = "task_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        /**
         * true����Ϣ�־û�����
         * ��������֮�󣬷������յ���Ϣ��ͻ����̽���Ϣд�뵽Ӳ�̣��Ϳ��Է�ֹͻȻ�������ҵ�������������ݶ�ʧ�ˡ�
         * ���Ƿ�����������յ���Ϣ����û���ü�д�뵽Ӳ�̣��͹ҵ��ˣ����������޷�������Ϣ�Ķ�ʧ��
         */
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        //��װ������Ϣ
		String[] msg={"xuzheng....","test01.....","rabbitMQ......"};
		String message = getMessage(msg);
        channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        System.out.println("send:["+message+"]");

                // String message = null;
/*        for (int i = 0; i < 10; i++) {
            message = "���Թ�ƽ����"+i;
            //��Ϣ�־û�����MessageProperties.PERSISTENT_TEXT_PLAIN ��rabbitMQ��ʱdown�����´�����֮�󣬹����߻����ܽ���Ŀǰ���͵���Ϣ��
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            System.out.println("send:["+message+"]");
        }*/
        channel.close();
        conn.close();
    }

    private static String getMessage(String[] args) {
        if(args.length<1){
            return "Hello World!";
        }else{
            return joinStrings(args,"");
        }
    }

    private static String joinStrings(String[] args, String string) {
        int len = args.length;
        if(len == 0){
            return "";
        }
        StringBuilder words = new StringBuilder(args[0]);
        for (int i = 0; i < len; i++) {
            words.append(string).append(args[i]);
        }
        return words.toString();
    }
}
