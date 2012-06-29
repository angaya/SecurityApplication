package com.squadigital.securityApp.activities;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EmergencyContacts extends BaseSecurityApp implements OnClickListener {
	
	private EditText editTextName;
	private EditText editTextEmail;
	private EditText editTextPhone;
	
	private Button btnAdd;
	private Button btnBack;
	
	private String userId;
	private String name; 
	private String email;
	private String phone;
	private String message;
	private String count;
	
	private String[] messageList;
	private String[] statusList;
	private String[] countList;
	
	AgencyList agencyList = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addemergencycontacts);
		
		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(this);
		userId = sharedPreferencesUserId.getString("USERID", "null");
		
		editTextName  = (EditText)findViewById(R.id.editTextName);
		editTextEmail = (EditText)findViewById(R.id.editTextEmail);
		editTextPhone = (EditText)findViewById(R.id.editTextPhone);
		
		btnAdd  = (Button)findViewById(R.id.buttonAdd);
		btnBack = (Button)findViewById(R.id.buttonBack);
		btnAdd.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonAdd){
			 name  = editTextName.getText().toString();
			 email = editTextEmail.getText().toString();
			 phone = editTextPhone.getText().toString();
			 if(validation() == true)
			 connectWebservice();
		} else if(v.getId() == R.id.buttonBack ){
			finish();
		}
		
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
			
			showMessage(message); //print here the message
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
	
	private boolean validation(){
		  if(name.toString().equals(""))
			{
			  showMessage("Please fill Name!");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_complete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			startActivity(new Intent(getApplicationContext(), Home.class));
			break;
		case R.id.account:
			break;
		case R.id.profile:
			startActivity(new Intent(getApplicationContext(), ManageProfile.class));
			break;
		case R.id.help:
			startHelp();
			break;
		case R.id.logout:
			break;
		}
		return true; 
	}
	
	

}
