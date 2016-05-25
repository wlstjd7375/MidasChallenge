package com.example.kimjs.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.kimjs.sample.R;
import com.example.kimjs.sample.artifacts.ScheduleInfo;

import java.util.ArrayList;

public class ScheduleAdapter extends ArrayAdapter<ScheduleInfo> {

	private ViewHolder viewHolder = null;
	private LayoutInflater inflater = null;
	private ArrayList<ScheduleInfo> infoList = null;
	private Context mContext = null;

	public ScheduleAdapter(Context c, int textViewResourceId,
						   ArrayList<ScheduleInfo> arrays) {
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
	public ScheduleInfo getItem(int position) {
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
			v = inflater.inflate(R.layout.list_row_schedule, null);
			viewHolder.tvScheduleName = (TextView) v.findViewById(R.id.tvListRowScheduleName);
			viewHolder.tvScheduleTime = (TextView) v.findViewById(R.id.tvListRowScheduleTime);
			viewHolder.createdDate = getItem(position).scheduleCreatedDate;
			viewHolder.scheduleDate = getItem(position).scheduleDate;
			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvScheduleName.setText(getItem(position).scheduleName);
		viewHolder.tvScheduleTime.setText(getItem(position).scheduleTime);

		
		return v;
	}
}