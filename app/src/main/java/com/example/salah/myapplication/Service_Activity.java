package com.example.salah.myapplication;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Service_Activity {
    Map<String, String> info = new HashMap<String, String>(); //回傳資料
    String url;
    Elements link;

    public Map<String, String> Showinfo() throws Exception {

        Document doc;                 //此處更改地區
        url = "http://weather.yam.com/today/";
        doc= Jsoup.connect(url).get();
        link =doc.select("body > div > table:nth-child(11) > tbody > tr > td:nth-child(3) > table:nth-child(4) > tbody > tr > td > table > tbody > tr:nth-child(8) > td:nth-child(3)");
        try {
            info.put("Conditions", link.get(0).text());//天氣狀況
        }catch (Exception ex){
            info.put("Conditions", "無");
        }
        url = "http://weather.yam.com" + doc.select("body > div > table:nth-child(11) > tbody > tr > td:nth-child(3) > table:nth-child(4) > tbody > tr > td > table > tbody > tr:nth-child(8) > td:nth-child(1) > a").attr("href");
        doc= Jsoup.connect(url).get();
        link = doc.select("body > div > table:nth-child(11) > tbody > tr > td:nth-child(3) > table:nth-child(3) > tbody > tr > td > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(3)");
        try {
        info.put("Temperature", link.get(0).text());//溫度
        }catch (Exception ex){
            info.put("Temperature", "無");
        }
        link = doc.select("body > div > table:nth-child(11) > tbody > tr > td:nth-child(3) > table:nth-child(3) > tbody > tr > td > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(5)");
        try {
            info.put("Rainfall", link.get(0).text());//降雨機率
        }catch (Exception ex){
            info.put("Rainfall", "無");
        }
        link = doc.select("body > div > table:nth-child(11) > tbody > tr > td:nth-child(3) > table:nth-child(3) > tbody > tr > td > table:nth-child(1) > tbody > tr:nth-child(3) > td:nth-child(3)");
        try {
            info.put("N_Temperature", link.get(0).text());//溫度
        }catch (Exception ex){
            info.put("N_Temperature", "無");
        }
        link = doc.select("body > div > table:nth-child(11) > tbody > tr > td:nth-child(3) > table:nth-child(3) > tbody > tr > td > table:nth-child(1) > tbody > tr:nth-child(3) > td:nth-child(5)");
        try {
            info.put("N_Rainfall", link.get(0).text());//降雨機率
        }catch (Exception ex){
            info.put("N_Rainfall", "無");
        }

        return info;
    }
}