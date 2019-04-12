package com.gayatri.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gayatri.testapp.R;

/**
 * Created by Gayatri on 07-09-2015.
 */
public class ReadModeActivity extends Activity implements View.OnClickListener {
    private  TextView readbyself,readexternal,endbtn;
    private   LinearLayout readdatamain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readmodebookscreen);
        init();

    }

    private void init() {
        readbyself=(TextView)findViewById(R.id.readbyself);
        readexternal=(TextView)findViewById(R.id.readexternal);
        endbtn=(TextView)findViewById(R.id.endbtn);
         readdatamain=(LinearLayout)findViewById(R.id.readdatamain);
        readbyself.setOnClickListener(this);
        readexternal.setOnClickListener(this);
        endbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==endbtn)
        {
            Intent i=new Intent(ReadModeActivity.this,SelectAccountActivity.class);
            startActivity(i);
        }
    }
}
