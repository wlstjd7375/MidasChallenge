package com.midas.hidas.hidasmemo.db_util;

/**
 * Created by Jun on 2016-05-28.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Hidas.sqlite";
    private static final String SD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private final String ROOT_DIR = SD_PATH + "/Hidas/data/"
            + DATABASE_NAME;
    private SQLiteDatabase db;
    DBHelper DBAdapter;

    Context context;

    private static final String Q_CHECK_TABLE = "SELECT count(*) FROM sqlite_master;";

    public DBHelper(Context context) {

        super(context, "Hidas.sqlite", null, 1);
        this.context = context;
        CREATEDB();
        checkTableIsCreated();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    public void open() {
        this.db = SQLiteDatabase.openDatabase(ROOT_DIR, null,
                SQLiteDatabase.OPEN_READWRITE);
        // db.execSQL(DATABASE_CREATE);
    }

    public void Close() {
        db.close();
    }

    public void checkTableIsCreated() {

        int temp = 0;
        Cursor cursor = db.rawQuery(Q_CHECK_TABLE, null);
        while (cursor.moveToNext()) {
            temp = cursor.getInt(0);
        }
        if (temp == 2) {


        } else {
        }

    }

    public Cursor GET_LIST_ALL_VALUE() {
        return db.rawQuery("SELECT * FROM T_Memo ", null);
    }
    public Cursor GET_TAG_BY_ID(int id) {return db.rawQuery("SELECT * FROM T_TAG WHERE id="+id, null); }

    public Cursor GET_LIST_BY_SEARCH(String query) {
        return db.rawQuery("SELECT * FROM T_Memo,T_Tag where title like '%"+query+"%' group by T_Memo.id", null);
    }


    public int DELETE_MY_LIST_COLOR(String ID) {
//      String query = "delete from myColor where _id="+ID;
//      db.rawQuery(query, null);
        return db.delete("myColor", "_id=?", new String[]{ID});
    }

    public void delete(String _query) {
//           SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
    }


    public Cursor GET_MY_LIST_ALL_VALUE() {
        return db.rawQuery("SELECT * FROM myColor ORDER BY _id DESC", null);
    }

    public Cursor GET_LIST_VALUE_Color(int color) {

        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        return db.rawQuery("SELECT * FROM SensibleColorList WHERE (cbaR1>='"
                + (red - 30) + "'AND cbaR1<='" + (red + 30) + "'AND cbaG1>='"
                + (green - 30) + "'AND cbaG1<='" + (green + 30) + "'"
                + "AND cbaB1>='" + (blue - 30) + "'AND cbaB1<='" + (blue + 30)
                + "') OR (cbaR2>='" + (red - 10) + "'AND cbaR2<='" + (red + 10)
                + "'AND cbaG2>='" + (green - 10) + "'AND cbaG2<='"
                + (green + 10) + "'" + "AND cbaB2>='" + (blue - 10)
                + "'AND cbaB2<='" + (blue + 10) + "') OR" + "(cbaR3>='"
                + (red - 10) + "'AND cbaR3<='" + (red + 10) + "'AND cbaG3>='"
                + (green - 10) + "'AND cbaG3<='" + (green + 10) + "'"
                + "AND cbaB3>='" + (blue - 10) + "'AND cbaB3<='" + (blue + 10)
                + "')", null);
    }

    public Cursor GET_LIST_VALUE_KeyWord2RGB(String keyword) {
        return db
                .rawQuery(
                        "SELECT cbaR1,cbaG1,cbaB1,cbaR2,cbaG2,cbaB2,cbaR3,cbaG3,cbaB3 FROM SensibleColorList where cbakeyword ='"
                                + keyword + "';", null);
    }

    public long SET_MY_2LIST_VALUE(String keyword, int color_num, int cbaR1,
                                   int cbaG1, int cbaB1, int cbaR2, int cbaG2, int cbaB2) {
        ContentValues values = new ContentValues();
        values.put("cbaKeyword", keyword);
        values.put("color_num", color_num);
        values.put("cbaR1", cbaR1);
        values.put("cbaG1", cbaG1);
        values.put("cbaB1", cbaB1);
        values.put("cbaR2", cbaR2);
        values.put("cbaG2", cbaG2);
        values.put("cbaB2", cbaB2);

        long result = db.insert("myColor", null, values);
        return result;
    }

    public long SET_MY_3LIST_VALUE(String keyword, int color_num, int cbaR1,
                                   int cbaG1, int cbaB1, int cbaR2, int cbaG2, int cbaB2, int cbaR3,
                                   int cbaG3, int cbaB3) {
        ContentValues values = new ContentValues();
        values.put("cbaKeyword", keyword);
        values.put("color_num", color_num);
        values.put("cbaR1", cbaR1);
        values.put("cbaG1", cbaG1);
        values.put("cbaB1", cbaB1);
        values.put("cbaR2", cbaR2);
        values.put("cbaG2", cbaG2);
        values.put("cbaB2", cbaB2);
        values.put("cbaR3", cbaR3);
        values.put("cbaG3", cbaG3);
        values.put("cbaB3", cbaB3);

        long result = db.insert("myColor", null, values);
        return result;
    }

    public long SET_MY_4LIST_VALUE(String keyword, int color_num, int cbaR1,
                                   int cbaG1, int cbaB1, int cbaR2, int cbaG2, int cbaB2, int cbaR3,
                                   int cbaG3, int cbaB3, int cbaR4, int cbaG4, int cbaB4) {
        ContentValues values = new ContentValues();
        values.put("cbaKeyword", keyword);
        values.put("color_num", color_num);
        values.put("cbaR1", cbaR1);
        values.put("cbaG1", cbaG1);
        values.put("cbaB1", cbaB1);
        values.put("cbaR2", cbaR2);
        values.put("cbaG2", cbaG2);
        values.put("cbaB2", cbaB2);
        values.put("cbaR3", cbaR3);
        values.put("cbaG3", cbaG3);
        values.put("cbaB3", cbaB3);
        values.put("cbaR4", cbaR4);
        values.put("cbaG4", cbaG4);
        values.put("cbaB4", cbaB4);

        long result = db.insert("myColor", null, values);
        return result;
    }

    public void CREATEDB() {

        File folder;
        folder = new File(ROOT_DIR);
        AssetManager am = context.getAssets();

        if (folder.isFile()) {
            Set_DB();
        } else {
            try {
                folder = new File(SD_PATH + "/Hidas/data/");
                folder.mkdirs();
                File outfile = new File(SD_PATH + "/Hidas/data/"
                        + DATABASE_NAME);
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                InputStream is = am.open("Hidas.sqlite");
                BufferedInputStream bis = new BufferedInputStream(is);

                // ���࿡ ������ �ִٸ� ����� �ٽ� ��
                if (outfile.exists()) {
                    outfile.delete();
                    outfile.createNewFile();
                } else {
                    outfile.createNewFile();
                }
                fos = new FileOutputStream(outfile);
                bos = new BufferedOutputStream(fos);

                int read = -1;
                byte[] buffer = new byte[1024];
                while ((read = bis.read(buffer, 0, 1024)) != -1) {
                    bos.write(buffer, 0, read);
                }
                bos.flush();

                fos.close();
                bos.close();
                is.close();
                bis.close();
                Set_DB();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void Set_DB() {
        this.db = SQLiteDatabase.openDatabase(ROOT_DIR, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

}