package cn.omsfuk.spider.speed.entity;


/**
 * Created by omsfuk on 2017/7/22.
 */

public class Book {

    private Integer id;

    private String title;

    private String press;

    private String carrier;

    private String author;

    private String theme;

    private String cls;

    private String abst;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
    }

    public Book() {
    }

    public Book(String title, String press, String carrier, String author, String theme, String cls, String abst) {
        this.title = title;
        this.press = press;
        this.carrier = carrier;
        this.author = author;
        this.theme = theme;
        this.cls = cls;
        this.abst = abst;
    }
}
