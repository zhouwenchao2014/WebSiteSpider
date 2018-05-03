package cn.myhomespace.zhou.consumer;

import cn.myhomespace.zhou.object.Page;

import java.util.concurrent.BlockingQueue;

/**
 * Created by zhouw on 2018/5/3.
 */
public class Monitor implements Runnable {

    private BlockingQueue<String> noSpiderUrls;

    private BlockingQueue<String> spiderUrls;

    private BlockingQueue<Page> pages;

    public Monitor(BlockingQueue<String> noSpiderUrls, BlockingQueue<String> spiderUrls, BlockingQueue<Page> pages) {
        this.noSpiderUrls = noSpiderUrls;
        this.spiderUrls = spiderUrls;
        this.pages = pages;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("未消费队列长度："+noSpiderUrls.size());
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
