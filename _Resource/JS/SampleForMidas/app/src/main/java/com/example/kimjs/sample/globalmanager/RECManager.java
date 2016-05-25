package com.example.kimjs.sample.globalmanager;

import android.app.Activity;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.os.Environment;

import com.example.kimjs.sample.util.RehearsalAudioRecorder;

import java.io.File;
import java.io.IOException;


public class RECManager {

    private static RECManager instance;


    private String TAG = "RECManager";
    //재생기능과 녹음기능, 둘다 가능하게 해놓는다.
    public static final int STOP = 1;
    public static final int REC_ING = 2;
    public static final int PLAY_ING = 4;


    private String defaultExStoragePath;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    private RehearsalAudioRecorder mrecorder = null;

    private int flag_state = STOP;


    private CustomOnCompletionListener mCustomOnCompletionListener;

    public interface CustomOnCompletionListener {
        void onCompletion();
    }


    private RECManager() {
        defaultExStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mynah/";
    }

    public static RECManager getInstance()
    {
        if(instance == null) {
            instance = new RECManager();
        }
        return instance;
    }

    public void startRecording(String filename) {

        if(recorder == null) {
            recorder = new MediaRecorder();
        }
        recorder.reset();

        try{
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//파일포멧에 관한건 다시 봐야할듯.
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);

            File appTmpPath = new File(defaultExStoragePath);
            appTmpPath.mkdirs();
            String tempDestFile = appTmpPath.getAbsolutePath() + "/" + filename;

            recorder.setOutputFile(tempDestFile);
            recorder.prepare();
            recorder.start();
            flag_state = REC_ING;
            //토스트로 시작 상황 띄우기
            Log.d(TAG,"녹음 시작");


        }
        catch (IllegalStateException e)
        {

        }
        catch (IOException e)
        {
            Log.d(TAG,"입출력 에러");
        }

    }

    public void stopRecording()
    {
        try {
            recorder.stop();
            flag_state = STOP;
            Log.d(TAG,"녹음 중지");
        }
        finally
        {

            recorder.release();
            recorder = null;
        }
    }

    public void startPlaying(String filename)
    {
        if(player != null)
        {
            player.stop();
            player.release();
            player = null;
        }

        try{
            player = new MediaPlayer();
            //player.setDataSource(default_path + filename);

            File appTmpPath = new File(defaultExStoragePath);
            appTmpPath.mkdirs();
            String tempDestFile = appTmpPath.getAbsolutePath() + "/" + filename;

            player.setDataSource(tempDestFile);



            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    flag_state = STOP;
                    mCustomOnCompletionListener.onCompletion();
                }
            });

            player.prepare();
            player.start();
            flag_state = PLAY_ING;
            Log.d(TAG,"녹음 재생 시작");

        } catch (Exception e)
        {
            Log.d(TAG,"입출력 에러");
        }
    }

    public void stopPlaying()
    {
        if(player == null)
        {
            return;
        }
        player.stop();
        player.release();
        flag_state = STOP;
        player = null;
        Log.d(TAG,"녹음 재생 끝");

    }

    public String exsistFastRecordFile(String filename)
    {
        String str = "";

        return str;
    }


    public int getState()
    {
        return flag_state;
    }
    public String getDefaultExStoragePath() { return defaultExStoragePath; }


    public void setCustomOnCompletionListener(CustomOnCompletionListener listener)
    {
        mCustomOnCompletionListener = listener;
    }




}
