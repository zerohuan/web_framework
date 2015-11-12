package com.yjh.base.config;

import com.yjh.search.site.crawler.CrawlerConfig;
import com.yjh.search.site.index.IndexerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 搜索引擎的配置
 *
 * Created by yjh on 15-11-6.
 */
@Configuration
@SuppressWarnings("unused")
public class SearchEngineConfiguration {
    @Bean
    public CrawlerConfig crawlerConfig() {
        CrawlerConfig crawlerConfig = new CrawlerConfig();
        crawlerConfig.setFilePath("/home/yjh/data/search_engine/download/");
        crawlerConfig.setVisitedFile(crawlerConfig.getFilePath() + "/visitedUrls.txt");
        crawlerConfig.setExtendFile(crawlerConfig.getFilePath() + "/extendUrls.txt");
        crawlerConfig.setCount(Long.MAX_VALUE);
        crawlerConfig.setMinutes(60);
        crawlerConfig.setKeys("军事" + CrawlerConfig.KEY_SEPARATOR +
                        "科技" + CrawlerConfig.KEY_SEPARATOR +
                        "编程" + CrawlerConfig.KEY_SEPARATOR +
                        "技术" + CrawlerConfig.KEY_SEPARATOR
        );
        String[] seedUrls = {"http://www.guokr.com/"};
        crawlerConfig.setSeedUrls(seedUrls);
        return crawlerConfig;
    }

    @Bean
    public IndexerConfig indexerConfig() {
        IndexerConfig indexerConfig = new IndexerConfig();
        indexerConfig.setPath("/home/yjh/data/search_engine/download/");
        indexerConfig.setIndexPath("/home/yjh/data/search_engine/index/");
        return indexerConfig;
    }
}
