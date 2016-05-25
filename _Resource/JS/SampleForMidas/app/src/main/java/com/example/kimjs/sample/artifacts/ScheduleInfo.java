package com.example.kimjs.sample.artifacts;

/**
 * Created by KimJS on 2015-06-02.
 * 스케줄 1개를 담고있는 객체
 * 스케줄이름, 시간, 날짜
 */
public class ScheduleInfo {

    public String scheduleName;
    //hh:mm
    public String scheduleTime;
    //yyyy-mm-dd
    public String scheduleDate;
    //yyyy-mm-ddThh:mm:ss+09:00
    public String scheduleCreatedDate;

    public boolean isCorrectDate(String date)
    {
        if(date.equals(scheduleDate))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}


