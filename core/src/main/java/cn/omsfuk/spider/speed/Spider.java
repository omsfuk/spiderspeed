package cn.omsfuk.spider.speed;

import cn.omsfuk.spider.speed.downloader.Downloader;
import cn.omsfuk.spider.speed.downloader.impl.SimpleDownloader;
import cn.omsfuk.spider.speed.pipeline.Pipeline;
import cn.omsfuk.spider.speed.pipeline.impl.ConsolePipeline;
import cn.omsfuk.spider.speed.processor.PageProcessor;
import cn.omsfuk.spider.speed.scheduler.Scheduler;
import cn.omsfuk.spider.speed.scheduler.impl.ListScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class Spider {

    private int thread = 1;

    private int retryTimes = 3;

    private PageProcessor pageProcessor;

    private Downloader downloader = new SimpleDownloader();

    private Scheduler scheduler = new ListScheduler();

    private List<Pipeline> pipelines = new ArrayList<>();

    private AtomicInteger pagesPerSecond = new AtomicInteger(0);

    private Spider(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public static Spider build(PageProcessor pageProcessor) {
        return new Spider(pageProcessor);
    }

    public Spider thread(int n) {
        this.thread = n;
        return this;
    }

    public Spider pipline(Pipeline pipeline) {
        pipelines.add(pipeline);
        return this;
    }

    public Spider downloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public Spider scheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }
    public Spider url(String... urls) {
        scheduler.add(urls);
        return this;
    }

    public Spider retry(int times) {
        this.retryTimes = times;
        return this;
    }

    public void start() {
        runServer();
        if (pipelines.size() == 0) {
            this.pipelines.add(new ConsolePipeline());
        }
        ExecutorService executorService = Executors.newFixedThreadPool(thread);
        IntStream.range(0, thread).forEach(i -> executorService.execute(this::runSync));
        executorService.shutdown();
    }

    private void runSync() {
        String url = scheduler.get();
        while (url != null) {
            try {
                String content = "";
                boolean success = true;
                int retry = retryTimes;
                while (retry > 0) {
                    try {
                        content = downloader.download(url);
                    } catch (Exception e) {
                        success = false;
                    }
                    if (success) break;
                    retry--;
                }

                if (!success) {
                    System.out.println("[Spider-Speed] : fail to download page [ " + url + " ]");
                } else {
                    Page page = new Page(content, url);
                    pageProcessor.processPage(page);
                    if (!page.isSkip()) {
                        pipelines.forEach(pipeline -> pipeline.process(page.getResult()));
                    }
                    pagesPerSecond.incrementAndGet();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            url = scheduler.get();
        }
    }

    public void runServer() {
        MessageSource messageSource = new MessageSource();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                messageSource.setMessage(pagesPerSecond.get());
                pagesPerSecond.set(0);
            }
        }, 0, 1000);
        InfoServer server = new InfoServer(messageSource, 8123);
        server.start();
    }

}
