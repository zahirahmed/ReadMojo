package com.gayatri.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gayatri.testapp.Utils.SlideHolder;

/**
 * Created by Gayatri on 07-09-2015.
 */
public class SelectAccountActivity extends Activity implements View.OnClickListener {

    private ImageView iv_menu,next_btn;
    private SlideHolder slider;
    private LinearLayout mainll;


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
        setContentView(R.layout.chooseaccount);
        init();

    }

    private void init() {
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
       /* next_btn=(ImageView)findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);*/
        mainll=(LinearLayout)findViewById(R.id.mainll);
        mainll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }
        if (v==mainll)
        {

        }

    }
}
