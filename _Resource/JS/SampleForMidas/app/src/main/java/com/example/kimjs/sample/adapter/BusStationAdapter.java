package com.example.kimjs.sample.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kimjs.sample.R;
import com.example.kimjs.sample.artifacts.BusStationInfo;


public class BusStationAdapter extends ArrayAdapter<BusStationInfo> {

	private ViewHolder viewHolder = null;
	private LayoutInflater inflater = null;
	private ArrayList<BusStationInfo> infoList = null;
	private Context mContext = null;

	public BusStationAdapter(Context c, int textViewResourceId,
			ArrayList<BusStationInfo> arrays) {
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
	public BusStationInfo getItem(int position) {
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
			viewHolder.tvBusStopNameListRow = (TextView) v.findViewById(R.id.tvListRow);

			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvBusStopNameListRow.setText(getItem(position).stNm);
		viewHolder.busStationInfo = infoList.get(position);
		
		return v;
	}
}