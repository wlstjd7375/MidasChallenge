package com.example.kimjs.sample.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kimjs.sample.R;
import com.example.kimjs.sample.artifacts.WeatherLocationInfo;


public class CityNameAdapter extends ArrayAdapter<WeatherLocationInfo> {

	private ViewHolder viewHolder = null;
	private LayoutInflater inflater = null;
	private ArrayList<WeatherLocationInfo> infoList = null;
	private Context mContext = null;

	public CityNameAdapter(Context c, int textViewResourceId, 
			ArrayList<WeatherLocationInfo> arrays) {
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
	public WeatherLocationInfo getItem(int position) {
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
			viewHolder.tvCityNameListRow = (TextView) v.findViewById(R.id.tvListRow);

			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvCityNameListRow.setText(getItem(position).top_name + " "
				+ getItem(position).mdl_name + " " + getItem(position).city_name);
		viewHolder.weatherLocationInfo = infoList.get(position);
		
		return v;
	}
}