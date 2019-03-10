package cn.omsfuk.spider.speed.selector;

import cn.omsfuk.spider.speed.downloader.impl.SimpleDownloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class Selector {

    private boolean isSingleResult = true;

    private String result;

    private List<String> results;

    private Selector(String html) {
        this.result = html;
    }

    public static Selector build(String html) {
        return new Selector(html);
    }

    public Selector $(String cssQuery) {
        if (isSingleResult) {
            Document document = Jsoup.parse(result);
            Elements elements = document.select(cssQuery);

            if (elements.size() == 1) {
                isSingleResult = true;
                result = elements.html();
            } else {
                isSingleResult = false;
                results = new ArrayList<>(elements.size());
                elements.forEach(element -> results.add(element.html()));
            }
        } else {
            results = new LinkedList<>();
            for (String resutl : results) {
                Document document = Jsoup.parse(result);
                Elements elements = document.select(cssQuery);
                elements.forEach(element -> results.add(element.html()));
            }
            if (results.size() == 1) {
                isSingleResult = true;
                result = results.get(0);
            } else {
                isSingleResult = false;
            }
        }

        return this;
    }

    public Selector regex(String regex) {
        Pattern pattern = Pattern.compile(regex);
        if (isSingleResult) {
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                result = matcher.groupCount() >= 2 ? matcher.group(1) : matcher.group();
            } else {
                isSingleResult = true;
                result = "";
            }
        } else {
            results = new LinkedList<>();
            for (String result : results) {
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    results.add(matcher.groupCount() >= 2 ? matcher.group(1) : matcher.group());
                }
            }

            if (results.size() == 1) {
                isSingleResult = true;
                result = results.get(0);
            } else {
                isSingleResult = false;
            }
        }
        return this;
    }

    public Selector first() {
        if (!isSingleResult) {
            result = results.get(0);
            isSingleResult = true;
        }
        return this;
    }

    public List<String> all() {
        if (isSingleResult) {
            results = new ArrayList<>(1);
            results.add(result);
        }
        return results;
    }

    @Override
    public String toString() {
        if (isSingleResult) {
            return result;
        } else {
            return results.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println(Selector.build(new SimpleDownloader().download("http://www.cnblogs.com/lizhenghn/p/5155933.html"))
                .$("#cnblogs_post_body h2").all());
    }
}
