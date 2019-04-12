package com.gayatri.testapp.Utils;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class MyTextView extends TextView {
	private final Context context;
	public MyTextView(Context context) {
		super(context);
		this.context = context;
	}


	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	// //intercept Typeface change and set it with our custom font
	// @Override
	// public void setTypeface(Typeface tf, int style) {
	// if (style == Typeface.BOLD) {
	// super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
	// "fonts/Vegur-B 0.602.otf"));
	// } else {
	// super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
	// "fonts/Vegur-R 0.602.otf"));
	// }
	// }
	
	 public void setTypeface(Typeface tf, int style) {
		 if (style == Typeface.BOLD) {
	        // super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "canaro_extra_bold.ttf")/*, -1*/);
	     } else {

//	        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "HPSimplifiedRegular.ttf")/*, -1*/);
	     }
	 }
}