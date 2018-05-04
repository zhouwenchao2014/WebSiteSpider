package cn.myhomespace.zhou.consumer;

import cn.myhomespace.zhou.object.Page;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zhouw on 2018/5/3.
 */
public class Monitor implements Runnable {

    private Map<String,BlockingQueue<String>> noSpiderUrlsMap;

    private BlockingQueue<String> spiderUrls;

    private BlockingQueue<Page> pages;

    public Monitor(Map<String,BlockingQueue<String>> noSpiderUrlsMap, BlockingQueue<String> spiderUrls, BlockingQueue<Page> pages) {
        this.noSpiderUrlsMap = noSpiderUrlsMap;
        this.spiderUrls = spiderUrls;
        this.pages = pages;
    }

    @Override
    public void run() {
        while (true){
            Set<String> keys = noSpiderUrlsMap.keySet();
            for(String key : keys){
                BlockingQueue<String> key_Queue = noSpiderUrlsMap.get(key);
                int size = key_Queue.size();
                System.out.println("未消费"+key+"队列长度："+size);
            }
            System.out.println("已消费队列长度："+spiderUrls.size());
            System.out.println("页面队列长度："+pages.size());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
