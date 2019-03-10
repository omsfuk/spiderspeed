package cn.omsfuk.spider.speed.oschina;

import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Talk is cheap. Show me the code
 * 多说无益，代码上见真章
 * -------  by omsfuk  2017/7/29
 */
public class OsChinaBlogSpider implements PageProcessor {

    @Override
    public void process(Page page) {
        System.out.println(page.getRequest().getUrl());
        page.addTargetRequests(page.getHtml().links().regex("http://blog\\.csdn\\.net.*?details.*").all());
        String content = page.getHtml().get();
        page.putField("fileName", page.getRequest().getUrl().replaceAll("[\\\\:/.?]", ""));
        page.putField("content", content);
    }

    public static class CustomPipeline implements Pipeline {

        @Override
        public void process(ResultItems resultItems, Task task) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("D:/lucene/source/" +
                        resultItems.get("fileName") + ".txt"));
                writer.write((String) resultItems.get("content"));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setTimeOut(3000).setRetryTimes(3).setSleepTime(100);
    }

    public static void main(String[] args) {
        Spider.create(new OsChinaBlogSpider())
                .thread(1)
                .addUrl("http://www.csdn.net/")
                .addPipeline(new CustomPipeline())
                .start();
    }
}
