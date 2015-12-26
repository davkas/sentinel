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
         * true：消息持久化设置
         * 这样设置之后，服务器收到消息后就会立刻将消息写入到硬盘，就可以防止突然服务器挂掉，而引起的数据丢失了。
         * 但是服务器如果刚收到消息，还没来得及写入到硬盘，就挂掉了，这样还是无法避免消息的丢失。
         */
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        //封装发送消息
		String[] msg={"xuzheng....","test01.....","rabbitMQ......"};
		String message = getMessage(msg);
        channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        System.out.println("send:["+message+"]");

                // String message = null;
/*        for (int i = 0; i < 10; i++) {
            message = "测试公平调度"+i;
            //消息持久化设置MessageProperties.PERSISTENT_TEXT_PLAIN 当rabbitMQ暂时down掉，下次重启之后，工作者还是能接受目前发送的消息。
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
