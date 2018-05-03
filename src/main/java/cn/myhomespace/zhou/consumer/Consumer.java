package cn.myhomespace.zhou.consumer;

import cn.myhomespace.zhou.db.JDBCConnection;
import cn.myhomespace.zhou.object.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zhouw on 2018/5/3.
 */
public class Consumer implements Runnable{

    private BlockingQueue<Page> pages;

    public Consumer(BlockingQueue<Page> pages) {
        this.pages = pages;
    }

    @Override
    public void run() {
        while (true){
            List<Page> page_list = new ArrayList<>();
            for(int i=0;i<10;i++){
                try {
                    Page take = pages.take();
                    page_list.add(take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            JDBCConnection.insertResultFormatClass(page_list,"spider_page");
        }
    }
}
