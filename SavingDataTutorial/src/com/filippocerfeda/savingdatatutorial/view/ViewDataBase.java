package com.filippocerfeda.savingdatatutorial.view;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.filippocerfeda.savingdatatutorial.R;
import com.filippocerfeda.savingdatatutorial.activity.MainActivity;
import com.filippocerfeda.savingdatatutorial.factory.ITutorialViewSaving;

public class ViewDataBase implements ITutorialViewSaving {

	private Button save,show,delete;  
	private ListView listView;
	private ViewGroup container;
	private MainActivity activity;
	
	private final String TABLE_CONTACTS = "fc_contacts";
	private final String FIELD_CONTACT = "Contact";
	
	private Integer positionContactSelected;

	public ViewDataBase(MainActivity activity, ViewGroup container) {
		this.activity = activity;
		this.container = container;
		createTableData();
	}
	
	@Override
	public View viewSavingData() {
		
		View view = activity.getLayoutInflater().inflate(R.layout.pager_item_database, container, false);
		this.save = (Button) view.findViewById(R.id.buttonSaveContact);
		this.show = (Button) view.findViewById(R.id.buttonShowContact);
		this.delete = (Button) view.findViewById(R.id.buttonDeleteContact);
		settingListenerButtons();
		
		this.listView = (ListView) view.findViewById(R.id.listViewContacts);
		this.listView.setOnItemClickListener(new ListContactItemClickListner());
		
		container.addView(view);		
		return view;
	}
	
	
	private void settingListenerButtons() {
		save.setOnClickListener(new OnClickListener() {  
			@Override  
			public void onClick(View v) {
				EditText inputContact = (EditText)container.findViewById(R.id.db_field_contact);
				String contact = inputContact.getText().toString();
				ContentValues contentValues = new ContentValues();
				contentValues.put(FIELD_CONTACT, contact);
				long result = activity.mydatabase.insert(TABLE_CONTACTS, null, contentValues); 
				inputContact.setText("");
				if ( result > -1) {
					Toast.makeText(activity.getBaseContext(), "Saved the contact "+contact, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity.getBaseContext(), "Not saved "+contact, Toast.LENGTH_LONG).show();
				}					
			}  
		});
		show.setOnClickListener(new OnClickListener() {  
			@Override  
			public void onClick(View v) {
				refreshListContact();
				Toast.makeText(activity.getBaseContext(), "Message : Show "+ listView.getCount()+ " contacts", Toast.LENGTH_LONG).show();  
			}  
		});
		delete.setOnClickListener(new OnClickListener() {  
			@Override  
			public void onClick(View v) {
				String contact = (String)listView.getItemAtPosition(positionContactSelected);
				int result = deleteContact(contact);

				refreshListContact();
				delete.setEnabled(false);
				
				if ( result > -1) {
					Toast.makeText(activity.getBaseContext(), "Delete "+contact, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity.getBaseContext(), "Not Delete "+contact, Toast.LENGTH_LONG).show();
				}					
			}  
		});	
	}
	
	
	private void refreshListContact() {
		ArrayList<String> listContacts = getContacts();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, listContacts);
		listView.setAdapter(adapter);
	}
	
	private void createTableData() {
		activity.mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_CONTACTS+"(Contact VARCHAR);");
	}
	
	private ArrayList<String> getContacts() {
		ArrayList<String> array_list = new ArrayList<String>();
		Cursor cursor = activity.mydatabase.rawQuery( "select * from "+TABLE_CONTACTS, null );
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
		      array_list.add(cursor.getString(cursor.getColumnIndex(FIELD_CONTACT)));
		      cursor.moveToNext();
		}
		return array_list;
	}
	
	private int deleteContact(String contact) {
		
		String selection = FIELD_CONTACT + " LIKE ?";
		String[] selectionArgs = { String.valueOf(contact) };
		return activity.mydatabase.delete(TABLE_CONTACTS, selection, selectionArgs);		
	}
		
	private class ListContactItemClickListner implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			positionContactSelected = position;
			delete.setEnabled(true);
		}
	}	
}
