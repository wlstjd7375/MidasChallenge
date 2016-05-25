package com.example.kimjs.sample.artifacts;

public class BusRouteInfo {
	
	public String busRouteId; //노선 id
	public String busRouteNm; //노선명
	public String routeType; //노선 유형
	// 버스 노선 유형 (1:공항, 3:간선, 4:지선, 5:순환, 6:광역, 7:인천, 8:경기, 9:폐지, 0:공용)
	
	public String stStationNm; //기점
	public String edStationNm; //종점
	
	//나머지 정보는 필요없음
	
	public BusRouteInfo() {
		// TODO Auto-generated constructor stub
	}

}
