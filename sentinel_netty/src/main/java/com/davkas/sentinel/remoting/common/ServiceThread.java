package com.davkas.sentinel.remoting.common;

/**
 * Created by hzzhengxianrui on 2015/12/27.
 */
public abstract   class ServiceThread implements Runnable  {
    private final Thread thread;

    public static long getJoinTime() {
        return JoinTime;
    }

    private static final long JoinTime = 90 * 1000;
    protected volatile boolean hasNotified = false;
    protected volatile boolean stoped = false;

    public ServiceThread() {
        this.thread = new Thread(this,this.getServiceName());
    }

    protected abstract String getServiceName();

    public void start(){
        this.thread.start();
    }

    public void shutdown(){
        this.shutdown(false);
    }

    private void shutdown(final boolean interrupt) {
        this.stoped = true;
        synchronized (this){
            if(!this.hasNotified){
                this.hasNotified = true;
                this.notify();
            }
        }
        try{
            if(interrupt) {
                this.thread.interrupt();
            }
            this.thread.join(this.getJoinTime());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        this.stop(false);
    }

    public void makeStop(){
        this.stoped = true;
    }

    private void stop(boolean interrupt) {
        this.stoped = true;
        synchronized (this){
            if(!this.hasNotified){
                this.hasNotified = true;
                this.notify();
            }
        }
        if(interrupt){
            this.thread.interrupt();
        }
    }

    public void wakeup(){
        synchronized (this){
            if(!this.hasNotified){
                this.hasNotified = true;
                this.notify();
            }
        }
    }

    protected void waitForRunning(long interval){
        synchronized (this){
            if(this.hasNotified){
                this.hasNotified = false;
                this.onWaitEnd();
                return;
            }
            try{
                this.wait(interval);
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                this.hasNotified = false;
                this.onWaitEnd();
            }
        }

    }

    private void onWaitEnd() {
    }

    public boolean isStoped(){
        return stoped;
    }


}
