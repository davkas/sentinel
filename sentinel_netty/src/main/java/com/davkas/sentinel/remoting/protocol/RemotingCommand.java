package com.davkas.sentinel.remoting.protocol;

import com.davkas.sentinel.remoting.CommandCustomHeader;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.FutureTask;

/**
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class RemotingCommand {


    private static String RemotingVersionKey = "rocketmq.remting.version";

    class ReorderExample {
        int a = 0;
        boolean flag = false;

        public void writer() {
            a = 1;                   //1
            flag = true;             //2
        }
        Public void reader() {
            if (flag) {                //3
                int i =  a * a;        //4
            }
        }
    }


}
