package com.davkas.sentinel.storm.rabbitmq.consumer;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.Scheme;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.rabbitmq.client.Channel;
import io.latent.storm.rabbitmq.Declarator;
import io.latent.storm.rabbitmq.Message;
import io.latent.storm.rabbitmq.RabbitMQSpout;
import io.latent.storm.rabbitmq.config.ConnectionConfig;
import io.latent.storm.rabbitmq.config.ConsumerConfig;
import io.latent.storm.rabbitmq.config.ConsumerConfigBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzzhengxianrui on 2015/12/26.
 */
public class Consumer {

    private static final String MESSAGE="TEST MESSAGE";
    private static final String RABBITMQ_HOST = "localhost";
    private static final String RABBITMQ_USER = "guest";
    private static final String RABBITMQ_PASS = "guest";
    private static final String EXCHANGE_NAME = "storm-test-change";
    private static final String QUEUE_NAME = "storm-test-queue";
    private static final int NUM_MESSAGE = 100;

    public static void main(String[] args) {
        final Message msg = new Message(MESSAGE.getBytes());
        final ConnectionConfig  connectionConfig= new ConnectionConfig(
                RABBITMQ_HOST,RABBITMQ_USER,RABBITMQ_PASS);
        final ConsumerConfig spoutConfig = new ConsumerConfigBuilder().connection(connectionConfig)
                .queue(QUEUE_NAME)
                .prefetch(200)
                .requeueOnFail()
                .build();
        final Declarator decl = new CustomDeclarator(EXCHANGE_NAME,QUEUE_NAME);
        Scheme scheme = new CustomSchem();
        IRichSpout spout = new RabbitMQSpout(scheme,decl);
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("myspout",spout)
                .addConfigurations(spoutConfig.asMap())
                .setMaxSpoutPending(200);

        Config conf = new Config();
        conf.setDebug(true);


        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test",conf,builder.createTopology());

    }

    private static class CustomSchem implements Scheme{

        public List<Object> deserialize(byte[] ser) {
            try {
                return new Values(new String(ser, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public Fields getOutputFields() {
            return new Fields("str");
        }
    }


    private static class CustomDeclarator implements Declarator {
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
