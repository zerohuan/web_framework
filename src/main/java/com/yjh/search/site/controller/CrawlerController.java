package com.yjh.search.site.controller;

import com.yjh.base.exception.BRequestHandler;
import com.yjh.base.site.entities.BResponseData;
import com.yjh.search.site.crawler.MainCrawler;
import com.yjh.search.site.index.HtmlIndexer;
import com.yjh.search.site.service.LinkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * 控制抓取行为
 *
 * Created by yjh on 15-11-6.
 */
@Controller
@SuppressWarnings("unused")
public class CrawlerController {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private LinkService linkService;
    @Inject
    private MainCrawler crawler;
    @Inject
    private HtmlIndexer htmlIndexer;

    @RequestMapping("/startCrawl")
    @ResponseBody
    public BResponseData startCrawler(Long minutes, Integer count, String keys) { //抓取上限

        return new BRequestHandler((responseData) -> {
            if(crawler != null) {
                if(!crawler.getIsRunning().get()) {
                    new Thread(crawler).start();
                    responseData.setData(new BResponseData.BResult(true));
                } else {
                    responseData.setData(new BResponseData.BResult(false));
                }
            }
        }).execute();
    }

    @RequestMapping("/startIndex")
    @ResponseBody
    public BResponseData startIndex() {
        return new BRequestHandler((responseData) -> {
            htmlIndexer.createIndex();
            responseData.setData(new BResponseData.BResult(true));
        }).execute();
    }
}
