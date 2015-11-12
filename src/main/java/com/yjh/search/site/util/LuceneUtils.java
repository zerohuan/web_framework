package com.yjh.search.site.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * lucene 工具类
 *
 * Created by yjh on 15-11-6.
 */
public final class LuceneUtils {
    private static final Logger logger = LogManager.getLogger();

    private LuceneUtils() {
    }

    public static FSDirectory openFSDirectory(String luceneDir) {
        FSDirectory directory = null;
        try {
            directory = FSDirectory.open(Paths.get(luceneDir));
            /**
             * 注意：isLocked方法内部会试图去获取Lock,如果获取到Lock，会关闭它，否则return false表示索引目录没有被锁，
             * 这也就是为什么unlock方法被从IndexWriter类中移除的原因
             */
            IndexWriter.isLocked(directory);
        } catch (IOException e) {
            logger.error(e);
        }
        return directory;
    }

    public static List<Document> query(IndexSearcher searcher,Query query) {
        TopDocs topDocs = null;
        List<Document> docList = null;
        try {
            topDocs = searcher.search(query, Integer.MAX_VALUE);
            ScoreDoc[] scores = topDocs.scoreDocs;
            int length = scores.length;
            if (length <= 0) {
                return Collections.emptyList();
            }
            docList = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Document doc = searcher.doc(scores[i].doc);
                docList.add(doc);
            }
        } catch (IOException e) {
            logger.error(e);
        }


        return docList;
    }
}
