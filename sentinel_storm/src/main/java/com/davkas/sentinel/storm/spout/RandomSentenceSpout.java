package com.davkas.sentinel.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;

import java.io.FileReader;
import java.util.Map;

/**
 * Created by davkas on 15/11/28.
 */
public class RandomSentenceSpout implements IRichSpout {

    private SpoutOutputCollector collector;
    private FileReader fileReader;
    private boolean completed = false;
    private TopologyContext context;
    private boolean isDistributed(){return false;}

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        System.out.println();
    }

    public void close() {

    }

    public void activate() {

    }

    public void deactivate() {

    }

    public void nextTuple() {

    }

    public void ack(Object o) {
        System.out.println("ok = [" + o + "]");
    }

    public void fail(Object o) {
        System.out.println("fail = [" + o + "]");
    }
}