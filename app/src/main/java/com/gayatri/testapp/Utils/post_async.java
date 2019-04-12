package com.gayatri.testapp.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gayatri.testapp.AllBooksListActivity;
import com.gayatri.testapp.CreateBooksActivity;
import com.gayatri.testapp.CreateChildAccount;
import com.gayatri.testapp.LoginActivity;
import com.gayatri.testapp.R;
import com.gayatri.testapp.RecoverPasswordActivity;
import com.gayatri.testapp.ShareActivity;
import com.gayatri.testapp.SignupActivity;
import com.gayatri.testapp.TestActivity;
import com.gayatri.testapp.TextActivity;


@SuppressWarnings("deprecation")
public class post_async extends AsyncTask<String, Integer, String> {

    private LoginActivity loginActivity;
    private SignupActivity signupActivity;
    private RecoverPasswordActivity recoverPasswordActivity;
    private TextActivity textActivity;
    private TestActivity testActivity;
    private CreateChildAccount createChildAccount;
    private CreateBooksActivity createBooksActivity;
    private AllBooksListActivity allBooksListActivity;
    private ShareActivity shareActivity;
    static String action = "", resultString = "";
    Activity activity;
    ProgressBar progressbar;


    public post_async(LoginActivity loginactivity, String action) {
        this.loginActivity = loginactivity;
        this.activity = loginactivity;
        this.action = action;
    }

    public post_async(SignupActivity signupActivity, String action) {
        this.signupActivity = signupActivity;
        this.activity = signupActivity;
        this.action = action;
    }

    public post_async(RecoverPasswordActivity recoverPasswordActivity, String action) {
        this.recoverPasswordActivity = recoverPasswordActivity;
        this.activity = recoverPasswordActivity;
        this.action = action;
    }

    public post_async(TextActivity textActivity, String action) {
        this.textActivity = textActivity;
        this.activity = textActivity;
        this.action = action;
    }

    public post_async(TestActivity testActivity, String action) {
        this.testActivity = testActivity;
        this.activity = testActivity;
        this.action = action;
    }

    public post_async(CreateChildAccount createChildAccount, String action) {
        this.createChildAccount = createChildAccount;
        this.activity = createChildAccount;
        this.action = action;
    }

    public post_async(CreateBooksActivity createBooksActivity, String action) {
        this.createBooksActivity = createBooksActivity;
        this.activity = createBooksActivity;
        this.action = action;
    }

    public post_async(AllBooksListActivity allBooksListActivity, String action) {
        this.allBooksListActivity = allBooksListActivity;
        this.activity = allBooksListActivity;
        this.action = action;
    }

    public post_async(ShareActivity shareActivity, String shareBook)
    {
    this.shareActivity = shareActivity;
    this.activity = shareActivity;
    this.action = action;
}


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Constants.dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        progressbar = new ProgressBar(activity);
        progressbar.setBackgroundResource(R.drawable.ajax_loader);
        Constants.dialog.addContentView(progressbar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        Window window = Constants.dialog.getWindow();
        window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        if (Constants.dialog != null) {
            Constants.dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 2) {
            invoke(params[0], params[1]);

        } else if (params.length == 1) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i("System out", "result: " + resultString);
        try {
            Constants.dialog.dismiss();
            System.gc();
            Runtime.getRuntime().gc();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("here in post execute method");
        sendResult();
    }

    private void sendResult() {


        if (this.loginActivity != null && action.equalsIgnoreCase("Login")) {
            this.loginActivity.responseoflogin(resultString);
        } else if (this.signupActivity != null && action.equalsIgnoreCase("SignUp")) {
            this.signupActivity.responseofsignup(resultString);
        } else if (this.recoverPasswordActivity != null && action.equalsIgnoreCase("forgotpassword")) {
            this.recoverPasswordActivity.responseofforgotpassword(resultString);
        } else if (this.textActivity != null && action.equalsIgnoreCase("create_book")) {
            this.textActivity.responseofcretebook(resultString);

        } else if (this.testActivity != null && action.equalsIgnoreCase("create_book_test")) {
            this.testActivity.responseoftestbook(resultString);

        } else if (this.createChildAccount != null && action.equalsIgnoreCase("createchild")) {
            this.createChildAccount.responseofcreateChildAccount(resultString);

        } else if (this.allBooksListActivity != null && action.equalsIgnoreCase("allcreated_bookdetail")) {
            this.allBooksListActivity.responseofallcreated_bookdetail(resultString);

        }
else if (this.shareActivity != null && action.equalsIgnoreCase("ShareBook")) {
            this.shareActivity.responseofShareBook(resultString);

        }


    }

    @SuppressWarnings("deprecation")
    public static String invoke(String url, String postString) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String s = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);

        HttpClient client = new DefaultHttpClient(httpParameters);
        client.getParams().setParameter("http.protocol.version",
                HttpVersion.HTTP_1_1);
        client.getParams().setParameter("http.socket.timeout",
                new Integer(2000));
        client.getParams().setParameter("http.protocol.content-charset",
                HTTP.UTF_8);

        httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
        HttpPost request = new HttpPost(url);
        request.getParams().setParameter("http.socket.timeout", new Integer(5000));

        Log.i("System out", "link : " + url);
        Log.i("System out", "postString : " + postString);
        try {
            org.apache.http.Header[] headers = request.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Log.i("System out", "headername : " + headers[i].getName()
                        + ", value: " + headers[i].getValue());
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("webdata", postString));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                resultString = EntityUtils.toString(httpEntity).toString();
                Log.i("System out", "response is :" + resultString);
            }
            httpEntity = null;
            response = null;
        } catch (ClientProtocolException e) {
            Log.d("System out", "client protocol exception");
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (Exception e) {
            e.printStackTrace();
        }
        httppost = null;
        httpclient = null;
        return resultString;
    }

}
