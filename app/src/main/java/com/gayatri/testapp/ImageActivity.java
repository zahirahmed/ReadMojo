package com.gayatri.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gayatri.testapp.Utils.SlideHolder;

/**
 * Created by Gayatri on 10-08-2015.
 */
public class ImageActivity extends Activity implements View.OnClickListener {
    private ImageView iv_menu,next_btn;
    private SlideHolder slider;

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
        setContentView(R.layout.imagebookscreen);
        init();

    }

    private void init() {
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
        next_btn=(ImageView)findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }

    }
}
