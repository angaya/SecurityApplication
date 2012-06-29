package com.squadigital.securityApp.activities;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squadigital.securityApp.activities.SecurityAppActivity.PlanDetailsThread;

public class ProfileContacts extends Activity implements OnClickListener {

	private Button btnarrowdown;
	private Button btncontactfive;
	
	private LinearLayout linearLayoutViewdashboard;
	private LinearLayout linearLayoutSettings;
	private LinearLayout linearLayout;
	
	ProgressDialog progressDialog;
	
	private String contactList[];
	private String idList[];
	
	private String userId;
	private String name; 
	private String email;
	private String phone;
	private String message;
	private String count;
	
	private String[] messageList;
	private String[] statusList;
	private String[] countList;
	
	AddContactsThread addContactsThread;
	
	AgencyList agencyList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_detail);
		
		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(this);
		userId = sharedPreferencesUserId.getString("USERID", "null");
		
		linearLayoutViewdashboard = (LinearLayout)findViewById(R.id.linearLayoutdashboard);
		linearLayoutSettings = (LinearLayout)findViewById(R.id.linearLayoutsettings);
		btnarrowdown = (Button) findViewById(R.id.btnarrowdown);
		btncontactfive = (Button)findViewById(R.id.btncontactfive);
		
		
		btncontactfive.setOnClickListener(this);
		
		linearLayoutViewdashboard.setOnClickListener(this);
		linearLayoutSettings.setOnClickListener(this);
		btnarrowdown.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearLayoutdashboard:
			startActivity(new Intent(getApplicationContext(), Home.class));
			break;
		case R.id.btncontactfive:
			addContact();
			break;
		case R.id.linearLayoutsettings:
			startActivity(new Intent(getApplicationContext(), Settings.class));
			break;
		case R.id.btnarrowdown:
			startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
			break;

		default:
			break;
		}
	}
	
	public void addContact(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		final EditText username = new EditText(this);
		final EditText emailaddress = new EditText(this);
		final EditText phonenumber = new EditText(this);
		emailaddress.setHint("Email");
		username.setHint("User name");
		phonenumber.setHint("Phone number");
		emailaddress.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		username.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		phonenumber.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_CLASS_PHONE);
		linearLayout.addView(username, layoutParams);
		linearLayout.addView(emailaddress, layoutParams);
		linearLayout.addView(phonenumber, layoutParams);
		alert.setView(linearLayout);
		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				name = username.getText().toString();
				email = emailaddress.getText().toString();
				phone = phonenumber.getText().toString();
				if(validation()){
					setProgressBarIndeterminateVisibility(true);
					progressDialog = ProgressDialog.show(ProfileContacts.this, "", "Please wait...");
					addContactsThread = new AddContactsThread();
					addContactsThread.start();
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
		
	}
	
	private boolean validation(){
			if(name.toString().equals(""))
			{
			  showMessage("Please fill the fields!");
			   return false;
			}else if (email.toString().equals("")) {
				showMessage("Please fill Email Address!");
				 return false;
			}else if (phone.toString().equals("")) {
				showMessage("Please fill phone!");
				 return false;
			}
			
		  else {
				return true;
			}	
	}
	
	private void showMessage(String msg) {
		new AlertDialog.Builder(this)
        .setMessage(msg)
        .setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {                
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	((AlertDialog)dialog).getButton(which).setVisibility(View.GONE);
            	
            }
        })
        .show(); 
	}
	
	public void fetchXml(String url ){
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			URL sourceUrl = new URL(url);
			MyXMLHandler myXMLHandler = new MyXMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(sourceUrl.openStream()));
		} catch (Exception e) {
			System.out.println("XML Parsing Exception = " + e);
		}
	}
	
	public void connectWebservice(){
		fetchXml(Global.url +"clients/saveClientContact/"+userId+"/"+name+"/"+email+"/"+phone);
		agencyList = MyXMLHandler.agencyList;
		statusList = new String[agencyList.getStatus().size()];
		statusList = agencyList.getStatus().toArray(statusList);
		messageList = new String[agencyList.getMessage().size()];
		messageList = agencyList.getMessage().toArray(messageList);
		countList = new String[agencyList.getCount().size()];
		countList = agencyList.getCount().toArray(countList);
		for(int i = 0; i < statusList.length; i++){
			String status = statusList[i];
			if(status.equals("false")){
			 message = messageList[i]; //only show the message.
			} else if(status.equals("true")){
				count = countList[i];
				message = messageList[i];
				if(count.equals("5"))
			    message = messageList[i];
			}
			
		}
//		startActivity(new Intent(getApplicationContext(), Account.class));
		
	}
	
	public class AddContactsThread extends Thread{
		@Override
		public void run() {
			connectWebservice();
			handler.sendEmptyMessage(0);
			
		}
		  }
	
	Handler handler = new Handler(){
		 
		 @Override
			public void handleMessage(Message msg) {
			 progressDialog.hide();
			 progressDialog.dismiss();
			 showMessage(message); 
			}
		 
	 };

}
