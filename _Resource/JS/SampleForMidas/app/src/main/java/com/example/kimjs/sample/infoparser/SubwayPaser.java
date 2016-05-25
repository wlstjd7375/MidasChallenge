package com.example.kimjs.sample.infoparser;


import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.kimjs.sample.artifacts.SubwayInfo;
import com.example.kimjs.sample.artifacts.TimeToSubway;
import com.example.kimjs.sample.artifacts.SubwayStationInfo;


public class SubwayPaser {
	
	
	
	private final String open_Url = "http://openAPI.seoul.go.kr:8088/";
	private final String serviceKey = "7058616c627361793637446d4b5152";  
	
	
	public SubwayPaser() {
		
		
	}
	
	public SubwayInfo getTimeTableByID(SubwayInfo sinfo)
	{
		
		String station_cd =  sinfo.station.station_cd;
		String week_tag = String.valueOf(sinfo.week_tag);
		String inout_tag = String.valueOf(sinfo.station.inout_tag);
		
		int start = 1;
		int end = 200;
		int i = 0;
		boolean done = false;
		
		sinfo.array_tts.clear();
		
		try {
			
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			
			while(i < end && !done)
			{
				String str = open_Url + serviceKey + "/xml/SearchSTNTimeTableByIDService/"
						+ String.valueOf(start) + "/" + String.valueOf(end) + "/"
						+ station_cd + "/" + week_tag + "/" + inout_tag;
				
				URL url = new URL(str);
				
				System.out.println(url.toString());
				
				ReceiveXml rx = new ReceiveXml(url);
								
				rx.start();
				rx.join();
				
				parser.setInput(new StringReader(rx.getXml()));
				
				int eventType = parser.getEventType();
	 
	            
	            TimeToSubway tts = null;
				
	            while (eventType != XmlPullParser.END_DOCUMENT && !done){
	            	String name = null;
	            	String temp = null;
	                switch (eventType){
	                    case XmlPullParser.START_DOCUMENT:
	                        
	                        break;
	                    case XmlPullParser.START_TAG:
	                        // 태그를 식별한 뒤 태그에 맞는 작업을 수행합니다.
	                        name = parser.getName();
	                        if (name.equalsIgnoreCase("row")){
	                            tts = new TimeToSubway();
	                            i++;
	                        }else if (name.equalsIgnoreCase("MESSAGE")){
	                        	temp = parser.nextText();
	                        	if(temp.equalsIgnoreCase("해당하는 데이터가 없습니다."))
	                        	{
	                        		done = true;
	                        	}
	                        }else if (name.equalsIgnoreCase("STATION_NM")){
	                        	sinfo.station.station_nm = parser.nextText();
	                        }else if (name.equalsIgnoreCase("LINE_NUM")){
	                        	sinfo.station.line_num = parser.nextText();	
	                        }else if (name.equalsIgnoreCase("TRAIN_NO")){
	                        	tts.train_no = parser.nextText();
	                        }else if (name.equalsIgnoreCase("LEFTTIME")){
	                        	tts.arr_time = parser.nextText();
	                        }else if (name.equalsIgnoreCase("SUBWAYSNAME")){
	                        	tts.subway_start_name = parser.nextText();
	                        }else if (name.equalsIgnoreCase("SUBWAYENAME")){
	                        	tts.subway_end_name = parser.nextText();
	                        }else if (name.equalsIgnoreCase("FL_FLAG")){
	                        	tts.fl_flag = parser.nextText();
	                        	sinfo.array_tts.add(tts);
	                        }else if (name.equalsIgnoreCase("list_total_count")){
	                        	end = Integer.valueOf(parser.nextText());
	                        }
	                        break;
	                    
	                }
	                eventType = parser.next();
	                
	            }
	            
	            if(i < end) start = i + 1;
				
				
			}
			
			
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
		
		return sinfo;
		
	}
	
	
	public ArrayList<SubwayStationInfo> getStationInfoByName(String station_nm) {
		
		ArrayList<SubwayStationInfo> array_station = new ArrayList<SubwayStationInfo>();
		
		
		int start = 1;
		int end = 200;
		int i = 0;
		boolean done = false;
		String temp;
		
		try {
			
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			String _station_nm =  URLEncoder.encode(station_nm, "utf-8");
			
			while(i < end && !done)
			{
				String str = open_Url + serviceKey + "/xml/SearchInfoBySubwayNameService/"
						+ String.valueOf(start) + "/" + String.valueOf(end) + "/"
						+ _station_nm;
				
				URL url = new URL(str);
				
				System.out.println(url.toString());
				
				ReceiveXml rx = new ReceiveXml(url);
								
				rx.start();
				rx.join();
				
				parser.setInput(new StringReader(rx.getXml()));
				
				int eventType = parser.getEventType();
	 
	            SubwayStationInfo station = null;
	            SubwayStationInfo station2 = null;
				
	            while (eventType != XmlPullParser.END_DOCUMENT && !done){
	            	String name = null;
	            	
	                switch (eventType){
	                    case XmlPullParser.START_DOCUMENT:
	                        
	                        break;
	                    case XmlPullParser.START_TAG:
	                        // 태그를 식별한 뒤 태그에 맞는 작업을 수행합니다.
	                        name = parser.getName();
	                        if (name.equalsIgnoreCase("row")){
	                            station = new SubwayStationInfo();
	                            station2 = new SubwayStationInfo();
	                            i++;
	                        }else if (name.equalsIgnoreCase("MESSAGE")){
	                        	temp = parser.nextText();
	                        	if(temp.equalsIgnoreCase("해당하는 데이터가 없습니다."))
	                        	{
	                        		done = true;
	                        	}
	                        }else if (name.equalsIgnoreCase("STATION_CD")){
	                        	temp = parser.nextText();
	                        	station.station_cd = temp;
	                        	station2.station_cd = temp;
	                        }else if (name.equalsIgnoreCase("STATION_NM")){
	                        	temp = parser.nextText();
	                        	station.station_nm = temp;
	                        	station2.station_nm = temp;
	                        }else if (name.equalsIgnoreCase("LINE_NUM")){
	                        	temp = parser.nextText();
	                        	station.line_num = temp;
	                        	station2.line_num = temp;
	                        }else if (name.equalsIgnoreCase("FR_CODE")){
	                        	temp = parser.nextText();
	                        	station.fr_code = temp;
	                        	station2.fr_code = temp;
	                        	station.inout_tag = "1";
	                        	station2.inout_tag = "2";
	                        	array_station.add(station);
	                        	array_station.add(station2);
	                        }else if (name.equalsIgnoreCase("list_total_count")){
	                        	end = Integer.valueOf(parser.nextText());
	                        }
	                        break;
	                    
	                }
	                eventType = parser.next();
	                
	            }
	            
	            if(i < end) start = i + 1;
				
				
			}
			
			
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
		
		return array_station;
		
	}

}
