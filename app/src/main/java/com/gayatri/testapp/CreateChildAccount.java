package com.gayatri.testapp;

import android.*;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.MyTextView;
import com.gayatri.testapp.Utils.post_async;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Gayatri on 03-04-2016.
 */
public class CreateChildAccount extends Activity implements View.OnClickListener {
    private CoordinatorLayout Clayout;
    private  AutoCompleteTextView Name,MailAddress;
    private  Button cancel;
    private TextView dob;
    private RadioGroup RadioGroupsignup;
    private RadioButton signup_female,signup_male;
    private LinearLayout cancel_ll;
    String Gender="Male";
    private Button bt_login_addaccount;
    private ListView lv_childlist;
    private  ChildArrayAdapter childArrayAdapter;
    SharedPreferences.Editor editor;

    SharedPreferences preference;


    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( Constants.dialog!=null && Constants.dialog.isShowing() ){
            Constants.dialog.cancel();
        }
        Constants.executeLogcat(CreateChildAccount.this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createchild);
        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        Constants.childrenarraylist.clear();
        init();

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("System out", "permission check " + permissionCheck);
        if (permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);


        }

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("System out", "permission check " + permissionCheck1);
        if (permissionCheck1 == android.content.pm.PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);


        }


        int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        Log.d("System out", "permission check " + permissionCheck2);
        if (permissionCheck2 == android.content.pm.PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1234);


        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 12: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("System out", "permission granted");
                }
            }
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("System out", "permission granted");
                }
            }

            case 1234: {
                // If request is cancelled, the result arrays are empty.
                int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
                Log.d("System out", "permission check " + permissionCheck2);
                if (permissionCheck2 == android.content.pm.PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1234);


                }
                return;
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request

    }
    private void init() {
        cancel = (Button) findViewById(R.id.bt_signup_cancel);
        lv_childlist = (ListView) findViewById(R.id.lv_childlist);
        dob = (TextView) findViewById(R.id.tv_signup_birthdate);
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbarlocation);
        RadioGroupsignup = (RadioGroup) findViewById(R.id.signup_rg);
        signup_male = (RadioButton) findViewById(R.id.rb_male);
        signup_female = (RadioButton) findViewById(R.id.rb_female);
        MailAddress = (AutoCompleteTextView) findViewById(R.id.et_login_madd);
        bt_login_addaccount = (Button) findViewById(R.id.bt_login_addaccount);
        //cancel_ll = (LinearLayout) findViewById(R.id.cancel_ll);
        Name = (AutoCompleteTextView) findViewById(R.id.et_login_mname);
        //cancel_ll.setOnClickListener(this);
        signup_female.setOnClickListener(this);
        bt_login_addaccount.setOnClickListener(this);

        signup_male.setOnClickListener(this);
        dob.setOnClickListener(this);
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
                        //Toast.makeText(SignupActivity.this,"Cannot Enter Current Date", Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        Date date = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(str);
                        long milliseconds = date.getTime();
                        //  long millisecondsFromNow = milliseconds - (new Date()).getTime();
                        //Toast.makeText(this, "Milliseconds to future date="+millisecondsFromNow, Toast.LENGTH_SHORT).show();

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
                    //  Toast.makeText(SignupActivity.this,"Please select past date", Toast.LENGTH_LONG).show();
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


    @Override
    public void onClick(View v) {
        if (v==cancel_ll)
        {
            finish();
        }else if (v==signup_female)
        {

        }
        else if (v==signup_male)
        {

        }
        if (v == dob)
        {
            showDialog(1);
        }
        if (v == signup_female)
        {
            signup_male.setChecked(false);
            signup_female.setChecked(true);

            Gender=signup_female.getText().toString();
        }
        if (v == signup_male)
        {
            signup_male.setChecked(true);
            signup_female.setChecked(false);
            Gender=signup_male.getText().toString();

        }
         if (v==bt_login_addaccount)
         {

             if (TextUtils.isEmpty(Name.getText().toString()))
             {
                 Snackbar.make(Clayout, "Enter Name.", Snackbar.LENGTH_SHORT).show();
             }

             else if (Name.getText().length() > 25)
             {
                 Snackbar.make(Clayout, "please enter less the 25 characher in user name.", Snackbar.LENGTH_SHORT).show();
             }  else if (TextUtils.isEmpty(MailAddress.getText().toString()))
             {
                 Snackbar.make(Clayout, "Enter Email Address.", Snackbar.LENGTH_SHORT).show();
             }
             else if (!checkEmail(MailAddress.getText().toString()))
             {
                 Snackbar.make(Clayout, "Please enter valid email address.", Snackbar.LENGTH_SHORT).show();
             }
             else
             {
                 try {
                     HashMap<String, String> map = new HashMap<String, String>();
                     map.put("name", Name.getText().toString());
                     map.put("email", MailAddress.getText().toString());
                     map.put("gender", Gender);
                     map.put("dob", dob.getText().toString());
                     Constants.childrenarraylist.add(map);
                     if (Constants.childrenarraylist.size() > 0) {
                         lv_childlist.setVisibility(View.VISIBLE);
                         childArrayAdapter = new ChildArrayAdapter(CreateChildAccount.this, Constants.childrenarraylist);
                         lv_childlist.setAdapter(childArrayAdapter);
                     } else {
                         lv_childlist.setVisibility(View.GONE);
                     }

                     sendchildrendata(map);
                 }
                 catch (Exception e)
                 {
                     e.printStackTrace();
                 }

             }
         }
    }



    private void sendchildrendata(HashMap<String, String> map) {
        if(IsNetworkConnection.checkNetworkConnection(CreateChildAccount.this))
        {
            String webdata = "{\"" + "name" + "\":" + "\""+ map.get("name") + "\"" + ",\""
                    + "dob"+ "\":" + "\"" + map.get("dob") + "\"" +",\""
                    + "gender" + "\":" + "\""+ map.get("gender") + "\"" + ",\""
                    + "language"+ "\":" + "\"" +"English"+ "\"" + ",\""
                    + "user_id" + "\":" + "\"" +preference.getString("id","") + "\"" + ",\""
                    + "created_by" + "\":" + "\"" +"1" + "\"" + "}";

            String url = Constants.SERVER_URL+"create_child" ;
            Log.d("System out", "url" + url);
            new post_async(CreateChildAccount.this, "createchild").execute(url, webdata);

        }
        else {

            Snackbar.make(Clayout, "No Network.", Snackbar.LENGTH_SHORT).show();

            //Toast.makeText(SignupActivity.this, "No Network", Toast.LENGTH_SHORT).show();
        }
    }

    public void responseofcreateChildAccount(String resultString) {
        Log.d("System out","response of create child"+resultString);

        Constants.executeLogcat(CreateChildAccount.this);

        if (resultString.length()>2)
        {
            try
            {

                JSONObject json = new JSONObject(resultString);

               String sStatus="";
                try {
                     sStatus = json.getString("sStatus"); //<< get value here
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


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
                    String gender=sData.getString("gender");
                    String device_token=sData.getString("device_token");
                    String created_by=sData.getString("created_by");
                    String created_on=sData.getString("created_on");
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


                    editor.commit();
                    Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();
                    editor.putString("fromsignup","yes").commit();

/*if (Constants.childrenarraylist.size()>0)
{
    for (int i=0;i<Constants.childrenarraylist.size();i++)
    {
        sendchildrendata(id,i);
    }

}
            else
{*/
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

//}


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

    public class ChildArrayAdapter extends BaseAdapter {
        private Activity context;
        private ArrayList<HashMap<String, String>> mobileValues=new ArrayList<HashMap<String, String>>();
        public ChildArrayAdapter(Activity context, ArrayList<HashMap<String, String>> mobileValues) {
            this.context = context;
            this.mobileValues = mobileValues;
        }

        @Override
        public int getCount() {
            return mobileValues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

         //   LayoutInflater inflater = (LayoutInflater) CreateChildAccount.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=null;
            try {


                /*LayoutInflater inflater = LayoutInflater.from(CreateChildAccount.this);
                 view = inflater.inflate(R.layout.childadapter, parent, false);*/

             /*   LayoutInflater inflater =(LayoutInflater)getSystemService(context.LAYOUT_INFLATER_SERVICE);
                 view = inflater.inflate(R.layout.childadapter, parent, true);*/

                view=convertView;
                if (view == null) {

                    LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(CreateChildAccount.this);
                    view = inflater.inflate(R.layout.childadapter, parent, false);

                    MyTextView child_name = (MyTextView) view.findViewById(R.id.child_name);
                    MyTextView child_dob = (MyTextView) view.findViewById(R.id.child_dob);
                    MyTextView child_gender = (MyTextView) view.findViewById(R.id.child_gender);
                    MyTextView child_email = (MyTextView) view.findViewById(R.id.child_email);
                    LinearLayout update_ll = (LinearLayout) view.findViewById(R.id.update_ll);
                    // set image based on selected text
                    child_name.setId(position);
                    child_dob.setId(position);
                    child_gender.setId(position);
                    child_email.setId(position);
                    child_name.setText("" + mobileValues.get(position).get("name"));
                    child_email.setText("" + mobileValues.get(position).get("email"));
                    child_gender.setText("" + mobileValues.get(position).get("gender"));
                    child_dob.setText("" + mobileValues.get(position).get("dob"));
                    update_ll.setId(position);

                    update_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int idretrived = v.getId();
                            Name.setText(mobileValues.get(position).get("name"));
                            MailAddress.setText(mobileValues.get(position).get("name"));
                            dob.setText(mobileValues.get(position).get("name"));
                            if (mobileValues.get(position).get("gender").equalsIgnoreCase("Male")) {
                                signup_female.setChecked(false);
                                signup_male.setChecked(true);
                            } else {
                                signup_female.setChecked(true);
                                signup_male.setChecked(false);
                            }

                        }
                    });

                }



                // set value into textview

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return view;
        }


    }

    private boolean checkEmail(String email) {
        return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
