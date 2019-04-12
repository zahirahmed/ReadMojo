package com.gayatri.testapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gayatri.testapp.Utils.CircularImagvieNew;
import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.IsNetworkConnection;
import com.gayatri.testapp.Utils.MyEditText;
import com.gayatri.testapp.Utils.MyTextView;
import com.gayatri.testapp.Utils.SlideHolder;
import com.gayatri.testapp.Utils.post_async;
import com.google.android.gms.drive.query.Filter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Gayatri on 10-08-2015.
 */
public class AllBooksListActivity extends AppCompatActivity implements
        View.OnClickListener {
    SlideHolder slider;
    ArrayList<HashMap<String, String>> allBooksList = new ArrayList<>();
    ArrayList<HashMap<String, String>> imageBooksList = new ArrayList<>();
    SharedPreferences.Editor editor;
    SharedPreferences preference;
    RecyclerView recyclerView;
    private CoordinatorLayout snackbar;
    private MyTextView no_books;
    RecyclerAdapterSeminar rv_books_adapter;
    private MyEditText et_search_txt;
    private String searchtext;

    private ArrayList<HashMap<String, String>> arrayactivitylist = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    DBAdapter dbAdapter;

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

        if (slider.isOpened()) {
            slider.toggle();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_layout);
        preference = getSharedPreferences(Constants.PREF, 0);
        editor = preference.edit();
        dbAdapter=new DBAdapter(AllBooksListActivity.this);
        init();
        GETALLBOOKS();

        et_search_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchtext = et_search_txt.getText().toString().trim();


                        InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                          if (rv_books_adapter!=null) {
                                rv_books_adapter.notifyDataSetChanged();
                                rv_books_adapter = new RecyclerAdapterSeminar(AllBooksListActivity.this,allBooksList);
                                recyclerView.setAdapter(rv_books_adapter);
                              rv_books_adapter.filter(allBooksList,searchtext);
                                rv_books_adapter.notifyDataSetChanged();
                            }
                         return true;
            }
        });

        et_search_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_search_txt.getText().toString())) {
                    searchtext = "";
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_search_txt, InputMethodManager.SHOW_IMPLICIT);
                    GETALLBOOKS();
                }
                else
                {
                    searchtext = et_search_txt.getText().toString().trim();

                    if (rv_books_adapter!=null) {
                        rv_books_adapter.notifyDataSetChanged();

                        rv_books_adapter = new RecyclerAdapterSeminar(AllBooksListActivity.this,allBooksList);
                        recyclerView.setAdapter(rv_books_adapter);
                        rv_books_adapter.filter(allBooksList,searchtext);
                        rv_books_adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void searchBooks() {
    }

    private void notifyAdapter(final ArrayList<HashMap<String, String>> datalist) {
        AllBooksListActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if (searchtext.length() > 1) {
                    searchBooks();
                } else {
                    InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    Collections.sort(allBooksList,
                            new Comparator<HashMap<String, String>>() {

                                public int compare(
                                        HashMap<String, String> lhs,
                                        HashMap<String, String> rhs) {
                                    return lhs.get("created_on")
                                            .compareTo(
                                                    rhs.get("created_on"));
                                }
                            });

                    rv_books_adapter = new RecyclerAdapterSeminar(AllBooksListActivity.this, allBooksList);
                    recyclerView.setAdapter(rv_books_adapter);
                    if (rv_books_adapter != null) {
                        rv_books_adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(rv_books_adapter);
                    }


                }
            }
        });
    }




    private void init() {
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        et_search_txt = (MyEditText) findViewById(R.id.et_search_txt);
        recyclerView = (RecyclerView) findViewById(R.id.rv_books_adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AllBooksListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        rv_books_adapter = new RecyclerAdapterSeminar(AllBooksListActivity.this,allBooksList);
        recyclerView.setAdapter(rv_books_adapter);
        snackbar = (CoordinatorLayout) findViewById(R.id.snackbar);
        no_books = (MyTextView) findViewById(R.id.no_books);
    }

    @Override
    public void onClick(View view) {

    }

    private void GETALLBOOKS() {

        if (IsNetworkConnection.checkNetworkConnection(AllBooksListActivity.this)) {
            String webdata = "{\"" + "user_id" + "\":" + "\"" + preference.getString("id", "") + "\"" +
                    "}";


            String url = Constants.SERVER_URL + "allcreated_bookdetail";
            Log.d("System out", "url" + url);
            new post_async(AllBooksListActivity.this, "allcreated_bookdetail").execute(url, webdata);

        } else {
            Toast.makeText(AllBooksListActivity.this, "No Network", Toast.LENGTH_SHORT).show();
        }
    }

    public void responseofallcreated_bookdetail(String resultString) {
        allBooksList.clear();
        imageBooksList.clear();
        Constants.executeLogcat(AllBooksListActivity.this);

        if (resultString.length() > 2) {
            try {

                JSONObject json = new JSONObject(resultString);
                String sStatus = json.getString("sStatus"); //<< get value here


                Log.d("System out", "sStatus" + sStatus);
                if (sStatus.equalsIgnoreCase("1")) {
                    JSONArray sArray = json.getJSONArray("sData");
                    for (int i=0;i<sArray.length();i++)
                    {
                        JSONObject sData=sArray.getJSONObject(i);
                        String book_id = sData.getString("book_id");
                        String user_id = sData.getString("user_id");
                        String name = sData.getString("name");
                        String type = sData.getString("type");
                        String book_image = sData.getString("book_image");
                        String content = sData.getString("content");
                        String audio_path = sData.getString("audio_path");
                        String moral = sData.getString("moral");
                        String price = sData.getString("price");
                        String created_by = sData.getString("created_by");
                        String created_on = sData.getString("created_on");
                        String updated_on = sData.getString("updated_on");
                        String updated_by = sData.getString("updated_by");
                        try
                        {
                            if (TextUtils.isEmpty(sData.getString("book_image")))
                            {

                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();

                            JSONArray ja = sData.getJSONArray("book_image");
                            for (int j = 0; j < ja.length(); j++) {
                                JSONObject jObj = ja.getJSONObject(j);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", jObj.getString("id"));
                                map.put("book_id", jObj.getString("book_id"));
                                map.put("seq_no", jObj.getString("seq_no"));
                                map.put("book_name", jObj.getString("book_name"));
                                map.put("created_on", jObj.getString("created_on"));
                                imageBooksList.add(map);
                                dbAdapter.open();
                                dbAdapter.insertImageBookdetail(jObj.getString("book_id"),"",jObj.getString("id"),jObj.getString("book_name"), jObj.getString("created_on"));
                                dbAdapter.close();
                            }
                        }


                        HashMap<String, String> map = new HashMap<>();
                        map.put("book_id", book_id);
                        map.put("user_id", user_id);
                        map.put("name", name);
                        map.put("type", type);
                        map.put("book_image", book_image);
                        map.put("content", content);
                        map.put("audio_path", audio_path);
                        map.put("moral", moral);
                        map.put("price", price);
                        map.put("created_by", created_by);
                        map.put("created_on", created_on);
                        map.put("updated_on", updated_on);
                        map.put("updated_by", updated_by);
                        dbAdapter.open();
                        dbAdapter.insertbookdetail(""+book_id,""+user_id,name,type,content,""+audio_path,moral,""+price,""+created_on);
                        dbAdapter.close();

                        allBooksList.add(map);
                    }

                } else {

                }

                if (allBooksList.size() > 0) {

                    Collections.sort(allBooksList, new Comparator<HashMap<String, String>>() {
                        @Override
                        public int compare(HashMap<String, String> lhs,
                                           HashMap<String, String> rhs) {
                            return lhs.get("created_on").compareTo(
                                    rhs.get("created_on"));
                        }
                    });

                    Collections.reverse(allBooksList);

                    no_books.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    rv_books_adapter = new RecyclerAdapterSeminar(AllBooksListActivity.this, allBooksList);
                    recyclerView.setAdapter(rv_books_adapter);
                    rv_books_adapter.notifyDataSetChanged();
                } else {
                    no_books.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

 public class RecyclerAdapterSeminar extends RecyclerView.Adapter<RecyclerAdapterSeminar.ViewHolder> {
        private Activity activity;
        public ViewHolder viewHolder;
        private ArrayList<HashMap<String, String>> mapgetis = new ArrayList<HashMap<String, String>>();
        private int lastPostion = -1;

        public RecyclerAdapterSeminar(Activity test, ArrayList<HashMap<String, String>> mapgetis) {

            this.activity = test;
            this.mapgetis = mapgetis;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.user_books_row, viewGroup, false);
            viewHolder = new ViewHolder(view);



            return viewHolder;

        }

     @Override
     public void onBindViewHolder(ViewHolder holder, final int position) {
         lastPostion = position;

         for (int i = 0; i < getItemCount(); i++) {
             animate(holder.cv_book_adapter, i);
         }
         for (int i1 = 0; i1 < imageBooksList.size(); i1++) {
             if (imageBooksList.get(i1).get("book_id").equalsIgnoreCase(mapgetis.get(position).get("book_id"))) {
                 Picasso.with(AllBooksListActivity.this).load(mapgetis.get(position).get("audio_path") + "&width=100&height=80").error(R.drawable.icon_books_graphite).placeholder(R.drawable.icon_books_graphite).into(holder.iv_seminar);
             }
         }
//            holder.userRatingBar.setRating(Float.parseFloat("" + mapgetis.get(position).get("Seminar_AvgRatting")));
         holder.tv_seminar_header.setText("Book Title :"+mapgetis.get(position).get("name"));
         holder.seminar_type.setText("Book Type :"+mapgetis.get(position).get("type"));
         holder.seminar_date.setText(getformatteddate(mapgetis.get(position).get("created_on")));
         holder.tv_seminar_location.setText("Book Moral :"+mapgetis.get(position).get("moral"));
         holder.tv_seminar_subheader.setText("Book Description :"+mapgetis.get(position).get("content"));

         viewHolder.cv_book_adapter.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent i = new Intent(AllBooksListActivity.this, BookDetailActivity.class);
                 i.putExtra("book_id", mapgetis.get(position).get("book_id"));
                 startActivity(i);
             }
         });



     }

     public ArrayList<HashMap<String,String>> filter(ArrayList<HashMap<String,String>> allBooksList, String charText) {
         charText = charText.toString().toLowerCase();
         final ArrayList<HashMap<String,String>> filteredModelList = new ArrayList<>();
         for (HashMap<String,String> model : allBooksList) {
             final String name = model.get("name").toString().trim().toLowerCase();
             final String content = model.get("content").toString().trim().toLowerCase();
             final String type = model.get("type").toString().trim().toLowerCase();
             final String created_on = getformatteddate(model.get("created_on")).toString().trim().toLowerCase();

             if ( charText.contains(name) || charText.contains(content) ||charText.contains(type) ||charText.contains(created_on)) {
                 filteredModelList.add(model);
             }

         }
         return filteredModelList;
     }
     private void animate(final View view, final int position) {
            if (view != null) {
                Animation animation = AnimationUtils.loadAnimation(AllBooksListActivity.this, R.anim.slideright);
                view.startAnimation(animation);
                lastPostion = position;
            }
        }

        @Override
        public int getItemCount() {
            return mapgetis.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View cv_book_adapter;
            private ImageView iv_seminar;
            private CircularImagvieNew civ_seminar_speaker;
            private ImageView event_icon;
            private LinearLayout speaker_name_ll;
            private MyTextView tv_seminar_header, seminar_type, seminar_date, seminar_comments, tv_seminar_subheader, tv_seminar_location, tv_civ_seminar_speakername;

            public ViewHolder(View convertView) {
                super(convertView);
                speaker_name_ll = (LinearLayout) convertView.findViewById(R.id.book_name_ll);
                iv_seminar = (ImageView) convertView.findViewById(R.id.iv_book);
                tv_seminar_header = (MyTextView) convertView.findViewById(R.id.tv_book_header);
                seminar_type = (MyTextView) convertView.findViewById(R.id.book_type);
                seminar_date = (MyTextView) convertView.findViewById(R.id.book_date);
                seminar_comments = (MyTextView) convertView.findViewById(R.id.book_comments);
                tv_seminar_subheader = (MyTextView) convertView.findViewById(R.id.tv_book_subheader);
                tv_seminar_location = (MyTextView) convertView.findViewById(R.id.tv_book_location);
                tv_civ_seminar_speakername = (MyTextView) convertView.findViewById(R.id.tv_civ_book_name);
                civ_seminar_speaker = (CircularImagvieNew) convertView.findViewById(R.id.civ_book_speaker);
                event_icon = (ImageView) convertView.findViewById(R.id.event_icon);
                cv_book_adapter = convertView.findViewById(R.id.cv_book_adapter);
            }
        }
     public void filter1(String charText) {

         InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
         charText = charText.toString();
         arraylist.clear();
         if (charText.length() == 0) {
             Log.d("System out", "active_list.size in dataentered length zero:" + arrayactivitylist.size()
                     + " arraylist.size:" + arraylist.size());
             arrayactivitylist.addAll(arraylist);
         }

         for (HashMap<String, String> salesHis : arrayactivitylist) {
             Log.d("System out", "msg text " + charText);
             Log.d("System out", "name  text " + salesHis.get("name").toLowerCase().contains(charText.toLowerCase()));
             Log.d("System out", "content  text " + salesHis.get("content").toLowerCase().contains(charText.toLowerCase()));
             Log.d("System out", "type  text " + salesHis.get("type").toLowerCase().contains(charText.toLowerCase()));
             Log.d("System out", "created_on  text " + getformatteddate(salesHis.get("created_on")).toLowerCase().contains(charText.toLowerCase()));
             if ((salesHis.get("name").toLowerCase()
                     .contains(charText.toLowerCase()))||(salesHis.get("moral").toLowerCase()
                     .contains(charText.toLowerCase())) || (salesHis.get("content").toLowerCase()
                     .contains(charText.toLowerCase()))|| (salesHis.get("type").toLowerCase()
                     .contains(charText.toLowerCase()))||(getformatteddate(salesHis.get("created_on")).toLowerCase())
                     .contains(charText.toLowerCase())) {
                 arraylist.add(salesHis);
             }
         }
         rv_books_adapter.notifyDataSetChanged();

         if (allBooksList.size() > 0) {

             Collections.sort(arraylist, new Comparator<HashMap<String, String>>() {
                 @Override
                 public int compare(HashMap<String, String> lhs,
                                    HashMap<String, String> rhs) {
                     return lhs.get("created_on").compareTo(
                             rhs.get("created_on"));
                 }
             });

             Collections.reverse(arraylist);

             arrayactivitylist.clear();
             imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

             no_books.setVisibility(View.GONE);
             recyclerView.setVisibility(View.VISIBLE);
             rv_books_adapter = new RecyclerAdapterSeminar(AllBooksListActivity.this, arraylist);
             recyclerView.setAdapter(rv_books_adapter);
             rv_books_adapter.notifyDataSetChanged();
         } else {
             no_books.setVisibility(View.VISIBLE);
             recyclerView.setVisibility(View.GONE);
             imm1.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

             Toast.makeText(AllBooksListActivity.this, "No Records Found", Toast.LENGTH_SHORT).show();
         }
         rv_books_adapter.notifyDataSetChanged();

     }



 }



    private String getformatteddate(String dateget) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expiry = null;
        String yourDate = "";
        try {


            expiry = formatter.parse(dateget);

        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd, KK:mm a");

        if (expiry != null) {
            yourDate = format.format(expiry);
        }
        return yourDate;
    }


}
