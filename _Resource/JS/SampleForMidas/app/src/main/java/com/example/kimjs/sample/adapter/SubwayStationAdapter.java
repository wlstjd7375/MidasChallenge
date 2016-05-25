package com.example.kimjs.sample.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kimjs.sample.R;
import com.example.kimjs.sample.artifacts.SubwayStationInfo;
import com.example.kimjs.sample.globalmanager.GlobalVariable;


public class SubwayStationAdapter extends ArrayAdapter<SubwayStationInfo> {

	private ViewHolder viewHolder = null;
	private LayoutInflater inflater = null;
	private ArrayList<SubwayStationInfo> infoList = null;
	private Context mContext = null;
	
	private boolean isFirst = true;

	public SubwayStationAdapter(Context c, int textViewResourceId, 
			ArrayList<SubwayStationInfo> arrays) {
		super(c, textViewResourceId, arrays);
		this.inflater = LayoutInflater.from(c);
		this.mContext = c;
		infoList = arrays;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public SubwayStationInfo getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {

		View v = convertview;

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_row, null);
			viewHolder.tvSubwayStationListRow = (TextView) v.findViewById(R.id.tvListRow);

			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();
		}
		
		String code = getItem(position).line_num;
		String stationName = GlobalVariable.hosunDecode(code);
		
		if(isFirst)
		{
			stationName += " 상행(내선)";
			isFirst = false;
		}
		else
		{
			stationName += " 하행(외선)";
			isFirst = true;
		}
		
		viewHolder.tvSubwayStationListRow.setText(stationName);
		viewHolder.subwayStationInfo = infoList.get(position);
		
		return v;
	}
	
}