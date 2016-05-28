package com.midas.hidas.hidasmemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.midas.hidas.hidasmemo.Adapter.MemoListAdapter;
import com.midas.hidas.hidasmemo.Adapter.ViewHolder;
import com.midas.hidas.hidasmemo.artifacts.MemoListData;
import com.midas.hidas.hidasmemo.db_util.DBManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private ListView lvMemoList;
    private MemoListAdapter adapter;
    private Context mContext;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TestActivity.class);
                //intent.putExtra("isNew", true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

        mContext = getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbManager = new DBManager(mContext);

        ArrayList<MemoListData> mDataList = new ArrayList<MemoListData>();
        mDataList = dbManager.getAllMemoList();
        setListView(mDataList);
    }

    private void setListView(ArrayList<MemoListData> mDataList)
    {
        lvMemoList = (ListView)findViewById(R.id.lvMemoList);
        adapter = new MemoListAdapter(mContext, R.layout.list_item, mDataList);
        lvMemoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder vh = (ViewHolder)view.getTag();
                int memoId = vh.memoId;
                Toast.makeText(mContext, "ID = " + id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, EditMemoActivity.class);
                intent.putExtra("isNew", false);
                intent.putExtra("memoID", id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                //
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.equals("")) {
                    System.out.println(s);
                    Log.d(TAG, s);
                    dbManager = new DBManager(mContext);
                    ArrayList<MemoListData> mDataList = new ArrayList<MemoListData>();
                    mDataList =  dbManager.getMemoListBySearch(s);
                    setListView(mDataList);
                }

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
