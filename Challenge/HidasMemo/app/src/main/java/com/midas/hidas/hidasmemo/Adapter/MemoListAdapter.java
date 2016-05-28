package com.midas.hidas.hidasmemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midas.hidas.hidasmemo.R;
import com.midas.hidas.hidasmemo.artifacts.MemoListData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by KimJS on 2016-05-28.
 */
public class MemoListAdapter extends ArrayAdapter<MemoListData> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<MemoListData> infoList = null;
    private Context mContext = null;

    public MemoListAdapter(Context c, int textViewResourceId, ArrayList<MemoListData> arrays) {
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
    public MemoListData getItem(int position) {
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
            v = inflater.inflate(R.layout.list_item, null);
            viewHolder.mLayout = (LinearLayout) v.findViewById(R.id.llListItem);
            viewHolder.ivThumbnail = (ImageView) v.findViewById(R.id.ivThumbnail);
            viewHolder.ivMemoLock = (ImageView) v.findViewById(R.id.ivMemoLock);

            viewHolder.tvMemoTitle = (TextView) v.findViewById(R.id.tvMemoTitle);
            viewHolder.tvMemoDate = (TextView) v.findViewById(R.id.tvMemoDate);
            viewHolder.tvMemoTag = (TextView) v.findViewById(R.id.tvMemoTag);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.memoId = getItem(position).memoId;

        viewHolder.ivThumbnail.setBackgroundResource(R.mipmap.ic_launcher);
        viewHolder.ivMemoLock.setBackgroundResource(R.drawable.icon_lock);
        viewHolder.tvMemoTitle.setText(getItem(position).memoTitle);
        viewHolder.tvMemoDate.setText(getItem(position).memoDate);
        viewHolder.tvMemoTag.setText(getItem(position).memoTag);

        return v;
    }
}

