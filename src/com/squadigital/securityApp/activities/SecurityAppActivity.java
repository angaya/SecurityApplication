package com.squadigital.securityApp.activities;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.CheckConnection;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squaddigital.securityApp.util.SecurityAppUtil;


public class SecurityAppActivity extends BaseSecurityApp implements OnClickListener {
    
	private TextView tvforgotpwd;
	
	private EditText editTextUserName;
	private EditText editTextPassword;
	
	private Button btnregister;
	private Button btnsignin;
	
	private CheckBox chkbxrememberme;
	
	private Boolean rememberMe;
	
	private String username;
	private String password;
	private String userid;
	private String messageStatus;
	private String statusmsg;
	
	private String[] agencyId;
	private String[] listOfAgencies;
	
	public SharedPreferences loginpreferences;
	public SharedPreferences.Editor loginPrefsEditor;
	
	AgencyList agencyList;
	
	ProgressDialog progressDialog;
	BackgroundThread backgroundThread;
	RegistrationThread registrationThread;
	PlanDetailsThread planDetailsThread;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	   if(!isUserLoggedIn()){
    		   initializeGui();
    	   }
           
    }
    
    
    public void fetchXml(String username, String password ){
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			URL sourceUrl = new URL(Global.url +"clients/login/"+username+"/"+password);
			MyXMLHandler myXMLHandler = new MyXMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(sourceUrl.openStream()));
		} catch (Exception e) {
			System.out.println("XML Parsing Exception = " + e);
			
		}
	}
    
	private class CustomTextWatcher implements TextWatcher {
	    private EditText mUsername;
	    private EditText mPwd;

	    public CustomTextWatcher(EditText username, EditText pwd) {
	    	mUsername = username;
	    	mPwd = pwd;
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    }

		@Override
		public void afterTextChanged(Editable s) {
			Drawable button = getResources().getDrawable(R.layout.button);
			if(mUsername.getText().length() > 0 && mPwd.getText().length() > 0 ){
				btnsignin.setBackgroundDrawable(button);
				btnsignin.setEnabled(true);
			}
			else{
				btnsignin.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttondisabled1));
				btnsignin.setEnabled(false);
			}
		}
	}
	
	public void initializeGui(){
		 setContentView(R.layout.main);
		 
		 btnsignin = (Button)findViewById(R.id.buttonsignin);
		 chkbxrememberme = (CheckBox)findViewById(R.id.checkBox1);
		 btnregister = (Button)findViewById(R.id.buttonregister);
		 editTextUserName = (EditText)findViewById(R.id.editTextusername);
	     editTextPassword = (EditText)findViewById(R.id.editTextpassword);
	     tvforgotpwd = (TextView)findViewById(R.id.textViewforgotpwd);
	     
	     loginpreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
	     loginPrefsEditor = loginpreferences.edit();
	     
	     rememberMe = loginpreferences.getBoolean("rememberMe", false);
	     if(rememberMe == true){
	    	 editTextUserName.setText(loginpreferences.getString("username", "")); 
	    	 editTextPassword.setText(loginpreferences.getString("password", "")); 
	    	 chkbxrememberme.setChecked(true);
	    	 btnsignin.setBackgroundDrawable(getResources().getDrawable(R.layout.button));
	    	 btnsignin.setEnabled(true);
	     }
	     
	   
	     editTextUserName.addTextChangedListener(new CustomTextWatcher(editTextUserName,editTextPassword));
	     editTextPassword.addTextChangedListener(new CustomTextWatcher(editTextPassword,editTextUserName));

	       btnregister.setOnClickListener(this);
	       btnsignin.setOnClickListener(this);
	       tvforgotpwd.setOnClickListener(this);
	}
	
	public void getEditTextValues(){
		username = editTextUserName.getText().toString().trim();
        password = editTextPassword.getText().toString();
	}
	
	public void webServiceConnections(){
		fetchXml(this.username, this.password);
		agencyList = MyXMLHandler.agencyList;
		if(agencyList != null){
		String [] status , message, id;
		status = new String[agencyList.getStatus().size()];
		status = agencyList.getStatus().toArray(status);
		
			for(int i = 0; i < status.length; i++){
				statusmsg = status[i];
				if(status[i].equals("true")){  //Login successful
					fetchXml(this.username, this.password);
					agencyList = MyXMLHandler.agencyList;
					id = new String[agencyList.getId().size()];
					id = agencyList.getId().toArray(id);
					userid = id[i];
					savepreferences();
					startActivity(new Intent(getApplicationContext(), Home.class));
					finish();
				} 
				else if(status[i].equals("false")){ //Login failed
					fetchXml(this.username, this.password);
					agencyList = MyXMLHandler.agencyList;
					message = new String[agencyList.getMessage().size()];
					message = agencyList.getMessage().toArray(message);
					messageStatus = message[i];
				 }
			}
		} else{
			statusmsg = "false";
			messageStatus ="Please try again when you regain connectivity";
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
	
	private void noNewtworkConnection(String msg) {
		new AlertDialog.Builder(this)
        .setMessage(msg)
        .setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {                
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	((AlertDialog)dialog).getButton(which).setVisibility(View.GONE);
            	finish();
            }
        })
        .show(); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			startHelp();
			break;
		case R.id.agencies:
			setProgressBarIndeterminateVisibility(true);
		    progressDialog = ProgressDialog.show(SecurityAppActivity.this, "", "Please wait...");
		    planDetailsThread = new PlanDetailsThread();
		    planDetailsThread.start();
		break;
		}
		return true;
	}

	public void savepreferences(){
		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(SecurityAppActivity.this);
		SharedPreferences.Editor editor = sharedPreferencesUserId.edit();
		editor.putString("USERID",userid);
		editor.commit();
	}
	
	public boolean isUserLoggedIn() {  
		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(this);
		userid = sharedPreferencesUserId.getString("USERID", "null");
	        if(!userid.equals("null")){
	            Intent i = new Intent(this, Home.class);
	            startActivity(i);
	            finish();
	            return true;
	        }  else {
	        	  return false;
	        }
	      
	    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public class BackgroundThread extends Thread{
		@Override
		public void run() {
			 webServiceConnections();
			 handler.sendEmptyMessage(0); 
		}
		  }
	
	
	 public class PlanDetailsThread extends Thread{
			@Override
			public void run() {
				String [] logourl;
				SecurityAppUtil.fetchXml(Global.url + "plans/agencyList");
				agencyList = MyXMLHandler.agencyList;
				
				listOfAgencies = new String[agencyList.getAgencyname().size()];
				listOfAgencies = agencyList.getAgencyname().toArray(listOfAgencies);
				
				agencyId = new String[agencyList.getAgencyid().size()];
				agencyId = agencyList.getAgencyid().toArray(agencyId);
				
				logourl = new String[agencyList.getLogourl().size()];
				logourl = agencyList.getLogourl().toArray(logourl);
				
				Bundle bundle = new Bundle();
				bundle.putStringArray("agencyList", listOfAgencies);
				bundle.putStringArray("agencyId", agencyId);
				bundle.putStringArray("logourl", logourl);
				Intent intent = new Intent(SecurityAppActivity.this, PlanDetails.class);
				intent.putExtras(bundle);
				startActivity(intent);
				
				planDeatilsHandler.sendEmptyMessage(0);
			}
			  }
	
	
	 public class RegistrationThread extends Thread{
		@Override
		public void run() {
			SecurityAppUtil.fetchXml(Global.url +"plans/agencyList");
			agencyList = MyXMLHandler.agencyList;
			listOfAgencies = new String[agencyList.getAgencyname().size()];
			listOfAgencies = agencyList.getAgencyname().toArray(listOfAgencies);

			agencyId = new String[agencyList.getAgencyid().size()];
			agencyId = agencyList.getAgencyid().toArray(agencyId);
			
			Bundle bundle = new Bundle();
			bundle.putStringArray("agencyList", listOfAgencies);
			bundle.putStringArray("agencyId", agencyId);
			Intent intent = new Intent(SecurityAppActivity.this, Register.class);
			intent.putExtras(bundle);
			startActivity(intent);
			registerHandler.sendEmptyMessage(0);
		}
		  }
	 
	Handler planDeatilsHandler = new Handler(){
			 
			 @Override
				public void handleMessage(Message msg) {
				 progressDialog.hide();
				 progressDialog.dismiss();
				}
			 
		 };
	 
	 Handler registerHandler = new Handler(){
		 
		 @Override
			public void handleMessage(Message msg) {
			 progressDialog.hide();
			 progressDialog.dismiss();
			}
		 
	 };

		  Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
		 setProgressBarIndeterminateVisibility(false);
		 progressDialog.hide();
		 progressDialog.dismiss();
		 
		 if(statusmsg.equals("false")){
			 showMessage(messageStatus);
		 }
			
		}
		 
		  };

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.buttonregister:
				setProgressBarIndeterminateVisibility(true);
			    progressDialog = ProgressDialog.show(SecurityAppActivity.this,
			              "", "Please wait...");
			    registrationThread = new RegistrationThread();
			    registrationThread.start();
				break;
			case R.id.buttonsignin:
				if(!CheckConnection.isNetworkAvailable(getApplicationContext()) == true){
					noNewtworkConnection("Unable to connect. Please review your network settings.");
				} else{
					getEditTextValues();
					setProgressBarIndeterminateVisibility(true);
				    progressDialog = ProgressDialog.show(this, "", "Signing in...");
				    backgroundThread = new BackgroundThread();
				    backgroundThread.start();
				    if (chkbxrememberme.isChecked()) {
			            loginPrefsEditor.putBoolean("rememberMe", true);
			            loginPrefsEditor.putString("username", username);
			            loginPrefsEditor.putString("password", password);
			            loginPrefsEditor.commit();
			        } else {
			            loginPrefsEditor.clear();
			            loginPrefsEditor.commit();
			        }  
				}
				break;
			case R.id.textViewforgotpwd:
				Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.resetpassword);
				dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
				
				dialog.setCancelable(true);
				dialog.show();
				
			default:
				break;
			}
			
		}
}