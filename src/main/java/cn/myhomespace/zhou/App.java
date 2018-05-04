package cn.myhomespace.zhou;

import cn.myhomespace.zhou.consumer.Consumer;
import cn.myhomespace.zhou.consumer.Monitor;
import cn.myhomespace.zhou.consumer.Producer;
import cn.myhomespace.zhou.db.JDBCConnection;
import cn.myhomespace.zhou.object.Page;
import cn.myhomespace.zhou.object.SpiderProjectManage;
import cn.myhomespace.zhou.object.TableManageDo;
import cn.myhomespace.zhou.ui.MainPage;
import cn.myhomespace.zhou.utils.TableUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
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
        Map<String,BlockingQueue<String>> noSpiderUrlsMap=new HashMap<>();
        BlockingQueue<String> spiderUrls=new LinkedBlockingDeque<>();
        BlockingQueue<Page> pages=new LinkedBlockingDeque<>();
        List<SpiderProjectManage> spiderProjectManages = JDBCConnection.queryResultFormatByClass(SpiderProjectManage.class, TableUtils.TABLE_SPIDER_PROJECT_MANAGE, 0);
        for(SpiderProjectManage spiderProjectManage : spiderProjectManages){
            BlockingQueue<String> noSpiderUrls = new LinkedBlockingDeque<>();
            String config = spiderProjectManage.getConfig();
            noSpiderUrlsMap.put(spiderProjectManage.getName(),noSpiderUrls);
            noSpiderUrls.add(spiderProjectManage.getRootUrl());
            Producer producer = new Producer(noSpiderUrls,spiderUrls,pages,spiderProjectManage);
            Thread thread = new Thread(producer);
            thread.start();
        }



        Consumer consumer = new Consumer(pages);
        Thread consumer_thread = new Thread(consumer);
        consumer_thread.start();

        Monitor monitor = new Monitor(noSpiderUrlsMap,spiderUrls,pages);
        Thread monitor_thread = new Thread(monitor);
        monitor_thread.start();
    }
}
