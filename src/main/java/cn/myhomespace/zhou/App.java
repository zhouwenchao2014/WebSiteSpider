package cn.myhomespace.zhou;

import cn.myhomespace.zhou.consumer.Consumer;
import cn.myhomespace.zhou.consumer.Monitor;
import cn.myhomespace.zhou.consumer.Producer;
import cn.myhomespace.zhou.object.Page;
import cn.myhomespace.zhou.ui.MainPage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        MainPage mainPage = new MainPage();
//        mainPage.init();
        BlockingQueue<String> noSpiderUrls=new LinkedBlockingDeque<>();
        BlockingQueue<String> spiderUrls=new LinkedBlockingDeque<>();
        BlockingQueue<Page> pages=new LinkedBlockingDeque<>();
        String siteName="dyg";
        String rootUrl="http://www.dygang.net/";
        noSpiderUrls.add(rootUrl);
        Producer producer = new Producer(noSpiderUrls,spiderUrls,pages,siteName,rootUrl);
        Thread thread = new Thread(producer);
        thread.start();

        Consumer consumer = new Consumer(pages);
        Thread consumer_thread = new Thread(consumer);
        consumer_thread.start();

        Monitor monitor = new Monitor(noSpiderUrls,spiderUrls,pages);
        Thread monitor_thread = new Thread(monitor);
        monitor_thread.start();
    }
}
