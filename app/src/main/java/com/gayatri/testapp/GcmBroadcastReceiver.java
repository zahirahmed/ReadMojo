package com.gayatri.testapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Gayatri on 29-10-2015.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());

        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

}