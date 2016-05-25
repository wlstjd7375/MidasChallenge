package com.example.kimjs.sample.infoparser;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.example.kimjs.sample.artifacts.BusInfo;
import com.example.kimjs.sample.artifacts.BusRouteInfo;
import com.example.kimjs.sample.artifacts.BusStationInfo;
import com.example.kimjs.sample.artifacts.TimeToBus;

public class BusPaser {


	//2가지 버전으로 가능함
	//서울시 기준
	//국토교통부 기준
	//현재 2개 다 키는 발급되어 있는데 인증이 안됨요...
	//여기에 들어가는게 3가지임.
	
	private static String TAG = "BusPaser";
	
	private final String serviceKey = "0x3MtlITSbQrDntCBFU0A%2FKeCpVidWB9jeKt8acPuZ0ZHSTW%2FaMiRmuCAsifYUWyYn5jBZN0xEReaIKPMSJSWQ%3D%3D"; 

	//보훈이 계정 키
	//private final String serviceKey = "tes78LfDhi0OybX8hKJXyl6ACF3A9WVN2BHQ8dhx89N3yyDVqvH6gOZViL0LAkiUE4krx7WzZqOcZ78hSEgplg%3D%3D";

	private final String open_Url = "http://ws.bus.go.kr/api/rest";
	
	
	private final String option1_Arrive = "/arrive";
	private final String option1_Station = "/stationinfo";
	private final String option1_Route = "/busRouteInfo";
	
	
	//arrive
	private final String option2_GetArrInfoByRoute = "/getArrInfoByRoute";
	
	//station
	private final String option2_GetStationByName = "/getStationByName";
	private final String option2_GetStaionsByPosList = "/getStaionsByPosList";
	private final String option2_GetRouteByStationList = "/getRouteByStationList";
	private final String option2_GetStationByUid = "/getStationByUid";
	
	//route
	private final String option2_GetStaionsByRouteList = "/getStaionByRoute";
	private final String option2_GetBusRouteList = "/getBusRouteList";
	
	
	//노선번호 목록 조회를 하고!
	//노선 목록이 출력되면 그 버스의 busRouteld를 얻을 수 있다.
	//그 루트 아이디를 기반으로
	//그 루트를 경유하는 모든 정류소의 경로를 얻는다.
	//정류소를 선택한다.
	//그럼 세팅이 완료되고
	//그 정류소와 노선을 기점으로 지나가는 버스의 시간을 계속 받아온다.

	//시간이 남으면 이 역이 가능하도록 설계한다.

	
	public BusPaser() {
		
	}
	
	//기본 정보 검색... 버스 노선과 버스스테이션을 기준으로 시간 정보를 수집함		
	public BusInfo getBusArrInfoByRoute(BusInfo binfo)
	{
		
		binfo = getStationByUid(binfo);
		
		String stId =  binfo.station.stId;  // 스테이션 정류소 아이디
		//String arsId = binfo.station.arsId;
		String busRouteId = binfo.route.busRouteId; //노선 아이디
		String ord = binfo.staOrd;
		//SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREA);
		SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.KOREA);
		
		
		binfo.array_ttb.clear();
		
		
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			String str = open_Url + option1_Arrive + option2_GetArrInfoByRoute +
					"?ServiceKey=" + serviceKey + "&stId="
					+ stId + "&busRouteId=" + busRouteId 
					+ "&ord=" +  ord;
			
			URL url = new URL(str);
			ReceiveXml rx = new ReceiveXml(url);
			
			//System.out.println(url.toString());
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			//System.out.println(rx.getXml());
			
			int eventType = parser.getEventType();
 
            boolean done = false;
            TimeToBus ttb1 = null;
			TimeToBus ttb2 = null;
            boolean bus1 = true;
            boolean bus2 = true;
			
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                String temp = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        
                        break;
                    case XmlPullParser.START_TAG:
                    	
                        name = parser.getName();
                        if (name.equalsIgnoreCase("dir")){
                            binfo.dir = parser.nextText();
                        }else if (name.equalsIgnoreCase("kals1")){
                        	temp = parser.nextText();
                        	if(!temp.equalsIgnoreCase("0")){
                        		ttb1 = new TimeToBus();
                        		long now = System.currentTimeMillis();
                        		Date date = new Date(now);
                        		long temp_time = (date.getTime() + Long.valueOf(temp)*1000);
                        		date.setTime(temp_time);
                        		ttb1.time = CurDateFormat.format(date);
                        		//ttb1.time = temp;
                        	}
                        }else if (name.equalsIgnoreCase("kals2")){
                        	temp = parser.nextText();
                        	if(!temp.equalsIgnoreCase("0")){
                        		ttb2 = new TimeToBus();
                        		long now = System.currentTimeMillis();
                        		Date date = new Date(now);
                        		long temp_time = (date.getTime() + Long.valueOf(temp)*1000);
                        		date.setTime(temp_time);
                        		ttb2.time = CurDateFormat.format(date);
                        		//ttb2.time = temp;
                        	}
                        }else if (name.equalsIgnoreCase("rtNm")){
                        	binfo.route.busRouteNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("stNm")){
                        	binfo.station.stNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("mkTm")){
                        	binfo.last_update = parser.nextText();
                        }else if (name.equalsIgnoreCase("stationNm1")){
                        	if(ttb1!=null) ttb1.sectNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("stationNm2")){
                        	if(ttb2!=null) ttb2.sectNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("vehId1")){
                        	if(ttb1!=null) ttb1.vehId = parser.nextText();
                        }else if (name.equalsIgnoreCase("vehId2")){
                        	if(ttb2!=null) ttb2.vehId = parser.nextText();
                        }else if (name.equalsIgnoreCase("isLast1")){
                        	temp = parser.nextText();
                        	if(temp.equalsIgnoreCase("-2")){
                        		bus1 = false;
                        	}
                        }else if (name.equalsIgnoreCase("isLast2")){
                        	temp = parser.nextText();
                        	if(temp.equalsIgnoreCase("-2")){
                        		bus2 = false;
                        	}
                        }
                        
                        break;
                    
                }
                eventType = parser.next();
                
            }
            
            if(ttb1!=null && bus1) binfo.array_ttb.add(ttb1);
            if(ttb2!=null && bus2) binfo.array_ttb.add(ttb2);
            
            
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
		
		Log.d(TAG,"getBusArrInfoByRoute 완료");
		return binfo;
		
	}
	
	//정류소 고유번호를 통해 해당 통과하는 노선 모두의 버스 정보 알수있음..
	//여기서 ord만 추출하여 다시 넣음.
	public BusInfo getStationByUid(BusInfo binfo)
	{
		
		//String stId =  binfo.station.stId;  // 스테이션 정류소 아이디
		String arsId = binfo.station.arsId;
		//String busRouteId = binfo.route.busRouteId; //노선 아이디
		
		
		//clear 하면 안됨.
		//binfo.array_ttb.clear();
		
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			String str = open_Url + option1_Station + option2_GetStationByUid +
					"?ServiceKey=" + serviceKey + "&arsId="
					+ arsId;
			
			URL url = new URL(str);
			ReceiveXml rx = new ReceiveXml(url);
			
			//System.out.println(url.toString());
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			//System.out.println(rx.getXml());
			
			int eventType = parser.getEventType();
 
            boolean done = false;
            boolean check = false;
			
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
         
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        
                        break;
                    case XmlPullParser.START_TAG:
                    	//아직 남아있음... 낮에 확인해볼것...
                        name = parser.getName();
                        if (name.equalsIgnoreCase("busRouteId")){
                            if(parser.nextText().equalsIgnoreCase(binfo.route.busRouteId))
                            	check = true;
                            
                        }else if(name.equalsIgnoreCase("staOrd"))
                        {
                        	if(check) {
                        		binfo.staOrd = parser.nextText();
                        		done = true;
                        	}
                        }
                        break;
                    
                }
                eventType = parser.next();
                
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
		
		Log.d(TAG,"getStationByUid 완료");
		return binfo;
		
	}
	
	//정류소 고유번호 기준으로 루트 구함
	public ArrayList<BusRouteInfo> getRouteByStationList(String arsId)
	{
		
		ArrayList<BusRouteInfo> array_rinfo = new ArrayList<BusRouteInfo>();
		
		try {
			
			
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			
			String str = open_Url + option1_Station + option2_GetRouteByStationList +
					"?ServiceKey=" + serviceKey + "&arsId=" + arsId;
			
			URL url = new URL(str);
			
			ReceiveXml rx = new ReceiveXml(url);
			
			//System.out.println(url.toString());
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			//System.out.println(rx.getXml());
			
			int eventType = parser.getEventType();
 
            int i = -1;
            boolean done = false;
            
            BusRouteInfo rinfo = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                //String temp;
         
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        
                        break;
                    case XmlPullParser.START_TAG:
                        // 태그를 식별한 뒤 태그에 맞는 작업 수행.
                    	//키 인증모듈 에러코드가 떴을 때 에러 처리
                    	name = parser.getName();
                        if (name.equalsIgnoreCase("itemList")){
                            if (i != -1) {
                            	array_rinfo.add(rinfo);
                            }
                            rinfo = new BusRouteInfo();
                            i++;
                        }else if (name.equalsIgnoreCase("busRouteId")){
                        	rinfo.busRouteId = parser.nextText();
                        }else if (name.equalsIgnoreCase("busRouteNm")){
                        	rinfo.busRouteNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("routeType")){
                        	rinfo.routeType = parser.nextText();
                        }else if (name.equalsIgnoreCase("stStationNm")){
                        	rinfo.stStationNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("edStationNm")){
                        	rinfo.edStationNm = parser.nextText();
                        }
                        break;
                    
                }
                eventType = parser.next();
                
            }
            if (i != -1) {
            	array_rinfo.add(rinfo);
            }
            
            
            
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Mynah", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d(TAG,"getRouteByStationList 완료");
		return array_rinfo;
	}
	
	//검색용 노선 이름을 기반으로 노선정보를 얻음
	public ArrayList<BusRouteInfo> getBusRouteList(String _strSrch)
	{
		
		ArrayList<BusRouteInfo> array_rinfo = new ArrayList<BusRouteInfo>();
		
		try {
			
			//String strSrch =  URLEncoder.encode(_strSrch, "utf-8");
			
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			
			String str = open_Url + option1_Route + option2_GetBusRouteList +
					"?ServiceKey=" + serviceKey + "&strSrch=" + _strSrch;
			
			URL url = new URL(str);
			
			ReceiveXml rx = new ReceiveXml(url);
			
			//System.out.println(url.toString());
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			//System.out.println(rx.getXml());
			
			int eventType = parser.getEventType();
 
            int i = -1;
            boolean done = false;
            
            BusRouteInfo rinfo = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                //String temp;
         
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        
                        break;
                    case XmlPullParser.START_TAG:
                        // 태그를 식별한 뒤 태그에 맞는 작업 수행.
                    	//키 인증모듈 에러코드가 떴을 때 에러 처리
                    	name = parser.getName();
                        if (name.equalsIgnoreCase("itemList")){
                            if (i != -1) {
                            	array_rinfo.add(rinfo);
                            }
                            rinfo = new BusRouteInfo();
                            i++;
                        }else if (name.equalsIgnoreCase("busRouteId")){
                        	rinfo.busRouteId = parser.nextText();
                        }else if (name.equalsIgnoreCase("busRouteNm")){
                        	rinfo.busRouteNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("routeType")){
                        	rinfo.routeType = parser.nextText();
                        }else if (name.equalsIgnoreCase("stStationNm")){
                        	rinfo.stStationNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("edStationNm")){
                        	rinfo.edStationNm = parser.nextText();
                        }
                        break;
                    
                }
                eventType = parser.next();
                
            }
            if (i != -1) {
            	array_rinfo.add(rinfo);
            }
            
            
            
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Mynah", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d(TAG,"getBusRouteList 완료");
		return array_rinfo;
	}
	
	
	public ArrayList<BusStationInfo> getStaionsByRouteList(String busRouteId)
	{
		
		ArrayList<BusStationInfo> array_sinfo = new ArrayList<BusStationInfo>();
		
		try {
			
			
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			
			String str = open_Url + option1_Route + option2_GetStaionsByRouteList +
					"?ServiceKey=" + serviceKey + "&busRouteId=" + busRouteId;
			
			URL url = new URL(str);
			
			ReceiveXml rx = new ReceiveXml(url);
			
			//System.out.println(url.toString());
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			//System.out.println(rx.getXml());
			
			int eventType = parser.getEventType();
 
            int i = -1;
            boolean done = false;
            
            BusStationInfo sinfo = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                //String temp;
         
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        
                        break;
                    case XmlPullParser.START_TAG:
                        // 태그를 식별한 뒤 태그에 맞는 작업 수행.
                    	//키 인증모듈 에러코드가 떴을 때 에러 처리
                    	name = parser.getName();
                        if (name.equalsIgnoreCase("itemList")){
                            if (i != -1) {
                            	array_sinfo.add(sinfo);
                            }
                            sinfo = new BusStationInfo();
                            i++;
                        }else if (name.equalsIgnoreCase("stationNo")){
                        	sinfo.arsId = parser.nextText();
                        }else if (name.equalsIgnoreCase("station")){
                        	sinfo.stId = parser.nextText();
                        }else if (name.equalsIgnoreCase("stationNm")){
                        	sinfo.stNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("gpsX")){
                        	sinfo.tmX = parser.nextText();
                        }else if (name.equalsIgnoreCase("gpsY")){
                        	sinfo.tmY = parser.nextText();
                        }
                        break;
                    
                }
                eventType = parser.next();
                
            }
            if (i != -1) {
            	array_sinfo.add(sinfo);
            }
            
            
            
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Mynah", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d(TAG,"getStaionsByRouteList 완료");
		return array_sinfo;
	}
	
	//버스 이름 정보로 검색함.
	public ArrayList<BusStationInfo> getStationByNameList(String _stSrch)
	{
		
		ArrayList<BusStationInfo> array_sinfo = new ArrayList<BusStationInfo>();
		
		try {
			
			String stSrch =  URLEncoder.encode(_stSrch, "utf-8");
			
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			
			String str = open_Url + option1_Station + option2_GetStationByName +
					"?ServiceKey=" + serviceKey + "&stSrch=" + stSrch;
			
			URL url = new URL(str);
			
			ReceiveXml rx = new ReceiveXml(url);
			
			//System.out.println(url.toString());
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			//System.out.println(rx.getXml());
			
			int eventType = parser.getEventType();
 
            int i = -1;
            boolean done = false;
            
            BusStationInfo sinfo = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                //String temp;
         
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        
                        break;
                    case XmlPullParser.START_TAG:
                        // 태그를 식별한 뒤 태그에 맞는 작업 수행.
                    	//키 인증모듈 에러코드가 떴을 때 에러 처리
                    	name = parser.getName();
                        if (name.equalsIgnoreCase("itemList")){
                            if (i != -1) {
                            	array_sinfo.add(sinfo);
                            }
                            sinfo = new BusStationInfo();
                            i++;
                        }else if (name.equalsIgnoreCase("arsId")){
                        	sinfo.arsId = parser.nextText();
                        }else if (name.equalsIgnoreCase("stId")){	
                        	sinfo.stId = parser.nextText();
                        }else if (name.equalsIgnoreCase("stNm")){
                        	sinfo.stNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("tmX")){
                        	sinfo.tmX = parser.nextText();
                        }else if (name.equalsIgnoreCase("tmY")){
                        	sinfo.tmY = parser.nextText();
                        }
                        break;
                    
                }
                eventType = parser.next();
                
            }
            if (i != -1) {
            	array_sinfo.add(sinfo);
            }
            
            
            
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Mynah", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d(TAG,"getStationByNameList 완료");
		return array_sinfo;
		
	}
	
	
	public ArrayList<BusStationInfo> getStaionsByPosList(String tmX, String tmY, String radius)
	{
		
		ArrayList<BusStationInfo> array_sinfo = new ArrayList<BusStationInfo>();
		
		try {
			
			
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			
			
			String str = open_Url + option1_Station + option2_GetStaionsByPosList +
					"?ServiceKey=" + serviceKey + "&tmX=" + tmX + "&tmY=" + tmY + "&radius=" + radius;
			
			URL url = new URL(str);
			
			ReceiveXml rx = new ReceiveXml(url);
			
			//System.out.println(url.toString());
			
			rx.start();
			rx.join();
			
			parser.setInput(new StringReader(rx.getXml()));
			
			//System.out.println(rx.getXml());
			
			int eventType = parser.getEventType();
 
            int i = -1;
            boolean done = false;
            
            BusStationInfo sinfo = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                //String temp;
         
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        
                        break;
                    case XmlPullParser.START_TAG:
                        // 태그를 식별한 뒤 태그에 맞는 작업 수행.
                    	//키 인증모듈 에러코드가 떴을 때 에러 처리
                    	name = parser.getName();
                        if (name.equalsIgnoreCase("itemList")){
                            if (i != -1) {
                            	array_sinfo.add(sinfo);
                            }
                            sinfo = new BusStationInfo();
                            i++;
                        }else if (name.equalsIgnoreCase("arsId")){
                        	sinfo.arsId = parser.nextText();
                        }else if (name.equalsIgnoreCase("stId")){	
                        	sinfo.stId = parser.nextText();
                        }else if (name.equalsIgnoreCase("stNm")){
                        	sinfo.stNm = parser.nextText();
                        }else if (name.equalsIgnoreCase("tmX")){
                        	sinfo.tmX = parser.nextText();
                        }else if (name.equalsIgnoreCase("tmY")){
                        	sinfo.tmY = parser.nextText();
                        }
                        break;
                    
                }
                eventType = parser.next();
                
            }
            if (i != -1) {
            	array_sinfo.add(sinfo);
            }
            
            
            
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Mynah", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d(TAG,"getStaionsByPosList 완료");
		
		return array_sinfo;
		
	}
	
}
