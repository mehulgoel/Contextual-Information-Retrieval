package com.naivedya.ir_system;


import javafx.util.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchFiles {

    public SearchFiles() {
    }

    public static void main(String[] args) throws Exception {
        String usage =
                "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
            System.out.println(usage);
            System.exit(0);
        }
    }

    public List<Pair <String, String> > search(String s) throws Exception {
        String index = "/home/karan/IdeaProjects/ir_system/src/main/resources/public/index";
        String field = "contents";
        String queries = null;
        boolean raw = false;

        String queryString = s;


        int hitsPerPage = 100;
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        BufferedReader in = null;
        QueryParser parser = new QueryParser(field, analyzer);

        Query query = parser.parse(queryString);

        System.out.println("Searching for: " + query.toString(field));
        searcher.search(query, null, 100);
        List< Pair <String, String> > li = doSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null,
                reader, analyzer);
        reader.close();
        return li;
    }

    public List<Pair <String, String> > doSearch(BufferedReader in, IndexSearcher searcher, Query query,
                                 int hitsPerPage, boolean raw, boolean interactive, IndexReader reader, Analyzer
                                         analyzer) throws IOException {
        TopDocs results = searcher.search(query, 5 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;
        int numTotalHits = results.totalHits;
        System.out.println(numTotalHits + " total matching documents");
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));

        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);
        List<Pair <String, String>> li = new ArrayList<>();
        for (int i = start; i < end; i++) {
            int id = hits[i].doc;
            Document doc = searcher.doc(id);
            String path = doc.get("path");

            File f = new File(path);
            System.out.println(f.getName());
            String context = "";
            try{
                String text = doc.get("contents");

                /*
                int count_query = 0;
                Pattern p = Pattern.compile("hey");
                Matcher m = p.matcher( text );
                while (m.find()) {
                    count_query++;
                }
                System.out.println(count_query);
                */
                TokenStream tokenStream = TokenSources.getAnyTokenStream(reader, id, "contents", analyzer);
                TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);
                for (TextFragment aFrag : frag) {
                    if ((aFrag.getScore() > 0)) {
                        System.out.println(aFrag.toString());
                        System.out.println("------------------------------------------------------");
                        context = context + aFrag.toString() + "<br><br>";
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
            if (path != null) {
                //System.out.println((i + 1) + ". " + path);
                int ind = path.indexOf("testdata");
                path = path.substring(ind);
                li.add(new Pair(path, context));
                String title = doc.get("title");
                if (title != null) {
                    System.out.println("   Title: " + doc.get("title"));
                }
            } else {
                System.out.println((i + 1) + ". " + "No path for this document");
            }
        }
        return li;
    }
}
