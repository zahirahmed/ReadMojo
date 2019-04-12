package com.gayatri.testapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gayatri.testapp.AudioActivity;
import com.gayatri.testapp.R;
import com.gayatri.testapp.ShareActivity;
import com.gayatri.testapp.TextActivity;


public class Footer extends LinearLayout {

	TextView footercreatebookstxt, footerreporttxt, footersharetxt, footermoretxt;
	ImageView footercreatebooksicon, footershareicon, footerreporticon,footermoreicon, createbooksselctor,
			reportselctor, shareselctor,moreselctor;
	LinearLayout footerLinearLayout;
	FrameLayout footercreatebookstab, footersharetab, footerreporttab,footermoretab;
	public static final int CREATEBOOKS_TEXT = 100;
	public static final int SHARE_TEXT = 102;
	public static final int MORE_BADGETEXT = 101;
	public static final int REPORT_TEXT = 103;
	public static final int MORE_TEXT = 104;

	public static final int CREATEBOOKS_SELECTOR = 5;
	public static final int SHARE_SELECTOR = 6;
	public static final int REPORT_SELECTOR = 7;
	public static final int MORE_SELECTOR = 8;

	public static final int HOME_BG = 10;
	public static final int CREATEBOOKS_BG = 11;
	public static final int REPORT_BG = 12;
	public static final int SHARE_BG = 13;
	public static final int MORE_BG = 14;
	SharedPreferences prefrence;
	private TextView footernoticounttxt;
	
	
	public Footer(final Context context, AttributeSet attrs) {
		super(context, attrs);
		prefrence = context.getSharedPreferences(Constants.PREF, 0);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.footertab, null);
		
		footerLinearLayout = (LinearLayout) v.findViewById(R.id.mainfooter_ll);
		footercreatebookstab = (FrameLayout) v.findViewById(R.id.createbooksfooter_fl);
		 footerreporttab= (FrameLayout) v.findViewById(R.id.reportfooter_fl);
		footermoretab = (FrameLayout) v.findViewById(R.id.morefooter_fl);
		footersharetab= (FrameLayout) v.findViewById(R.id.sharefooter_fl);

		footercreatebooksicon = (ImageView) v.findViewById(R.id.iv_createbooks_img);
		footerreporticon = (ImageView) v.findViewById(R.id.iv_report_img);
		footershareicon = (ImageView) v.findViewById(R.id.iv_share_img);
		footermoreicon = (ImageView) v.findViewById(R.id.iv_more_img);
	

		footercreatebookstxt = (TextView) v.findViewById(R.id.tv_createbooksfooter);
		footerreporttxt = (TextView) v.findViewById(R.id.tv_reportfooter);
		footersharetxt = (TextView) v.findViewById(R.id.tv_sharefooter);
		footermoretxt = (TextView) v.findViewById(R.id.tv_morefooter);
	   

		createbooksselctor = (ImageView) v.findViewById(R.id.createbooks_selector);
		reportselctor = (ImageView) v.findViewById(R.id.report_selector);
		shareselctor = (ImageView) v.findViewById(R.id.share_selector);
		moreselctor = (ImageView) v.findViewById(R.id.more_selector);
		footercreatebookstxt.setTextColor(getResources().getColor(R.color.blueshade));
		createbooksselctor.setVisibility(View.VISIBLE);

		if (Constants.intDrawerSelection == 1) {
			
		//	footercreatebooksicon.setImageResource(R.drawable.footer_icon2_selected);

			footercreatebookstxt.setTextColor(getResources().getColor(R.color.blueshade));
			createbooksselctor.setVisibility(View.VISIBLE);

		} if (Constants.intDrawerSelection == 2) {
			shareselctor.setVisibility(View.VISIBLE);

			footersharetxt.setTextColor(getResources().getColor(R.color.blueshade));
			//footershareicon.setImageResource(R.drawable.footer_icon1_selected);
		//	footercreatebooksicon.setImageResource(R.drawable.footer_icon2_unselected);

			footercreatebookstxt.setTextColor(Color.parseColor("#000000"));
			createbooksselctor.setVisibility(View.INVISIBLE);

		} else if (Constants.intDrawerSelection == 3) {
			reportselctor.setVisibility(View.VISIBLE);

			footerreporttxt.setTextColor(getResources().getColor(R.color.blueshade));
		//	footerreporticon.setImageResource(R.drawable.footer_icon_selected);
		//	footercreatebooksicon.setImageResource(R.drawable.footer_icon2_unselected);

			footercreatebookstxt.setTextColor(Color.parseColor("#000000"));
			createbooksselctor.setVisibility(View.INVISIBLE);

		} else if (Constants.intDrawerSelection == 4) {
			moreselctor.setVisibility(View.VISIBLE);

			footermoretxt.setTextColor(getResources().getColor(R.color.blueshade));
			//footermoreicon.setImageResource(R.drawable.footer_icon3_selected);
			//footercreatebooksicon.setImageResource(R.drawable.footer_icon2_unselected);

			footercreatebookstxt.setTextColor(Color.parseColor("#000000"));
			createbooksselctor.setVisibility(View.INVISIBLE);
		}

		footercreatebookstab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.d("system out", "yes reach2");
				Intent i = new Intent(context, TextActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				i.putExtra("Flag", "Is first");
				Constants.intDrawerSelection= 1;
			
				context.startActivity(i);

			}
		});
		footerreporttab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				/*Log.d("system out", "yes reach3");
				Intent i = new Intent(context, ReportActvity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Constants.intDrawerSelection= 2;
				context.startActivity(i);*/
			}
		});
		footersharetab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(context, ShareActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Constants.intDrawerSelection= 3;
				context.startActivity(i);

			}
		});
		footermoretab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			/*
				if(prefrence.getString("MemID", "").toString().length()>0){
					Intent i = new Intent(context, MoreActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					Constants.intDrawerSelection= 4;
					context.startActivity(i);
				}else{
					prefrence.edit().putString("fromMore", "more").commit();
					Intent i = new Intent(context, LoginActivity.class);
					Constants.intDrawerSelection= 4;
					context.startActivity(i);
				}*/
			
			/*	Intent i = new Intent(context, MoreActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Constants.intDrawerSelection= 4;
				context.startActivity(i);
	*/		}
		});

		this.addView(footerLinearLayout);
	}

}
