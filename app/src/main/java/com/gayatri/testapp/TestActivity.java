package com.gayatri.testapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.SlideHolder;
import com.gayatri.testapp.Utils.post_async;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Gayatri on 10-08-2015.
 */
public class TestActivity extends Activity implements View.OnClickListener {
    private ImageView iv_viewimage,iv_menu;
    private SlideHolder slider;
    CoordinatorLayout Clayout;
    private Button bt_createbook;
    private ImageView iv_capture;
    private AutoCompleteTextView book_title,book_detail,book_moral;
    SharedPreferences.Editor editor;
    SharedPreferences preference;
    private long timeForImgname;
    private String NewimgName,title="";
    private File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.testbookscreen);
        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();

       getReadExternalStoragePermission();
        init();

    }

    private void getReadExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("System out", "permission check " + permissionCheck);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            getWriteExternalStoragePermission();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);


        }
    }

    public void getWriteExternalStoragePermission()
    {
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("System out", "permission check " + permissionCheck1);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED) {

            getCameraPesrmission();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);


        }
    }
    public  void getCameraPesrmission()
    {
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        Log.d("System out", "permission check " + permissionCheck2);
        if (permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1234);


        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 12: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("System out", "permission granted");
                    getCameraPesrmission();
                }
            }
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("System out", "permission granted");
                    getWriteExternalStoragePermission();
                }
            }

            case 1234: {
                // If request is cancelled, the result arrays are empty.
                return;
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( Constants.dialog!=null && Constants.dialog.isShowing() ){
            Constants.dialog.cancel();
        }
    }
    private void init() {
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbartextbook);
        bt_createbook=(Button)findViewById(R.id.bt_createbook);
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        book_title = (AutoCompleteTextView) findViewById(R.id.book_title);
        book_detail = (AutoCompleteTextView) findViewById(R.id.book_detail);
        book_moral = (AutoCompleteTextView) findViewById(R.id.book_moral);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        bt_createbook.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }
        else if (v==bt_createbook)
        {
            if (TextUtils.isEmpty(book_title.getText().toString())) {
                Snackbar.make(Clayout, "Enter Book Title.", Snackbar.LENGTH_SHORT).show();


            }
            else if (TextUtils.isEmpty(book_detail.getText().toString())) {
                Snackbar.make(Clayout, "Enter Book Detail.", Snackbar.LENGTH_SHORT).show();


            } else if (TextUtils.isEmpty(book_moral.getText().toString())) {
                Snackbar.make(Clayout, "Enter Book Moral.", Snackbar.LENGTH_SHORT).show();
            }
            else
            {
                savetextbook();
            }
        }

    }



    private void savetextbook() {

        if(IsNetworkConnection.checkNetworkConnection(TestActivity.this))
        {
            String webdata = "{\"" + "user_id" + "\":" + "\""+ preference.getString("id","") + "\"" + ",\""
                    + "name"+ "\":" + "\"" + book_title.getText().toString().trim() + "\"" +",\""
                    +"type"+"\":"+"\""+"booktype2"+"\""+",\""
                    + "content" + "\":" + "\""+ book_detail.getText().toString().trim() + "\"" + ",\""
                    + "moral"+ "\":" + "\"" +book_moral.getText().toString().trim() + "\"" + ",\""
                    + "price" + "\":" + "\"" +"free" + "\"" + ",\""
                    + "created_by" + "\":" + "\""+ "2" + "\"" +
                    "}";


            String url = Constants.SERVER_URL+"create_book_test" ;
            Log.d("System out", "url" + url);
            new post_async(TestActivity.this, "create_book_test").execute(url, webdata);

        }
        else {
            Toast.makeText(TestActivity.this, "No Network", Toast.LENGTH_SHORT).show();
        }
        //{"user_id":"31","name":"csvzjjz","type":"booktype2","content":"vzzjzjbzbzkzmz
       // vzbzbznxnx
       // zbnzmxnxjz","moral":"ahbsnzjxjxh","price":"free","created_by":"2"}

    }

    public void responseoftestbook(String resultString) {
        Log.d("System out","response of test book");

        try {

            JSONObject json = new JSONObject(resultString);

            String sStatus=json.getString("sStatus"); //<< get value here


            Log.d("System out","sStatus"+sStatus);
            if (sStatus.equalsIgnoreCase("1"))
            {
                //Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();
                Toast.makeText(this, ""+json.getString("sMessage"), Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            finish();


                        } catch (Exception e) {
                            Log.w("Exception in splash", e);
                        }
                    }
                }).start();



            }
            else
            {
                Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}