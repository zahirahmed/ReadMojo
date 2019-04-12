package com.gayatri.testapp.Utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gayatri.testapp.AudioActivity;
import com.gayatri.testapp.CreateBooksActivity;
import com.gayatri.testapp.ImageActivity;
import com.gayatri.testapp.LoginActivity;
import com.gayatri.testapp.R;
import com.gayatri.testapp.ShareActivity;
import com.gayatri.testapp.StatsActivity;
import com.gayatri.testapp.TeamActivity;
import com.gayatri.testapp.TestActivity;
import com.gayatri.testapp.TextActivity;


public class DrawerMenu extends LinearLayout implements OnClickListener {

    LinearLayout createbooks_ll, image_ll, audio, text, share, stats,logout,test,team;
    TextView txt_createbooks, txt_image, txt_audio, txt_share, txt_stats, txt_test,txt_logout,txt_text,txt_team;
    LinearLayout menulogo_ll,createbooks_iv_ll, image_iv_ll, audio_iv_ll, text_iv_ll, test_iv_ll, team_iv_ll,share_iv_ll,states_iv_ll;
    ImageView iv_createbooks, iv_text, iv_test, iv_team, iv_audio, iv_image,iv_share,iv_states;
    Context context;
    ImageView menulogo;
   private SharedPreferences prefrence;
	


    public DrawerMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    prefrence = context.getSharedPreferences("My_Pref", 0);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.drawer_menu, null);
        RelativeLayout footerLinearLayout = (RelativeLayout) view.findViewById(R.id.ll_main_drawer);

        createbooks_ll = (LinearLayout) view.findViewById(R.id.createbooks);
        image_ll = (LinearLayout) view.findViewById(R.id.image);
        text = (LinearLayout) view.findViewById(R.id.text);
        test = (LinearLayout) view.findViewById(R.id.test);
        audio = (LinearLayout) view.findViewById(R.id.audio);
    share = (LinearLayout) view.findViewById(R.id.share);
        stats = (LinearLayout) view.findViewById(R.id.stats);
        team = (LinearLayout) view.findViewById(R.id.team);
        logout = (LinearLayout) view.findViewById(R.id.logout);


     //  menulogo = (ImageView) view.findViewById(R.id.menulogo);
        iv_createbooks = (ImageView) view.findViewById(R.id.iv_createbooks);
        iv_audio = (ImageView) view.findViewById(R.id.iv_audio);
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_share = (ImageView) view.findViewById(R.id.iv_share);
        iv_states = (ImageView) view.findViewById(R.id.iv_states);
        iv_team = (ImageView) view.findViewById(R.id.iv_team);
        iv_test = (ImageView) view.findViewById(R.id.iv_test);
        iv_text= (ImageView) view.findViewById(R.id.iv_text);


        createbooks_iv_ll = (LinearLayout) view.findViewById(R.id.createbooks_iv_ll);
        test_iv_ll = (LinearLayout) view.findViewById(R.id.test_iv_ll);
        text_iv_ll = (LinearLayout) view.findViewById(R.id.text_iv_ll);
        team_iv_ll = (LinearLayout) view.findViewById(R.id.team_iv_ll);
        share_iv_ll = (LinearLayout) view.findViewById(R.id.share_iv_ll);
        audio_iv_ll = (LinearLayout) view.findViewById(R.id.audio_iv_ll);
        image_iv_ll = (LinearLayout) view.findViewById(R.id.image_iv_ll);
        states_iv_ll = (LinearLayout) view.findViewById(R.id.states_iv_ll);
       // menulogo_ll=(LinearLayout)findViewById(R.id.menulogo_ll);

        txt_createbooks = (TextView) view.findViewById(R.id.txt_createbooks);
        txt_team = (TextView) view.findViewById(R.id.txt_team);
        txt_test = (TextView) view.findViewById(R.id.txt_test);
        txt_text = (TextView) view.findViewById(R.id.txt_text);
        txt_share = (TextView) view.findViewById(R.id.txt_share);
        txt_audio = (TextView) view.findViewById(R.id.txt_audio);
        txt_stats = (TextView) view.findViewById(R.id.txt_stats);
        txt_image = (TextView) view.findViewById(R.id.txt_image);
        txt_logout = (TextView) view.findViewById(R.id.txt_logout);


        createbooks_ll.setOnClickListener(this);
        team.setOnClickListener(this);
        test.setOnClickListener(this);
        text.setOnClickListener(this);
        audio.setOnClickListener(this);
        image_ll.setOnClickListener(this);
        share.setOnClickListener(this);
        stats.setOnClickListener(this);


        logout.setOnClickListener(this);
        //menulogo_ll.setOnClickListener(this);
        this.addView(footerLinearLayout);

    }

    @Override
    public void onClick(View v) {
        if (v == createbooks_ll) {
          
            Intent i = new Intent(context, AudioActivity.class);
            ComponentName cn = i.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);


        } else if (v == audio) {
        	Intent i = new Intent(context, CreateBooksActivity.class);
            ComponentName cn = i.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);

        } else if (v == image_ll) {
        	  Intent i = new Intent(context, ImageActivity.class);
            ComponentName cn = i.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);

        } else if (v == test) {
        	 Intent i = new Intent(context, TestActivity.class);
            ComponentName cn = i.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);

        } else if (v == text) {
        	Intent i = new Intent(context, TextActivity.class);
             ComponentName cn = i.getComponent();
		       Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
		       mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		       context.startActivity(mainIntent);

        } else if (v == stats) {
        	Intent i = new Intent(context, StatsActivity.class);
            ComponentName cn = i.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);
        }
        else if (v == team) {
            Intent i = new Intent(context, TeamActivity.class);
            ComponentName cn = i.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);
        }
        else if (v == share) {


            Intent i = new Intent(context, ShareActivity.class);
            ComponentName cn = i.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);
        } else if(v==logout)
        	{
        	 prefrence.edit().clear().commit();
 			Intent intent = new Intent(this.context,LoginActivity.class);
 			ComponentName cn = intent.getComponent();
 			Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
 			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 			context.startActivity(mainIntent);
        	
        	}
        /*	else if (v == menulogo_ll) {
        	
        	    Intent i = new Intent(context,CreateBooksActivity.class);
                ComponentName cn = i.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(mainIntent);
            
        	
        }*/
    }


}
