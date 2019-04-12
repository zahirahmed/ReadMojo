package com.gayatri.testapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.SlideHolder;
import com.gayatri.testapp.uploadMultupleImage.CallAPiActivity;
import com.gayatri.testapp.uploadMultupleImage.GetResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Gayatri on 07-09-2015.
 */
public class CreateImageBook extends Activity implements View.OnClickListener {
    private SlideHolder slider;
    private ImageView iv_menu,next_btn;
    private RecyclerView iv_grid;
    SharedPreferences.Editor editor;
    SharedPreferences preference;
    private LinearLayout takePicture_ll;
    Bitmap bitmap;

    //images

    private static int TAKE_PICTURE = 1, SELECT_PICTURE = 0;
    private long timeForImgname;
    private String selectedImagePath = "", path, imgName, imgStore;
    private File f;
    private Bitmap bm;
    private String Imagenameselected="";
    private String NewimgName="";
    private String title="";
    ArrayList<HashMap<String,Object>> allImagelist = new ArrayList<>();
    ArrayList<String> imagelist = new ArrayList<>();
    private viewImageBookAdapter viewImageBookAdapter;
    private Button bt_create_image_book;
    private AutoCompleteTextView book_data;
    private TextView tv_attach_image;
    CoordinatorLayout Clayout;
    CallAPiActivity callAPiActivity;
    public static String URLUPDATEUSER;
    private ProgressBar progressbar;
    private String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagebookscreen);
        //  get permissions
        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        callAPiActivity = new CallAPiActivity(CreateImageBook.this);
        URLUPDATEUSER = "http://dev.readmojo.com/readmojo_ws2/index.php/services/user_details/create_book";

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
        init();

        //viewImageBookAdapterr.notifyDataSetChanged();
    }

    private void init() {
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
        next_btn=(ImageView)findViewById(R.id.next_btn);
        bt_create_image_book=(Button)findViewById(R.id.bt_create_image_book);
        book_data=(AutoCompleteTextView)findViewById(R.id.book_data);
        tv_attach_image=(TextView)findViewById(R.id.tv_attach_image);
        iv_grid=findViewById(R.id.iv_grid);

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2);
        iv_grid.setLayoutManager(layoutManager);
        takePicture_ll=(LinearLayout)findViewById(R.id.takePicture_ll);
        Clayout = (CoordinatorLayout)findViewById(R.id.snackbarImageBook);

        next_btn.setOnClickListener(this);
        bt_create_image_book.setOnClickListener(this);
        takePicture_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }
        else if (v==takePicture_ll) {
            selectImage();
        } else if (v==bt_create_image_book) {
            if (allImagelist.size()<=0) {
                Snackbar.make(Clayout, "Please select image to create book.", Snackbar.LENGTH_SHORT).show();


            }
            else if (TextUtils.isEmpty(book_data.getText().toString())) {
                Snackbar.make(Clayout, "Enter Book Detail.", Snackbar.LENGTH_SHORT).show();


            }
            else
            {
                //uploadFile();
                createImageBook();
            }
        }

    }

    private void createImageBook() {

        if ((allImagelist.size() > 0)) {
            Constants.dialog = new Dialog(CreateImageBook.this, android.R.style.Theme_Translucent_NoTitleBar);
            progressbar = new ProgressBar(CreateImageBook.this);
            progressbar.setBackgroundResource(R.drawable.ajax_loader);
            Constants.dialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = Constants.dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            if (Constants.dialog != null) {
                Constants.dialog.show();
            }

            if (Constants.dialog!=null) {
                Constants.dialog.show();
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", preference.getString("id", ""));
            map.put("name", "");
            map.put("type", "booktype2");
            map.put("content", book_data.getText().toString().trim());
            map.put("moral", "");
            map.put("price", "free");
            map.put("created_by", "2");
            callAPiActivity.doPostWithFiles(CreateImageBook.this, map, URLUPDATEUSER, imagelist, "bookimage[]", new GetResponse() {

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

                        Toast.makeText(CreateImageBook.this, ""+result.getString("sMessage"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                @Override
                public void onFailureResult(String message) {
                    Constants.dialog.dismiss();
                    Toast.makeText(CreateImageBook.this, " "+message, Toast.LENGTH_SHORT).show();
                    Log.d("messageeeeeeeeeee",message);

                }
            });
        }


    }

    /*private void uploadFile() {

        InputStream inputStream;
        byte[] data;
        try {
            // data = IOUtils.toByteArray(inputStream);
            //http://stackoverflow.com/questions/34930167/upload-dynamic-number-of-files-with-okhttp3

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

            String webdata = "{\"" + "user_id" + "\":" + "\""+ preference.getString("id","") + "\"" + ",\""
                    + "name"+ "\":" + "\"" + "" + "\"" +",\""
                    +"type"+"\":"+"\""+"booktype2"+"\""+",\""
                    + "content" + "\":" + "\""+ book_data.getText().toString().trim()+ "\"" + ",\""
                    + "moral"+ "\":" + "\"" +""+ "\"" + ",\""
                    + "price" + "\":" + "\"" +"free" + "\"" + ",\""
                    + "created_by" + "\":" + "\""+ "2" + "\"" +
                    "}";

            MultipartBody.Builder buildernew = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("webdata",webdata);
            String allImagesName="";
            for (int i=0;i<allImagelist.size();i++)
            {
                inputStream = new FileInputStream(new File(allImagelist.get(i).get("selectedImagePath").toString()));
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                data = new byte[16384];

                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();

                File file=new File(allImagelist.get(i).get("selectedImagePath").toString());
//                RequestBody requestfiles=RequestBody.create(MediaType.parse("multipart/form-data"),file);
//                MultipartBody.Part partFiles=MultipartBody.Part.createFormData("multipart/form-data",allImagelist.get(0).get("title").toString(),requestfiles);
//
//                buildernew.addFormDataPart("bookimage[]", partFiles);

                buildernew.addFormDataPart("bookimage[]", allImagelist.get(0).get("title").toString(), RequestBody.create(MEDIA_TYPE_PNG, file));

            }

            Log.d("System out","webdata"+webdata.toString());


            MultipartBody requestBody = buildernew.build();

            Request request = new Request.Builder()
                    .url(Constants.SERVER_URL+"create_book")
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if(response != null) {

                JSONObject json = null;
                try {
                    json = new JSONObject(response.toString());
                    String message=json.getString("message");
                    if (message.equalsIgnoreCase("OK") || response.message().equalsIgnoreCase("OK"))
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
            } else { // Error, no response.

            }


            // Handle response back from script.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/
    private void selectImage() {


        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateImageBook.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    dialog.dismiss();

                    dispatchTakePictureIntent(TAKE_PICTURE);

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    dialog.dismiss();

                    Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), SELECT_PICTURE);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    private void dispatchTakePictureIntent(int actionCode) {
        timeForImgname = System.currentTimeMillis();
        imgName = "img_" + timeForImgname + ".jpg";
        imgStore = imgName;
        f = new File(android.os.Environment.getExternalStorageDirectory(), imgName);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(cameraIntent, 1);
    }
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Imagenameselected = "";
        super.onActivityResult(requestCode, resultCode, data);
        tv_attach_image.setVisibility(View.VISIBLE);
        if (resultCode == RESULT_OK) {
            Log.d("System out", "result code" + resultCode + "    " + requestCode);
            if (requestCode == TAKE_PICTURE) {

                File file = new File(String.valueOf(data));
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                Log.d("file_size",""+file_size);
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getRealPathFromURI(selectedImageUri);

                Log.d("System out", "imgname" + selectedImagePath);
                String[] split = selectedImagePath.split("/");
                title = split[split.length - 1];
                timeForImgname = System.currentTimeMillis();
                NewimgName = "picimg_" + timeForImgname + ".jpg";
                title = NewimgName;
                Uri selectedImage = data.getData();
                getContentResolver().notifyChange(selectedImage, null);
                ContentResolver cr = getContentResolver();
                try {
                    bm = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("title",title);
                    map.put("selectedImagePath",selectedImagePath);
                    map.put("bm",bm);
                    allImagelist.add(map);
                    imagelist.add(selectedImagePath);
                    viewImageBookAdapter = new viewImageBookAdapter(allImagelist,this);
                    iv_grid.setAdapter(viewImageBookAdapter);
                    tv_attach_image.setVisibility(View.GONE);
                    viewImageBookAdapter.notifyDataSetChanged();
                    if (bm != null) {
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                "picimg_"+System.currentTimeMillis() + ".jpg");

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

        //ivImage.setImageBitmap(thumbnail);
        Uri tempUri = getImageUri(getApplicationContext(), bitmap);


        selectedImagePath=getRealPathFromURI(tempUri);
        String[] split = selectedImagePath.split("/");
        title = split[split.length - 1];
        timeForImgname = System.currentTimeMillis();
        NewimgName = "picimg_" + timeForImgname + ".jpg";
        title = NewimgName;
        Log.d("selectedImagePath","gottt this"+selectedImagePath);
        HashMap<String,Object> map=new HashMap<>();
        map.put("title",title);
        map.put("selectedImagePath",selectedImagePath);
        map.put("bm",bitmap);
        allImagelist.add(map);
        imagelist.add(selectedImagePath);
        viewImageBookAdapter = new viewImageBookAdapter(allImagelist,this);
        iv_grid.setAdapter(viewImageBookAdapter);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
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

    public class viewImageBookAdapter extends RecyclerView.Adapter<viewImageBookAdapter.MyViewHolder>
    {
        ArrayList<HashMap<String,Object>> arrayList;
        Context context;

        public viewImageBookAdapter(ArrayList<HashMap<String,Object>> arrayList, Context context)
        {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(context).inflate(R.layout.item_books_view_cell,parent,false);

            MyViewHolder myViewHolder=new MyViewHolder(v);

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Log.d("imageeeesssss",position+"    "+allImagelist.size()+"    ");
            holder.iv_viewimage.setImageBitmap((Bitmap) allImagelist.get(position).get("bm"));
            //holder.iv_viewimage.setImageBitmap(setPic(holder.iv_viewimage,allImagelist.get(position).get("selectedImagePath").toString()));
            holder.close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allImagelist.remove(position);
                    notifyItemRemoved(position);
                    viewImageBookAdapter.notifyDataSetChanged();

                }
            });

        }

        @Override
        public int getItemCount() {
            return allImagelist.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {

            final ImageView iv_viewimage;
            final ImageView close_iv;
            public MyViewHolder(View itemView) {
                super(itemView);
                iv_viewimage = (ImageView) itemView.findViewById(R.id.iv_viewimage);
                close_iv = (ImageView) itemView.findViewById(R.id.close_iv);

            }
        }
    }

    private Bitmap setPic(ImageView imageView,String mPhotoPath) {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
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
        imageView.setImageBitmap(rotatedBitmap);


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




}
