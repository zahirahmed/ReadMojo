package com.gayatri.testapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Gayatri on 01-05-2016.
 */
public class CreateChildAccountActivity extends Activity implements View.OnClickListener {
    private Button bt_add_child,bt_skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addchild_skip);
        iniit();
    }

    private void iniit() {
        bt_add_child=(Button)findViewById(R.id.bt_add_child);
        bt_skip=(Button)findViewById(R.id.bt_skip);
        bt_skip.setOnClickListener(this);
        bt_add_child.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==bt_skip)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                finish();
                            }
                        });
                    } catch (Exception e) {
                        Log.w("Exception in splash", e);
                    }
                }
            }).start();

        }else if (v==bt_add_child)
        {

            Intent i=new Intent(CreateChildAccountActivity.this,CreateChildAccount.class);
            startActivity(i);
            finish();
        }
    }
}
