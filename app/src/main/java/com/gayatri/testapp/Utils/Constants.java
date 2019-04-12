package com.gayatri.testapp.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;




public class Constants {
	
	
	public static int intDrawerSelection=0;
	public static int intBackbtn=0;
	
	static File cacheDir;

	public static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      ); 
	public static String DEVICE_ID="";
	public static String PREF="My_Pref";
	 public static String DEVICE_TOKEN="";
	 public static String SENDER_ID="111255332447";  //skentqc
	public static Dialog dialog;
	//public static String SERVER_URL="http://localhost:80/readmojo_ws/index.php/services/user_details/";
//	public static String SERVER_URL="http://dev.readmojo.com/readmojo_ws/index.php/services//user_details/";
	public static String SERVER_URL="http://dev.readmojo.com/readmojo_ws2/index.php/services/user_details/";
    public static ArrayList<HashMap<String,String>> childrenarraylist=new ArrayList<HashMap<String,String>>();



	public static void executeLogcat(Context context){
			Log.d("System out", "Create Log file..");	
			
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
				cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"READMOJO" +
                        "");
		    else
		        cacheDir=context.getCacheDir();
			
		    if(!cacheDir.exists())
		        cacheDir.mkdirs();
			
		    	File logFile = new File(cacheDir, "logoutput.log"); // log file name
		    	int sizePerFile = 60; // size in kilobytes
		    	int rotationCount = 10; // file rotation count
		    	String filter = "D"; // Debug priority

		    	String[] args = new String[] { "logcat",
		    					"-v", "time",
		    					"-f",logFile.getAbsolutePath(),
		    					"-r", Integer.toString(sizePerFile),
		    					"-n", Integer.toString(rotationCount),
		    					"*:" + filter };

		    	try {
		    		Runtime.getRuntime().exec(args);
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
		    }
		
	}
