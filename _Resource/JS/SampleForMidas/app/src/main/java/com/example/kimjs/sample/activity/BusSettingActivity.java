package com.example.kimjs.sample.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kimjs.sample.R;
import com.example.kimjs.sample.adapter.BusStationAdapter;
import com.example.kimjs.sample.adapter.ViewHolder;
import com.example.kimjs.sample.artifacts.BusInfo;
import com.example.kimjs.sample.artifacts.BusRouteInfo;
import com.example.kimjs.sample.artifacts.BusStationInfo;
import com.example.kimjs.sample.database.DBManager;
import com.example.kimjs.sample.infoparser.BusPaser;

import java.util.ArrayList;

public class BusSettingActivity extends Activity {
   
   private ImageView ivBusNameSearch;
   private ListView lvBusStop;
   private EditText etBusName;
   private TextView tvCurrentBusRoute;
   private BusStationAdapter adapter;
   
  
   private BusRouteInfo busRouteInfo;
   
   //for tvCurrentBusRoute
   private BusInfo binfo;
   private ArrayList<BusInfo> busArrayList;
   
   private boolean mIsBackKeyPressed = false;
   
   private String TAG = "BusSettingActivity";
   
   protected void onCreate(Bundle savedInstanceState) 
   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bus);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
       //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        etBusName = (EditText)findViewById(R.id.etBusName);
        lvBusStop = (ListView)findViewById(R.id.lvBusStop);
        tvCurrentBusRoute = (TextView)findViewById(R.id.tvCurrentBusRoute);
        
        busArrayList = new ArrayList<BusInfo>();
        busArrayList = DBManager.getManager(getApplicationContext()).getBusDBbyLog();
        
        
        if(busArrayList.size() != 0)
        {
        	binfo = busArrayList.get(0);
            binfo = DBManager.getManager(getApplicationContext()).getBusDB(binfo);

            if (binfo.array_ttb.size() == 0)
            {
                BusPaser bp = new BusPaser();
                binfo = bp.getBusArrInfoByRoute(binfo);
                DBManager.getManager(getApplicationContext()).setBusDB(binfo);
                binfo = DBManager.getManager(getApplicationContext()).getBusDB(binfo);
            }
            String str = binfo.route.busRouteNm;
           
            tvCurrentBusRoute.setText(str);

        }
        
        
        
        ivBusNameSearch = (ImageView)findViewById(R.id.ivBusNameSearch);
        ivBusNameSearch.setOnClickListener(new OnClickListener() {
           @Override
         public void onClick(View v) {
            // TODO Auto-generated method stub
              String bus_route = etBusName.getText().toString().trim();

               if(bus_route.equals(""))
               {
                   if(binfo == null)
                   {
                       showSearchError();
                       return;
                   }
                   bus_route = tvCurrentBusRoute.getText().toString();
               }

              BusPaser bp = new BusPaser();
              ArrayList<BusRouteInfo> array_route = new ArrayList<BusRouteInfo>();
              ArrayList<BusStationInfo> array_station = new ArrayList<BusStationInfo>();
              array_route = bp.getBusRouteList(bus_route);

              //if there is no such bus route
              if(array_route.size() == 0)
              {
                  showSearchError();
              }
              else
              {
                  busRouteInfo = array_route.get(0);
                  array_station = bp.getStaionsByRouteList(busRouteInfo.busRouteId);

                  adapter = new BusStationAdapter(getApplicationContext(), R.layout.list_row, array_station);
                  lvBusStop.setAdapter(adapter);
                  adapter.notifyDataSetChanged();
              }
           }
        });
        
        lvBusStop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusInfo binfo = new BusInfo();

                ViewHolder vh = (ViewHolder) view.getTag();

                binfo.route = busRouteInfo;
                binfo.station = vh.busStationInfo;
//                BusPaser bp = new BusPaser();
//                binfo = bp.getStationByUid(binfo);

                String stNm = vh.tvBusStopNameListRow.getText().toString();
                DBManager.getManager(getApplicationContext()).setBusDBbyLog(binfo);

                finish();
            }
        });
        
    }
   
   public void showSearchError()
   {
       String bus_route = etBusName.getText().toString();
       AlertDialog alertDialog = new AlertDialog.Builder(this).create();
       alertDialog.setTitle("");

       alertDialog.setMessage(bus_route + "버스를 찾을 수 없습니다.");

       alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which)
           {
               etBusName.setText("");
               dialog.dismiss();
           }
       });

       alertDialog.show();
   }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}


