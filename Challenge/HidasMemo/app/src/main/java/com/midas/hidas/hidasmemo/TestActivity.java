package com.midas.hidas.hidasmemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.midas.hidas.hidasmemo.util.HtmlParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestActivity extends AppCompatActivity {
    private String TAG = "TestActivity";
    private EditText etUrlAddress;
    private TextView tvTitle;
    private TextView tvContent;
    // 웹사이트 주소를 저장할 변수
    private String urlAddress = "http://m.news.naver.com/read.nhn?sid1=105&oid=030&aid=0002483613";

    private final int SIGNAL_UI_UPDATE = 200;

    //private Handler handler = new Handler(); // 화면에 그려주기 위한 객체
    private String title = "";
    private String contents = "";

    private Thread mThread;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case SIGNAL_UI_UPDATE:
                        if(!title.equals("")) {
                            tvTitle.setText(title);
                        }
                        if(!contents.equals("")) {
                            tvContent.setText(contents);
                        }
                        break;
                    default:
                        break;
                }
            }catch(NullPointerException npe)
            {
                Log.d(TAG,"NullPointerException");
            }
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //Get Html and Parsing
            HtmlParser htmlParser = new HtmlParser();
            htmlParser.getHtml(urlAddress, "utf-8"); //"euc-kr"
            title = htmlParser.getTitle();
            contents = htmlParser.getContents();

            //Send message to handler
            mHandler.sendEmptyMessage(SIGNAL_UI_UPDATE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        etUrlAddress = (EditText)findViewById(R.id.etUrlAddress);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvContent = (TextView)findViewById(R.id.tvContent);

        //loadHtml();
        mThread = new Thread(mRunnable);
        mThread.start();
    }
/*
    void loadHtml() { // 웹에서 html 읽어오기
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
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
                                    (conn.getInputStream(),"euc-kr"));//"utf-8"
                            while(true) {
                                String line = br.readLine();
                                if (line == null) break;
                                sb.append(line+"\n");
                            }
                            br.close(); // 스트림 해제
                        }
                        conn.disconnect(); // 연결 끊기
                        HtmlParser htmlParser = new HtmlParser(sb.toString());
                        title = htmlParser.getTitle();
                    }
                    // 값을 출력하기
                    Log.d("test", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, title);
                            tvTitle.setText(title);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작
    }
*/

}
