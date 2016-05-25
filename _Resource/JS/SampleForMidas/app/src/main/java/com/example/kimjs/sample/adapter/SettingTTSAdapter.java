package com.example.kimjs.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import  com.example.kimjs.sample.R;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-06-11.
 */

//String[]  = {"name", "isChecked"}

public class SettingTTSAdapter extends ArrayAdapter<String[]> {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<String[]> infoList = null;
    private Context mContext = null;

    public SettingTTSAdapter(Context c, int textViewResourceId,
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
            v = inflater.inflate(R.layout.list_row_setting_tts, null);
            viewHolder.tvSettingTTS = (TextView)v.findViewById(R.id.tvListRowTTS);
            viewHolder.cbSettingTTS = (CheckBox)v.findViewById(R.id.cbListRowTTS);
            viewHolder.cbSettingTTS.setClickable(false);
            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvSettingTTS.setText(getItem(position)[0]);
        if(getItem(position)[1].equals("true"))
        {
            viewHolder.cbSettingTTS.setChecked(true);
        }
        else
        {
            viewHolder.cbSettingTTS.setChecked(false);
        }

        return v;
    }
}
