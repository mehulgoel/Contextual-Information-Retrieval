package com.naivedya.ir_system;
import javafx.util.Pair;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;

/**
 * Created by naivedya on 30/3/17.
 */
public class Search {
    public static void main(String[] args) {
        staticFileLocation("/public");
        get("/hello", (req, res) -> "Hello World");

        get("/", (req, res) -> {
            return new ModelAndView(null, "home.hbs");
        }, new HandlebarsTemplateEngine());

        post("/Search", (Request req, Response res) -> {
            String query = req.queryParams("query");
            System.out.println(query);
            String location = req.queryParams("location");
            System.out.println(location);
            SearchFiles sc=new SearchFiles();
            Synonyms obj=new Synonyms();

            MySpellChecker spellChecker = new MySpellChecker("/home/karan/IdeaProjects/ir_system/src/main/resources/public/dictionary/corncob_lowercase.txt");
            String correction = spellChecker.doCorrection(query);
            ArrayList<String> synonyms = obj.getSynonyms(query);
            for (String word : synonyms) {
                System.out.println(word);
            }
            if(query.equalsIgnoreCase(correction))
                correction = "";
            Map<String, Object> model = new HashMap<>();
            model.put("query", query);
            String q = "";
            StringTokenizer st = new StringTokenizer(query);
                    while (st.hasMoreTokens()) {
                        String tmp = st.nextToken();
                        if(tmp.equalsIgnoreCase("local")){
                            q=q+location+" AND ";
                        }
                        else
                        q = q + tmp + " AND ";
                    }
            q = q.substring(0, q.length()-5);
            System.out.println(q);
            List<Pair<String, String>> results=sc.search(q);
            model.put("correction", correction);
            model.put("results", results);
            model.put("count", results.size());
            model.put("synonyms", synonyms);
            model.put("size", Math.min(10, synonyms.size()));
            return new ModelAndView(model, "results_styled.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
