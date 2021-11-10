package com.G2T8.CS203WebApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URL;
import java.io.File;
import java.util.*; 
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@RestController
@RequestMapping("/api/v1/CMS")
public class CMSController {

    @GetMapping("/news")
    public List<String> webParsing(){

        ArrayList<String> overallPoints = new ArrayList<>();
        try {
            Document d = Jsoup.connect("https://www.mom.gov.sg/covid-19/requirements-for-safe-management-measures")
                    .timeout(6000).get();
            Element block = d.select("body").select("#mainform").select("#MainContent").first();

            // Get Last Updated Information
            Elements lastUpdated = block.getElementsByClass("content-wrapper").first().getElementsByClass("container").first().getElementsByClass("page-content").select("#pagecontent_0_documentcontent_0_DivCode").select("p");

            String lastDate = lastUpdated.get(1).text();

            overallPoints.add(lastDate);


            // Get Alert Note
            Elements alert = block.getElementsByClass("content-wrapper").first().getElementsByClass("container").first().getElementsByClass("page-content").select("#pagecontent_0_documentcontent_0_DivCode").select("div[class=alert alert--note]");

            for (Element e : alert) {
                e.getElementsByTag("sup").remove();
            }

            overallPoints.add(alert.text());

            // select elements in ordered list
            Elements round2 = block.getElementsByClass("content-wrapper").first().getElementsByClass("container").first().getElementsByClass("page-content")
                    .select("#pagecontent_0_documentcontent_0_DivCode").select("ol").first().getElementsByTag("li");
            
            // loop for point 5 for example
            for (Element bigelm : round2) {
                Element pointBlockAndChildren = bigelm;

                // String cleanup = pointBlockAndChildren.replace("a.","").replace("b.","").replace("c.","").replace("i.","").replace("ii.","").replace("iii.","").replace("iv","");

                // remove superscript
                pointBlockAndChildren.getElementsByTag("sup").remove();

                // remove child elements
                pointBlockAndChildren.getElementsByTag("ol").remove();

                // add to list
                overallPoints.add(pointBlockAndChildren.text());
            }

        } catch (Exception e) {

        }
        return overallPoints; 

    }

    

}







        
    
        

    


    

