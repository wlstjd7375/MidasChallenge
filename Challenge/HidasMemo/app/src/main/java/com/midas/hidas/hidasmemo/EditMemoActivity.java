package com.midas.hidas.hidasmemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EditMemoActivity extends AppCompatActivity {

    private String TAG = "EditMemoActivity";
    private int memoId;
    private Context mContext;

    private static final int VIDEO_CAPTURE = 101;
    private static final int IMAGE_CAPTURE = 102;
    private static final int GALLERY = 103;

    private Button btnAdd;
    private Uri fileUri;
    LinearLayout Parent_Linear;

    ArrayList<View> arr_ViewObject;
    EditText edit_object;
    private AlertDialog mDialog = null;

    public void createEditText(){
        EditText edit = new EditText(mContext);
        edit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        arr_ViewObject.add(edit);
        Parent_Linear.addView(edit);
    }

    public void SaveView(){

        for(int i = 0 ; i < arr_ViewObject.size(); i ++){
            Log.i("HANJ",arr_ViewObject.get(i) +" ");
        }

    }

    public void deleteEditText(){
        for(int i = 0 ; i < arr_ViewObject.size(); i ++){

        }
    }

    public void deleteImage(ImageView in){
        for(int i = 0 ; i < arr_ViewObject.size(); i ++){
            Log.i("HANJ", i +" , " + arr_ViewObject.get(i));
            if(arr_ViewObject.get(i)==in){
                ImageView  tt= (ImageView)arr_ViewObject.get(i);
                tt.setImageResource(0);
                arr_ViewObject.get(i).setVisibility(View.GONE);
                arr_ViewObject.remove(i);
                Log.i("HANJ","Delete OK");
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        mContext = getApplicationContext();

        Intent intent = getIntent();

        //If it is new memo
        if(intent.getExtras().getBoolean("isNew")) {

        }
        else
        {
            memoId = intent.getExtras().getInt("memoId");
            //Log.d(TAG, memoId + "");
            //get memo informations by memo id

            //set UI
        }

        Parent_Linear = new LinearLayout(this);
        Parent_Linear = (LinearLayout) findViewById(R.id.parent_linear);

        edit_object = new EditText(this);
        edit_object = (EditText)findViewById(R.id.edit_object);
        arr_ViewObject= new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_memo, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.gallery) {
            goGallery();
        }
        else if(id == R.id.camera) {
            startImageCapture();
        }
        else if(id == R.id.text) {
            createEditText();
        }
        return super.onOptionsItemSelected(item);

    }

    private void createDialog(ImageView imv) {
        final ImageView f_imv = imv;
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Title");
        ab.setMessage("내용");
        ab.setCancelable(false);
        ab.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));

        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deleteImage(f_imv);
                setDismiss(mDialog);
            }
        });

        ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                setDismiss(mDialog);
            }
        });
        ab.create();
        ab.show();
    }

    private void setDismiss(Dialog dialog){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void startImageCapture()
    {
        File mediaFile = new
                File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/myimage.jpeg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(mediaFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    void goGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY);
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    void AddImageView(Intent data){
        Uri selectedImageUri = data.getData();
        //String selectedImagePath = getPath(selectedImageUri);
        Log.i("HANJ", "Image URI == " + selectedImageUri);
        Log.i("HANJ", "Image Path == " + selectedImageUri.getPath());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap src = BitmapFactory.decodeFile(getRealPathFromURI(mContext, selectedImageUri), options);
        Bitmap resized = Bitmap.createScaledBitmap(src, src.getWidth(), src.getHeight(), true);

        try {
            ImageView imv = new ImageView(mContext);
            imv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imv.setPadding(30,30,30,30);
            imv.setImageBitmap(resized);
            imv.setLongClickable(true);
            arr_ViewObject.add(imv);
            final ImageView t = imv;
            imv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    final ImageView temp = imv;
                    Log.i("HANJ","Long Click");
                    createDialog(t);
                    return false;
                }
            });

            Parent_Linear.addView(imv);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                AddImageView(data);
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {

        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    public void saveMemo()
    {

    }
}
