/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gayatri.testapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.gayatri.testapp.Utils.Constants;

/**
 * This activity is run as the click activity for {@link IncomingMessageView}. When
 * it comes up, it also clears the notification, because the "message" has been
 * "read."
 */

public class IncomingMessageView extends Activity {

	SharedPreferences preferences;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		preferences = getSharedPreferences(Constants.PREF, 0);
		editor=preferences.edit();
		editor.putString("activity", "incoming");
		editor.commit();
		Log.d("System out", "in imcoming msg" );
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(0);
		Intent intent = new Intent(getApplicationContext(),	LoginActivity.class);
		startActivity(intent);
		IncomingMessageView.this.finish();
	}
}
