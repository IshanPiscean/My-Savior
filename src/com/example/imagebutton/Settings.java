package com.example.imagebutton;

import com.example.imagebutton.R;
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
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone; 

public class Settings extends Activity {
        
	private String contactId; // Unique Contact ID 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		try{
		if(!mydata.callNumber.equals(""))
		{
			TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
	        tv.setText(mydata.callNumber);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
		 if(mydata.msgNumber.size()!=0)
			{
				EditText etcts = (EditText)findViewById(R.id.SMS_CONTACTS_TEXT);
		        etcts.setText("");
				for(Object ob:mydata.msgNumber.toArray())
		        {
					etcts.setText(etcts.getText()+ob.toString()+"; ");
		        }
		    }
		}
		catch(Exception e)
		 {
		 e.printStackTrace();
	     }
		try{
			if(!mydata.message.equals(""))
		{
			EditText etsms = (EditText)findViewById(R.id.SMS_MESSAGE);
	        etsms.setText(mydata.message);
	    }		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//CONTACT PICKER FOR EMERGENCY CONTACT
	private static final int CONTACT_PICKER_RESULT = 1;  
	public void doLaunchContactPicker(View view) {  
		System.out.println("inside emergency picker block!");
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
		
		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
		} 
	
	//CONTACT PICKER FOR SMS CONTACTS
	private static final int SMS_CONTACT_PICKER_RESULT = 2;  
	public void launchSmsContactPicker(View view) {  
		System.out.println("inside sms contact picker block!");
	    Intent contactPickerIntent2 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); //*CHANGE* to  CONTENT_LOOKUP_URI if above it works fine
		
	    startActivityForResult(contactPickerIntent2, SMS_CONTACT_PICKER_RESULT);  
	}  
  
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){  
		
	 if(resultCode == RESULT_OK){  
	   switch(requestCode){  
	     case(CONTACT_PICKER_RESULT):
    	 	Uri uriContact = data.getData();   //Returns a URI for intent
	        String contactNumber = null;      // Stores telephone number
     		     // contact's unique ID  
     		//System.out.println("Contact ID inside CONTACT_PICKER_RESULT: "+ contactId);
        	//Cursor cursor1 = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactId}, null); 
            //Returns a cursor obj ith only ID column
	     	Cursor cursorID = getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID},null,null,null); 
        	if(cursorID.moveToFirst())
        	{
        		contactId = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        	}
        	cursorID.close();  //Close cursor for contact ID
        	
        	// Use above contact ID now we will get associated phone number 
        	Cursor cursorPhoneNum = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, 
        			ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",new String[]{contactId},null);  // Inside query previous contactId is matched with CONTACT_ID and telephone number(NUMBER) is displayed
        	//Move the phone cursor to the first row as a person might have multiple contact numbers
        	if(cursorPhoneNum.moveToFirst()){
        		contactNumber = cursorPhoneNum.getString(cursorPhoneNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        		TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
		        tv.setText(contactNumber);
        	}
        	System.out.println("Contact No:"+ contactNumber);
        	// Close cursor for telephone number
        	cursorPhoneNum.close();	
        	break;
//        	String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//        	int hasNumber = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex( HAS_PHONE_NUMBER )));
//      
//        	//cursor = getContentResolver().query(contactData, null, null, null, null);
//        	if(hasNumber > 0){	
//		    		//Cursor cursor2 = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactId}, null);
//		    	while(cursor1.moveToFirst()){
//			         //String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(Phone.NUMBER)); 
//			    	 String emergencyCallNumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//			    		
//			         TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
//			         tv.setText(emergencyCallNumber);
//		    	 }
//		    	}
	       //break;  
        case(SMS_CONTACT_PICKER_RESULT): 
        	//Cursor cursor2 = null;
        	Uri contactData2 = data.getData();
        	String contactId2 = contactData2.getLastPathSegment();
	        	Cursor cursor2 = getContentResolver().query(Phone.CONTENT_URI, null, Phone._ID + "=?", new String[]{contactId2}, null);
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
	          TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
	          tv.setText(phoneNumber2);
	            
	        }
	  
	           
	    } 
		else {  
	        // gracefully handle failure  
	       //Log.w(DEBUG_TAG, "Warning: activity result not ok");  
	    }  
	 }

	
	
	//This stores the result of settings page. Values get set here for our mydata.java class variables
	public void callSave(View view) {  
		TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
        mydata.callNumber = tv.getText().toString();
        EditText etcts = (EditText)findViewById(R.id.SMS_CONTACTS_TEXT);
        EditText etsms = (EditText)findViewById(R.id.SMS_MESSAGE);
        mydata.message = etsms.getText().toString();
        //mydata.msgNumber.clear();
        //mydata.msgNumber.removeAllElements();
        String arr[] = etcts.getText().toString().split("; ");
        for(String ct:arr)
    	{
        	mydata.msgNumber.add(ct);
    	}
        Toast.makeText(getBaseContext(), "INFORMATION SAVED", Toast.LENGTH_SHORT).show();
       
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
		mydata.msgNumber.clear();
		//mydata.msgNumber.removeAllElements();
//		try
//		{
//			File cacheDir = getCacheDir();
//			File file = new File(cacheDir.getAbsoluteFile(), "mydata.txt");
//			file.delete();
//		}
//		catch(Exception e)
//		{}
		Toast.makeText(getBaseContext(), "Information Reseted! Please enter new contacts", Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
