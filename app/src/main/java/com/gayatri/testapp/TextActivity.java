package com.gayatri.testapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.SlideHolder;
import com.gayatri.testapp.Utils.post_async;
import com.gayatri.testapp.uploadMultupleImage.CallAPiActivity;
import com.gayatri.testapp.uploadMultupleImage.GetResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Gayatri on 10-08-2015.
 */
public class TextActivity extends Activity implements View.OnClickListener {
    private ImageView iv_viewimage,iv_menu;
    private SlideHolder slider;
    CoordinatorLayout Clayout;
    private Button bt_createbook;
    private ImageView iv_capture;
    private AutoCompleteTextView book_title,book_detail,book_moral;
    SharedPreferences.Editor editor;
    SharedPreferences preference;
    private static int TAKE_PICTURE = 1, SELECT_PICTURE = 0;
    private long timeForImgname;
    private String selectedImagePath = "", path, imgName, imgStore;
    private File f;
    private Bitmap bm;
    private String Imagenameselected="";
    private String NewimgName="";
    private String title="";
    ArrayList<String> titlelist = new ArrayList<String>();
    private boolean photo=false;
    private Dialog dialog=null;
    private ProgressBar progressbar;
    String mediapath = "";
    ArrayList<String> imagelist=new ArrayList<>();
    CallAPiActivity callAPiActivity;
    String picture;
    public static String URLUPDATEUSER;
    File file;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createbooksscreen);
        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        URLUPDATEUSER = "http://dev.readmojo.com/readmojo_ws2/index.php/services/user_details/create_book";
        callAPiActivity = new CallAPiActivity(TextActivity.this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("System out", "permission check " + permissionCheck);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);


        }

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("System out", "permission check " + permissionCheck1);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);


        }


        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        Log.d("System out", "permission check " + permissionCheck2);
        if (permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1234);


        }
        init();

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
                int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                Log.d("System out", "permission check " + permissionCheck2);
                if (permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1234);


                }
                   return;
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( Constants.dialog!=null && Constants.dialog.isShowing() ){
            Constants.dialog.cancel();
        }
    }
    private void init() {
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbartextbook);
        iv_capture = (ImageView) findViewById(R.id.iv_capture);
        bt_createbook=(Button)findViewById(R.id.bt_createbook);
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        book_title = (AutoCompleteTextView) findViewById(R.id.book_title);
        book_detail = (AutoCompleteTextView) findViewById(R.id.book_detail);
        book_moral = (AutoCompleteTextView) findViewById(R.id.book_moral);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_viewimage = (ImageView) findViewById(R.id.iv_viewimage);
        iv_menu.setOnClickListener(this);
        bt_createbook.setOnClickListener(this);
        iv_capture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }
        else if (v==bt_createbook)
        {
            if (TextUtils.isEmpty(book_title.getText().toString())) {
                Snackbar.make(Clayout, "Enter Book Title.", Snackbar.LENGTH_SHORT).show();


            }
            else if (TextUtils.isEmpty(book_detail.getText().toString())) {
                Snackbar.make(Clayout, "Enter Book Detail.", Snackbar.LENGTH_SHORT).show();


            } else if (TextUtils.isEmpty(book_moral.getText().toString())) {
                Snackbar.make(Clayout, "Enter Book Moral.", Snackbar.LENGTH_SHORT).show();
            }
            else
            {

                //uploadFile(selectedImagePath,title);
                createBookWithImage();
                //savetextbook();
//                new UploadimageTask().execute();

            }
        }
        else if (v==iv_capture)
        {


            selectImage();

        }
    }

    class UploadimageTask extends AsyncTask<String, String, String> {
        private Exception exception;
        protected void onPreExecute() {
            dialog = new Dialog(TextActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            progressbar = new ProgressBar(TextActivity.this);
            progressbar.setBackgroundResource(R.drawable.progress_background);
            dialog.addContentView(progressbar, new ViewGroup.LayoutParams(40, 40));
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            dialog.show();

        }
        protected String doInBackground(String... urls) {
            Log.d("System out", "execute");
            try {

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    Log.i("System out", "selectedImagePath : " + selectedImagePath);
                    String split123[] = selectedImagePath.split("\\.");
                    if (split123[1].equals("jpg") || split123[1].equals("jpeg")) {
                        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    } else {
                        bm.compress(Bitmap.CompressFormat.PNG, 75, bos);
                    }
                   byte[] data = bos.toByteArray();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(Constants.SERVER_URL
                        +"create_book");
//                ByteArrayBody bab = new ByteArrayBody(data, title);
//                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//               // reqEntity.addPart("action", new StringBody("imageUpload"));
//               // reqEntity.addPart("File upload", new StringBody("bookimage"));
//                Log.d("System out","bab"+bab.toString());
//                // reqEntity.addPart("action", new StringBody("imageUpload"));
//
//                //reqEntity.addPart("action", new StringBody("imageUpload"));
//                File file=new File(selectedImagePath);
//                reqEntity.addPart("bookimage",new FileBody(file));
//                reqEntity.addPart("bookimage", inputStreamBody);
                /*reqEntity.addPart("type", new StringBody("booktype2"));
                reqEntity.addPart("user_id", new StringBody( preference.getString("id","")));
                reqEntity.addPart("name", new StringBody(book_title.getText().toString().trim()));
                reqEntity.addPart("content", new StringBody(book_detail.getText().toString().trim()));
                reqEntity.addPart("moral", new StringBody(book_moral.getText().toString().trim()));
                reqEntity.addPart("price", new StringBody("free"));
                reqEntity.addPart("created_by", new StringBody("2"));*/

//                Log.d("System out","bab"+bab.toString());
//                postRequest.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    Log.i("System out", "under reader...1" + sResponse.toString());
                    s = s.append(sResponse);
                }
                String ResponseString = s.toString();
                System.out.println("Response: " + s);

                try {

                    JSONObject json = new JSONObject(ResponseString);

                    String sStatus=json.getString("sStatus"); //<< get value here


                    Log.d("System out","sStatus"+sStatus);
                    if (sStatus.equalsIgnoreCase("1"))
                    {
                        Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();


//                        savetextbook();




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



            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    private void createBookWithImage() {

        Toast.makeText(this, "called createBookWithImage  "+mediapath, Toast.LENGTH_SHORT).show();

        Constants.dialog = new Dialog(TextActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        progressbar = new ProgressBar(TextActivity.this);
        progressbar.setBackgroundResource(R.drawable.ajax_loader);
        Constants.dialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = Constants.dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        if (Constants.dialog!=null) {
            Constants.dialog.show();
        }


        if ((mediapath != null) && (!(mediapath.isEmpty()))) {

            Toast.makeText(this, "mediapath != null && !(mediapath.isEmpty())", Toast.LENGTH_SHORT).show();
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", preference.getString("id", ""));
            map.put("name", book_title.getText().toString().trim());
            map.put("type", "booktype2");
            map.put("content", book_detail.getText().toString().trim());
            map.put("moral", book_moral.getText().toString().trim());
            map.put("price", "free");
            map.put("created_by", "2");
            callAPiActivity.doPostWithFiles(TextActivity.this, map, URLUPDATEUSER, imagelist, "bookimage[]", new GetResponse() {

                @Override
                public void onSuccesResult(JSONObject result) throws JSONException {
                    Constants.dialog.dismiss();
                    String status = result.getString("sStatus");
                    Log.d("responseeee",result.toString()+"");
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    if(status.equalsIgnoreCase("1")) {
                        jsonObject = result.getJSONObject("sData");
                        jsonArray = jsonObject.getJSONArray("book_images");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            picture = jsonObject1.getString("book_name");

                        }
                        Log.d("result",""+picture);

                        editor.putString("book_image", picture);
                        editor.commit();
                        Glide
                                .with(TextActivity.this)
                                .load(picture)
                                .asBitmap().centerCrop().into(iv_viewimage);
                        Toast.makeText(TextActivity.this, ""+result.getString("sMessage"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                @Override
                public void onFailureResult(String message) {
                    Constants.dialog.dismiss();
                    Toast.makeText(TextActivity.this, " "+message, Toast.LENGTH_SHORT).show();
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
                 data = new byte[16384];

                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();

                File file=new File(selectedImagePath);


                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

                String webdata = "{\"" + "user_id" + "\":" + "\""+ "22" + "\"" + ",\""
                        + "name"+ "\":" + "\"" + book_title.getText().toString().trim() + "\"" +",\""
                        +"type"+"\":"+"\""+"booktype2"+"\""+",\""
                        + "content" + "\":" + "\""+ book_detail.getText().toString().trim() + "\"" + ",\""
                        + "moral"+ "\":" + "\"" +book_moral.getText().toString().trim() + "\"" + ",\""
                        + "price" + "\":" + "\"" +"free" + "\"" + ",\""
                        + "created_by" + "\":" + "\""+ "2" + "\"" +
                        "}";


                Log.d("System out","webdata"+webdata.toString());
                MultipartBody.Builder buildernew = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("webdata",webdata)
                        .addFormDataPart("bookimage[]", imgName, RequestBody.create(MEDIA_TYPE_PNG, file));

                MultipartBody requestBody = buildernew.build();

                Request request = new Request.Builder()
                        .url(Constants.SERVER_URL+"create_book")
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                if(response != null) {
                    Log.d("System out",""+response.body().string());

                    ResponseBody responseBody=response.body();
String dataresponse=""+responseBody.string();
                    try {
                        JSONObject json  = new JSONObject(dataresponse);
                        String message=json.getString("sMessage");

                        if (message.equalsIgnoreCase("Book Created sucessfully"))
                        {
                            //Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();

                            this.finish();
                        }
                        else
                        {
                            Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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



    /* RequestBody requestBody = new MultipartBuilder()
             .type(MultipartBuilder.FORM)
             .addFormDataPart("user_id", preference.getString("id",""))
             .addFormDataPart("name", book_title.getText().toString().trim())
             .addFormDataPart("type", "booktype2")
             .addFormDataPart("price", "free")
             .addFormDataPart("created_by", "2")
             .addFormDataPart("content",book_detail.getText().toString().trim())
             .addFormDataPart("moral",book_moral.getText().toString().trim())
             .addFormDataPart("file", imgName, RequestBody.create(MEDIA_TYPE_PNG, file))
             .build();*/

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_PICTURE);
    }


    private void selectImage() {


        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TextActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                    cameraIntent();
                    dialog.dismiss();
/*
                    dispatchTakePictureIntent(TAKE_PICTURE);
                  *//*  timeForImgname = System.currentTimeMillis();
                    f = new File(android.os.Environment.getExternalStorageDirectory(), "img_" + timeForImgname + ".jpg");
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(cameraIntent, 1);*/
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    galleryIntent();
                    /*
                   *//* Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
*//*
                    dialog.dismiss();

                    Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        *//*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*//*
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), SELECT_PICTURE);*/
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

/*

    private void dispatchTakePictureIntent(int actionCode) {
        timeForImgname = System.currentTimeMillis();
        imgName = "img_" + timeForImgname + ".jpg";
        imgStore = imgName;
        f = new File(android.os.Environment.getExternalStorageDirectory(), imgName);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".my.package.name.provider", f));
        startActivityForResult(cameraIntent, 1);
    }
*/


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    iv_viewimage.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";



                    f.delete();
                    OutputStream outFile = null;
                    selectedpath=path;
                    title=String.valueOf(System.currentTimeMillis()) + ".jpg";
                    File file = new File(path, title);
                    try {
                        outFile = openFileInput(file);
                       // outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                selectedpath=picturePath;
                title=String.valueOf(System.currentTimeMillis()) + ".jpg";
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("System out", picturePath+"");
                iv_viewimage.setImageBitmap(thumbnail);
            }
        }

}*/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), thumbnail, "Title", null);
        Uri uri=Uri.parse(path);

        mediapath=getRealPathFromURI(uri);

        imagelist.add(mediapath);


        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        iv_viewimage.setImageBitmap(thumbnail);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            iv_viewimage.setImageBitmap(bm);

    }
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Imagenameselected = "";
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("System out", "result code" + resultCode + "    " + requestCode);
            if (requestCode == TAKE_PICTURE) {
                onCaptureImageResult(data);


                /*Uri pictureUri = Uri.fromFile(f);
                selectedImagePath = getPath1(pictureUri);
                Log.d("System out", "imgname" + selectedImagePath);
                timeForImgname = System.currentTimeMillis();
                NewimgName = "img_" + timeForImgname + ".jpg";
                title = NewimgName;
                titlelist.add(title);

                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureUri);
                    Log.d("System out", "after bm data" + bm);
                    BitmapDrawable ob = new BitmapDrawable(bm);
                    //setPic(selectedImagePath);

                    Glide.with(this)
                            .load(selectedImagePath)
                            .asBitmap()
                            .into(iv_viewimage);
                    photo = true;
                    if (bm != null) {
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(TextActivity.this, "Photo captured", Toast.LENGTH_LONG);
                try {

                } catch (Exception e) {
                }*/
            }
            else if (requestCode == SELECT_PICTURE) {/*
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath1(selectedImageUri);
                Log.d("System out", "imgname" + selectedImagePath);
                String[] split = selectedImagePath.split("/");
                title = split[split.length - 1];
                titlelist.add(title);
                timeForImgname = System.currentTimeMillis();
                NewimgName = "picimg_" + timeForImgname + ".jpg";
                title = NewimgName;
                Uri selectedImage = data.getData();
                getContentResolver().notifyChange(selectedImage, null);
                ContentResolver cr = getContentResolver();
                try {
                    bm = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                    photo = true;
                    setPic(selectedImagePath);
                    photo = true;
                    if (bm != null) {
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                }*/
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int clumnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediapath = cursor.getString(clumnIndex);
                file = new File(mediapath);
                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                Log.d("file_size", "mediapath : " + mediapath + " ---- " + file_size);
                imagelist.add(mediapath);
                //setPic(mediapath);
                Glide.with(this)
                        .load(mediapath)
                        .asBitmap()
                        .into(iv_viewimage);

            }
        }

    }
    private Bitmap setPic(String mPhotoPath) {
        int targetW = iv_viewimage.getWidth();
        int targetH = iv_viewimage.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        final Matrix matrix = new Matrix();
        Log.d("System out", "Orientaion :  " + getBitmapRotation(mPhotoPath));
        if(getBitmapRotation(mPhotoPath) == 90){
            Log.d("System out", "90dre ");
            matrix.setRotate(90);
        }else if(getBitmapRotation(mPhotoPath) == 180){
            Log.d("System out", "180dre ");
            matrix.setRotate(180);
        }else if(getBitmapRotation(mPhotoPath) == 270){
            matrix.setRotate(270);
        }else if(getBitmapRotation(mPhotoPath) == 360){
            matrix.setRotate(360);
        }else if(getBitmapRotation(mPhotoPath) == 0){
            matrix.setRotate(0);
        }
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, bmOptions);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
          iv_viewimage.setImageBitmap(rotatedBitmap);


        return rotatedBitmap;
    }
    private int getExifOrientation(String mImagePath) {
        ExifInterface exif;
        int orientation = 0;
        try {
            exif = new ExifInterface(mImagePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("System out", "got orientation " + orientation);
        return orientation;
    }
    private int getBitmapRotation(String mImagePath) {
        int rotation = 0;
        switch (getExifOrientation(mImagePath)) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotation = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotation = 270;
                break;
        }
        return rotation;
    }


    public String getPath1(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }



    private void savetextbook() {

        if(IsNetworkConnection.checkNetworkConnection(TextActivity.this))
        {
            String webdata = "{\"" + "user_id" + "\":" + "\""+ preference.getString("id","") + "\"" + ",\""
                    + "name"+ "\":" + "\"" + book_title.getText().toString().trim() + "\"" +",\""
                    +"type"+"\":"+"\""+"booktype2"+"\""+",\""
                    + "content" + "\":" + "\""+ book_detail.getText().toString().trim() + "\"" + ",\""
                    + "moral"+ "\":" + "\"" +book_moral.getText().toString().trim() + "\"" + ",\""
                    + "price" + "\":" + "\"" +"free" + "\"" + ",\""
                    + "created_by" + "\":" + "\""+ "2" + "\"" +
                    "}";


            String url = Constants.SERVER_URL+"create_book" ;
            Log.d("System out", "url" + url);
            new post_async(TextActivity.this, "create_book").execute(url, webdata);

        }
        else {
            Toast.makeText(TextActivity.this, "No Network", Toast.LENGTH_SHORT).show();
        }

    }

    public void responseofcretebook(String resultString) {
        Log.d("System out","response of text book");

        try {

            JSONObject json = new JSONObject(resultString);

            String sStatus=json.getString("sStatus"); //<< get value here


            Log.d("System out","sStatus"+sStatus);
            if (sStatus.equalsIgnoreCase("1"))
            {
                Snackbar.make(Clayout, ""+json.getString("sMessage"), Snackbar.LENGTH_SHORT).show();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

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