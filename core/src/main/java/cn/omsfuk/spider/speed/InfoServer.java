package cn.omsfuk.spider.speed;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/24
 */
public class InfoServer {

    private MessageSource messageSource;

    private static Gson gson = new Gson();

    private volatile boolean stop = false;

    private int port = 8123;

    public static void main(String[] args) {
        MessageSource messageSource = new MessageSource();
        messageSource.setMessage("a string");
        new InfoServer(messageSource, 80);
    }

    public InfoServer(MessageSource messageSource, int port) {
        this.messageSource = messageSource;
        this.port = port;
    }

    public void start() {
        new Thread(() -> {
            ExecutorService service = Executors.newFixedThreadPool(10);
            try {
                ServerSocket server = new ServerSocket(port);
                while (!stop) service.execute(new Responser(server.accept()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        this.stop = true;
    }

    public class Responser implements Runnable {

        private Socket client;

        public Responser(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                writer.write(gson.toJson(messageSource.getMessage()));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
