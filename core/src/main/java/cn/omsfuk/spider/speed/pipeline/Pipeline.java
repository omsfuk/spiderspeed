package cn.omsfuk.spider.speed.pipeline;

import cn.omsfuk.spider.speed.Result;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public interface Pipeline {

    void process(Result result);
}
