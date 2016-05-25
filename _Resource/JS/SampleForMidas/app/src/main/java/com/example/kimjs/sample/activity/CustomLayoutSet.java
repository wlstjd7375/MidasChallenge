package com.example.kimjs.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kimjs.sample.util.MyTranslateAnimation;
import com.example.kimjs.sample.R;
import com.example.kimjs.sample.artifacts.*;
import com.example.kimjs.sample.database.DBManager;
import com.example.kimjs.sample.globalmanager.GlobalVariable;
import com.example.kimjs.sample.globalmanager.RECManager;
import com.example.kimjs.sample.summarize.InfoTextSummarizer;
import com.example.kimjs.sample.util.TransparentProgressDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomLayoutSet extends RelativeLayout {

    private static final String TAG = "CustomLayoutSet";

    private static final int SIGNAL_UI_UPDATE = 0x10001001;
    private static final int SIGNAL_UI_UPDATE_START = 0x10001011;
    private static final int SIGNAL_UI_UPDATE_FINISH = 0x10001000;

    private TransparentProgressDialog progressDialog;
    private SubwayInfo sInfo;
    private BusInfo bInfo;
    private WeatherInfo wInfo;
    private Thread mThread;

    private int width;
    private int height;
    private int real_height;
    private int margin_height;
    private int title_height;
    private int setting_height;

    //TODO
    private final int index_title = 0;
    private final int index_weather = 1;
    private final int index_schedule = 2;
    private final int index_bus = 3;
    private final int index_subway = 4;
    private final int index_setting = 5;
    private final int size = 6;

    private boolean isMoving = false;

    private ArrayList<RelativeLayout> f_list;

    private LinearLayout llTitle, llSchedule, llBus, llSubway, llWeather, llRecord,
            llRefresh, llSetting, llProgressbar, llPlaying;

    //for title
    private TextView tvTitle;
    private ImageView ivTitle;

    private TextView tvTitleText1;
    private ImageView ivIcon1;

    private TextView tvTitleText2;
    private ImageView ivIcon2;

    //private TextView tvTitleText3;
    //private ImageView ivIcon3;

    private TextView tvTitleText4;


    //for Schedule
    private ImageView ivScheduleImage;
    private LinearLayout llScheduleImage;

    private LinearLayout llScheduleFirst;
    private LinearLayout llScheduleSecond;

    private static int maxSchedules = 10;
    private static int maxPreparations = 10;

    private LinearLayout llScheduleList;
    private LinearLayout llPreparation;

    private LinearLayout llScheduleList2;
    private LinearLayout llPreparation2;

    private TextView tvSchedulesDate;
    private TextView tvSchedulesDate2;

    private TextView tvSchedules[];
    private TextView tvPreparation;
    //private CalendarManager calendarManager;
    private ArrayList<ScheduleInfo> scheduleInfo;
    private String today;
    private String nextday;

    //for Bus
    private ImageView ivBusImage;
    private LinearLayout llBusImage;

    private TextView tvBusStopName;
    private TextView tvBusDirName;
    private TextView tvBusNumName; // 121버스

    private TextView tvBusNextTime; //3
    private TextView tvBusNextText;  //분전

    private TextView tvBusNextTime2;
    private TextView tvBusNextText2;

    private LinearLayout llBusFullCut;
    private LinearLayout llBusShortCut;

    private TextView tvBusStopNameSC;
    private TextView tvBusNumAndDirSC; // 121버스 서울숲 방향

    private TextView tvBusNextTimeSC; //3
    private TextView tvBusNextTextSC;  //분전

    private TextView tvBusNextTime2SC;
    private TextView tvBusNextText2SC;

    private ArrayList<BusInfo> busArrayList;

    //for Subway
    private ImageView ivSubwayImage;
    private LinearLayout llSubwayImage;

    private TextView tvSubwayStopName; //청량리역(내가 원하는 도착역)
    private TextView tvSubwayLineName; //1호선

    private TextView tvSubwayDirName;
    private TextView tvSubwayNextTime;
    private TextView tvSubwayNextText;

    private TextView tvSubwayDirName2;
    private TextView tvSubwayNextTime2;
    private TextView tvSubwayNextText2;

    private LinearLayout llSubwayFullCut;
    private LinearLayout llSubwayShortCut;

    private TextView tvSubwayStopNameSC; //청량리역(내가 원하는 도착역)
    private TextView tvSubwayLineNameSC; //1호선

    private TextView tvSubwayDirNameSC;
    private TextView tvSubwayNextTimeSC;
    private TextView tvSubwayNextTextSC;

    private TextView tvSubwayDirName2SC;
    private TextView tvSubwayNextTime2SC;
    private TextView tvSubwayNextText2SC;



    private ArrayList<SubwayInfo> subwayArrayList;

    //for Weather
    private ImageView ivWeatherImage;
    private LinearLayout llWeatherImage;

    private TextView tvWeatherType;
    private TextView tvWeatherPlace;
    private TextView tvWeatherPlace2;
    private TextView tvWeatherTemper;
    private TextView tvWeatherReh; // 습도
    private TextView tvWeatherPop; // 강수확률
    private TextView tvWeatherTime;

    private LinearLayout llWeatherFullCut;
    private LinearLayout llWeatherShortCut;

    private TextView tvWeatherTypeSC;
    private TextView tvWeatherPlaceSC;
    private TextView tvWeatherPlace2SC;
    private TextView tvWeatherTemperSC;
    private TextView tvWeatherPopSC;

    private ArrayList<WeatherLocationInfo> weatherArrayList;


    //for record
    private TextView tvRecord;
    private ImageView ivRecord;


    //for Playing
    private TextView tvPlaying;
    private ImageView ivPlaying;


    private int now_index;

    private Context mContext;
    //private TTSManager mTTSManager;
    private Activity mActivity;


    long animationDuration = 800;

    private Interpolator interpolator = new LinearInterpolator();
/*
    private AnimationDrawable animTrain;
    private AnimationDrawable animBus;
    private AnimationDrawable animCaldendar;
    private AnimationDrawable animWeather;
*/

    private Vibrator mVib;
    private final static long vibLength = 100;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case SIGNAL_UI_UPDATE:
                        //Show Progress Dialog
                        progressDialog = new TransparentProgressDialog(mContext);
                        progressDialog = TransparentProgressDialog.show(mContext, "", ".", true, false, null);

                        mThread = new Thread(refresh);
                        mThread.start();

                        break;
                    case SIGNAL_UI_UPDATE_START:
                        //Dismiss Progress Dialog
                        if(progressDialog.isShowing())
                        {
                            progressDialog.dismiss();
                        }
                        //UI Update
                        setWeatherInfo(wInfo);
                        setBusInfo(bInfo);
                        setSubwayInfo(sInfo);

                        //Thread free
                        if(mThread.isAlive())
                        {
                            mThread.interrupt();
                        }
                        break;

                    default:
                        break;
                }
            }catch(NullPointerException npe)
            {
                Log.d(TAG,"NullPointerException");
            }
        }
    };

    public Handler getHandler()
    {
        return mHandler;
    }

    private Runnable refresh = new Runnable() {
        @Override
        public void run() {
            //Get updated informations from DB
            wInfo = InfoTextSummarizer.getInstance(mContext).getWeatherInfo();
            bInfo = InfoTextSummarizer.getInstance(mContext).getBusInfo();
            sInfo = InfoTextSummarizer.getInstance(mContext).getSubwayInfo();

            //Send message to handler
            mHandler.sendEmptyMessageDelayed(SIGNAL_UI_UPDATE_START, 500);
        }
    };

    //Constructor
    public CustomLayoutSet(final Context context, Activity activity) {

        super(context);

        mContext = context;
        mActivity = activity;

        f_list = new ArrayList<RelativeLayout>();
        //mTTSManager = new TTSManager(mContext);

        mVib = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);

        for(int i=0;i<size;i++)
        {
            RelativeLayout f = new RelativeLayout(context);
            f.setTag(i);
            f_list.add(f);
        }

        post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();

                title_height = (int) height / 20;
                setting_height = (int) height / 10;

                int temp_height = height - title_height - setting_height;

                margin_height = (int) (temp_height / (size - 2 + 2));
                real_height = margin_height * 3;

                initWithDimentions(context);
                initMainLayout();

                initTitleView();
                initWeatherView();
                initBusView();
                initSubwayView();
                initScheduleView();
                initPlayingView();
                initRecordView();

                //기본이 날씨 0임.
                now_index = 1;
                changeWeatherView(0);
                changeBusView(1);
                changeScheduleView(1);
                changeSubwayView(1);

                initClickListener();
                initAnimation();

                changeStatusText();
                changeUpdateTime();

                setMarquee();
            }
        });
    }

    public void changeStatusText()
    {
        //String text = "";
        if(GlobalVariable.isServerOn)
        {
            //text = "Server";
            ivIcon1.setImageResource(R.drawable.ic_v);
        }
        else
        {
            //text = "Server";
            ivIcon1.setImageResource(R.drawable.ic_x);
        }
        //tvTitleText1.setText(text);

        if(GlobalVariable.isBluetoothOn)
        {
            //text = " Bluetooth";
            ivIcon2.setImageResource(R.drawable.ic_v);
        }
        else
        {
            //text = " Bluetooth";
            ivIcon2.setImageResource(R.drawable.ic_x);
        }
    }

    public void changeUpdateTime()
    {
        //TODO 일단은 디폴트로 시간 없는걸로 표기
        tvTitleText4.setText("");
    }


    private void initAnimation()
    {
        //ivBusImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.bus_anim, 100, 100));
        //ivBusImage.setBackgroundResource(R.drawable.bus_anim);
        //ivBusImage.setScaleX((float) 0.7);
        //ivBusImage.setScaleY((float) 0.7);
        //animBus = (AnimationDrawable) ivBusImage.getBackground();
        //animBus = (AnimationDrawable) ivBusImage.getDrawable();


        //ivWeatherImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.sun_anim, 100, 100));
        ivWeatherImage.setBackgroundResource(R.drawable.sun_anim);
        AnimationDrawable animWeather =  (AnimationDrawable) ivWeatherImage.getBackground();
        animWeather.start();

//        ivWeatherImage.setScaleX((float)0.7);
//        ivWeatherImage.setScaleY((float)0.7);
        //animWeather = (AnimationDrawable) ivWeatherImage.getBackground();
        //animWeather = (AnimationDrawable) ivWeatherImage.getDrawable();


        //ivScheduleImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.calendar_anim, 100, 100));
        //ivScheduleImage.setBackgroundResource(R.drawable.calendar_anim);
//        ivScheduleImage.setScaleX((float) 0.7);
//        ivScheduleImage.setScaleY((float) 0.7);
        //animCaldendar = (AnimationDrawable) ivScheduleImage.getBackground();
        //animCaldendar = (AnimationDrawable) ivScheduleImage.getDrawable();


        //ivSubwayImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.train_anim, 100, 100));
        //ivSubwayImage.setBackgroundResource(R.drawable.train_anim);
//        ivSubwayImage.setScaleX((float) 0.7);
//        ivSubwayImage.setScaleY((float) 0.7);
        //animTrain = (AnimationDrawable) ivSubwayImage.getBackground();
       //animTrain = (AnimationDrawable) ivSubwayImage.getDrawable();

    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        //옵션 조정.
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;
        options.inPurgeable = true;

        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public void startAnimationOnFocusChanged()
    {
        for(int i=1;i<size-1;i++)
        {
            if(i==now_index) showAnimation(i,true);
            else showAnimation(i,false);
        }
    }


    private void aniStopRecycle(AnimationDrawable ad)
    {
        for (int i = 0; i < ad.getNumberOfFrames(); ++i){
            Drawable frame = ad.getFrame(i);
            if (frame instanceof BitmapDrawable) {
                ((BitmapDrawable)frame).getBitmap().recycle();
            }
            frame.setCallback(null);
        }
        ad.setCallback(null);
    }


    private void showAnimation(int type, boolean on_off)
    {
        switch (type)
        {
            case index_weather:
                if(on_off){
                    ivWeatherImage.setBackgroundResource(R.drawable.sun_anim);
                    llWeatherImage.setVisibility(VISIBLE);
                    AnimationDrawable animWeather = (AnimationDrawable) ivWeatherImage.getBackground();
                    animWeather.start();
                }else {
                    llWeatherImage.setVisibility(INVISIBLE);
                    AnimationDrawable animWeather = (AnimationDrawable) ivWeatherImage.getBackground();
                    animWeather.stop();
                    ivWeatherImage.setBackgroundResource(0);
                    //aniStopRecycle(animWeather);
                }
                break;
            case index_subway:
                if(on_off){
                    ivSubwayImage.setBackgroundResource(R.drawable.train_anim);
                    llSubwayImage.setVisibility(VISIBLE);
                    AnimationDrawable animTrain = (AnimationDrawable) ivSubwayImage.getBackground();
                    animTrain.start();
                }else {
                    llSubwayImage.setVisibility(INVISIBLE);
                    AnimationDrawable animTrain = (AnimationDrawable) ivSubwayImage.getBackground();
                    animTrain.stop();
                    ivSubwayImage.setBackgroundResource(0);
                    //aniStopRecycle(animTrain);
                }
                break;
            case index_schedule:
                if(on_off){
                    ivScheduleImage.setBackgroundResource(R.drawable.calendar_anim);
                    llScheduleImage.setVisibility(VISIBLE);
                    AnimationDrawable animCaldendar = (AnimationDrawable) ivScheduleImage.getBackground();
                    animCaldendar.start();
                }else{
                    llScheduleImage.setVisibility(INVISIBLE);
                    AnimationDrawable animCaldendar = (AnimationDrawable) ivScheduleImage.getBackground();
                    animCaldendar.stop();
                    ivSubwayImage.setBackgroundResource(0);
                    //aniStopRecycle(animCaldendar);
                }
                break;
            case index_bus:
                if(on_off) {
                    ivBusImage.setBackgroundResource(R.drawable.bus_anim);
                    llBusImage.setVisibility(VISIBLE);
                    AnimationDrawable animBus = (AnimationDrawable) ivBusImage.getBackground();
                    animBus.start();
                }else{
                    llBusImage.setVisibility(INVISIBLE);
                    AnimationDrawable animBus = (AnimationDrawable) ivBusImage.getBackground();
                    animBus.stop();
                    ivBusImage.setBackgroundResource(0);
                    //aniStopRecycle(animBus);
                }
                break;

            default:
                break;
        }
    }



    private void initClickListener()
    {
        final Class[] arr = {null, WeatherSettingActivity.class, null, BusSettingActivity.class, SubwaySettingActivity.class};
        //title하고 option 클릭 이벤트 삭제
        for(int i = 1; i < size-1; i++)
        {
            f_list.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((int)v.getTag() == now_index)
                    {
                        mVib.vibrate(vibLength);
                        Intent intent = new Intent(mContext, arr[now_index]);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        showView((int) v.getTag());
                    }
                }
            });
        }
    }


    private void initWithDimentions(Context context) {

        View v_title = new View(context);
        View v_weather = new View(context);
        View v_bus = new View(context);
        View v_schedule = new View(context);
        View v_subway = new View(context);
        View v_option = new View(context);

        v_title = inflate(context,R.layout.layout_title,f_list.get(index_title));
        v_weather = inflate(context,R.layout.layout_weather,f_list.get(index_weather));
        v_schedule = inflate(context,R.layout.layout_schedule,f_list.get(index_schedule));
        v_bus = inflate(context,R.layout.layout_bus,f_list.get(index_bus));
        v_subway = inflate(context, R.layout.layout_subway,f_list.get(index_subway));
        v_option = inflate(context,R.layout.layout_option, f_list.get(index_setting));


        for(int i =0;i<size;i++) {
            f_list.get(i).setId(i);
        }

        RelativeLayout container = new RelativeLayout(context);

        LayoutParams containerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        container.setLayoutParams(containerParams);
        containerParams.setMargins(0, 0, 0, 0);


        container.addView(f_list.get(0));
        container.addView(f_list.get(1));
        container.addView(f_list.get(2));
        container.addView(f_list.get(3));
        container.addView(f_list.get(4));
        container.addView(f_list.get(5));


        LayoutParams params0 = new LayoutParams(LayoutParams.MATCH_PARENT, title_height);
        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, real_height);
        LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, real_height);
        LayoutParams params3 = new LayoutParams(LayoutParams.MATCH_PARENT, real_height);
        LayoutParams params4 = new LayoutParams(LayoutParams.MATCH_PARENT, real_height);
        LayoutParams params5 = new LayoutParams(LayoutParams.MATCH_PARENT, setting_height+5);

        params0.setMargins(0, 0, 0, 0);
        f_list.get(0).setLayoutParams(params0);

        //params1.addRule(RelativeLayout.BELOW, 0);
        params1.setMargins(0, title_height, 0, 0);
        f_list.get(1).setLayoutParams(params1);


        //params2.addRule(RelativeLayout.BELOW, 1);
        params2.setMargins(0, real_height + title_height, 0, 0);
        f_list.get(2).setLayoutParams(params2);


        //params3.addRule(RelativeLayout.BELOW, 2);
        params3.setMargins(0, real_height + margin_height + title_height, 0, 0);
        f_list.get(3).setLayoutParams(params3);


        //params4.addRule(RelativeLayout.BELOW, 3);
        params4.setMargins(0, real_height + margin_height * 2 + title_height, 0, 0);

        f_list.get(4).setLayoutParams(params4);


        params5.setMargins(0, real_height + margin_height * 3 + title_height, 0,0);
        f_list.get(5).setLayoutParams(params5);

        addView(container);

    }

    private void initMainLayout()
    {

        //default type에 대해서 서술
        llTitle = (LinearLayout)findViewById(R.id.llTitle);
        llSchedule = (LinearLayout)findViewById(R.id.llSchedule);
        llBus = (LinearLayout)findViewById(R.id.llBus);
        llSubway = (LinearLayout)findViewById(R.id.llSubway);
        llWeather = (LinearLayout)findViewById(R.id.llWeather);
        llRecord = (LinearLayout)findViewById(R.id.llRecord);
        llRefresh = (LinearLayout)findViewById(R.id.llRefresh);
        llSetting = (LinearLayout)findViewById(R.id.llSetting);
        llProgressbar = (LinearLayout)findViewById(R.id.llProgressbar);
        llPlaying = (LinearLayout)findViewById(R.id.llPlaying);

    }

    private void initTitleView()
    {
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitleText1 = (TextView)findViewById(R.id.tvTitleText1);
        tvTitleText2 = (TextView)findViewById(R.id.tvTitleText2);
        //tvTitleText3 = (TextView)findViewById(R.id.tvTitleText3);
        tvTitleText4 = (TextView)findViewById(R.id.tvTitleText4);

        ivIcon1 = (ImageView)findViewById(R.id.ivIcon1);
        ivIcon2 = (ImageView)findViewById(R.id.ivIcon2);
        //ivIcon3 = (ImageView)findViewById(R.id.ivIcon3);

    }


    //SCHEDULE
    private void initScheduleView()
    {

        llScheduleFirst = (LinearLayout) findViewById(R.id.llScheduleFirst);
        llScheduleSecond = (LinearLayout) findViewById(R.id.llScheduleSecond);

        layoutSizeChange(llScheduleFirst,real_height * 9 / 10);
        layoutSizeChange(llScheduleSecond,real_height * 9 / 10);

        llScheduleList = (LinearLayout) findViewById(R.id.llScheduleList);
        llScheduleList2 = (LinearLayout) findViewById(R.id.llScheduleList2);

        tvSchedulesDate = (TextView) findViewById(R.id.tvScheduleDate);
        tvSchedulesDate2 = (TextView) findViewById(R.id.tvScheduleDate2);

        ivScheduleImage = (ImageView) findViewById(R.id.ivScheduleImage);
        llScheduleImage = (LinearLayout) findViewById(R.id.llScheduleImage);

        tvSchedules = new TextView[maxSchedules];
        tvPreparation = new TextView(mContext);

        //calendarManager = GlobalGoogleCalendarManager.calendarManager;
        int tableCount = DBManager.getManager(mContext).getSchedulesCount();

        if(tableCount == 0)
        {
            llScheduleList.removeAllViews();
            tvSchedules[0] = new TextView(mContext);
            tvSchedules[0].setTextColor(Color.parseColor("#ffffff"));
            tvSchedules[0].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvSchedules[0].setText("길게 터치하여 구글캘린더와 연동을 시작하세요.");

            llScheduleList.setGravity(Gravity.CENTER);
            llScheduleList.addView(tvSchedules[0]);
        }

    }

    public void scheduleRefresh()
    {
        //Request service to get schedule information (through Mynah DB)
        //ArrayList<ScheduleInfo> scheduleInfos = ServiceAccessManager.getInstance().getService().getScheduleInfo();
        ArrayList<ScheduleInfo> scheduleInfos = InfoTextSummarizer.getInstance(mContext).getScheduleInfo();

        if(scheduleInfos == null)
        {
            scheduleInfos = new ArrayList<ScheduleInfo>();
        }
        setScheduleInfo(scheduleInfos);
    }

    public void setScheduleInfo(ArrayList<ScheduleInfo> scheduleInfos)
    {
        int size = scheduleInfos.size();

        String str_date = "";
        String str_next_date = "";

        Date date = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("MM월dd일");
        str_date = date_format.format(date);

        long cDate = date.getTime();
        cDate += (1000*60*60*24);
        date.setTime(cDate);

        str_next_date = date_format.format(date);

        tvSchedulesDate.setText(str_date);
        tvSchedulesDate2.setText(str_next_date);

        llScheduleList.removeAllViews();
        llScheduleList.setGravity(Gravity.NO_GRAVITY);
        if(size == 0)
        {
            tvSchedules[0] = new TextView(mContext);
            tvSchedules[0].setTextColor(Color.parseColor("#ffffff"));
            tvSchedules[0].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvSchedules[0].setText("등록된 스케줄이 없습니다.");

            //llScheduleList.setGravity(Gravity.CENTER);
            llScheduleList.addView(tvSchedules[0]);
        }

        for(int i = 0; i < size; i++)
        {
            tvSchedules[i] = new TextView(mContext);
            tvSchedules[i].setTextColor(Color.parseColor("#ffffff"));
            tvSchedules[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            String str = scheduleInfos.get(i).scheduleTime + "    " + scheduleInfos.get(i).scheduleName;
            tvSchedules[i].setText(str);

            tvSchedules[0].setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvSchedules[0].setSelected(true);
            tvSchedules[0].setSingleLine();

            llScheduleList.addView(tvSchedules[i]);
        }
    }

    public void changeScheduleView(int type)
    {
        if(type == 0) //focusing
        {
            llScheduleList.setVisibility(VISIBLE);
            llScheduleList2.setVisibility(VISIBLE);
        }
        else //not
        {
            llScheduleList.setVisibility(VISIBLE);
            llScheduleList2.setVisibility(VISIBLE);
        }
    }


    //init BUS
    private void initBusView() {
        Log.d(TAG,"initBusView start");

        llBusShortCut = (LinearLayout) findViewById(R.id.llBusShortCut);
        layoutSizeChange(llBusShortCut, margin_height * 9 / 10);

        llBusFullCut = (LinearLayout) findViewById(R.id.llBusFullCut);
        layoutSizeChange(llBusFullCut,real_height * 9 / 10);

        ivBusImage = (ImageView) findViewById(R.id.ivBusImage);
        llBusImage = (LinearLayout) findViewById(R.id.llBusImage);

        //full cut
        tvBusStopName = (TextView)findViewById(R.id.tvBusStopName);
        tvBusDirName = (TextView) findViewById(R.id.tvBusDirName);
        tvBusNumName = (TextView) findViewById(R.id.tvBusNumName);

        tvBusNextTime = (TextView) findViewById(R.id.tvBusNextTime);
        tvBusNextText = (TextView) findViewById(R.id.tvBusNextText);
        tvBusNextTime2 = (TextView) findViewById(R.id.tvBusNextTime2);
        tvBusNextText2 = (TextView) findViewById(R.id.tvBusNextText2);

        //short cut
        tvBusStopNameSC = (TextView)findViewById(R.id.tvBusStopNameSC);
        tvBusNumAndDirSC = (TextView) findViewById(R.id.tvBusNumAndDirSC);

        tvBusNextTimeSC = (TextView) findViewById(R.id.tvBusNextTimeSC);
        tvBusNextTextSC = (TextView) findViewById(R.id.tvBusNextTextSC);
        tvBusNextTime2SC = (TextView) findViewById(R.id.tvBusNextTime2SC);
        tvBusNextText2SC = (TextView) findViewById(R.id.tvBusNextText2SC);



        Log.d(TAG, "initBusView end");
    }

    private void clearBusInfo()
    {
        //full cut
        tvBusNumName.setText("");
        tvBusStopName.setText("");
        tvBusDirName.setText("");
        tvBusNextTime.setText("");
        tvBusNextText.setText("");
        tvBusNextTime2.setText("");
        tvBusNextText2.setText("");

        //short cut
        tvBusStopNameSC.setText("");
        tvBusNumAndDirSC.setText("");
        tvBusNextTimeSC.setText("");
        tvBusNextTextSC.setText("");
        tvBusNextTime2SC.setText("");
        tvBusNextText2SC.setText("");
    }


    private void setBusInfo(BusInfo binfo)  {

        String bRoute;
        String bDir;
        String bStation;
        String time1, text1;
        String time2, text2;

        clearBusInfo();

        time1 = ""; text1 = "";
        time2 = ""; text2 = "";

        if (binfo == null)
        {
            bRoute = "";
            bStation = "";
            bDir = "길게 터치해서 정보를 입력하세요.";

            tvBusDirName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            //tvBusDirNameSC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        }
        else
        {
            tvBusDirName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            //tvBusDirNameSC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            bRoute = binfo.route.busRouteNm + " 버스";
            bStation = binfo.station.stNm;
            bDir = binfo.dir + " 방향";

            if (binfo.array_ttb.size() == 0)
            {
                time1 = "";
                text1 = "차고지 대기 중";
            }
            else if (binfo.array_ttb.size() == 1)
            {
                time1 = getBusArriveTime(binfo, 0);
                if(time1.compareTo("0")==0) {
                    time1 = "접근";
                    text1 = " 중";
                }
                else {
                    text1 = " 분 전";
                }
            }
            else
            {
                time1 = getBusArriveTime(binfo, 0);
                if(time1.compareTo("0")==0) {
                    time1 = "접근";
                    text1 = " 중";
                }
                else {
                    text1 = " 분 전";
                }
                time2 = getBusArriveTime(binfo, 1);
                if(time2.compareTo("0")==0) {
                    time2 = "접근";
                    text2 = " 중";
                }
                else {
                    text2 = " 분 전";
                }
            }
        }

        //full cut
        tvBusNumName.setText(bRoute);
        tvBusStopName.setText(bStation);
        tvBusDirName.setText(bDir);
        tvBusNextTime.setText(time1);
        tvBusNextText.setText(text1);
        tvBusNextTime2.setText(time2);
        tvBusNextText2.setText(text2);

        //short cut
        tvBusStopNameSC.setText(bStation);
        tvBusNumAndDirSC.setText(bRoute + " " + bDir);
        tvBusNextTimeSC.setText(time1);
        tvBusNextTextSC.setText(text1);
        tvBusNextTime2SC.setText(time2);
        tvBusNextText2SC.setText(text2);

    }

    private String getBusArriveTime(BusInfo binfo, int pos)
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
        time = arriveMinute + "";
        return time;
    }

    public void changeBusView(int type)
    {
        if(type == 0)
        {
            llBusFullCut.setVisibility(VISIBLE);
            llBusShortCut.setVisibility(GONE);
        }
        else
        {
            llBusFullCut.setVisibility(GONE);
            llBusShortCut.setVisibility(VISIBLE);
        }
    }


    //SUBWAY
    private void initSubwayView() {
        Log.d(TAG,"initSubwayView start");

        llSubwayFullCut = (LinearLayout)findViewById(R.id.llSubwayFullCut);
        layoutSizeChange(llSubwayFullCut,real_height*9/10);
        llSubwayShortCut = (LinearLayout)findViewById(R.id.llSubwayShortCut);
        layoutSizeChange(llSubwayShortCut,margin_height*9/10);

        ivSubwayImage = (ImageView) findViewById(R.id.ivSubwayImage);
        llSubwayImage = (LinearLayout) findViewById(R.id.llSubwayImage);
        //full cut

        tvSubwayLineName = (TextView)findViewById(R.id.tvSubwayLineName);
        tvSubwayStopName = (TextView)findViewById(R.id.tvSubwayStopName);

        tvSubwayDirName = (TextView)findViewById(R.id.tvSubwayDirName);
        tvSubwayNextTime = (TextView)findViewById(R.id.tvSubwayNextTime);
        tvSubwayNextText = (TextView)findViewById(R.id.tvSubwayNextText);

        tvSubwayDirName2 = (TextView) findViewById(R.id.tvSubwayDirName2);
        tvSubwayNextTime2 = (TextView)findViewById(R.id.tvSubwayNextTime2);
        tvSubwayNextText2 = (TextView)findViewById(R.id.tvSubwayNextText2);

        //short cut

        tvSubwayLineNameSC = (TextView)findViewById(R.id.tvSubwayLineNameSC);
        tvSubwayStopNameSC = (TextView)findViewById(R.id.tvSubwayStopNameSC);

        tvSubwayDirNameSC = (TextView)findViewById(R.id.tvSubwayDirNameSC);
        tvSubwayNextTimeSC = (TextView)findViewById(R.id.tvSubwayNextTimeSC);
        tvSubwayNextTextSC = (TextView)findViewById(R.id.tvSubwayNextTextSC);

        tvSubwayDirName2SC = (TextView)findViewById(R.id.tvSubwayDirName2SC);
        tvSubwayNextTime2SC = (TextView)findViewById(R.id.tvSubwayNextTime2SC);
        tvSubwayNextText2SC = (TextView)findViewById(R.id.tvSubwayNextText2SC);

        Log.d(TAG, "initSubwayView end");
    }

    private void clearSubwayInfo()
    {

        //full cut
        tvSubwayStopName.setText("");
        tvSubwayLineName.setText("");

        tvSubwayNextTime.setText("");
        tvSubwayNextText.setText("");
        tvSubwayDirName.setText("");

        tvSubwayNextTime2.setText("");
        tvSubwayNextText2.setText("");
        tvSubwayDirName2.setText("");


        //short cut
        tvSubwayStopNameSC.setText("");
        tvSubwayLineNameSC.setText("");

        tvSubwayNextTimeSC.setText("");
        tvSubwayNextTextSC.setText("");
        tvSubwayDirNameSC.setText("");

        tvSubwayNextTime2SC.setText("");
        tvSubwayNextText2SC.setText("");
        tvSubwayDirName2SC.setText("");

    }


    private void setSubwayInfo(SubwayInfo sinfo) {

        clearSubwayInfo();

        String stopname, linename;
        String dirname1, time1, text1;
        String dirname2, time2, text2;
        String tag = "";

        stopname = ""; linename = "";
        dirname1 = ""; time1 = ""; text1 = "";
        dirname2 = ""; time2 = ""; text2 = "";

        if (sinfo == null) {
            // 초기화
            linename = "길게 터치해서 정보를 입력하세요.";

            tvSubwayLineName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tvSubwayLineNameSC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        }
        else
        {

            tvSubwayLineName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            tvSubwayLineNameSC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            if(sinfo.station.inout_tag.equalsIgnoreCase("1"))
            {
                //TODO 상행 내선
                tag = "상행(내선)";

            }
            else if (sinfo.station.inout_tag.equalsIgnoreCase("2"))
            {
                //TODO 하행 외선
                tag = "하행(외선)";
            }
            linename = sinfo.station.line_num + "호선" + " " + tag;
            stopname = sinfo.station.station_nm + "역";


            Date curTime = new Date();
            SimpleDateFormat cur_format = new SimpleDateFormat("HH:mm", Locale.KOREA);

            try {
                curTime = cur_format.parse(cur_format.format(curTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dirname1 = sinfo.array_tts.get(0).subway_end_name + "행";

            if (sinfo.array_tts.size() == 0) {

            }
            else
            {
                long tt = 0;
                try {
                    tt = cur_format.parse(sinfo.array_tts.get(0).arr_time).getTime() - curTime.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tt = tt/1000/60;
                if(tt >= 1000) {
                    time1 = "";
                    text1 = "종착역입니다.";
                } else if(tt==0) {
                    time1 = "";
                    text1 = "지금 도착";
                } else {
                    time1 = tt + "";
                    text1 = " 분 전";
                }
                dirname1 = sinfo.array_tts.get(0).subway_end_name + "행";

                if (sinfo.array_tts.size() == 1)
                {

                }
                else
                {
                    long tt2 = 0;
                    try {
                        tt2 = cur_format.parse(sinfo.array_tts.get(1).arr_time).getTime() - curTime.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tt2 = tt2/1000/60;
                    if(tt2 == 0) {
                        //time2 = "지금 도착";
                        time2 = "";
                        text2 = "지금 도착";
                    } else
                    {
                        time2 = tt2 + "";
                        text2 = " 분 전";
                    }
                    dirname2 = sinfo.array_tts.get(1).subway_end_name + "행";
                }
            }
        }

        //full cut
        tvSubwayStopName.setText(stopname);
        tvSubwayLineName.setText(linename);

        tvSubwayNextTime.setText(time1);
        tvSubwayNextText.setText(text1);
        tvSubwayDirName.setText(dirname1);

        tvSubwayNextTime2.setText(time2);
        tvSubwayNextText2.setText(text2);
        tvSubwayDirName2.setText(dirname2);

        //short cut
        tvSubwayStopNameSC.setText(stopname);
        tvSubwayLineNameSC.setText(linename);

        tvSubwayNextTimeSC.setText(time1);
        tvSubwayNextTextSC.setText(text1);
        tvSubwayDirNameSC.setText(dirname1);

        tvSubwayNextTime2SC.setText(time2);
        tvSubwayNextText2SC.setText(text2);
        tvSubwayDirName2SC.setText(dirname2);

    }

    public void changeSubwayView(int type)
    {
        if(type ==0)
        {
            llSubwayFullCut.setVisibility(VISIBLE);
            llSubwayShortCut.setVisibility(GONE);
        }
        else
        {
            llSubwayFullCut.setVisibility(GONE);
            llSubwayShortCut.setVisibility(VISIBLE);
        }

    }


    //WEATHER
    private void initWeatherView() {
        Log.d(TAG, "initWeatherView Start");

        llWeatherFullCut = (LinearLayout) findViewById(R.id.llWeatherFullCut);
        llWeatherShortCut = (LinearLayout) findViewById(R.id.llWeatherShortCut);
        layoutSizeChange(llWeatherFullCut,real_height*9/10);
        layoutSizeChange(llWeatherShortCut,margin_height*9/10);

        ivWeatherImage = (ImageView)findViewById(R.id.ivWeatherImage);
        llWeatherImage = (LinearLayout) findViewById(R.id.llWeatherImage);

        tvWeatherType = (TextView)findViewById(R.id.tvWeatherType);
        tvWeatherPlace = (TextView)findViewById(R.id.tvWeatherPlace);
        tvWeatherPlace2 = (TextView) findViewById(R.id.tvWeatherPlace2);
        tvWeatherTemper = (TextView) findViewById(R.id.tvWeatherTemper);
        tvWeatherPop = (TextView) findViewById(R.id.tvWeatherPop);
        tvWeatherReh = (TextView) findViewById(R.id.tvWeatherReh);
        tvWeatherTime = (TextView) findViewById(R.id.tvWeatherTime);

        tvWeatherTypeSC = (TextView)findViewById(R.id.tvWeatherTypeSC);
        tvWeatherPlaceSC = (TextView)findViewById(R.id.tvWeatherPlaceSC);
        tvWeatherPlace2SC = (TextView) findViewById(R.id.tvWeatherPlace2SC);
        tvWeatherTemperSC = (TextView) findViewById(R.id.tvWeatherTemperSC);
        tvWeatherPopSC = (TextView) findViewById(R.id.tvWeatherPopSC);

        setButtonsMarquee();

        Log.d(TAG, "initWeatherView End");
    }

    public void weatherRefresh() {
        Log.d(TAG, "weatherRefresh Start");

        // Request service to get weather Information
        //WeatherInfo wInfo = ServiceAccessManager.getInstance().getService().getWeatherInfo();
        WeatherInfo wInfo = InfoTextSummarizer.getInstance(mContext).getWeatherInfo();
        setWeatherInfo(wInfo);

        Log.d(TAG, "weatherRefresh End");
    }

    private void clearWeatherInfo()
    {
        //full cut
        tvWeatherPlace.setText("");
        tvWeatherPlace2.setText("");
        tvWeatherTemper.setText("");
        tvWeatherPop.setText("");
        tvWeatherReh.setText("");
        tvWeatherType.setText("");
        tvWeatherTime.setText("");

        //short cut
        tvWeatherPlaceSC.setText("");
        tvWeatherPlace2SC.setText("");
        tvWeatherTemperSC.setText("");
        tvWeatherPopSC.setText("");
        //tvWeatherRehSC.setText("");
        tvWeatherTypeSC.setText("");
    }


    private void setWeatherInfo(WeatherInfo winfo) {
        Log.d(TAG, "setWeatherInfo Start");

        clearWeatherInfo();

        String place, place2;
        String temp, pop, reh, type, time;
        place = ""; place2 = "";
        temp = ""; pop = ""; reh = ""; type = ""; time = "";

        if (winfo == null) {
            // 초기화
            place2 = "길게 터치해서 정보를 입력하세요.";
            tvWeatherPlace2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tvWeatherPlace2SC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }
        else
        {
            tvWeatherPlace2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tvWeatherPlace2SC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            place = winfo.location.city_name;
            place2 = winfo.location.mdl_name;
            temp = winfo.array_ttw.get(0).temp + " °C";
            pop = "강수확률 : " + winfo.array_ttw.get(0).pop + "%";
            reh = "습도 : " + winfo.array_ttw.get(0).reh + "%";
            type = winfo.array_ttw.get(0).wfKor;
            time = winfo.array_ttw.get(0).hour;

            Date date = new Date();
            SimpleDateFormat df_source = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = df_source.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df_change = new SimpleDateFormat("MM월dd일 HH시 기준");
            time = df_change.format(date);

            // Set weather image type
            //TODO 날씨 여러개 다 세팅 바꾸고 해놓을것.

            int code = GlobalVariable.wfKorDecode(winfo.array_ttw.get(0).wfKor);
            setWeatherImage(code);
        }

        //full cut
        tvWeatherPlace.setText(place);
        tvWeatherPlace2.setText(place2);
        tvWeatherTemper.setText(temp);
        tvWeatherPop.setText(pop);
        tvWeatherReh.setText(reh);
        tvWeatherType.setText(type);
        tvWeatherTime.setText(time);

        //short cut
        tvWeatherPlaceSC.setText(place);
        tvWeatherPlace2SC.setText(place2);
        tvWeatherTemperSC.setText(temp);
        tvWeatherPopSC.setText(pop);
        //tvWeatherRehSC.setText(reh);
        tvWeatherTypeSC.setText(type);

        Log.d(TAG, "setWeatherInfo End");

    }

    private void setWeatherImage(int type) {

        AnimationDrawable animWeather = (AnimationDrawable) ivWeatherImage.getBackground();
        animWeather.stop();

        switch (type) {
            case GlobalVariable.WeatherConstant.WFKOR.CLEAR: // 클리어 맑음.
                ivWeatherImage.setBackgroundResource(R.drawable.sun_anim);
                break;
            case GlobalVariable.WeatherConstant.WFKOR.PARTLY_CLOUDY:
                //구름조금
                ivWeatherImage.setBackgroundResource(R.drawable.cloud_anim);
                break;
            case GlobalVariable.WeatherConstant.WFKOR.MOSTLY_CLOUDY:
                //구름 많음
                ivWeatherImage.setBackgroundResource(R.drawable.cloud_anim);
                break;
            case GlobalVariable.WeatherConstant.WFKOR.CLOUDY:
                //흐림
                ivWeatherImage.setBackgroundResource(R.drawable.cloud_anim);
                break;
            case GlobalVariable.WeatherConstant.WFKOR.RAIN:
                //비
                ivWeatherImage.setBackgroundResource(R.drawable.rain_anim);
                break;
            case GlobalVariable.WeatherConstant.WFKOR.SNOW_RAIN:
                //눈/비 ---> 눈
                ivWeatherImage.setBackgroundResource(R.drawable.snow_anim);
                break;
            case GlobalVariable.WeatherConstant.WFKOR.SNOW:
                //눈
                ivWeatherImage.setBackgroundResource(R.drawable.snow_anim);
                break;
            default:
                //해당하는 것 없을때
                break;

        }

        animWeather = (AnimationDrawable) ivWeatherImage.getBackground();
        animWeather.start();
    }

    public void changeWeatherView(int type)
    {
        if(type == 0)
        {
            llWeatherFullCut.setVisibility(VISIBLE);
            llWeatherShortCut.setVisibility(GONE);
        }
        else
        {
            llWeatherFullCut.setVisibility(GONE);
            llWeatherShortCut.setVisibility(VISIBLE);
        }
    }

    private void initRecordView()
    {
        Log.d(TAG, "recordInitView Start");

        ivRecord = (ImageView)findViewById(R.id.ivRecord);
        tvRecord = (TextView)findViewById(R.id.tvRecord);


        //재생이 완료했을 경우를 대비함.
        RECManager.getInstance().setCustomOnCompletionListener(new RECManager.CustomOnCompletionListener() {
            @Override
            public void onCompletion() {
                recordRefresh();
            }
        });

        llRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    llRecord.setAlpha((float) 0.8);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    llRecord.setAlpha((float) 1.0);
                    if (RECManager.getInstance().getState() == RECManager.STOP) {
                        //녹음 시작
                        RECManager.getInstance().startRecording("fastrecord.mp4");
                        Toast.makeText(mContext, "녹음을 시작합니다.", Toast.LENGTH_SHORT).show();
                        recordRefresh();
                    } else if (RECManager.getInstance().getState() == RECManager.REC_ING) {
                        //녹음 중지
                        Toast.makeText(mContext, "녹음을 종료하고 저장합니다.", Toast.LENGTH_SHORT).show();
                        RECManager.getInstance().stopRecording();
                        //그리고 재생
                        //RECManager.getInstance().startPlaying("test.mp4");
                        //DebugToast.makeText(mContext,"녹음 파일을 전송", Toast.LENGTH_SHORT).show();

                        recordRefresh();
                    }
                    return true;
                }
                return true;
            }
        });

        Log.d(TAG, "recordInitView End");
    }


    private void initPlayingView()
    {
        Log.d(TAG, "playingInitView Start");

        ivPlaying = (ImageView) findViewById(R.id.ivPlaying);
        tvPlaying = (TextView) findViewById(R.id.tvPlaying);

        ivPlaying.setImageResource(R.drawable.ic_playing);

        llPlaying.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    llPlaying.setAlpha((float) 0.8);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    llPlaying.setAlpha((float) 1.0);

                    if (RECManager.getInstance().getState() == RECManager.STOP) {
                        //재생을 시작한다.
                        Toast.makeText(mContext, "빠른 녹음 재생을 시작합니다.", Toast.LENGTH_SHORT).show();
                        RECManager.getInstance().startPlaying("fastrecord.mp4");
                    } else if (RECManager.getInstance().getState() == RECManager.PLAY_ING) {
                        //재생을 중지한다.
                        RECManager.getInstance().stopPlaying();

                    }
                    recordRefresh();
                    return true;
                }
                return true;
            }
        });

        Log.d(TAG, "playingInitView End");
    }
/*
    private void ttsTest()
    {
        String tts = InfoTextSummarizer.getInstance(mContext).makeTotalTTS();

        SessionUserInfo suInfo = DBManager.getManager(mContext).getSessionUserDB();
        mTTSManager.saveTTS(tts, "tts_temp.mp3");
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
            Log.d(TAG, e.getMessage());
        }
        mTTSManager.startPlaying("tts_temp.mp3");
    }*/

    private void recordRefresh()
    {

        Log.d(TAG, "recordRefresh Start");

        if(RECManager.getInstance().getState() == RECManager.STOP)
        {
            tvRecord.setText("빠른 녹음");
            ivRecord.setImageResource(R.drawable.ic_speaker);
            llRecord.setClickable(true);

            tvPlaying.setText("재생");
            ivPlaying.setImageResource(R.drawable.ic_playing);
            llPlaying.setClickable(true);
        }
        else if (RECManager.getInstance().getState() == RECManager.REC_ING)
        {
            tvRecord.setText("녹음 중지");
            ivRecord.setImageResource(R.drawable.ic_speaker_ing);

            tvPlaying.setText("녹음중 재생 불가");
            llPlaying.setClickable(false);
        }
        else if (RECManager.getInstance().getState() == RECManager.PLAY_ING) {
            tvRecord.setText("재생중 녹음 불가");
            llRecord.setClickable(false);

            tvPlaying.setText("재생");
            ivPlaying.setImageResource(R.drawable.ic_stop);
            llPlaying.setClickable(true);
        }

        Log.d(TAG, "recordRefresh End");
    }

    private void setMarquee()
    {
        tvBusStopName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvBusStopName.setSelected(true);
        tvBusStopName.setSingleLine();

        tvBusDirName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvBusDirName.setSelected(true);
        tvBusDirName.setSingleLine();

        tvBusStopNameSC.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvBusStopNameSC.setSelected(true);
        tvBusStopNameSC.setSingleLine();

        tvBusNumAndDirSC.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvBusNumAndDirSC.setSelected(true);
        tvBusNumAndDirSC.setSingleLine();
    }

    private void setButtonsMarquee()
    {
//        tvPlace.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        tvPlace.setSelected(true);
//        tvPlace.setSingleLine();
//
//        tvPlace2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        tvPlace2.setSelected(true);
//        tvPlace2.setSingleLine();
//
//        tvTemper.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        tvTemper.setSelected(true);
//        tvTemper.setSingleLine();
//
//        tvPop.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        tvPop.setSelected(true);
//        tvPop.setSingleLine();
    }

    private void moveLayout(int code, int type, int type2)
    {

        //full cut(이미지가 안으로 들어옴) //풀컷이 들어오고 숏컷이 나감
        if(type == 0)
        {
            if(type2 == 0) //아래에서 올라옴.
            {
                showAnimation(code,true);
                switch (code)
                {
                    case index_weather:
                        moveLayoutDowntoIn(llWeatherImage,real_height,3);
                        moveLayoutDowntoIn(llWeatherFullCut,real_height,0);
                        moveLayoutUptoOut(llWeatherShortCut,margin_height,1);
                        break;

                    case index_schedule:
                        moveLayoutDowntoIn(llScheduleImage,real_height,3);
                        //moveLayoutDowntoIn(llScheduleFirst);
                        //moveLayoutDowntoIn(llScheduleSecond);
                        break;

                    case index_bus:
                        moveLayoutDowntoIn(llBusImage,real_height,3);
                        moveLayoutDowntoIn(llBusFullCut,real_height,0);
                        moveLayoutUptoOut(llBusShortCut,margin_height,1);
                        break;

                    case index_subway:
                        moveLayoutDowntoIn(llSubwayImage,real_height,3);
                        moveLayoutDowntoIn(llSubwayFullCut,real_height,0);
                        moveLayoutUptoOut(llSubwayShortCut,margin_height,1);
                        break;

                    default:
                        break;
                }

            }
            else //위에서 내려옴  (type2 == 1)
            {
                showAnimation(code,true);
                switch (code)
                {
                    case index_weather:
                        moveLayoutUptoIn(llWeatherImage,real_height,3);
                        moveLayoutUptoIn(llWeatherFullCut,real_height,0);
                        moveLayoutDowntoOut(llWeatherShortCut,real_height,1);
                        break;

                    case index_schedule:
                        moveLayoutUptoIn(llScheduleImage,real_height,3);
                        //moveLayoutUptoIn(llScheduleFirst,real_height);
                        //moveLayoutUptoIn(llScheduleSecond,real_height);
                        break;

                    case index_bus:
                        moveLayoutUptoIn(llBusImage,real_height,3);
                        moveLayoutUptoIn(llBusFullCut,real_height,0);
                        moveLayoutDowntoOut(llBusShortCut,real_height,1);
                        break;

                    case index_subway:
                        moveLayoutUptoIn(llSubwayImage,real_height,3);
                        moveLayoutUptoIn(llSubwayFullCut,real_height,0);
                        moveLayoutDowntoOut(llSubwayShortCut,real_height,1);
                        break;

                    default:
                        break;
                }
            }
        }
        //short cut (이미지가 밖으로 나감) // 숏컷이 들어오고 풀컷이 나감
        else
        {
            if(type2 == 0) // 아래에서 밖으로 나감
            {
                showAnimation(code,false);
                switch (code)
                {
                    case index_weather:
                        moveLayoutDowntoOut(llWeatherImage,real_height,3);
                        moveLayoutDowntoOut(llWeatherFullCut,real_height,0);
                        moveLayoutUptoIn(llWeatherShortCut,margin_height,1);
                        break;

                    case index_schedule:
                        moveLayoutDowntoOut(llScheduleImage,real_height,3);
                        //moveLayoutDowntoOut(llScheduleFirst);
                        //moveLayoutDowntoOut(llScheduleSecond);
                        break;

                    case index_bus:
                        moveLayoutDowntoOut(llBusImage,real_height,3);
                        moveLayoutDowntoOut(llBusFullCut,real_height,0);
                        moveLayoutUptoIn(llBusShortCut,margin_height,1);
                        break;

                    case index_subway:
                        moveLayoutDowntoOut(llSubwayImage,real_height,3);
                        moveLayoutDowntoOut(llSubwayFullCut,real_height,0);
                        moveLayoutUptoIn(llSubwayShortCut,margin_height,1);
                        break;

                    default:
                        break;
                }
            }
            else // 위에서 밖으로 나감
            {
                showAnimation(code,false);
                switch (code)
                {
                    case index_weather:
                        moveLayoutUptoOut(llWeatherImage,real_height,3);
                        moveLayoutUptoOut(llWeatherFullCut,real_height,0);
                        moveLayoutDowntoIn(llWeatherShortCut,real_height,1);
                        break;

                    case index_schedule:
                        moveLayoutUptoOut(llScheduleImage,real_height,3);
                        //moveLayoutDowntoOut(llScheduleFirst);
                        //moveLayoutDowntoOut(llScheduleSecond);
                        break;

                    case index_bus:
                        moveLayoutUptoOut(llBusImage,real_height,3);
                        moveLayoutUptoOut(llBusFullCut,real_height,0);
                        moveLayoutDowntoIn(llBusShortCut,real_height,1);
                        break;

                    case index_subway:
                        moveLayoutUptoOut(llSubwayImage,real_height,3);
                        moveLayoutUptoOut(llSubwayFullCut,real_height,0);
                        moveLayoutDowntoIn(llSubwayShortCut,real_height,1);
                        break;

                    default:
                        break;
                }
            }
        }
    }


    private void moveLayoutUptoIn(final LinearLayout ll, int move_size, final int type)
    {
        //Animation moveAnimation = new MyTranslateAnimation(ll,0,0,ll.getY()-move_size, ll.getY());
        Animation moveAnimation = new TranslateAnimation(0,0,ll.getY()-move_size, ll.getY());
        moveAnimation.setInterpolator(interpolator);
        moveAnimation.setDuration(animationDuration);
        moveAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
                if(type == 1) {
                    ll.setVisibility(GONE);
                    return;
                }
                if(ll.getVisibility() == GONE) ll.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isMoving = false;
                ll.clearAnimation();
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                //params.setMargins(0, 0, 0, 0);
                ll.setVisibility(VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll.startAnimation(moveAnimation);
    }

    private void moveLayoutUptoOut(final LinearLayout ll, int move_size, final int type)
    {
//        Animation moveAnimation = new MyTranslateAnimation(ll,0,0,ll.getY(), ll.getY()-move_size);
        Animation moveAnimation = new TranslateAnimation(0,0,ll.getY(), ll.getY()-move_size);
        moveAnimation.setInterpolator(interpolator);
        moveAnimation.setDuration(animationDuration);
        moveAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
                if(type == 1) {
                    ll.setVisibility(GONE);
                    return;
                }
                if(ll.getVisibility() == GONE) ll.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isMoving = false;
                ll.clearAnimation();
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                //params.setMargins(0, 0, 0, 0);
                if(type == 3){
                    ll.setVisibility(INVISIBLE);
                }
                else
                {
                    ll.setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll.startAnimation(moveAnimation);
    }

    private void moveLayoutDowntoIn(final LinearLayout ll, int move_size, final int type)
    {
        //Animation moveAnimation = new MyTranslateAnimation(ll, 0,0,ll.getY()+move_size,ll.getY());
        Animation moveAnimation = new TranslateAnimation(0,0,ll.getY()+move_size,ll.getY());
        moveAnimation.setInterpolator(interpolator);
        moveAnimation.setDuration(animationDuration);
        moveAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
                if(type == 1) {
                    ll.setVisibility(GONE);
                    return;
                }
                if (ll.getVisibility() == GONE) ll.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isMoving = false;
                ll.clearAnimation();
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                //params.setMargins(0, 0, 0, 0);
                ll.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll.startAnimation(moveAnimation);
    }

    private void moveLayoutDowntoOut(final LinearLayout ll, int move_size, final int type)
    {
        //Animation moveAnimation = new MyTranslateAnimation(ll,0,0,ll.getY(),ll.getY()+move_size);
        Animation moveAnimation = new TranslateAnimation(0,0,ll.getY(),ll.getY()+move_size);
        moveAnimation.setInterpolator(interpolator);
        moveAnimation.setDuration(animationDuration);

        moveAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
                if(type == 1) {
                    ll.setVisibility(GONE);
                    return;
                }
                if(ll.getVisibility() == GONE) ll.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isMoving = false;
                ll.clearAnimation();
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                //params.setMargins(0, 0, 0, 0);
                isMoving = false;
                ll.clearAnimation();
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                //params.setMargins(0, 0, 0, 0);
                if(type == 3){
                    ll.setVisibility(INVISIBLE);
                }
                else
                {
                    ll.setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll.startAnimation(moveAnimation);
    }


    private void scrollUp(int index)
    {
        Animation moveAnimation = new MyTranslateAnimation(f_list.get(index),0,0,f_list.get(index).getY() ,f_list.get(index).getY() - margin_height * 2);
        moveAnimation.setInterpolator(interpolator);
        moveAnimation.setDuration(animationDuration);
        moveAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isMoving = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        f_list.get(index).startAnimation(moveAnimation);
    }

    private void scrollDown(int index)
    {
        Animation moveAnimation = new MyTranslateAnimation(f_list.get(index),0,0,f_list.get(index).getY(), f_list.get(index).getY() + margin_height * 2);
        moveAnimation.setInterpolator(interpolator);
        moveAnimation.setDuration(animationDuration);
        moveAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                isMoving = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        f_list.get(index).startAnimation(moveAnimation);
    }

    public void layoutSizeChange(View ll,int height)
    {
        ll.getLayoutParams().height = height;
        //LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, title_height);
        ll.requestLayout();
    }


    private void showView(int index)
    {

        if(isMoving==true) return;
        if(index == now_index) return;

        //더 큰 인덱스로 이동하는 것이므로
        //위로 올라가면서 메인이 아래에서 위로 올라오고
        //아닌 것이 중간에서 위로 올라감
        if(index > now_index)
        {
            for(int i=now_index+1;i<=index;i++)
            {
                if(i != 0 && i != 1 ) scrollUp(i);
            }
            moveLayout(now_index,1,1); //->숏컷
            moveLayout(index,0,0); //->풀컷
        }
        else if (index < now_index)
        {
            for(int i=index+1 ; i<=now_index; i++ )
            {
                if( i != 0 && i != 1 ) scrollDown(i);
            }
            //더 작은 인덱스로 가는것이므로 아래것이 줄어들고 위에것이 늘어남 즉
            //아래컷이 위로 올라가면서 숏컷이 되고
            //위에컷이 아래로 내려가면서 풀컷이 됨
            moveLayout(now_index,1,0); //->숏컷
            moveLayout(index,0,1); //->풀컷
        }
        now_index = index;
    }

    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void setInterpolator(Interpolator i) {
        interpolator = i;
    }

    public void startAnimation(int index) {
        showView(index);
    }

    public RelativeLayout getFrameLayout(int index) { return f_list.get(index);}



/*
    class doAllRefresh extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Boolean result = false;
        private TransparentProgressDialog progressDialog;
        //private ProgressDialog progressDialog;

        public doAllRefresh(Context context) {
            mContext = context;
            progressDialog = new TransparentProgressDialog(mContext);
            //progressDialog = new ProgressDialog(mContext, R.style.TransparentDialog);
        }

        @Override
        protected void onPreExecute() {
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //progressDialog.setMessage("로딩중입니다...");
            //progressDialog.setCancelable(false);
            //progressDialog.show();

            progressDialog = TransparentProgressDialog.show(mContext, "", ".", true, false, null);
        }

        @Override
        protected Void doInBackground(Void... params) {

            //Test >> Do work like communication with SQLITE, API REQUEST
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Do just changing UI by data from doInBackground
            allRefresh();
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
        }
    }*/


}
