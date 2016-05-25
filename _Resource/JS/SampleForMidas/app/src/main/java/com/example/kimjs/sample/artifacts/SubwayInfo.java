package com.example.kimjs.sample.artifacts;

import java.util.ArrayList;

public class SubwayInfo {


	public SubwayStationInfo station;
	public String week_tag; //요일
	public String last_update; //의미없음

	public ArrayList<TimeToSubway> array_tts;
	
	public SubwayInfo() {
		station = new SubwayStationInfo();
		array_tts = new ArrayList<TimeToSubway>();
	}
	
}
