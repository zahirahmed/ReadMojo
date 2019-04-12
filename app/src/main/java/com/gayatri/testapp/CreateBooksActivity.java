package com.gayatri.testapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.SlideHolder;
import com.gayatri.testapp.uploadImage.CallAPiActivity;
import com.gayatri.testapp.uploadImage.GetResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Gayatri on 10-08-2015.
 */
public class CreateBooksActivity extends Activity implements
        View.OnClickListener  {
    SlideHolder slider;
    private ImageView iv_menu;
    private RelativeLayout taptorecord_rl;
    protected static final int RESULT_SPEECH = 1;
    private ImageView next_btn;
    private AutoCompleteTextView book_detail;
    private TextView tv_browseaudio;
    private String filepath = "MyFileStorage";
    File myExternalFile;
    FileOutputStream fos;
    OutputStreamWriter osw;
    SharedPreferences.Editor editor;
    SharedPreferences preference;
    Uri audioFileUri;
    private String selectedAudioPath;
    private String audiotitle;
    static String encoded;
    private CoordinatorLayout Clayout;
    private Button bt_browseaudio;
    boolean isAudioSelected=false;
    byte[] sig = new byte[500000] ;
    int sigPos = 0 ;
    private String outputFile;
    CallAPiActivity callAPiActivity;
    public static String URLUPDATEUSER;
    private ProgressBar progressbar;


    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

        if (slider.isOpened()) {
            slider.toggle();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.createaudiobook);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toast.makeText(this, "CreateBooks", Toast.LENGTH_SHORT).show();
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReadMojorecording.mp3";
        callAPiActivity = new CallAPiActivity(CreateBooksActivity.this);
        URLUPDATEUSER = "http://dev.readmojo.com/readmojo_ws2/index.php/services/user_details/create_audio_book";

        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        init();

    }

    private void init() {
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbaraudiobook);
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        book_detail = (AutoCompleteTextView) findViewById(R.id.book_detail);
        tv_browseaudio = (TextView) findViewById(R.id.tv_browseaudio);
        tv_browseaudio.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        taptorecord_rl=(RelativeLayout)findViewById(R.id.taptorecord_rl);
        taptorecord_rl.setOnClickListener(this);
        next_btn=(ImageView)findViewById(R.id.next_btn);
        bt_browseaudio=(Button)findViewById(R.id.bt_browseaudio);
        next_btn.setOnClickListener(this);
        bt_browseaudio.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==next_btn)
        {
            if(!(book_detail.getText().toString().isEmpty()))
            {
                Intent i=new Intent(CreateBooksActivity.this,CreateAudioBook.class).putExtra("outputFile",outputFile).putExtra("book_detail",book_detail.getText().toString());
                startActivity(i);
                finish();
            }
        }
        if (view == iv_menu) {
            slider.toggle();
        }
        if (view==tv_browseaudio)
        {
            /*Intent intent;
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.setType("audio/mpeg");
            intent.setType("audio/*");*/
            Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Audio File!"), 4);
            Toast.makeText(CreateBooksActivity.this, "audiooooo", Toast.LENGTH_SHORT).show();


        }
        if (view==taptorecord_rl)
        {
            if (ActivityCompat.checkSelfPermission(CreateBooksActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(CreateBooksActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                        0);

            }
            else {
//                recordAudio();

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
                intent.putExtra("android.speech.extra.GET_AUDIO", true);


                SpeechRecognizer recognizer = SpeechRecognizer
                        .createSpeechRecognizer(this.getApplicationContext());

                RecognitionListener listener = new RecognitionListener() {

                    @Override
                    public void onResults(Bundle results) {
                        ArrayList<String> voiceResults = results
                                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (voiceResults == null) {
                            Log.e("voiceResults", "No voice results");
                        } else {
                            Log.d("voiceResults", "Printing matches: ");
                            for (String match : voiceResults) {
                                Log.d("voiceResults", match);
                            }
                        }
                    }

                    @Override
                    public void onReadyForSpeech(Bundle params) {
                        Log.d("voiceResults", "Ready for speech");
                    }

                    @Override
                    public void onError(int error) {
                        Log.d("voiceResults",
                                "Error listening for speech: " + error);
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        Log.d("voiceResults", "Speech starting");
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                        // TODO Auto-generated method stub
                        //TextView display=(TextView)findViewById (R.id.Text1);
                        book_detail.setText("True");

                        System.arraycopy(buffer, 0, sig, sigPos, buffer.length) ;
                        sigPos += buffer.length ;

                    }

                    @Override
                    public void onEndOfSpeech() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                        // TODO Auto-generated method stub

                    }
                };
                recognizer.setRecognitionListener(listener);
                recognizer.startListening(intent);


                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    //     txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }
        if (view==bt_browseaudio)
        {
            if (isAudioSelected==true)
            {
                //uploadFile(selectedAudioPath,audiotitle);
                createBookWithAudio();
            }
            else
            {
                Snackbar.make(Clayout, "Please select/create audio file.", Snackbar.LENGTH_SHORT).show();

            }
        }

    }

    public void recordAudio() {

        long timeForImgname = System.currentTimeMillis();
        String imgName = "img_" + timeForImgname ;
        String fileName = imgName;
        final MediaRecorder recorder = new MediaRecorder();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sound/";
        File cacheDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir=new File(Environment.getExternalStorageDirectory(),"/sound" +"");
        else
            cacheDir=CreateBooksActivity.this.getCacheDir();

        if(!cacheDir.exists())
            cacheDir.mkdirs();

        File myfile = new File(cacheDir, fileName + ".mp3"); // log file name

        recorder.setOutputFile(Environment.getExternalStorageDirectory()+"/sound" +"/"+fileName + ".mp3");
        try {
            recorder.prepare();
        } catch (Exception e){
            e.printStackTrace();
        }

        final ProgressDialog mProgressDialog = new ProgressDialog(CreateBooksActivity.this);
        mProgressDialog.setTitle("Create Audio Book");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setButton("Stop recording", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mProgressDialog.dismiss();
                recorder.stop();
                recorder.release();
            }
        });

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
            public void onCancel(DialogInterface p1) {
                recorder.stop();
                recorder.release();
            }
        });
        if (recorder!=null) {
            recorder.start();
        }
        mProgressDialog.show();
    }
    void savefile(Uri sourceuri)
    {
        String sourceFilename= sourceuri.getPath();
        String destinationFilename = Environment.getExternalStorageDirectory().getPath()+File.separatorChar+"RMrecorded.mp3";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   /* String maincontent=" ";
                    for (int i=0;i<text.size();i++)
                    {
                        maincontent=maincontent+text.get(i);
                    }*/
                    book_detail.setText(text.get(0));
                   // Toast.makeText(CreateBooksActivity.this,"data is"+text.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("System out","Data get is"+text.get(0)+"text data is:"+text.toString());
                   // txtText.setText(text.get(0));
                    // the recording url is in getData:
                    Bundle bundle = data.getExtras();
                    Uri audioUri = data.getData();
                    Log.d("audioUriiii",""+audioUri+"    "+bundle);
                   // combinedPath=Environment.getExternalStorageDirectory().getAbsolutePath() + "/STT.mp3";
                    String finalPath = outputFile.replaceAll(" ","");
                    Log.d("Filepath",outputFile.replaceAll(" ",""));
                    ContentResolver contentResolver = getContentResolver();
                    try {
                        InputStream filestream = contentResolver.openInputStream(audioUri);
                        File targetFIle = new File(finalPath);
                        FileUtils.copyInputStreamToFile(filestream,targetFIle);
                        selectedAudioPath=finalPath;
                        isAudioSelected=true;
                        savefile(audioUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   /* try {
//                      /  myExternalFile = new File(getExternalFilesDir(filepath), "recorded");

                        myExternalFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"rec");
                        fos = new FileOutputStream(myExternalFile);
                        osw = new OutputStreamWriter(fos);
                    } catch (Exception e) {

                    }
                    try {
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
                break;
            }
            case 4:
            {
            // https://www.tutorialspoint.com/android/android_audio_capture.htm
//                http://responsivevoice.org/free-text-to-speech-mp3-audio-files/




                //http://stackoverflow.com/questions/11116051/how-can-i-record-voice-in-android-as-long-as-hold-a-button
                //try must http://stackoverflow.com/questions/11116051/how-can-i-record-voice-in-android-as-long-as-hold-a-button
                // audio read web https://github.com/westonruter/html5-audio-read-along
                //play audio http://www.compiletimeerror.com/2013/10/playing-audio-in-android-application.html#.Vxfj4Pl97IU
                if (requestCode == 4 && resultCode == Activity.RESULT_OK){
                    if ((data != null) && (data.getData() != null)){

                        Toast.makeText(CreateBooksActivity.this, "audiooooo", Toast.LENGTH_SHORT).show();
                        audioFileUri = data.getData();

                        Uri selectedAudioUri = data.getData();


                        String[] filePathColumn={MediaStore.Audio.Media.DATA};
                        Cursor cursor=getContentResolver().query(selectedAudioUri,filePathColumn,null,null,null);
                        assert cursor!=null;
                        cursor.moveToFirst();
                        int clumnIndex=cursor.getColumnIndex(filePathColumn[0]);
                        selectedAudioPath=cursor.getString(clumnIndex);
                        File file = new File(selectedAudioPath);
                        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                        Log.d("file_sizeee",""+selectedAudioPath);
                        //Log.d("file_sizeeee", "mediapath : " +getRealPathFromURI(selectedAudioUri)+"   "+ audioFileUri + " ---- ");

/*

                        String[] dataa = {MediaStore.Audio.Media.DATA};
                        CursorLoader loader = new CursorLoader(getApplicationContext(), selectedAudioUri, dataa, null, null, null);
                        Cursor cursor = loader.loadInBackground();
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        cursor.moveToFirst();
                        selectedAudioPath= cursor.getString(column_index);
                        Log.d("file_size", "mediapath : " +getAudioPath(audioFileUri)+"     "+ selectedAudioPath + " ---- ");
*/



                        //selectedAudioPath = getPath(selectedAudioUri);
                        Log.d("System out", "SELECT_AUDIO Path : " + selectedAudioPath);

                        /*String[] split = selectedAudioPath.split("/");
                        audiotitle = split[split.length - 1];
*/
                        Log.d("System out", "audio title is" + audiotitle);

                        //getExtension(audiotitle);
                        isAudioSelected=true;
                        Log.d("System out","audioFileUri is"+audioFileUri);
                        // Now you can use that Uri to get the file path, or upload it, ...
                    }
                }

            }

        }
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();return cursor.getString(column_index);
    }

    public static String getPathForAudio(Context context, Uri uri)
    {
        String result = null;
        Cursor cursor = null;

        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor == null) {
                result = uri.getPath();
            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                result = cursor.getString(column_index);
                cursor.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    private void createBookWithAudio() {


        Constants.dialog = new Dialog(CreateBooksActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        progressbar = new ProgressBar(CreateBooksActivity.this);
        progressbar.setBackgroundResource(R.drawable.ajax_loader);
        Constants.dialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = Constants.dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        if (Constants.dialog!=null) {
            Constants.dialog.show();
        }


        if ((selectedAudioPath != null) && (!(selectedAudioPath.isEmpty()))) {

            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", preference.getString("id", ""));
           /* map.put("name", audiotitle);
            map.put("type", "booktype2");
            map.put("content", book_detail.getText().toString().trim());
            map.put("moral", "");
            map.put("price", "free");
            map.put("created_by", "2");*/
            callAPiActivity.doPostWithFiles(CreateBooksActivity.this, map, URLUPDATEUSER, selectedAudioPath, "bookaudio", new GetResponse() {

                @Override
                public void onSuccessResult(JSONObject result) throws JSONException {
                    Constants.dialog.dismiss();
                    String status = result.getString("sStatus");
                    Log.d("responseeee",result.toString()+"");
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    if(status.equalsIgnoreCase("1")) {
                        jsonObject = result.getJSONObject("sData");

                        String book_id=jsonObject.getString("book_id");

                        Toast.makeText(CreateBooksActivity.this, ""+result.getString("sMessage"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                @Override
                public void onFailureResult(String message) {
                    Constants.dialog.dismiss();
                    Toast.makeText(CreateBooksActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    Log.d("messageeeeeeeeeee",message);

                }
            });
        }


    }

    private void uploadFile(String filePath, String fileName) {

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(filePath));
            byte[] data;
            try {
                // data = IOUtils.toByteArray(inputStream);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//http://stackoverflow.com/questions/34930167/upload-dynamic-number-of-files-with-okhttp3
                int nRead;
                data = new byte[163984];

                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                ProgressBar progressbar;
                if (Constants.dialog!=null) {
                    Constants.dialog.dismiss();
                    Constants.dialog=null;

                }
                Constants.dialog = new Dialog(CreateBooksActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
                progressbar = new ProgressBar(CreateBooksActivity.this);
                progressbar.setBackgroundResource(com.gayatri.testapp.R.drawable.ajax_loader);
                Constants.dialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Window window = Constants.dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                if (Constants.dialog!=null) {
                    Constants.dialog.show();
                }

                buffer.flush();

                File file=new File(selectedAudioPath);


//                final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/*");
                final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/mpeg3");
//                final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/x-mpeg-3");

                String webdata = "{\"" + "user_id" + "\":" + "\""+ preference.getString("id","") + "\"" + ",\""
                        + "name"+ "\":" + "\"" + audiotitle + "\"" +",\""
                        +"type"+"\":"+"\""+"booktype2"+"\""+",\""
                        + "content" + "\":" + "\""+ book_detail.getText().toString().trim() + "\"" + ",\""
                        + "moral"+ "\":" + "\"" +"" + "\"" + ",\""
                        + "price" + "\":" + "\"" +"free" + "\"" + ",\""
                        + "created_by" + "\":" + "\""+ "2" + "\"" +
                        "}";


                Log.d("System out","webdata"+webdata.toString());
                MultipartBody.Builder buildernew = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("webdata",webdata)
                        .addFormDataPart("bookaudio[]", audiotitle, RequestBody.create(MEDIA_TYPE_AUDIO, file));

                MultipartBody requestBody = buildernew.build();

                Request request = new Request.Builder()
                        .url(Constants.SERVER_URL+"create_audio_book")
                        .post(requestBody)
                        .build();

                OkHttpClient client;// = new OkHttpClient();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .cache(null)
                        .followRedirects(true)
                        .followSslRedirects(true)
                        .retryOnConnectionFailure(true)
                        .build();

                client = builder.build();
                /*
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(30, TimeUnit.MINUTES)
                        .writeTimeout(30, TimeUnit.MINUTES)
                        .readTimeout(30, TimeUnit.MINUTES);*/

               client = new OkHttpClient();
//                OkHttpClient client = builder.build();


                Response response = client.newCall(request).execute();

                if(response != null) {

                    JSONObject json = null;
                    Log.d("System out","response"+response.body().string());

                    try {
//                        json = new JSONObject(response.toString());
//                        String message=json.getString("message");
                        if (response.message().equalsIgnoreCase("OK"))
                        {
                            if (Constants.dialog!=null) {
                                Constants.dialog.dismiss();
                                Constants.dialog=null;
                            }

                            this.finish();
                        }
                        else
                        {
                            Snackbar.make(Clayout, ""+json.getString("message"), Snackbar.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (!TextUtils.isEmpty(response.toString()))
                        {
                            finish();
                        }
                    }



                    //savetextbook();
                } else { // Error, no response.

                }


                // Handle response back from script.

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }


    public static String getExtension(String fileName) {

        try {
            encoded = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            encoded = fileName;
        }
        return MimeTypeMap.getFileExtensionFromUrl(encoded).toLowerCase();

    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    public String getPath(Uri uri) {


        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat
                && DocumentsContract.isDocumentUri(
                CreateBooksActivity.this, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
			/*
			 * else if (isDownloadsDocument(uri)) {
			 *
			 * final String id = DocumentsContract.getDocumentId(uri); final Uri
			 * contentUri = ContentUris.withAppendedId(
			 * Uri.parse("content://<span id="
			 * IL_AD2" class="IL_AD">downloads</span>/public_downloads"),
			 * Long.valueOf(id));
			 *
			 * return getDataColumn(AddTestResultActivity.this, contentUri,
			 * null, null); }
			 */
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(CreateBooksActivity.this,
                        contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(CreateBooksActivity.this, uri, null,
                    null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /*public String getAudioPath(Uri uri) {


        String fileName = getFileName(uri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(TEMP_DIR_PATH + File.separator + fileName);
            copy(this, uri, copyFile);
            return copyFile.getAbsolutePath();
        }

        if (uri == null) {
            return null;
        }
        String[] projection = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        Log.d("System out", "get audio url is:" + uri.getPath());
        return uri.getPath();

    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }*/

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
