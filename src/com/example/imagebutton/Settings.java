package com.example.imagebutton;

//import com.paad.contactpicker.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

import android.app.Activity;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;  

public class Settings extends Activity {
         /**
          *onCreate is our first method to be invoked when we open settings page of our app
          * All the User interface elements are binded together 
          * as mentioned in androindmanifest.xml file
          * I am not using any database to store information. I have a mydata.java class to store results of variables.
          * 
          * 
          * 
          * /
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		if(!mydata.callNumber.equals(""))
		{
			TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
	        tv.setText(mydata.callNumber);
		}
		if(mydata.msgNumber.size()!=0)
		{
			EditText etcts = (EditText)findViewById(R.id.SMS_CONTACTS_TEXT);
	        etcts.setText("");
			for(Object ob:mydata.msgNumber.toArray())
	        {
				etcts.setText(etcts.getText()+ob.toString()+"; ");
	        }
	    }
		if(!mydata.message.equals(""))
		{
			EditText etsms = (EditText)findViewById(R.id.SMS_MESSAGE);
	        etsms.setText(mydata.message);
	    }		
	}

	//CONTACT PICKER FOR EMERGENCY CONTACT
		private static final int CONTACT_PICKER_RESULT = 1;  
		public void doLaunchContactPicker(View view) {  
		    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
		            Contacts.CONTENT_URI);  
		    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
		} 
	//CONTACT PICKER FOR SMS CONTACTS
	private static final int SMS_CONTACT_PICKER_RESULT = 2;  
	public void launchSmsContactPicker(View view) {  
	    Intent contactPickerIntent2 = new Intent(Intent.ACTION_PICK,  
	            Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent2, SMS_CONTACT_PICKER_RESULT);  
	}  
  
	/** For saving all the settings information to mydata instance variables 
	  * A Toast message will be displayed when you click on save button
	  * /
	public void callSave(View view) {  
		TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
        mydata.callNumber = tv.getText().toString();
        EditText etcts = (EditText)findViewById(R.id.SMS_CONTACTS_TEXT);
        EditText etsms = (EditText)findViewById(R.id.SMS_MESSAGE);
        mydata.message = etsms.getText().toString();
        mydata.msgNumber.removeAllElements();
        String arr[] = etcts.getText().toString().split("; ");
        for(String ct:arr)
    	{
        	mydata.msgNumber.add(ct);
    	}
        Toast.makeText(getBaseContext(), "INFORMATION SAVED", Toast.LENGTH_SHORT).show();
       //toast saved data!
	}  
	//This stores the result of settings page. Values get set here for our mydata.java class variables
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{  
		if (resultCode == RESULT_OK) {  
	        switch (requestCode) {  
	        case CONTACT_PICKER_RESULT:  
	        	Uri contactData = data.getData();
	        	String contactId = contactData.getLastPathSegment();
	        	Cursor cursor = getContentResolver().query(  
				        Phone.CONTENT_URI, null,  
				        Phone._ID + "=?", new String[]{contactId}, null);
	        	//startManagingCursor(cursor);
	        	 //Boolean numbersExist = cursor.moveToFirst(); 
	    	    System.out.println("3");
	        	cursor.moveToFirst();
	             String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(Phone.NUMBER)); 
	             TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
	             tv.setText(phoneNumber);
	             break;  
	        case SMS_CONTACT_PICKER_RESULT:  
	        	Uri contactData2 = data.getData();
	        	String contactId2 = contactData2.getLastPathSegment();
	        	Cursor cursor2 = getContentResolver().query(  
				        Phone.CONTENT_URI, null,  
				        Phone._ID + "=?", new String[]{contactId2}, null);
	        	cursor2.moveToFirst();
	            String phoneNumber2 = cursor2.getString(cursor2.getColumnIndexOrThrow(Phone.NUMBER)); 
	            EditText etcts = (EditText)findViewById(R.id.SMS_CONTACTS_TEXT);
	            if(etcts.getText().toString().contains(phoneNumber2))
	            {
	            	 Toast.makeText(getBaseContext(), etcts.getText().toString() + "Already in SMS Contacts", Toast.LENGTH_LONG).show();
	            }
	            else
	            {
	            	mydata.msgNumber.add(phoneNumber2);
	            	etcts.setText(phoneNumber2 + "; " + etcts.getText());
	            }
	            //TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
	            //tv.setText(phoneNumber);
	            
	        }
	  
	           
	    } else {  
	        // gracefully handle failure  
	       //Log.w(DEBUG_TAG, "Warning: activity result not ok");  
	    }  
	}  
	// When you press RESET Button then all elements get erased
	public void reset(View view)
	{
		EditText etcts = (EditText)findViewById(R.id.SMS_CONTACTS_TEXT);
        EditText etsms = (EditText)findViewById(R.id.SMS_MESSAGE);
        TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
		etcts.setText("");
		tv.setText("");
		etsms.setText("");
		mydata.message="";
		mydata.callNumber = "";
		mydata.msgNumber.removeAllElements();
		try
		{
			File cacheDir = getCacheDir();
			File file = new File(cacheDir.getAbsoluteFile(), "mydata.txt");
			file.delete();
		}
		catch(Exception e)
		{}
		Toast.makeText(getBaseContext(), "Information Reseted! Please enter new contacts", Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
