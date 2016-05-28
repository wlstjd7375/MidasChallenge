package com.midas.hidas.hidasmemo.util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by KimJS on 2016-05-29.
 */
public class HtmlParser {
    private String html;
    private String TAG = "HtmlParser";
    public HtmlParser() {
    }

    public void getHtml(String urlAddress, String encoding) {
        final StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(urlAddress);
            HttpURLConnection conn =
                    (HttpURLConnection)url.openConnection();// 접속
            if (conn != null) {
                conn.setConnectTimeout(2000);
                conn.setUseCaches(false);
                if (conn.getResponseCode()
                        ==HttpURLConnection.HTTP_OK){
                    //    데이터 읽기
                    BufferedReader br
                            = new BufferedReader(new InputStreamReader
                            (conn.getInputStream(), encoding));//"utf-8" or euc-kr
                    while(true) {
                        String line = br.readLine();
                        if (line == null) break;
                        sb.append(line+"\n");
                    }
                    br.close(); // 스트림 해제
                }
                conn.disconnect(); // 연결 끊기
            }
            // 값을 출력하기
            //Log.d("test", sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        html = sb.toString();
    }

    public String getTitle() {
        String title = "";
        if(!html.equals("")) {
            Document doc = Jsoup.parse(html);
            Elements eTitles = doc.select("title");
            Element eTitle = eTitles.first();
            title = eTitle.toString();
            //Log.d(TAG, title);
        }
        //TODO
        //parsing using eTitle or title

        return title;
    }

    public String getContents() {
        String contents = "";
        if(!html.equals("")) {
            Document doc = Jsoup.parse(html);
            Elements eContents = doc.select("div[id=dic_area]");
            Element eTitle = eContents.first();
            contents = eTitle.toString();
            //Log.d(TAG, contents);
        }
        //TODO
        //parsing using eTitle or title

        return contents;
    }
}

