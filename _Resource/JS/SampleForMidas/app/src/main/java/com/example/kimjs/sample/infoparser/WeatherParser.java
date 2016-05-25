package com.example.kimjs.sample.infoparser;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import com.example.kimjs.sample.artifacts.*;



//자체가 쓰레딩하지 않도록 함(다운받는곳 제외)
//파서를 호출하는 클래스에서 쓰레딩


public class WeatherParser {

	//파싱한 자료를 DB에 넣는것까지만 하는 것으로 한다.
	//현 시점을 기준으로 업데이트한다.
	//DB 내부 클래스의 경우 중복되거나 새로 추가되야하는 부분은 
	//그쪽에서 다 처리한다.
	
	private final String open_Url = "http://www.kma.go.kr/";
	
	private final String option_Location = "DFSROOT/POINT/DATA/";
	
	private final String option_Query = "wid/queryDFS.jsp?";
	
	
	//private SimpleDateFormat formater = new SimpleDateFormat("YYYYMMDDHH");
	
	public WeatherParser() {
		
	}
    
	public WeatherInfo getWeatherInfo(WeatherInfo winfo)
	{
		
		String xpos = String.valueOf(winfo.location.city_xpos); 
		String ypos = String.valueOf(winfo.location.city_ypos);
		
		long now = System.currentTimeMillis();
		Date standard_date = new Date(now);
		Date date = new Date();
		SimpleDateFormat changeDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		winfo.array_ttw.clear();
		
		try {
			standard_date = changeDateFormat.parse(changeDateFormat.format(standard_date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			String str = open_Url + option_Query + "girdx=" + xpos + "&gridy=" + ypos; 
			
			URL url = new URL(str);
			
			ReceiveXml rx = new ReceiveXml(url);
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			int eventType = parser.getEventType();
 
            boolean done = false;
			int i = -1;
            TimeToWeather ttw = null;
			long day_hour = 0;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                String temp = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        // 태그를 식별한 뒤 태그에 맞는 작업을 수행합니다.
                        name = parser.getName();
                        if (name.equalsIgnoreCase("data")){
                            if (i != -1) {
                            	winfo.array_ttw.add(ttw);
                            }
                            ttw = new TimeToWeather();
                            i++;
                        }else if (name.equalsIgnoreCase("tm")){
                        	winfo.last_update = parser.nextText();
                        }else if (name.equalsIgnoreCase("wfKor")){
                        	ttw.wfKor = parser.nextText();
                        }else if (name.equalsIgnoreCase("pop")){
                        	ttw.pop = parser.nextText();
                        }else if (name.equalsIgnoreCase("reh")){
                        	ttw.reh = parser.nextText();
                        }else if (name.equalsIgnoreCase("sky")) {
							ttw.sky = parser.nextText();
                        }else if (name.equalsIgnoreCase("hour")){
                        	
                        	temp = parser.nextText();
                        	long temp_time = (standard_date.getTime() + (Long.valueOf(temp)+day_hour)*1000*60*60);
                    		date.setTime(temp_time);
                    		ttw.hour = CurDateFormat.format(date);
                        	
                    		if(temp.equalsIgnoreCase("24")) day_hour += 24;
                        	
                    		
                        }else if (name.equalsIgnoreCase("temp")){
                        	ttw.temp = parser.nextText();
                        }
                        break;
                    
                }
                eventType = parser.next();
                
            }
            winfo.array_ttw.add(ttw);
            
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return winfo;
		
	}
	
	//대한민국 안에서 날씨 검색 가능한 모든 지역 추출
	public ArrayList<WeatherLocationInfo> getAllLocationInfo()  {
	
		
		ArrayList<WeatherLocationInfo> array_location = new ArrayList<WeatherLocationInfo>();
		
		ReceiveXml rx;
		ReceiveXml rx2;
		ReceiveXml rx3;
		
		String top_name, mdl_name, leaf_name;
		String top_code, mdl_code, leaf_code;
		String xpos, ypos;
		URL url;
		
		try {
			url = new URL(open_Url + option_Location + "top.json.txt");
			rx = new ReceiveXml(url);
			JSONObject jso = new JSONObject();
			JSONArray jsa;
			rx.start();
			rx.join();
			jsa = new JSONArray(rx.getXml()); 
			
			for(int i=0;i<jsa.length();++i)
			{
				jso = jsa.getJSONObject(i);
				top_name = jso.getString("value");
				top_code = jso.getString("code");
				url = new URL(open_Url + option_Location + "mdl." + top_code + ".json.txt");
				rx2 = new ReceiveXml(url);
				JSONObject jso2 = new JSONObject();
				JSONArray jsa2;
				rx2.start();
				rx2.join();
				jsa2 = new JSONArray(rx2.getXml());
				
				for(int j=0;j<jsa2.length();++j)
				{
					jso2 = jsa2.getJSONObject(j);
					mdl_name = jso2.getString("value");
					mdl_code = jso2.getString("code");
					url = new URL(open_Url + option_Location + "leaf." + mdl_code + ".json.txt");
					rx3 = new ReceiveXml(url);
					JSONObject jso3 = new JSONObject();
					JSONArray jsa3;
					rx3.start();
					rx3.join();
					jsa3 = new JSONArray(rx3.getXml());
					
					for (int k=0;k<jsa3.length();++k)
					{
						WeatherLocationInfo location = new WeatherLocationInfo();
						jso3 = jsa3.getJSONObject(k);
						leaf_name = jso3.getString("value");
						xpos = jso3.getString("x");
						ypos = jso3.getString("y");
						leaf_code = jso3.getString("code");
						location.top_name = top_name;
						location.mdl_name = mdl_name;
						location.leaf_name = leaf_name;
						location.city_name = leaf_name;
						location.city_code = leaf_code;
						location.city_xpos = xpos;
						location.city_ypos = ypos;
						array_location.add(location);

					}
					
				}
				
			}
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return array_location;
		
	}
	
}
