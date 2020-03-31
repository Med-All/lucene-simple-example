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
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class LuceneSimpleSearch {

    public static void main(String[] args) throws IOException, ParseException {

        //New index
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();

        //Input
        String inputFilePath = "/tmp/lucene/input/input.txt";

        //Output
        String outputPath = "/tmp/lucene/output";
        File file = new File(inputFilePath);

        Directory directory = FSDirectory.open(Paths.get(outputPath));
        IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //Create a writer
        IndexWriter writer = new IndexWriter(directory, config);

        Document document = new Document();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath)) ) {

            document.add(new TextField("content", br));
            writer.addDocument(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
