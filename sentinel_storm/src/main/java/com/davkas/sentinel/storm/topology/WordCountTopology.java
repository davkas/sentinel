package com.davkas.sentinel.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.davkas.sentinel.storm.bolt.ReportBolt;
import com.davkas.sentinel.storm.bolt.SequenceBolt;
import com.davkas.sentinel.storm.bolt.WordCountBolt;
import com.davkas.sentinel.storm.spout.SequenceSpout;

/**
 * Created by davkas on 15/12/6.
 */
public class WordCountTopology {

    private static final String SENTENCE_SPOUT_ID ="sentence-spout";
    private static final String SPLIT_BOLT_ID = "split-bolt";
    private static final String COUNT_BOLT_ID ="count-bolt";
    private static final String REPORT_BOLT_ID = "report-bolt";
    private static final String TOPOLOGY_NAME ="word-count-topology";

    public static void main(String[] args) {
        SequenceSpout spout = new SequenceSpout();
        SequenceBolt sbolt = new SequenceBolt();
        WordCountBolt wCountBolt = new WordCountBolt();
        ReportBolt reportBolt = new ReportBolt();

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(SENTENCE_SPOUT_ID, spout);
        topologyBuilder.setBolt(SPLIT_BOLT_ID,sbolt).shuffleGrouping(SENTENCE_SPOUT_ID);
        topologyBuilder.setBolt(COUNT_BOLT_ID,wCountBolt).fieldsGrouping(SPLIT_BOLT_ID, new Fields("word"));
        topologyBuilder.setBolt(REPORT_BOLT_ID,reportBolt).globalGrouping(COUNT_BOLT_ID);
        Config config = new Config();
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology(TOPOLOGY_NAME,config,topologyBuilder.createTopology());
        try{
            Thread.sleep(100000);
        }catch (Exception e){

        }
        localCluster.killTopology(TOPOLOGY_NAME);
        localCluster.shutdown();

    }
}
