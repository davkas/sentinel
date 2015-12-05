package com.davkas.sentinel.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;

/**
 * Created by davkas on 15/12/6.
 */
public class SequenceSpout extends BaseRichSpout {
    private SpoutOutputCollector spoutOutputCollector;
    private String[] sequence = {
            "my dog has flease",
            "I like cold beverager",
            "dont have a cow man"

    };

    private  int index = 0;

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(sequence));
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
    }

    public void nextTuple() {
        this.spoutOutputCollector.emit(new Values(sequence[index]));
        index ++;
        if(index>=sequence.length) {
            index = 0;
        }
    }
}
