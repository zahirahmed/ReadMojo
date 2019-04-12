package com.gayatri.testapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.MyEditText;
import com.gayatri.testapp.Utils.post_async;

public class RecoverPasswordActivity extends Activity implements OnClickListener {
    Button recoverpwd;
    AutoCompleteTextView EmailAddress;
    LinearLayout cancel_ll;

    Display display;
    int height, width;
    SharedPreferences prefrence;
    Editor editor;
    CoordinatorLayout Clayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recoverpassword);
        display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        width = display.getWidth();
        prefrence = getSharedPreferences(Constants.PREF, 0);
        editor = prefrence.edit();

        recoverpwd = (Button) findViewById(R.id.bt_rcvrpwd);
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbarlocation);

        EmailAddress = (AutoCompleteTextView) findViewById(R.id.et_rem_madd);
        cancel_ll = (LinearLayout) findViewById(R.id.cancel_ll);


        cancel_ll.setOnClickListener(this);
        recoverpwd.setOnClickListener(this);

    }

    private boolean checkEmail(String email) {
        return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    @Override
    public void onClick(View v) {
        if(v==cancel_ll)
        {
            Intent i = new Intent(RecoverPasswordActivity.this,LoginActivity.class);
            startActivity(i);
            finish();

        }else if(v==recoverpwd)
        {
            if (TextUtils.isEmpty(EmailAddress.getText().toString())) {
                Snackbar.make(Clayout, "Enter Your Email Address.", Snackbar.LENGTH_SHORT).show();


            }
            else if (!checkEmail(EmailAddress.getText().toString())) {
                Snackbar.make(Clayout, "Invalid Email Address.", Snackbar.LENGTH_SHORT).show();


            }
            else
            {
         Forgotpassword();
            }


        }
    }

    private void Forgotpassword() {
        if(IsNetworkConnection.checkNetworkConnection(RecoverPasswordActivity.this))
        {
            String webdata = "{\"" + "email_id" + "\":" + "\""+ EmailAddress.getText().toString().trim()+ "\"" + "}";

            String url = Constants.SERVER_URL+"forgot_password" ;
            Log.d("System out", "url" + url);
            new post_async(RecoverPasswordActivity.this, "forgotpassword").execute(url, webdata);

        }
        else {
            Toast.makeText(RecoverPasswordActivity.this, "No Network", Toast.LENGTH_SHORT).show();
        }
    }

    public void responseofforgotpassword(String resultString) {

        try {

            JSONObject json = new JSONObject(resultString);

            String sStatus=json.getString("sStatus"); //<< get value here


            Log.d("System out","sStatus"+sStatus);
            if (sStatus.equalsIgnoreCase("1"))
            {
                JSONObject sData = json.getJSONObject("sData");
                String id=sData.getString("id");
                String email_id=sData.getString("email_id");
                Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();
                finish();

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



   /* Snackbar.make(Clayout, "This snack bar located at top", Snackbar.LENGTH_LONG).
    setAction("Retry",snackbarClickListener).show();
    View.OnClickListener snackbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
*/
}
