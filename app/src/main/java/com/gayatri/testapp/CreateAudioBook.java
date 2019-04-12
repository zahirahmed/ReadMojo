package com.gayatri.testapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gayatri.testapp.Utils.CustomLayoutManager;
import com.gayatri.testapp.Utils.SlideHolder;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Gayatri on 07-09-2015.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
@SuppressWarnings("deprecation")
public class CreateAudioBook extends Activity implements View.OnClickListener,TextToSpeech.OnInitListener,OnUtteranceCompletedListener {

    private ImageView iv_menu,next_btn;
    private SlideHolder slider;
    //private MyEditText audio_data;
    private TextView taptoreadnext;
    private TextToSpeech tts;
    HashMap<String, String> params = new HashMap<String, String>();
    RecyclerView rc_sentence;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    ArrayList<String> arrayList=new ArrayList<>();
    public static int i=-1;
    private int focusedItem = -1;
    int currentVolume;
    NestedScrollView rec_scroll;
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

        if (slider.isOpened()) {
            slider.toggle();
        }
    }
/*
*//**//*
*//**//*
*//**//*
*//**//*
*//**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiobookscreen2);
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        //Toast.makeText(CreateAudioBook.this, "VOLLLL : "+currentVolume, Toast.LENGTH_SHORT).show();

        if(currentVolume<2)
        {
            Toast.makeText(CreateAudioBook.this, "Media volume is low, set to high for speech to be audible", Toast.LENGTH_LONG).show();
        }
        i=-1;
        focusedItem = -1;
        String s = getIntent().getStringExtra("book_detail");
        //String[] arr = s.split("\\W+");
        String[] arr = s.split(" ");

        if(arrayList.size()>0)
        {
            arrayList.clear();
        }

        for ( String ss : arr) {
            arrayList.add(ss);
        }

        init();

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

    }
    //
    private void init() {
        slider = (SlideHolder) findViewById(R.id.drawer_layout);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        rc_sentence = findViewById(R.id.rc_sentence);
        rec_scroll = findViewById(R.id.rec_scroll);
        //RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager layoutManager=new CustomLayoutManager(this,0);
        FlexboxLayoutManager layoutManagerr = new FlexboxLayoutManager(this);
        layoutManagerr.setFlexDirection(FlexDirection.ROW);
        layoutManagerr.setJustifyContent(JustifyContent.FLEX_START);
        rc_sentence.setLayoutManager(layoutManagerr);
        rc_sentence.setAdapter(new SentenceAdapter(arrayList,CreateAudioBook.this));
        rc_sentence.setNestedScrollingEnabled(false);
        Log.d("ssssssssss",i+" "+arrayList+"   ");
        iv_menu.setOnClickListener(this);
        /*audio_data=(MyEditText)findViewById(R.id.audio_data);
        audio_data.setText(getIntent().getStringExtra("book_detail"));*/
        next_btn=(ImageView)findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
        taptoreadnext=(TextView)findViewById(R.id.taptoreadnext);

    }

    public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.MyViewHolder>
    {

        ArrayList<String> arrayList;
        Context context;

        public SentenceAdapter(ArrayList<String> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_layout,viewGroup,false);
            MyViewHolder myViewHolder=new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

            myViewHolder.word.setText(""+arrayList.get(i));
            myViewHolder.word.setSelected(focusedItem == i);
           /* if(MainActivity.i==i){
                myViewHolder.word.setTextColor(Color.parseColor("#FFEFD701"));
            }
            else
            {
                myViewHolder.word.setTextColor(Color.parseColor("#000000"));
            }*/

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView word;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                word=itemView.findViewById(R.id.word);
                taptoreadnext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        RecyclerView.LayoutManager lm = rc_sentence.getLayoutManager();
                        if(arrayList.size()>0)
                        {
                           /* recycler.getLayoutManager().scrollToPosition(MainActivity.i);
                            //recycler.getLayoutManager().findViewByPosition(MainActivity.i).setBackgroundColor(Color.parseColor("#FFEFD701"));
                            TextView t=recycler.findViewHolderForAdapterPosition(MainActivity.i).itemView.findViewById(R.id.word);
                            t.setTextColor(Color.parseColor("#FFEFD701"));*/
                            //MainActivity.i=i;
                            // notifyDataSetChanged();
                            // myViewHolder.word.setTextColor(Color.parseColor("#FFEFD701"));
                            tryMoveSelection(word,lm, 1);

                        }
                        Log.d("ssssssssss", CreateAudioBook.i+" "+arrayList+"   ");
                    }
                });
                rc_sentence.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (!recyclerView.canScrollVertically( -1)) {
                            Toast.makeText(CreateAudioBook.this, "Reset", Toast.LENGTH_LONG).show();
                            CreateAudioBook.i=-1;
                            focusedItem = -1;
                            word.setSelected(false);

                        }
                    }
                });
            }
        }
        private boolean tryMoveSelection(TextView word, RecyclerView.LayoutManager lm, int direction) {
            int tryFocusItem = focusedItem + direction;

            // If still within valid bounds, move the selection, notify to redraw, and scroll
            if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {

                notifyItemChanged(focusedItem);

                focusedItem = tryFocusItem;
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
                notifyItemChanged(focusedItem);
                //lm.scrollToPosition(focusedItem);
                // Method yet to be defined
                String words = getIntent().getStringExtra("book_detail");
                Log.d("System out", "get text data is" + words);
                //speakWords(words);
                speakWords(arrayList.get(focusedItem), "done");

                /*TextView t=recycler.findViewHolderForAdapterPosition(focusedItem).itemView.findViewById(R.id.word);
                t.setTextColor(Color.parseColor("#FFEFD701"));*/
                CreateAudioBook.i++;

                return true;
            }
            else
            {
                //  Toast.makeText(context, arrayList.size()+" elseeee  "+focusedItem, Toast.LENGTH_SHORT).show();

                notifyItemChanged(focusedItem);
                lm.scrollToPosition(0);

                rec_scroll.fullScroll(View.FOCUS_UP);
                rec_scroll.scrollTo(0,0);
                CreateAudioBook.i=-1;
                focusedItem = -1;
                word.setSelected(false);

            }

            return false;
        }

    }

    @Override
    public void onClick(View v) {

        if (v == iv_menu) {
            slider.toggle();
        }

    }


    //speak the user text
    private void speakWords(String speech,String utteranceId) {

        //speak straight away
         Log.d("System out", "in speak words method");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, params);

    }

    UtteranceProgressListener utteranceProgressListener=new UtteranceProgressListener() {

        @Override
        public void onStart(String utteranceId) {
            Log.d("System out", "onStart ( utteranceId :"+utteranceId+" ) ");
        }

        @Override
        public void onError(String utteranceId) {
            Log.d("System out", "onError ( utteranceId :"+utteranceId+" ) ");
        }

        @Override
        public void onDone(String utteranceId) {
            Log.d("System out", "onDone ( utteranceId :"+utteranceId+" ) ");
        }
    };

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("System out","in activity result request code"+requestCode+"my-data_check code"+MY_DATA_CHECK_CODE);

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS

                Log.d("System out","both equal");

                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Log.d("System out","in intent install tts data");

                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    //setup TTS
    public void onInit(int initStatus) {
        Log.d("System out","in on init method");

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            Log.d("System out","in language success");
            myTTS.setOnUtteranceCompletedListener(this);

            if(myTTS.isLanguageAvailable(Locale.ENGLISH)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.ENGLISH);
            Log.d("System out","language set");

        }
        else if (initStatus == TextToSpeech.ERROR) {
            Log.d("System out","in error");

            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        if (utteranceId.equalsIgnoreCase("done")) {
         Toast.makeText(this.getApplicationContext(),"Your story completed",Toast.LENGTH_SHORT).show();        }

    }
}
