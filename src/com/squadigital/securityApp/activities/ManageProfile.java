package com.squadigital.securityApp.activities;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;

public class ManageProfile extends BaseSecurityApp  {
	
	private EditText editTextFirstname;
	private EditText editTextMiddlename;
	private EditText editTextLastname;
	private EditText editTextCity;
	private EditText editTextStreet1;
	private EditText editTextStreet2;
	
	private Spinner spinnerCountry;
	
	private String[] countryList = {"Kenya", "Ghana","Nigeria","Uganda","Tanzania"};
	private String[] messageList;
	
	private Button btnUpdate;
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String street1;
	private String street2;
	private String userId;
	private String country;
	private String city;
	private String message;
	
	AgencyList agencyList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manageprofile);
		
		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(this);
		userId = sharedPreferencesUserId.getString("USERID", "null");
		
		editTextFirstname  = (EditText)findViewById(R.id.editTextFName);
		editTextMiddlename = (EditText)findViewById(R.id.editTextMiddleName);
		editTextCity       = (EditText)findViewById(R.id.editTextCity);
		editTextLastname   = (EditText)findViewById(R.id.editTextLastName);
		editTextStreet1    = (EditText)findViewById(R.id.editTextStreet1);
		editTextStreet2    = (EditText)findViewById(R.id.editTextStreet2);
		
		spinnerCountry     =(Spinner)findViewById(R.id.spinner1);
		
		spinnerCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(spinnerCountry.getSelectedItem().toString().equalsIgnoreCase("Kenya"))
					country = "110";
				else if(spinnerCountry.getSelectedItem().toString().equalsIgnoreCase("Ghana"))
					country = "82";
				else if(spinnerCountry.getSelectedItem().toString().equalsIgnoreCase("Nigeria"))
					country = "156";
				else if(spinnerCountry.getSelectedItem().toString().equalsIgnoreCase("Uganda"))
					country = "219";
				else if(spinnerCountry.getSelectedItem().toString().equalsIgnoreCase("Tanzania"))
					country = "208";
					
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		ArrayAdapter<String> countryListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,countryList);
		countryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCountry.setAdapter(countryListAdapter);
		
		btnUpdate  =(Button)findViewById(R.id.buttonUpdate);
		btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.buttonUpdate){
					firstName  = editTextFirstname.getText().toString();
					middleName = editTextMiddlename.getText().toString();
					lastName   = editTextLastname.getText().toString();
					street1    = editTextStreet1.getText().toString();
					street2    = editTextStreet2.getText().toString();
					city	   = editTextCity.getText().toString();
					if(middleName.equals(""))
						middleName = null;
					if(street2.equals(""))
						street2 = null;
					if(validation() == true){
						connectWebservice();
					}
				}
			}
		});
	}
	
	private void fetchXml(String url ){
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
	
	private void connectWebservice(){
		fetchXml(Global.url + "clients/saveClientDetail/"+userId+"/"+firstName+
				"/"+middleName+"/"+lastName+"/"+city+"/"+country+"/"+street1+"/"+street2);
		agencyList = MyXMLHandler.agencyList;
		messageList = new String[agencyList.getMessage().size()];
		messageList = agencyList.getMessage().toArray(messageList);
		for(int i = 0; i < messageList.length; i++){
			message = messageList[i];
		}
		showMessage(message);
	}
	
	private void showMessage(String msg) {
		new AlertDialog.Builder(this)
        .setMessage(msg)
        .setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {                
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	((AlertDialog)dialog).getButton(which).setVisibility(View.GONE);
            	startActivity(new Intent(getApplicationContext(), Home.class));
            }
        })
        .show(); 
	}

	 private boolean validation(){
		  if(firstName.toString().equals(""))
			{
			  showMessageToast("Please fill First Name!");
			   return false;
			}else if (lastName.toString().equals("")) {
				showMessageToast("Please fill Last Name!");
				 return false;
			}else if (city.toString().equals("")) {
				showMessageToast("Please fill City!");
				 return false;
			}else if (street1.toString().equals("")) {
				showMessageToast("Please fill Street1!");
				 return false;
			}
		  else {
				return true;
			}	
	    
	}
	 private void showMessageToast(String msg) {
			Toast toast=Toast.makeText(ManageProfile.this, msg, Toast.LENGTH_SHORT);
	        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
	        toast.show();
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuprofile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.account:
//			startActivity(new Intent(getApplicationContext(), Account.class));
			break;
		case R.id.home:
			startActivity(new Intent(getApplicationContext(), Home.class));
			break;
		case R.id.help:
			startHelp();
			break;
		case R.id.logout:
			showMessage();
			break;
		}
		return true; 
	}
	
	

}
