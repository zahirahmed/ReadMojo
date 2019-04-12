package com.gayatri.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gayatri.testapp.Model.BookDetailClass;
import com.gayatri.testapp.Model.ImageBookDetailClass;
import com.gayatri.testapp.Utils.CircularImagvieNew;
import com.gayatri.testapp.Utils.Constants;
import com.gayatri.testapp.Utils.MyTextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 12-Feb-17.
 */
public class BookDetailActivity extends AppCompatActivity {

    private DBAdapter db;
    private ArrayList<BookDetailClass> bookList = new ArrayList<BookDetailClass>();
    private ArrayList<ImageBookDetailClass> imageList = new ArrayList<ImageBookDetailClass>();
    private String book_id="";
    private ViewPager pager;
    CustomScrollView sv_main;
    private SharedPreferences prefrence;
    private SharedPreferences.Editor editor;
    private String id_of_;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_screen);
        prefrence = getSharedPreferences("My_Pref", 0);
        editor = prefrence.edit();
        book_id = getIntent().getStringExtra("book_id");
        db = new DBAdapter(this);
        bookList.clear();
        imageList.clear();
        db.open();
        bookList = db.getbookdetail();
        imageList = db.getimagebookdetail();
        db.close();
        init();
        if (bookList.size()>0)
        loadData();
    }

    private void init() {
        pager=(ViewPager) findViewById(R.id.pager);
    }

    private void loadData() {

        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(new FullScreenAdapter(BookDetailActivity.this, bookList));
        pager.getAdapter().notifyDataSetChanged();


        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getBook_id().trim().equalsIgnoreCase(book_id.trim())) {
                pager.setCurrentItem(i);
                break;
            }

        }


        pager.setOnTouchListener(new View.OnTouchListener() {

            int dragthreshold = 30;
            int downX;
            int downY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int distanceX = Math.abs((int) event.getRawX() - downX);
                        int distanceY = Math.abs((int) event.getRawY() - downY);

                        if (distanceY > distanceX && distanceY > dragthreshold) {
                            pager.getParent().requestDisallowInterceptTouchEvent(false);
                        } else if (distanceX > distanceY && distanceX > dragthreshold) {
                            pager.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        pager.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                editor.putString("book_id", bookList.get(position).getBook_id()).commit();
                id_of_ = bookList.get(position).getBook_id();
                Log.d("System out", "id of scroll: " + id_of_);
                /*viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                viewPager.setAdapter(new FullScreenAdapterInner(ItemDetailActivity.this, id_of_));
                viewPager.getAdapter().notifyDataSetChanged();*/

            }

            @Override
            public void onPageSelected(int position) {
                pager.getAdapter().notifyDataSetChanged();
                if (position == ViewPager.SCROLL_STATE_DRAGGING) {
                    sv_main.requestDisallowInterceptTouchEvent(true);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                pager.getAdapter().notifyDataSetChanged();

            }
        });
    }


    public class FullScreenAdapter extends PagerAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        Context context;
        private ArrayList<BookDetailClass> arrBooks = new ArrayList<BookDetailClass>();

        public FullScreenAdapter(Activity activity, ArrayList<BookDetailClass> arrBooks) {
            this.activity = activity;
            this.arrBooks = arrBooks;
        }

        @Override
        public int getCount() {

            return arrBooks.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.book_detail_layout, container, false);

            final MyTextView tv_book_title = (MyTextView) viewLayout.findViewById(R.id.tv_book_title);
            final MyTextView tv_book_type = (MyTextView) viewLayout.findViewById(R.id.tv_book_type);
            final MyTextView tv_date = (MyTextView) viewLayout.findViewById(R.id.tv_date);
            final MyTextView tv_book_moral = (MyTextView) viewLayout.findViewById(R.id.tv_book_moral);
            final MyTextView tv_book_description = (MyTextView) viewLayout.findViewById(R.id.tv_book_description);
            sv_main = (CustomScrollView) viewLayout.findViewById(R.id.sv_main);
            final Button  bt_share_detail = (Button) viewLayout.findViewById(R.id.bt_share_detail);
            final CircularImagvieNew civ_profile_drawer = (CircularImagvieNew) viewLayout.findViewById(R.id.civ_profile_drawer);


            tv_book_title.setText("Book Title :"+arrBooks.get(position).getBookName());
            tv_book_type.setText("Book Type :"+arrBooks.get(position).getBookType());
            tv_book_description.setText("Book Description :"+arrBooks.get(position).getBookContent());
            tv_date.setText(getformatteddate(arrBooks.get(position).getCreated_on()));
            tv_book_moral.setText("Book Moral :"+arrBooks.get(position).getMoral());
            Picasso.with(BookDetailActivity.this).load(arrBooks.get(position).getAudio_path() + "&h=150").error(R.drawable.books_bulk).placeholder(R.drawable.books_bulk).into(civ_profile_drawer);

//http://blog.nkdroidsolutions.com/how-to-share-image-from-android-app-programmatically-example/
            bt_share_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(BookDetailActivity.this,ShareActivity.class);
                    i.putExtra("bookDetail",arrBooks.get(position));
                    startActivity(i);
                    finish();
                }
            });
            Log.e("System out", "imageurl1 ");
            ((ViewPager) container).addView(viewLayout);
            return viewLayout;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
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
