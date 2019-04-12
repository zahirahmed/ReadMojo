package com.gayatri.testapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.SimpleArrayMap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.MyEditText;
import com.gayatri.testapp.Utils.post_async;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by Gayatri on 27-07-2015.
 */
public class SignupActivity extends Activity implements View.OnClickListener {


    AutoCompleteTextView MailAddress, Name, phnno, pwd, cpwd,MiddleName,LastNae;
    RadioButton male, female;

    Button signup, cancel;
    TextView dob;
    CoordinatorLayout Clayout;
    CheckBox terms;
    RadioGroup RadioGroupsignup;
    RadioButton signup_female, signup_male;
    String Gender="Male";
    SharedPreferences.Editor editor;

    SharedPreferences preference;
    private TextView termsncond;

    public static String MobilePattern = "[0-9]{10}";
    private LinearLayout cancel_ll;
    FloatingActionButton fab;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private final static String TAG = "LaunchActivity";

    private GoogleCloudMessaging gcm =null;
    private String regid = null;
    private Context context= null;
    String regId ="",msg ="";
    private HashMap<String, String> map;


    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( Constants.dialog!=null && Constants.dialog.isShowing() ){
            Constants.dialog.cancel();
        }
        Constants.executeLogcat(SignupActivity.this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupscreen);
        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();

        init();
        if (preference.getString("from","").equalsIgnoreCase("fb"))
        {
            Name.setText(""+preference.getString("str_firstname",""));
            Name.setText(""+preference.getString("name",""));
            MiddleName.setText("" + preference.getString("str_email", ""));
            LastNae.setText("" + preference.getString("str_lastname", ""));
        }
        else
        {
            Name.setText("");
            LastNae.setText("");
            MailAddress.setText("");
        }
        context = getApplicationContext();
        if (checkPlayServices())
        {
            gcm = GoogleCloudMessaging.getInstance(this);
            getRegisterationID();
        }

        if (IsNetworkConnection.checkNetworkConnection(this)) {
        } else {
            Toast.makeText(this, "No Network", Toast.LENGTH_SHORT).show();
        }

    }



    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(TAG, "This device is not supported - Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }




    public void getRegisterationID() {

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                // TODO Auto-generated method stub
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(SignupActivity.this);
                    }
                    regId = gcm.register(Constants.SENDER_ID);
                    Log.d("in async task", regId);

                    // try
                    msg = "Device registered, registration ID=" + regId;
                    Constants.DEVICE_TOKEN=regId;
                    Log.d("in async task", "DEVICE_TOKEN"+Constants.DEVICE_TOKEN);


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void init() {
        signup = (Button) findViewById(R.id.bt_signup);
        cancel = (Button) findViewById(R.id.bt_signup_cancel);
        dob = (TextView) findViewById(R.id.tv_signup_birthdate);
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbarlocation);
        RadioGroupsignup = (RadioGroup) findViewById(R.id.signup_rg);
        signup_male = (RadioButton) findViewById(R.id.rb_male);
        signup_female = (RadioButton) findViewById(R.id.rb_female);
        MailAddress = (AutoCompleteTextView) findViewById(R.id.et_signup_add);
        Name = (AutoCompleteTextView) findViewById(R.id.et_signup_name);
        MiddleName = (AutoCompleteTextView) findViewById(R.id.et_signup_middlename);
        LastNae = (AutoCompleteTextView) findViewById(R.id.et_signup_lastname);
        phnno = (AutoCompleteTextView) findViewById(R.id.et_signup_phnno);
        pwd = (AutoCompleteTextView) findViewById(R.id.et_signup_pwd);
        cpwd = (AutoCompleteTextView) findViewById(R.id.et_signup_cpwd);
        terms = (CheckBox) findViewById(R.id.cb_terms);
         fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setVisibility(View.GONE);
        fab.setOnClickListener(this);
        termsncond = (TextView) findViewById(R.id.tv_termsncond);


        signup.setOnClickListener(this);
        termsncond.setOnClickListener(this);
        cancel.setOnClickListener(this);
        terms.setOnClickListener(this);
        signup_female.setOnClickListener(this);
        signup_male.setOnClickListener(this);
        dob.setOnClickListener(this);

        cancel_ll = (LinearLayout) findViewById(R.id.cancel_ll);


        cancel_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });



    }

    private boolean checkEmail(String email) {
        return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    protected Dialog onCreateDialog(int id) {
        int year;
        int month;
        int day;
        switch (id) {

            case 1:
                // set date picker as current date
                final Calendar c1 = Calendar.getInstance();
                year = c1.get(Calendar.YEAR);
                month = c1.get(Calendar.MONTH);
                day = c1.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, datePickerListener, year, month,day);

        }

        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == signup) {
            if (TextUtils.isEmpty(Name.getText().toString()))
            {
                Snackbar.make(Clayout, "Enter Name.", Snackbar.LENGTH_SHORT).show();

            }

            else if (Name.getText().length() > 25)
            {
                Snackbar.make(Clayout, "please enter less the 25 characher in user name.", Snackbar.LENGTH_SHORT).show();


            } else if (TextUtils.isEmpty(MiddleName.getText().toString()))
            {
                Snackbar.make(Clayout, "Enter Middle Name.", Snackbar.LENGTH_SHORT).show();

            }

            if (MiddleName.getText().length() > 25)
            {
                Snackbar.make(Clayout, "please enter less the 25 characher in middle name.", Snackbar.LENGTH_SHORT).show();



            }
            if (TextUtils.isEmpty(LastNae.getText().toString()))
            {

                Snackbar.make(Clayout, "Enter Last Name.", Snackbar.LENGTH_SHORT).show();

            }

            if (LastNae.getText().length() > 25)
            {
                Snackbar.make(Clayout, "please enter less the 25 characher in last name.", Snackbar.LENGTH_SHORT).show();



            }
            else if (TextUtils.isEmpty(MailAddress.getText().toString()))
            {
                Snackbar.make(Clayout, "Enter Email Address.", Snackbar.LENGTH_SHORT).show();

            }

            else if (!checkEmail(MailAddress.getText().toString()))
            {   Snackbar.make(Clayout, "Please enter valid email address.", Snackbar.LENGTH_SHORT).show();


            } else if (TextUtils.isEmpty(phnno.getText().toString()))
            {

                Snackbar.make(Clayout, "Enter Phone No.", Snackbar.LENGTH_SHORT).show();
            } else if (!phnno.getText().toString().matches(MobilePattern))
            {
                Snackbar.make(Clayout, "Please enter valid 10 digit Phone No.", Snackbar.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(pwd.getText().toString()))
            {
                Snackbar.make(Clayout, "Enter Password.", Snackbar.LENGTH_SHORT).show();


            }else if ((cpwd.getText().toString() == null))
            {
                Snackbar.make(Clayout, "Enter Confirm Password.", Snackbar.LENGTH_SHORT).show();

            } else if (!(cpwd.getText().toString().equals(pwd.getText().toString())))
            {

                Snackbar.make(Clayout, "Enter same password.", Snackbar.LENGTH_SHORT).show();
            }
            else if (terms.isChecked()==false) {
                Snackbar.make(Clayout, "Accept terms and condition.", Snackbar.LENGTH_SHORT).show();

            }else {
                  SIGNUP();

            }
        }

        if (v == signup_female)
        {


            Gender=signup_female.getText().toString();
        }
        if (v == signup_male)
        {

            Gender=signup_male.getText().toString();

        }
         if(v==fab)
         {
             createCustomAnimation();
             Intent i=new Intent(SignupActivity.this,CreateChildAccount.class);
             startActivity(i);
         }

        if (v == terms)
        {
            terms();
        }
        if (v == dob)
        {
            showDialog(1);
        }

        if (v==termsncond) {
            Intent i=new Intent(SignupActivity.this,TermsAndConditionActivity.class);
            startActivity(i);

        }
        if (v == cancel)
        {
            Intent i=new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void createCustomAnimation() {


        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(fab.isSelected(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fab.isSelected(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fab.isFocused(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fab.isFocused(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fab.setImageResource(fab.isSelected()
                        ? R.drawable.ic_close : R.drawable.add_user_icon);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));


    }


    private void terms()
    {
        if (((CheckBox) terms).isChecked())
        {
           // terms.setButtonDrawable(R.drawable.chacked);


        } else
        {
           // terms.setButtonDrawable(R.drawable.unchacked);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        private String stYear;
        private String stMonth;
        private String stDay;
        private String stDate;

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay)
        {
            stYear = "" + selectedYear;

            selectedMonth += 1;
            if (selectedMonth < 10) {
                stMonth = "" + 0 + selectedMonth;
            } else {
                stMonth = "" + selectedMonth;
            }

            if (selectedDay < 10) {
                stDay = "" + 0 + selectedDay;
            } else {
                stDay = "" + selectedDay;
            }

            stDate = stDay + "-" + stMonth + "-" + stYear;
            String dateofbirth = stDate;
            String inputPattern = "dd-MM-yyyy";
            String outputPattern = "MM-dd-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date ddate = null;
            String str = null;

            try {
                ddate = inputFormat.parse(dateofbirth);
                str = outputFormat.format(ddate);

                Date now = new Date(System.currentTimeMillis());

                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                String s = formatter.format(now);
                System.out.println(s);

                Log.d("System out", "Current date is:" + s);


                if (ddate.before(new Date()) )
                {
                    if (str.equals(s)) {
                        Snackbar.make(Clayout, "Cannot select current date.", Snackbar.LENGTH_SHORT).show();

                    }
                    else
                    {

                        Date date = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(str);
                        long milliseconds = date.getTime();

                        if (Integer.parseInt(getAge(milliseconds))>=18) {
                            dob.setText(str + "");
                        }
                        else
                        {
                            Snackbar.make(Clayout, "You are too younger to register.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                } else
                {
                    Snackbar.make(Clayout, "Cannot select future date.", Snackbar.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }

    };

    private String getAge(long selectedMilli) {
        Date dateOfBirth = new Date(selectedMilli);
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob
                .get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        if (age < 18) {
            //do something
        } else {

        }

        String str_age = age + "";
        Log.d("", getClass().getSimpleName() + ": Age in year= " + age);
        return  str_age;

    }


    private void SIGNUP() {


        String d=dob.getText().toString();

        String inputPattern = "MM-dd-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date ddate = null;
        String str = null;

        try {
            ddate = inputFormat.parse(d);
            str = outputFormat.format(ddate);

        } catch (Exception e) {
            e.printStackTrace();
        }




        if(IsNetworkConnection.checkNetworkConnection(SignupActivity.this))
        {
            String webdata = "{\"" + "first_name" + "\":" + "\""+ Name.getText().toString().trim() + "\"" + ",\""
                    + "last_name"+ "\":" + "\"" + LastNae.getText().toString().trim() + "\"" +",\""
                    +"middle_name"+"\":"+"\""+MiddleName.getText().toString().trim()+"\""+",\""
                    + "email_id" + "\":" + "\""+ MailAddress.getText().toString().trim() + "\"" + ",\""
                    + "mobile_no" + "\":" + "\""+ phnno.getText().toString().trim() + "\"" + ",\""
                    + "password"+ "\":" + "\"" +cpwd.getText().toString().trim() + "\"" + ",\""
                    + "dob" + "\":" + "\"" +str.trim() + "\"" + ",\""
                    + "gender" + "\":" + "\"" +Gender.trim() + "\"" + ",\""
                    + "language" + "\":" + "\""+  "English"+ "\"" + ",\""
                    + "device_token" + "\":" + "\""+Constants.DEVICE_TOKEN+ "\"" + ",\""
                    + "created_by" + "\":" + "\""+ "1" + "\"" +
                    "}";

            String url = Constants.SERVER_URL+"register" ;
            Log.d("System out", "url" + url);
            new post_async(SignupActivity.this, "SignUp").execute(url, webdata);

        }
        else {

            Snackbar.make(Clayout, "No Network.", Snackbar.LENGTH_SHORT).show();


        }
    }
    public void responseofsignup(String resultString) {
        Log.d("System out","response of signup"+resultString);
        Constants.executeLogcat(SignupActivity.this);

        if (resultString.length()>2)
{
    try
    {

        JSONObject json = new JSONObject(resultString);
       String sStatus=json.getString("sStatus"); //<< get value here


        Log.d("System out","sStatus"+sStatus);
        if (sStatus.equalsIgnoreCase("1"))
        {
            JSONObject sData = json.getJSONObject("sData");
            String id=sData.getString("id");
            String first_name=sData.getString("first_name");
            String last_name=sData.getString("last_name");
            String email_id=sData.getString("email_id");
            String password=sData.getString("password");
            String mobile_no=sData.getString("mobile_no");
            String dob=sData.getString("dob");
            String gender=sData.getString("gender");
            String device_token=sData.getString("device_token");
            String created_by=sData.getString("created_by");
            String created_on=sData.getString("created_on");
            editor.putString("id",id);
            editor.putString("first_name",first_name);
            editor.putString("last_name",last_name);
            editor.putString("email_id",email_id);
            editor.putString("password",password);
            editor.putString("mobile_no",mobile_no);
            editor.putString("dob",dob);
            editor.putString("gender",gender);
            editor.putString("device_token",device_token);
            editor.putString("created_by",created_by);
            editor.putString("created_on",created_on);

            editor.commit();
            Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();
            editor.putString("fromsignup","yes").commit();

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1500);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Intent i=new Intent(SignupActivity.this,CreateChildAccountActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            } catch (Exception e) {
                Log.w("Exception in splash", e);
            }
        }
    }).start();

        }
        else
        {
            Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();

        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}

    }


}
