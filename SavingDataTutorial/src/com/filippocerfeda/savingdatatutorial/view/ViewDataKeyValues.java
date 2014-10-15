package com.filippocerfeda.savingdatatutorial.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filippocerfeda.savingdatatutorial.R;
import com.filippocerfeda.savingdatatutorial.activity.MainActivity;
import com.filippocerfeda.savingdatatutorial.factory.ITutorialViewSaving;

public class ViewDataKeyValues implements ITutorialViewSaving {
	
	//Identify the preferences of the application
	private final static String DATA_PREFERENCES = "SavingDataTutorial_DataPref";
	//Identify of the key for this tutorial
	private final static String KEY_TIMESTEMP = "SavingDataTutorial_DataPref";
	//String for timestamp
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());
	
	private MainActivity activity;
	private ViewGroup container;
	private String data_time_start;

	public ViewDataKeyValues(MainActivity activity, ViewGroup container) {
		this.activity = activity;
		this.container = container;
		Date currDate = new Date();
		data_time_start = sdf.format(currDate);
	}
	
	@Override
	public View viewSavingData() {
		
		View view = activity.getLayoutInflater().inflate(R.layout.pager_item_keyvalues, container, false);
		container.addView(view);
		
		//instance of the {@link SharedPreference} to store the value of the key {@code DATA_PREFERENCES}
		SharedPreferences prefs = activity.getSharedPreferences(DATA_PREFERENCES, Context.MODE_PRIVATE);
		boolean notFoundKeyInPrefs = existKey(prefs);

		if (notFoundKeyInPrefs) {
			//store the new value of the key in the preferences
			SharedPreferences.Editor edit = prefs.edit().putString(KEY_TIMESTEMP, data_time_start);
			edit.commit();
		}
		
		//read the value of the key in the preferences
		String value_textViewKeySaved = prefs.getString(KEY_TIMESTEMP, null);
		
		TextView viewTextSaved = (TextView) view.findViewById(R.id.textViewKeySaved);
		viewTextSaved.setText(value_textViewKeySaved);
		
		TextView viewTextTimestamp = (TextView) view.findViewById(R.id.textViewTimestamp);
		Date currDate = new Date();
		viewTextTimestamp.setText(sdf.format(currDate));
		
		return view;
	}
	
	//verify if the key is present in SharePreferences
	private boolean existKey(SharedPreferences prefs) {
		return (prefs.getString(KEY_TIMESTEMP, null) == null);	
	}
	
	
	

}
