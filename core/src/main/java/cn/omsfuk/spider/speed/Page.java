package cn.omsfuk.spider.speed;

import cn.omsfuk.spider.speed.selector.Selector;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class Page {

    private String html;

    private String url;

    private boolean skip;

    public Result result = new Result();

    public Selector getHtml() {
        return Selector.build(html);
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public void putField(String key, String value) {
        result.put(key, value);
    }
    
    public Object getField(String key) {
        return result.get(key);
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Page(String html, String url) {
        this.html = html;
        this.url = url;
    }

    public Result getResult() {
        return result;
    }
}
