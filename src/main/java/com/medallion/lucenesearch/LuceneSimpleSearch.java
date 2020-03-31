package com.medallion.lucenesearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class LuceneSimpleSearch {

    public static void main(String[] args) throws IOException, ParseException {

        //New index
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);

        //Create a writer
        IndexWriter writer = new IndexWriter(directory, config);
        Document document = new Document();

        document.add(new TextField("content", "Test run", Field.Store.YES));
        writer.addDocument(document);

        document.add(new TextField("content", "Adam test", Field.Store.YES));
        writer.addDocument(document);

        writer.close();

        //Create a search
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("content", standardAnalyzer);

        Query query = parser.parse("Test");
        TopDocs results  = searcher.search(query, 5);
        System.out.println("Hits for Test -->" + results.totalHits);

        query = parser.parse("test");
        results  = searcher.search(query, 5);
        System.out.println("Hits for test -->" + results.totalHits);

        query = parser.parse("tester");
        results  = searcher.search(query, 5);
        System.out.println("Hits for tester -->" + results.totalHits);
    }
}
