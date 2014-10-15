package com.filippocerfeda.savingdatatutorial.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.filippocerfeda.savingdatatutorial.R;
import com.filippocerfeda.savingdatatutorial.activity.MainActivity;
import com.filippocerfeda.savingdatatutorial.factory.ITutorialViewSaving;

public class ViewDataFile implements ITutorialViewSaving  {

	private Button save,load;  
	private EditText message;
	private ViewGroup container;
	private MainActivity activity;
	
	private final int BUFFER = 100;
	private final String FILE_NAME = "fc_test_file_data.txt";

	public ViewDataFile(MainActivity activity, ViewGroup container) {
		this.activity = activity;
		this.container = container;
	}
	
	@Override
	public View viewSavingData() {
		
		View view = activity.getLayoutInflater().inflate(R.layout.pager_item_filedata, container, false);
		this.save = (Button) view.findViewById(R.id.buttonSaveToFile);
		this.load = (Button) view.findViewById(R.id.buttonLoadFromFile);
		this.message = (EditText) view.findViewById(R.id.textValue);
		settingListenerButtons();
		container.addView(view);		
		return view;
	}
	
	private void settingListenerButtons() {
		save.setOnClickListener(new OnClickListener() {  
			@Override  
			public void onClick(View v) {  
				try {  
					FileOutputStream fou = activity.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);  
					OutputStreamWriter osw = new OutputStreamWriter(fou);  
					try {  
						osw.write(message.getText().toString());  
						osw.flush();  
						osw.close();  
						Toast.makeText(activity.getBaseContext(), "Saved ", Toast.LENGTH_LONG).show();  
					} catch (IOException e) {  
						e.printStackTrace();  
					}  
				} catch (FileNotFoundException e) {  
					e.printStackTrace();  
				}  
			}  
		});
		load.setOnClickListener(new OnClickListener() {  
			@Override  
			public void onClick(View v) {  
				try {  
					FileInputStream fis = activity.openFileInput(FILE_NAME);  
					InputStreamReader isr = new InputStreamReader(fis);  
					char[] data = new char[BUFFER];  
					String string_data = "";  
					int size;  
					try {  
						while((size = isr.read(data))>0)  
						{  
							String read_data = String.copyValueOf(data, 0, size);       
							string_data+= read_data;  
							data = new char[BUFFER];  
						}  
						Toast.makeText(activity.getBaseContext(), "Message :"+string_data, Toast.LENGTH_LONG).show();  
					} catch (IOException e) {  
						e.printStackTrace();  
					}  
				} catch (FileNotFoundException e) {  
					e.printStackTrace();  
				}       
			}  
		});
	}
}
