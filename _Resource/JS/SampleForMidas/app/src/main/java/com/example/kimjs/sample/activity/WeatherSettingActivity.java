package com.example.kimjs.sample.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kimjs.sample.R;
import com.example.kimjs.sample.adapter.CityNameAdapter;
import com.example.kimjs.sample.adapter.ViewHolder;
import com.example.kimjs.sample.artifacts.WeatherInfo;
import com.example.kimjs.sample.artifacts.WeatherLocationInfo;
import com.example.kimjs.sample.database.DBManager;

public class WeatherSettingActivity extends Activity{

	private ImageView ivCityNameSearch;
	private ListView lvCityName;
	private EditText etCityName;
	private TextView tvCurrentWeatherLocation;
	private CityNameAdapter adapter;
	
	//for tvCurrentWeatherLocation
	private WeatherLocationInfo wlinfo;
	private ArrayList<WeatherLocationInfo> weatherArrayList;
	private WeatherInfo winfo;
	
	private boolean mIsBackKeyPressed = false;
	private String cityName;

	private static String TAG = "WeatherSettingActivity";
	
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_weather);
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        ivCityNameSearch = (ImageView)findViewById(R.id.ivCityNameSearch);
        lvCityName = (ListView)findViewById(R.id.lvCityName);
        etCityName = (EditText)findViewById(R.id.etCityName);
        tvCurrentWeatherLocation = (TextView)findViewById(R.id.tvCurrentWeatherLocation);
        
        weatherArrayList = new ArrayList<WeatherLocationInfo>();
        weatherArrayList = DBManager.getManager(getApplicationContext()).getWeatherDBbyLog();
        
        if(weatherArrayList.size() != 0)
        {
        	wlinfo = weatherArrayList.get(0);
			cityName = wlinfo.city_name;
            tvCurrentWeatherLocation.setText(cityName);
        }
        
        
        ivCityNameSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onClick Start");

				cityName = etCityName.getText().toString().trim();

				if (cityName.equals("")) {
					if (wlinfo == null) {
						showSearchError();
						return;
					}
					cityName = wlinfo.city_name;
				}
				//DB Transaction
				ArrayList<WeatherLocationInfo> array_location = new ArrayList<WeatherLocationInfo>();
				array_location = DBManager.getManager(getApplicationContext()).getWeatherLocationByName(cityName);

				if (array_location.size() == 0) {
					showSearchError();
				} else {
					adapter = new CityNameAdapter(getApplicationContext(), R.layout.list_row, array_location);
					lvCityName.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}

				Log.d(TAG, "onClick End");
			}
		});
        
        lvCityName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Log.d(TAG, "onItemClick Start");
				WeatherInfo winfo = new WeatherInfo();
				ViewHolder vh = (ViewHolder) view.getTag();

				DBManager.getManager(getApplicationContext()).setWeatherLocationDBbyLog(vh.weatherLocationInfo);

				finish();
				overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

				Log.d(TAG, "onItemClick End");
			}
		});
        
    }

	public void showSearchError()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("");
		alertDialog.setMessage(cityName + "을(를) 찾을수 없습니다.");
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				etCityName.setText("");
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
