package com.davkas.sentinel.storm.rabbitmq.producer;


import com.rabbitmq.client.Channel;
import io.latent.storm.rabbitmq.Declarator;
import io.latent.storm.rabbitmq.Message;
import io.latent.storm.rabbitmq.RabbitMQProducer;
import io.latent.storm.rabbitmq.config.ConnectionConfig;
import io.latent.storm.rabbitmq.config.ProducerConfig;
import io.latent.storm.rabbitmq.config.ProducerConfigBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzzhengxianrui on 2015/12/26.
 */
public class Producer {

    private static final String MESSAGE="TEST MESSAGE";
    private static final String RABBITMQ_HOST = "localhost";
    private static final String RABBITMQ_USER = "guest";
    private static final String RABBITMQ_PASS = "guest";
    private static final String EXCHANGE_NAME = "storm-test-change";
    private static final String QUEUE_NAME = "storm-test-queue";
    private static final int NUM_MESSAGE = 100;

    public static void main(String[] args) {
        final Message msg = Message.forSending(MESSAGE.getBytes(),null,
                EXCHANGE_NAME,"","text/plain","UTF-8",true);
        final ConnectionConfig connectionConfig = new ConnectionConfig(
                RABBITMQ_HOST,RABBITMQ_USER,RABBITMQ_PASS);
        final ProducerConfig pConfig = new ProducerConfigBuilder().connection(connectionConfig)
                .exchange(EXCHANGE_NAME)
                .contentEncoding("UTF-8")
                .contentType("text/plain")
                .build();
        final Declarator decl = new CustomDeclarator(EXCHANGE_NAME,QUEUE_NAME);
        RabbitMQProducer producer = new RabbitMQProducer(decl);
        producer.open(pConfig.asMap());
        for(int i = 0;i<NUM_MESSAGE;i++){
            producer.send(msg);
        }

    }

    private static class CustomDeclarator implements Declarator{
        private final String exchange;
        private final String queue;
        private final String routingKey;

        public CustomDeclarator(String exchange,String queue){
            this(exchange,queue,"");
        }

        public CustomDeclarator(String exchange,String queue,String routingKey){
            this.exchange = exchange;
            this.queue = queue;
            this.routingKey = routingKey;
        }

        public void execute(Channel channel) {
            try {
                Map<String, Object> args = new HashMap<String, Object>();
                channel.queueDeclare(queue, true, false, false, args);
                channel.exchangeDeclare(exchange, "topic", true);
                channel.queueBind(queue, exchange, routingKey);

            }catch(IOException ex){
                throw new RuntimeException("ERROR executing rabbitmq declarations",ex);
            }
        }
    }

}
