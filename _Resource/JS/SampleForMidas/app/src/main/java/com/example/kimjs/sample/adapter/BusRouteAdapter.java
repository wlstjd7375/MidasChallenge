//폐기 > BusStationAdapter로 변경

package com.example.kimjs.sample.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.kimjs.sample.artifacts.BusRouteInfo;

public class BusRouteAdapter extends ArrayAdapter<BusRouteInfo>{

	ArrayList<BusRouteInfo> array_bsinfo;
	
	public BusRouteAdapter(Context context, int resource,
			List<BusRouteInfo> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		array_bsinfo = new ArrayList<BusRouteInfo>();
		
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}
	
	@Override
	public int getCount() {
		return array_bsinfo.size();
	}
	
	@Override
	public BusRouteInfo getItem(int position) {
		// TODO Auto-generated method stub
		//return super.getItem(position);
		return array_bsinfo.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return super.getItemId(position);
		return position;
	}
	
	
	
}