package cn.omsfuk.spider.speed.oschina;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * by omsfuk
 * ---- 创建于 7/19/18 6:25 PM
 */
public abstract class HttpUtil {

    public static String download(String pageUrl, String encoding) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        StringBuffer sb = null;
        try {
            url = new URL(pageUrl);
            conn = (HttpURLConnection) url.openConnection();
            sb = new StringBuffer();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = in.read(buf)) != -1)
                    sb.append(new String(buf, 0, len, encoding));
                in.close();
            } else return null;
        } catch (MalformedURLException e) {
            System.err.println("url格式不规范:"+e.getMessage());

        } catch (IOException e) {
            System.err.println("IO操作错误："+e.getMessage());
        }
        return sb.toString();
    }

    public static void downloadFromUrl(String urlStr, String fileName) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();

        //获取自己数组

        byte[] buffer = new byte[1024];
        BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(fileName));

        int r = -1;
        while ((r = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, r);
        }
        inputStream.close();
        fileOutputStream.close();
    }

    public static void main(String[] args) throws IOException {
        downloadFromUrl("https://minecraft.curseforge.com/projects/storage-drawers/files/2576611/download", "/Users/omsfuk/storage-drawer.jar");
    }
}
