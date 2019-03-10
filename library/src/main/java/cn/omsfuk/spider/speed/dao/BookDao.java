package cn.omsfuk.spider.speed.dao;

import cn.omsfuk.spider.speed.entity.Book;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by omsfuk on 2017/7/22.
 */

public interface BookDao {

    @Insert("insert into book(title, press, carrier, clas, `desc`) " +
            "values(#{title}, #{press}, #{carrier}, #{cls}, #{abst})")
    int insertBook(Book book);

}
