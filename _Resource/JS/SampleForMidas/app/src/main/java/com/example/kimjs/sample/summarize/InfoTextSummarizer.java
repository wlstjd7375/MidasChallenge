package com.example.kimjs.sample.summarize;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.kimjs.sample.artifacts.*;
import com.example.kimjs.sample.database.DBManager;
import com.example.kimjs.sample.globalmanager.GlobalVariable;
import com.example.kimjs.sample.infoparser.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


//tts 음성 출력 방식을 결정짓는 중요한 싱글턴객체
//효율성을 위해 이전 문장과 이후 문장이 변경되었는지 확인하는 기능 추가
//
public class InfoTextSummarizer {

    private static InfoTextSummarizer instance;

    private int mTempdata;
    private String preTotalInfoText;
    private String mTotalInfoText;

    private Context mContext;

    private InfoTextSummarizer(Context context)
    {
        mContext = context;
        mTempdata = 0;
        preTotalInfoText = "";
        mTotalInfoText = "";
    }

    public static InfoTextSummarizer getInstance(Context context)
    {
        if(instance == null) {
            instance = new InfoTextSummarizer(context);
        }
        return instance;
    }


    public BusInfo getBusInfo()
    {
        BusInfo bInfo;
        ArrayList<BusInfo> busArrayList;

        // get current saved information from DB
        busArrayList = DBManager.getManager(mContext).getBusDBbyLog();

        if (busArrayList.size() == 0)
        {
            return null;
        }
        else
        {
            bInfo = busArrayList.get(0);
            bInfo = DBManager.getManager(mContext).getBusDB(bInfo);

            if (bInfo.array_ttb.size() == 0)
            {
                BusPaser bp = new BusPaser();
                bInfo = bp.getBusArrInfoByRoute(bInfo);
                DBManager.getManager(mContext).setBusDB(bInfo);
                bInfo = DBManager.getManager(mContext).getBusDB(bInfo);
            }
            return bInfo;
        }
    }

    public SubwayInfo getSubwayInfo()
    {
        SubwayInfo sInfo;
        ArrayList<SubwayInfo> subwayArrayList;

        // get current saved information from DB
        subwayArrayList = DBManager.getManager(mContext).getSubwayDBbyLog();

        if (subwayArrayList.size() == 0)
        {
            return null;
        }
        else
        {
            sInfo = subwayArrayList.get(0);
            sInfo = DBManager.getManager(mContext).getSubwayDB(sInfo);

            if (sInfo.array_tts.size() == 0)
            {
                SubwayPaser sp = new SubwayPaser();
                sInfo = sp.getTimeTableByID(sInfo);
                DBManager.getManager(mContext).setSubwayDB(sInfo);
                sInfo = DBManager.getManager(mContext).getSubwayDB(sInfo);
            }
            return sInfo;
        }
    }

    public int getTempData(){
        return mTempdata;
    }

    public void setTempData(int data)
    {
        mTempdata = data;
    }

    public WeatherInfo getWeatherInfo()
    {
        ArrayList<WeatherLocationInfo> weatherArrayList = DBManager.getManager(mContext).getWeatherDBbyLog();
        WeatherInfo wInfo = new WeatherInfo();

        if(weatherArrayList.size() == 0)
        {
            return null;
        }
        else
        {
            WeatherLocationInfo wLocationInfo = weatherArrayList.get(0);
            wInfo.location = wLocationInfo;
            wInfo = DBManager.getManager(mContext).getWeatherDB(wInfo);
            if(wInfo.array_ttw.size() == 0)
            {
                WeatherParser wp = new WeatherParser();
                wInfo = wp.getWeatherInfo(wInfo);
                DBManager.getManager(mContext).setWeatherDB(wInfo);
            }
            return wInfo;
        }
    }

    public ArrayList<ScheduleInfo> getScheduleInfo()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(date);

        // Request to get Schedule Information through Mynah DB
        ArrayList<ScheduleInfo> scheduleInfo;
        scheduleInfo = DBManager.getManager(mContext).getSchedulesByDateTimeDB(strDate).scheduleList;

        return scheduleInfo;
    }

    //TTS
    public String getScheduleInfoTTS(ArrayList<ScheduleInfo> sInfoList)
    {
        if(sInfoList == null)
        {
            return "";
        }
        String tts = "";
        int maxScheduleCount = 3;
        int cnt = 0;

        ArrayList<String> timeList = new ArrayList<String>();
        ArrayList<String> summaryList = new ArrayList<String>();

        for(ScheduleInfo sInfo : sInfoList)
        {
            String time = sInfo.scheduleDate + " " + sInfo.scheduleTime;
            String summary = sInfo.scheduleName;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            long curTime = System.currentTimeMillis();
            long scheduleTime = 0;
            try {
                Date date = format.parse(time);
                scheduleTime =date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long remainTime = (scheduleTime - curTime)/1000/60;

            // past schedule info
            if(remainTime < 0)
            {
                continue;
            }
            else
            {
                timeList.add(time);
                summaryList.add(summary);
                time = sInfo.scheduleTime;
                if(time.matches(".*:00"))
                {
                    time = time.replace(":00","시");
                }
                else
                {
                    time = time.replace(":","시").concat("분");
                }

                tts += time + ", " + summary + ", ";
                cnt ++;
                if(cnt == 3)
                {
                    break;
                }
            }
        }
        if(!tts.equals(""))
        {
            tts = "오늘의 스케줄, " + tts;
        }
        return tts;
    }

    public String getBusInfoTTS(BusInfo bInfo)
    {
        if (bInfo == null)
        {
            return "";
        }

        String tts = "";
        String time = "";
        String bRoute = "";

        bRoute = bInfo.route.busRouteNm;
        if (bInfo.array_ttb.size() == 0)
        {
            //time = " 운행종료, ";
            tts = bRoute + " , 버스, 차고지 대기중. ";
            return tts;
        }
        else if (bInfo.array_ttb.size() == 1)
        {
            time = getRemainedBusTime(bInfo, 0);
        }
        else
        {
            time = getRemainedBusTime(bInfo, 0);
            if(time.equals("접근중. "))
            {
                time = getRemainedBusTime(bInfo, 1);
            }
        }
        //예시 : 문장없음 / 121번 버스 운행종료 / 121번 버스 4분전 / 121번 버스 접근중
        //
        tts = bRoute + "번 버스, " + time;
        return tts;
    }

    private String getRemainedBusTime(BusInfo binfo, int pos)
    {
        long curTime = System.currentTimeMillis();

        String time = binfo.array_ttb.get(pos).time;
        Date date = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            date = date_format.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        long arriveTime = date.getTime();
        long arriveMinute = (arriveTime - curTime)/1000/60;
        if(arriveMinute == 0)
        {
            time = "접근중. ";
        }
        else
        {
            time = arriveMinute + "분 전. ";
        }

        return time;
    }

    public String getSubwayInfoTTS(SubwayInfo sInfo)
    {
        if(sInfo == null)
        {
            return "";
        }


        String tts = "";
        String time = "";
        String lineNum = sInfo.station.line_num;
        lineNum = GlobalVariable.hosunDecode(lineNum);

        String station = sInfo.station.station_nm;
        String dir = "";

        if(sInfo.array_tts.size() == 0)
        {
            //time = "운행종료, ";
        }
        else if(sInfo.array_tts.size() == 1)
        {
            time = getRemainedSubwayTime(sInfo, 0);
            dir = sInfo.array_tts.get(0).subway_end_name;
        }
        else
        {
            time = getRemainedSubwayTime(sInfo, 0);
            dir = sInfo.array_tts.get(0).subway_end_name;
            if(time.equals("접근중. "))
            {
                time = getRemainedSubwayTime(sInfo, 1);
                dir = sInfo.array_tts.get(1).subway_end_name;
            }
        }
        //예시 : 문장 없음 / 청량리역 1호선 용산행 운행종료 / 청량리역 1호선 용산행 3분전 / 청량리역 1호선 용산행 접근중
        tts = station + "역, " + lineNum + " " + dir + "행, " + time;
        return tts;
    }

    private String getRemainedSubwayTime(SubwayInfo sInfo, int pos)
    {
        Date curTime = new Date();
        SimpleDateFormat cur_format = new SimpleDateFormat("HH:mm", Locale.KOREA);
        try {
            curTime = cur_format.parse(cur_format.format(curTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        long tt = 0;
        try {
            tt = cur_format.parse(sInfo.array_tts.get(pos).arr_time).getTime() - curTime.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tt = tt/1000/60;
        String remain;
        if(tt == 0)
        {
            remain = "접근중. ";
        }
        else
        {
            remain = tt + "분 전. ";
        }

        return remain;
    }

    public String getWeatherInfoTTS(WeatherInfo wInfo)
    {
        if(wInfo == null)
        {
            return "";
        }

        String tts;
        String temper = wInfo.array_ttw.get(0).temp;
        String wfKor = wInfo.array_ttw.get(0).wfKor;
        //dummy
        //wfKor = "비";
        String rainfallProp = wInfo.array_ttw.get(0).pop;
        String temp = "";
        if(wfKor.equals("비")) temp = "우산을 챙기세요.";

        //예시 : 현재 기온 23.9도, 강수확률 32 퍼센트
        //변경
        //날씨 타입 맑으면 그냥 패스, 비가 올 확률이 높으면 '우산을 챙기세요';
        tts = "현재 기온, " + temper + "도, " + wfKor + "," + temp + ",";

        return tts;
    }

/*
    public String makeTotalTTS()
    {
        String tts = "";

        String schedule = getScheduleInfoTTS(getScheduleInfo());
        String bus = getBusInfoTTS(getBusInfo());
        String subway = getSubwayInfoTTS(getSubwayInfo());
        String weather = getWeatherInfoTTS(getWeatherInfo());
        //String gas = getGasTemperatureTTS();
        String record = "";
        String ttsList[] = {schedule, bus, subway, weather, record};
        SharedPreferences p = mContext.getSharedPreferences(ServiceAccessManager.TSTAT, Context.MODE_PRIVATE);

        int status = p.getInt("status", 31);

        for(int i=0;i<5;i++)
        {
            int v = 1 << i;
            int f = status & v;

            if(f != 0)
            {
                tts += ttsList[i];
            }
        }

        if(!tts.equals(""))
        {
            tts = DBManager.getManager(mContext).getSessionUserDB().userName + "님 ," + tts + "...";
        }

        preTotalInfoText = mTotalInfoText;
        mTotalInfoText = tts;
        return tts;

    }*/

    public boolean isUpdate()
    {
        if(preTotalInfoText.equals(mTotalInfoText))
        {
            return false;
        }
        else return true;
    }

}
