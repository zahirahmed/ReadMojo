package com.gayatri.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Gayatri on 27-07-2015.
 */
public class MainScreen extends Activity implements View.OnClickListener {

    LinearLayout login_ll,fblogin_ll,signup_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        init();
    }

    private void init() {

        login_ll= (LinearLayout) findViewById(R.id.login_ll);
        fblogin_ll= (LinearLayout) findViewById(R.id.fblogin_ll);
        signup_ll= (LinearLayout) findViewById(R.id.signup_ll);
        login_ll.setOnClickListener(this);
        fblogin_ll.setOnClickListener(this);
        signup_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==login_ll)
        {
            Intent i=new Intent(MainScreen.this,LoginActivity.class);
            startActivity(i);
            finish();

        }
        if (v==signup_ll)
        {
            Intent i=new Intent(MainScreen.this,SignupActivity.class);
            startActivity(i);

        }
    }
}
