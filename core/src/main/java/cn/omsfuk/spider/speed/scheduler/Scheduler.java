package cn.omsfuk.spider.speed.scheduler;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public interface Scheduler {

    String get();

    boolean add(String... urls);
}
