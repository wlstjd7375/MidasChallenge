package com.example.woqja.young;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MyData> mItems = new ArrayList<MyData>();
    private ErrorListener mOnTKokErrorListener;

    Response.Listener<JSONObject> mOnResponseListener = new Response.Listener<JSONObject>() {

        /*
        * 받아오면 response.get 으로 해서 string, int , array 등등 필요한거에 맞춰서 받아옴
        * */
        @Override
        public void onResponse(JSONObject response) {
            JsonResponse jsonResponse = new JsonResponse(response);
            if (!jsonResponse.isSucceed()) {
                mOnTKokErrorListener.showErrorMessage(jsonResponse.getErrorMessage());
                finish();
            }

            JsonResponse[] jsonRestaurants = jsonResponse.getJsonArray("restaurants");
            Log.e("MainActivity", "Success");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setTitle("TEST");
        }

        /*
        * 리스트뷰에 보여줄 목록
        * */
        mItems.add(new MyData("한준영",R.mipmap.ic_launcher));
        mItems.add(new MyData("신재범",R.mipmap.ic_launcher));
        mItems.add(new MyData("호호호",R.mipmap.ic_launcher));
        mItems.add(new MyData("히히히",R.mipmap.ic_launcher));

//        mItems.add("바보");
//        mItems.add("멍청이?/");
//        mItems.add("케케케케");
//        mItems.add("캬캬캬캬");
        setRecycleView();


            /*
        * error 나면 스넥바로 표시
        * */
        View mainLayout = findViewById(R.id.mainLayout);
        mOnTKokErrorListener = new ErrorListener(mainLayout);

        SessionRequest request = HttpClient.newRestaurantsRequest(mOnResponseListener, mOnTKokErrorListener);
        AppController.getInstance().addToRequestQueue(request);

    }

    private void setRecycleView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mItems);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
