package com.example.kimjs.sample.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;



public class DBHelper {

	private static final String DATABASE_NAME = "mynah.db";

	private static final int DATABASE_VERSION = 12;


	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;

	public DBHelper(Context context){
		this.mCtx = context;
	}

	public DBHelper open() throws SQLException{
		mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close(){
		mDB.close();
	}

	private class DatabaseHelper extends SQLiteOpenHelper{

		// 생성자
		public DatabaseHelper(Context context, String name,
							  CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// 최초 DB를 만들때 한번만 호출된다.
		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(MynahDB.CreateDB._CREATE_USER_TABLE);
			db.execSQL(MynahDB.CreateDB._CREATE_WEATHER_TABLE);
			db.execSQL(MynahDB.CreateDB._CREATE_WEATHER_CITY_TABLE);
			db.execSQL(MynahDB.CreateDB._CREATE_BUS_TABLE);
			db.execSQL(MynahDB.CreateDB._CREATE_SUBWAY_TABLE);

			db.execSQL(MynahDB.CreateDB._CREATE_WEATHER_LOG_TABLE);
			db.execSQL(MynahDB.CreateDB._CREATE_BUS_LOG_TABLE);
			db.execSQL(MynahDB.CreateDB._CREATE_SUBWAY_LOG_TABLE);

			//캘린더부분
			db.execSQL(MynahDB.CreateDB._CREATE_SCHEDULE_TABLE);

			//세션유져부분
			db.execSQL(MynahDB.CreateDB._CREATE_SESSION_USER_TABLE);
		}

		// 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._USER_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._WEATHER_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._WEATHER_CITY_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._BUS_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._SUBWAY_TABLE_NAME);

			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._WEATHER_LOG_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._BUS_LOG_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._SUBWAY_LOG_TABLE_NAME);

			//캘린더 지우기
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._SCHEDULE_TABLE_NAME);

			//세션유져 테이블 지우기
			db.execSQL("DROP TABLE IF EXISTS " + MynahDB._SESSION_USER_TABLE_NAME);

			onCreate(db);
		}
	}

}