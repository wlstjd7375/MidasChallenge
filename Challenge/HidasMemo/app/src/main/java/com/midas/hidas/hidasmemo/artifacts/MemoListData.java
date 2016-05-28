package com.midas.hidas.hidasmemo.artifacts;

import java.util.ArrayList;

/**
 * Created by KimJS on 2016-05-28.
 */
public class MemoListData {
    public int memoId;
    public String memoTitle;
    public String memoDate;
    public String memoTag;

    //TODO
    // Image path

    public MemoListData(int _id, String _title, String _date, String _tag) {
        memoId = _id;
        memoTitle = _title;
        memoDate = _date;
        memoTag = _tag;
    }
}
