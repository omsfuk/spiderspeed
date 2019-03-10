package cn.omsfuk.spider.speed.pipeline.impl;

import cn.omsfuk.spider.speed.Result;
import cn.omsfuk.spider.speed.pipeline.Pipeline;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class ConsolePipeline implements Pipeline {

    @Override
    public void process(Result result) {
        result.keys().forEach(key -> System.out.println(key + " : " + result.get(key)));
    }
}
