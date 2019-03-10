package cn.omsfuk.spider.speed;

import cn.omsfuk.spider.speed.dao.BookDao;
import cn.omsfuk.spider.speed.entity.Book;
import cn.omsfuk.spider.speed.pipeline.Pipeline;
import cn.omsfuk.spider.speed.pipeline.impl.ConsolePipeline;
import cn.omsfuk.spider.speed.processor.PageProcessor;
import cn.omsfuk.spider.speed.scheduler.Scheduler;
import cn.omsfuk.spider.speed.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class SpiderTest {

    private static Pattern pattern = Pattern.compile("<dd>([\\d\\D]*)</dd>", Pattern.MULTILINE);

    private static Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
    private static TransportClient client;

    static {
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static class CustomPageProcessor implements PageProcessor {

        @Override
        public void processPage(Page page) {
            if (!page.getHtml().toString().contains("题名/责任者")) {
                page.setSkip(true);
            } else {
//                System.out.println(page.getHtml().toString());
            }
            page.putField("html", page.getHtml().toString());
            page.putField("ur   l", page.getUrl().replaceAll("[\\\\/://?//.]", ""));
            page.getHtml().$("#item_detail dl").all().forEach(item -> {
                Matcher matcher = pattern.matcher(item);
                if (matcher.find()) {
                    String result = matcher.group(1).toString().replaceAll("<a.+?>", "").replace("</a>", "");
                    if (item.contains("题名/责任者")) {
                        page.putField("title", result);
                    } else if (item.contains("出版发行项")) {
                        page.putField("press", result);
                    } else if (item.contains("载体形态项")) {
                        page.putField("carrier", result);
                    } else if (item.contains("个人责任者")) {
                        page.putField("author", result);
                    } else if (item.contains("学科主题")) {
                        page.putField("theme", result);
                    } else if (item.contains("中图法分类号")) {
                        page.putField("class", result);
                    } else if (item.contains("提要文摘附注")) {
                        page.putField("abstract", result);
                    }
                }
            });
        }
    }

    static class CustomPipeline implements Pipeline {

        @Override
        public void process(Result result) {
            String title = result.get("title");
            String press = result.get("press");
            String carrier = result.get("carrier");
            String author = result.get("author");
            String theme = result.get("theme");
            String cls = result.get("class");
            String abst = result.get("abstract");
            SqlSession session = MyBatisUtil.getSession();
            BookDao bookDao = session.getMapper(BookDao.class);
            bookDao.insertBook(new Book(title, press, carrier, author, theme, cls, abst));
            session.commit();
            session.close();
            try {
                System.out.println(client.prepareIndex("library", "book")
                        .setSource(jsonBuilder()
                                .startObject()
                                .field("title", result.get("title"))
                                .field("press", result.get("press"))
                                .field("carrier", result.get("carrier"))
                                .field("author", result.get("author"))
                                .field("theme", result.get("theme"))
                                .field("cls", result.get("class"))
                                .field("abst", result.get("abstract"))
                                .field("date", new Date())
                                .endObject()
                        )
                        .get());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                BufferedWriter writer = new BufferedWriter(new FileWriter("D:/lucene/source/" + result.get("url") + ".txt"));
//                writer.write(result.get("html"));
//                writer.flush();
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    static class CustomScheduler implements Scheduler {

        private AtomicInteger id = new AtomicInteger(2000000);

        @Override
        public String get() {
            return "http://58.194.172.34/opac/item.php?marc_no=" + String.format("%010d", id.incrementAndGet());
        }

        @Override
        public boolean add(String... urls) {
            return true;
        }
    }

    public static void main(String[] args) {
        Spider.build(new CustomPageProcessor())
                .retry(3)
                .scheduler(new CustomScheduler())
                .pipline(new CustomPipeline())
                .pipline(new ConsolePipeline())
                .thread(100).start();
    }
}
