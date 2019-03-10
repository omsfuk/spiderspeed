package cn.omsfuk.spider.speed.scheduler.impl;

import cn.omsfuk.spider.speed.scheduler.Scheduler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class ListScheduler implements Scheduler {

    private Queue<String> urlQueue = new ConcurrentLinkedQueue<>();

    @Override
    public String get() {
        return urlQueue.poll();
    }

    @Override
    public boolean add(String... urls) {
        Stream.of(urls).forEach(urlQueue::add);
        return true;
    }
}
