package com.gayatri.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gayatri.testapp.Utils.SlideHolder;

/**
 * Created by Gayatri on 10-08-2015.
 */
public class TeamActivity extends Activity implements View.OnClickListener {

    private SlideHolder slider;
    private ImageView iv_menu;
    private LinearLayout ll1,ll3,ll2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosebook);
        init();

    }

    private void init() {

        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
       ll1=(LinearLayout)findViewById(R.id.ll1);
        ll2=(LinearLayout)findViewById(R.id.ll2);
        ll3=(LinearLayout)findViewById(R.id.ll3);
        ll3.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll1.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }
        if (v==ll1 || v==ll2 || v==ll3)
        {
            Intent i=new Intent(TeamActivity.this,ReadModeActivity.class);
            startActivity(i);
        }

    }
}
