package com.gayatri.testapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gayatri.testapp.Model.BookDetailClass;
import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.MyEditText;
import com.gayatri.testapp.Utils.SlideHolder;
import com.gayatri.testapp.Utils.post_async;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Gayatri on 10-08-2015.
 */
public class ShareActivity extends Activity implements View.OnClickListener {
    private ImageView iv_menu, next_btn;
    private SlideHolder slider;
    private TextView share_btn;
    private TextView book_title;

    BookDetailClass bookDetail;
    BookDetailClass imageBookDetail = new BookDetailClass();
    private RadioButton rb_book_public_visibility;
    private RadioButton rb_book_only_me_visibility;
    private RadioButton free_price_rb;
    private RadioButton specific_price_rb;
    private MyEditText et_price;
    private CheckBox cb_myaddressbook, cb_pintrest, cb_gpluse, cb_twitter, cb_fb;

    CoordinatorLayout Clayout;

    SharedPreferences.Editor editor;
    SharedPreferences preference;

    String shareVia = "";

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

        if (slider.isOpened()) {
            slider.toggle();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharebookscreen);
        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        bookDetail = new BookDetailClass();
//        imageBookDetail = (BookDetailClass) getIntent().getSerializableExtra("imageBookDetail");
        bookDetail = (BookDetailClass) getIntent().getSerializableExtra("bookDetail");
        init();

    }

    private void init() {
        slider =  findViewById(R.id.drawer_layout);
        iv_menu = findViewById(R.id.iv_menu);
        book_title =  findViewById(R.id.book_title);
        rb_book_only_me_visibility =  findViewById(R.id.rb_book_only_me_visibility);
        rb_book_public_visibility =  findViewById(R.id.rb_book_only_me_visibility);
        free_price_rb =  findViewById(R.id.free_price_rb);
        specific_price_rb = findViewById(R.id.specific_price_rb);
        et_price =  findViewById(R.id.et_price);
        book_title =  findViewById(R.id.book_title);
        cb_fb =  findViewById(R.id.cb_fb);
        cb_twitter =  findViewById(R.id.cb_twitter);
        cb_gpluse =  findViewById(R.id.cb_gpluse);
        cb_pintrest = findViewById(R.id.cb_pintrest);
        cb_myaddressbook =  findViewById(R.id.cb_myaddressbook);
        Clayout = findViewById(R.id.snackbarsharebook);

        iv_menu.setOnClickListener(this);
       /* 2next_btn=(ImageView)findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);*/
        share_btn =  findViewById(R.id.share_btn);
        share_btn.setOnClickListener(this);
        book_title.setText("Book Title" );//bookDetail.getBookName()
        free_price_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et_price.setVisibility(View.GONE);
                } else {
                    et_price.setVisibility(View.VISIBLE);

                }
                free_price_rb.setChecked(b);
            }
        });

        specific_price_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et_price.setVisibility(View.VISIBLE);
                } else {
                    et_price.setVisibility(View.GONE);

                }
                specific_price_rb.setChecked(b);
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }
        if (v == share_btn) {
            if (specific_price_rb.isChecked() && TextUtils.isEmpty(et_price.getText())) {
                Snackbar.make(Clayout, "Enter specific book amount.", Snackbar.LENGTH_SHORT).show();

            } else if (specific_price_rb.isChecked() && TextUtils.isEmpty(et_price.getText())) {
                Snackbar.make(Clayout, "Enter specific book amount.", Snackbar.LENGTH_SHORT).show();

            } else {
                shareVia = "";
                if (cb_fb.isChecked()) {
                    shareVia = "Facebook";
                } else if (cb_gpluse.isChecked()) {
                    shareVia = "Google Plus";
                } else if (cb_twitter.isChecked()) {
                    shareVia = "Twitter";

                } else if (cb_pintrest.isChecked()) {
                    shareVia = "Message";
                } else if (cb_myaddressbook.isChecked()) {
                    shareVia = "MyAddressBook";
                }

                shareBook();

            }
        }
    }

    private void ShareBookByApp() {

        String message = "Please read this amazing book named as" + bookDetail.getBookName();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.app_decription));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(sharingIntent);
        finish();
    }

    private void shareBook() {

        if (IsNetworkConnection.checkNetworkConnection(ShareActivity.this)) {
            String visibility = rb_book_public_visibility.isChecked() ? rb_book_public_visibility.getText().toString() : rb_book_only_me_visibility.getText().toString();


            String webdata = "{\"" + "user_id" + "\":" + "\"" + preference.getString("id", "") + "\"" + ",\""
                    + "book_id" + "\":" + "\"" + bookDetail.getBook_id() + "\"" + ",\""
                    + "book_name" + "\":" + "\"" + bookDetail.getBookName() + "\"" + ",\""
                    + "price" + "\":" + "\"" + et_price.getText().toString().trim() + "\"" + ",\""
                    + "visibility" + "\":" + "\"" + visibility + "\"" + ",\""
                    + "share_network" + "\":" + "\"" + shareVia + "\"" +
                    "}";


            String url = Constants.SERVER_URL + "share_book";
            Log.d("System out", "url" + url);
            new post_async(ShareActivity.this, "ShareBook").execute(url, webdata);

        } else {
            Toast.makeText(ShareActivity.this, "No Network", Toast.LENGTH_SHORT).show();
            ShareBookByApp();

        }

    }

    public void responseofShareBook(String resultString) {
        Log.d("System out", "response of share");
        Constants.executeLogcat(ShareActivity.this);

        try {

            JSONObject json = new JSONObject(resultString);

            String sStatus = json.getString("sStatus"); //<< get value here

            Log.d("System out", "sStatus" + sStatus);
            if (sStatus.equalsIgnoreCase("1")) {
                JSONObject sData = json.getJSONObject("sData");
                String id = sData.getString("id");

                Snackbar.make(Clayout, "" + json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();


            } else {
                Snackbar.make(Clayout, "" + json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();

            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        ShareBookByApp();


                    } catch (Exception e) {
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
