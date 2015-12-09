package com.davkas.sentinel.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.apache.avro.generic.GenericData;

import java.util.*;

/**
 * Created by davkas on 15/12/6.
 */
public class ReportBolt extends BaseRichBolt {
    private HashMap<String,Long> counts = null;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.counts = new HashMap<String, Long>();
    }

    public void execute(Tuple tuple) {
        String word = tuple.getStringByField("word");
        Long count = tuple.getLongByField("count");
        this.counts.put(word,count);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //this bolt does not emit anything
    }

    @Override
    public void cleanup() {
        System.out.println("-----FINAL COUNTS-----");
        List<String> keys = new ArrayList<String>();
        keys.addAll(this.counts.keySet());
        Collections.sort(keys);
        for(String key : keys){
            System.out.println(key+":"+this.counts.get(key));
        }
        super.cleanup();
    }
}
