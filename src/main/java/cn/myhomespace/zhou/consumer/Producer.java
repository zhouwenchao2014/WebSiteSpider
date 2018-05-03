package cn.myhomespace.zhou.consumer;

import cn.myhomespace.zhou.object.Page;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zhouw on 2018/5/3.
 */
public class Producer implements Runnable{

    private BlockingQueue<String> noSpiderUrls;

    private BlockingQueue<String> spiderUrls;

    private BlockingQueue<Page> pages;

    private String siteName;

    private String rootUrl;

    public Producer(BlockingQueue<String> noSpiderUrls, BlockingQueue<String> spiderUrls, BlockingQueue<Page> pages, String siteName, String rootUrl) {
        this.noSpiderUrls = noSpiderUrls;
        this.spiderUrls = spiderUrls;
        this.pages = pages;
        this.siteName = siteName;
        this.rootUrl = rootUrl;
    }

    @Override
    public void run() {
        int i=0;
        while (true){
            String take=null;
            try {
                take = noSpiderUrls.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!take.equals(rootUrl)&&spiderUrls.contains(take)){
                continue;
            }
            try {
                Document doc = Jsoup.connect(take).get();
                Elements elements = doc.getElementsByTag("a");
                Page page = new Page();
                page.setCreateTime(new Date(System.currentTimeMillis()));
                page.setSiteName(siteName);
                List<String> sources = new ArrayList<>();
                page.setTitle(doc.title());
                page.setUrl(take);
                for (Element element : elements){
                    String href = element.attr("href");
                    if(!StringUtils.isEmpty(href)&&href.indexOf(rootUrl)!=-1&&!noSpiderUrls.contains(href)&&!spiderUrls.contains(href)){
                        noSpiderUrls.add(href);
                    }
                    if(!StringUtils.isEmpty(href)&&(href.indexOf("magnet:?xt=urn:btih:")!=-1||href.indexOf("ed2k://|file|")!=-1)){
                        sources.add(href);
                    }
                }
                String s = JSONObject.toJSONString(sources);
                page.setSource(s);

                Elements imgs = doc.getElementsByTag("img");
                page.setPic("");
                for(Element img : imgs){
                    if(img.hasAttr("alt")){
                        String src = img.attr("src");
                        page.setPic(src);
                        break;
                    }
                }
                spiderUrls.add(take);
                pages.add(page);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(i==10){
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i=0;
            }else{
                i=i+1;
            }


        }
    }
}
