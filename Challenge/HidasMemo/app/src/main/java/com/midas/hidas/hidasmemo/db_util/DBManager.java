package com.midas.hidas.hidasmemo.db_util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.midas.hidas.hidasmemo.artifacts.MemoListData;

import java.util.ArrayList;

/**
 * Created by KimJS on 2016-05-28.
 */
public class DBManager {
    private Context mContext;
    private String TAG;

    public DBManager(Context _context) {
        mContext = _context;
    }

    public ArrayList<MemoListData> getAllMemoList() {
        ArrayList<MemoListData> dataList = new ArrayList<MemoListData>();

        DBHelper dbhelper = new DBHelper(mContext);
        Cursor c = dbhelper.GET_LIST_ALL_VALUE();
        c.moveToFirst();
        Log.d(TAG, "COUNT = " + c.getCount());
        while(!c.isAfterLast()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String title  = c.getString(c.getColumnIndex("title"));
            String date = c.getString(c.getColumnIndex("date"));

            String tag = "";
            Cursor c2 = dbhelper.GET_TAG_BY_ID(id);
            c2.moveToFirst();
            Log.d(TAG, "COUNT = " + c2.getCount());
            while(!c2.isAfterLast()) {
                tag = tag + "#" + c2.getString(c2.getColumnIndex("tag"));
                c2.moveToNext();
            }


            dataList.add(new MemoListData(id, title, date, tag));

            c.moveToNext();
        }
        c.close();

        return dataList;
    }

    public ArrayList<MemoListData> getMemoListBySearch(String query) {
        ArrayList<MemoListData> dataList = new ArrayList<MemoListData>();

        DBHelper dbhelper = new DBHelper(mContext);
        Cursor c = dbhelper.GET_LIST_BY_SEARCH(query);
        c.moveToFirst();
        Log.d(TAG, "COUNT = " + c.getCount());
        while(!c.isAfterLast()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String title  = c.getString(c.getColumnIndex("title"));
            String date = c.getString(c.getColumnIndex("date"));

            String tag = "";
            Cursor c2 = dbhelper.GET_TAG_BY_ID(id);
            c2.moveToFirst();
            Log.d(TAG, "COUNT = " + c2.getCount());
            while(!c2.isAfterLast()) {
                tag = tag + "#" + c2.getString(c2.getColumnIndex("tag"));
                c2.moveToNext();
            }


            dataList.add(new MemoListData(id, title, date, tag));

            c.moveToNext();
        }
        c.close();

        return dataList;
    }

}
