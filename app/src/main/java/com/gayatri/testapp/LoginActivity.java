package com.gayatri.testapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gayatri.testapp.Menu.MenuActivity;
import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.post_async;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Gayatri on 27-07-2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    SharedPreferences.Editor editor;
    SharedPreferences preference;
    AutoCompleteTextView MailAddress;
    EditText Password;
    CheckBox autologin;
    TextView forgotpwd;
    TextView clickhere;
    Button signin;
    TextView cancel;
    private CheckBox remember;
    private LinearLayout cancel_ll;
    private TextView iv_fblogin;
    private LoginButton fbbutton;
    CoordinatorLayout Clayout;
    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackmanager;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( Constants.dialog!=null && Constants.dialog.isShowing() ){
            Constants.dialog.cancel();
        }
        Constants.executeLogcat(LoginActivity.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        printHashKey();
        // Initialize SDK before setContentView(Layout ID)
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        init();

        // Initialize layout button
        fbbutton = (LoginButton) findViewById(R.id.fb_login_button);

        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call private method
                onFblogin();
            }
        });

        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        if (preference.getString("fromsignup","").equalsIgnoreCase("yes")) {
            editor.putString("fromsignup","no").commit();
            MailAddress.setText(preference.getString("email_id", ""));
        }
        else
        {

        }

        if (preference.getBoolean("isremember", true) == true) {


            Log.d("System out","u name" + preference.getString("useremail", ""));

            Log.d("System out", "pwd is" + preference.getString("password", ""));
        }
    }

    public void printHashKey() {

        try {
            PackageInfo info = this
                    .getPackageManager().getPackageInfo("com.gayatri.testapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("System out",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("System out",""+e);
        } catch (NoSuchAlgorithmException e) {
            Log.d("System out",""+e);
        }

    }



    private void init() {
        MailAddress = (AutoCompleteTextView) findViewById(R.id.et_login_madd);
        Password = (EditText) findViewById(R.id.et_login_pwd);
        forgotpwd = (TextView) findViewById(R.id.tv_frgtpwd);
        clickhere = (TextView) findViewById(R.id.tv_login_nwuser_click);
        signin = (Button) findViewById(R.id.bt_login_signin);
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbarlocation);


        signin.setOnClickListener(this);
        clickhere.setOnClickListener(this);
        forgotpwd.setOnClickListener(this);



    }

    private boolean checkEmail(String email) {
        return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        if (v == cancel) {

                finish();


        }
        if (v == iv_fblogin)
        {

        }
        if (v == signin) {

            if (TextUtils.isEmpty(MailAddress.getText().toString())) {
                Snackbar.make(Clayout, "Enter Your Email Address.", Snackbar.LENGTH_SHORT).show();


            }
            else if (!checkEmail(MailAddress.getText().toString())) {
                Snackbar.make(Clayout, "Invalid Email Address.", Snackbar.LENGTH_SHORT).show();


            } else if (TextUtils.isEmpty(Password.getText().toString())) {
                Snackbar.make(Clayout, "Enter Password.", Snackbar.LENGTH_SHORT).show();
            } else {


                editor.putString("from","").commit();
               loginapi();
            }

        }
        if (v == clickhere) {
            Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signup);
        }
        if (v == forgotpwd) {

            Intent recoverpwd = new Intent(LoginActivity.this,
                    RecoverPasswordActivity.class);
            startActivity(recoverpwd);

        }
        if (v == remember) {

        }
    }




    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {

            Log.d("System out", "is remember...in if");
           // remember.setButtonDrawable(R.drawable.unchacked);
            editor.putString("useremail", MailAddress.getText().toString());
            editor.putString("password", Password.getText().toString());
            editor.putBoolean("isremember", true);
            editor.commit();
            Log.d("System out", "User Login Data saved");

        } else {

          //  remember.setButtonDrawable(R.drawable.chacked);
            Log.d("System out", "is remember...in else2");
            editor.putString("useremail", "");
            editor.putString("password", "");
            editor.putBoolean("isremember", false);
            editor.commit();
        }

    }


    // Private method to handle Facebook login and callback
    private void onFblogin()
    {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("id","gender","birthday","email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            String jsonresult = String.valueOf(json);
                                            System.out.println("JSON Result" + jsonresult);

                                            String str_email = json.optString("email");
                                            String str_id = json.optString("id");
                                            String str_firstname = json.optString("first_name");
                                            String str_lastname = json.optString("last_name");
                                            String name = json.optString("name");

                                            editor.putString("str_email", str_email);
                                            editor.putString("str_firstname", str_firstname);
                                            editor.putString("str_lastname", str_lastname);
                                            editor.putString("str_id", str_id);
                                            editor.putString("from", "fb");
                                            editor.commit();

                                            Log.d("System out", "str_email" + str_email + "str_id" + str_id + "str_firstname" + str_firstname + "str_lastname" + str_lastname);
                                            LoginManager.getInstance().logOut();
                                            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                                            startActivity(i);
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {

                        Log.d("Syatem out", "On cancel");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("System out", error.toString());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    private void loginapi() {

            if(IsNetworkConnection.checkNetworkConnection(LoginActivity.this))
            {
                String webdata = "{\"" + "email_id" + "\":" + "\""+ MailAddress.getText().toString().trim() + "\"" + ",\""
                        + "password"+ "\":" + "\"" + Password.getText().toString().trim() + "\"" +"}";

                String url = Constants.SERVER_URL+"Login" ;
                Log.d("System out", "url" + url);
                new post_async(LoginActivity.this, "Login").execute(url, webdata);

            }
            else {
                Toast.makeText(LoginActivity.this, "No Network", Toast.LENGTH_SHORT).show();
            }

    }

    public void responseoflogin(String resultString) {
        Log.d("System out","response of login");
        Constants.executeLogcat(LoginActivity.this);

        try {

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
                String dob=sData.getString("dob");
                String language=sData.getString("language");
                String gender=sData.getString("gender");
                String device_token=sData.getString("device_token");
                String created_by=sData.getString("created_by");
                String created_on=sData.getString("created_on");
                String earned_point=sData.getString("earned_point");
                String updated_on=sData.getString("updated_on");
                String updated_by=sData.getString("updated_by");
                editor.putString("id",id);
                editor.putString("first_name",first_name);
                editor.putString("last_name",last_name);
                editor.putString("email_id",email_id);
                editor.putString("password",password);
                editor.putString("dob",dob);
                editor.putString("gender",gender);
                editor.putString("device_token",device_token);
                editor.putString("created_by",created_by);
                editor.putString("created_on",created_on);
                editor.putString("language",language);
                editor.putString("earned_point",earned_point);
                editor.putString("updated_on",updated_on);
                editor.putString("updated_by",updated_by);
                editor.commit();

                Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                                    Intent i3=new Intent(LoginActivity.this,
                                            MenuActivity.class);
                                    startActivity(i3);
                                    finish();


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
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
