package com.example.kimjs.sample.artifacts;

import java.util.ArrayList;


public class BusInfo {
	
	//정류장을 기준으로 특정 시간의 버스 도착 목록을 알려줌.
	//고유의 정보 하나씩을 가지고 있음.
	public String staOrd; // 정류소 순번 (정류소와 간선 정보를 모두 알고 있어야함)
	public String dir; //어디행(국민대행)
	public BusRouteInfo route;
	public BusStationInfo station;
	public ArrayList<TimeToBus> array_ttb;
	public String last_update;
	
	public BusInfo() {
		route = new BusRouteInfo();
		station = new BusStationInfo();
		array_ttb = new ArrayList<TimeToBus>();
	}
	
	
	
}
