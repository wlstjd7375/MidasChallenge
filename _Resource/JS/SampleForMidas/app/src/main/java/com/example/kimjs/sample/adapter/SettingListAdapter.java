package com.example.kimjs.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kimjs.sample.R;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-06-11.
 */
public class SettingListAdapter extends ArrayAdapter<String[]> {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<String[]> infoList = null;
    private Context mContext = null;

    public SettingListAdapter(Context c, int textViewResourceId,
                           ArrayList<String[]> arrays) {
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
    public String[] getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_setting, null);
            viewHolder.tvSettingList = (TextView)v.findViewById(R.id.tvListRowSetting);
            viewHolder.tvSettingStatus = (TextView)v.findViewById(R.id.tvListRowSettinStatus);
            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvSettingList.setText(getItem(position)[0]);
        viewHolder.tvSettingStatus.setText(getItem(position)[1]);

        return v;
    }
}
