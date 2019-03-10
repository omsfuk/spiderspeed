package cn.omsfuk.spider.speed.downloader.impl;

import cn.omsfuk.spider.speed.downloader.Downloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/23
 */
public class SimpleDownloader implements Downloader {

    @Override
    public String download(String url) {
        StringBuilder sb = new StringBuilder();
        try {
            URL urlConn = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.openStream()));
            String t = null;
            while ((t = reader.readLine()) != null) {
                sb.append(t).append('\r').append('\n');
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
