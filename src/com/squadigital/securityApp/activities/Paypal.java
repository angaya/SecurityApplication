package com.squadigital.securityApp.activities;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squaddigital.securityApp.util.DateSlider;
import com.squaddigital.securityApp.util.MonthYearDateSlider;
import com.squadigital.securityApp.activities.SecurityAppActivity.BackgroundThread;

public class Paypal extends BaseSecurityApp implements OnClickListener{
	
	static final int MONTHYEARDATESELECTOR_ID = 3;
	
	private EditText editTextExpiryDate;
	private EditText editTextCardNo;
	private EditText editTextcvvNo;
	
	private Button btnpay;
	private Button btnback;
	
	private String[] statusList;
	private String[] messageList;
	private String agencyId;
	private String planId;
	private String username;
	private String phone;
	private String password;
	private String email;
	private String cardNumber;
	private String cvvNumber;
	private String month;
	private String price;
	private String year;
	private String message = "";
	
	private ProgressDialog progressDialog;
	private BackgroundThread backgroundThread;
	
	AgencyList agencyList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg);
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		editTextCardNo = (EditText)findViewById(R.id.editTextcardnumber);
		editTextcvvNo  = (EditText)findViewById(R.id.editTextcvvnumber);
		
		planId   = sharedPreferences.getString("PLANS", "null");
		agencyId = sharedPreferences.getString("AGENCY", "null");
		username = sharedPreferences.getString("USERNAME", "null");
		password = sharedPreferences.getString("PASSWORD", "null");
		email    = sharedPreferences.getString("EMAIL", "null");
		phone    = sharedPreferences.getString("PHONE", "null");
		price    = sharedPreferences.getString("PRICEDOLLAR", "null");
		
		btnpay  = (Button)findViewById(R.id.btnpay);
		btnback = (Button)findViewById(R.id.btnback);
		
		btnpay.setOnClickListener(this);
		btnback.setOnClickListener(this);
		
		editTextExpiryDate = (EditText)findViewById(R.id.editTextexpirydate);
		editTextExpiryDate.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					showDialog(MONTHYEARDATESELECTOR_ID);
				}
				return false;
			}
		});
		
		editTextExpiryDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)		
					showDialog(MONTHYEARDATESELECTOR_ID);
				else
					dismissDialog(MONTHYEARDATESELECTOR_ID);
			}
		});
		
	}
	
	 private DateSlider.OnDateSetListener mMonthYearSetListener =
	        new DateSlider.OnDateSetListener() {
	            public void onDateSet(DateSlider view, Calendar selectedDate) {
	            	// update the dateText view with the corresponding date
	               editTextExpiryDate.setText(String.format("%tB %tY", selectedDate, selectedDate));
	            }
	    };
	    
    public void fetchXml(String url){
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
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	// this method is called after invoking 'showDialog' for the first time
    	// here we initiate the corresponding DateSlideSelector and return the dialog to its caller

    	// get todays date and the time
        final Calendar c = Calendar.getInstance();
        switch (id) {
        case MONTHYEARDATESELECTOR_ID:
            return new MonthYearDateSlider(this,mMonthYearSetListener,c);
        }
        return null;
    }
	    
    public String formateMonth(String month) {
    	Date date;
    	String myDate = null;
		try {
			date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
			String strDateFormat = "MM";
		 	SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		 	myDate =sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return myDate;
	}
	    
    public String monthSplit(String date){
		String words[] = date.split(" ");
		return words[0];
    }
    
    public String yearSplit(String date){
		String words[] = date.split(" ");
		String lastword = words[words.length - 1]; 
		return lastword;
    }
	    
    public void setValues(){
    	cardNumber = editTextCardNo.getText().toString();
    	cvvNumber  = editTextcvvNo.getText().toString();
    	month = editTextExpiryDate.getText().toString();
    	month = formateMonth(monthSplit(month));
    	year = yearSplit(editTextExpiryDate.getText().toString());
    }
	    
    public void showOutCome(){
    	setValues();
    	String link = Global.url +"clients/payRegistration/"+username+"/"+password+"/"+email+"/"+phone+"/"+agencyId+"/"+planId+"/"+cardNumber+"/"+cvvNumber+"/"+month+"/"+year+"/"+price;
    	fetchXml(link);
    	agencyList = MyXMLHandler.agencyList;
    	messageList = new String[agencyList.getMessage().size()];
    	messageList = agencyList.getMessage().toArray(messageList);
    	statusList = new String[agencyList.getStatus().size()];
    	statusList = agencyList.getStatus().toArray(statusList);
    	for(int i = 0; i < messageList.length; i++){
    		message = messageList[i];
    	}
    	for(int i = 0; i < statusList.length; i++){
    		String status = statusList[i];
    		if(status.equalsIgnoreCase("true"))
    			startActivity(new Intent(Paypal.this, SuccessfulRegistration.class));
    	}
    	
    }
    private boolean validation(){
		  if(cardNumber.equals(""))
			{
		       showMessage("Please fill card Number!");
			   return false;
			}else if (cvvNumber.equals("")) {
				 showMessage("Please fill cvv Number!");
				 return false;
			}else if (editTextExpiryDate.getText().toString().equals("")) {
				 showMessage("Please Select Expiry date!");
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
    
    public class BackgroundThread extends Thread{
		@Override
		public void run() {
			 showOutCome();
			 handler.sendEmptyMessage(0); 
		}
		  }
	 Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
			 setProgressBarIndeterminateVisibility(false);
			 progressDialog.dismiss();
			 new AlertDialog.Builder(Paypal.this)
		        .setMessage(message)
		        .setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {                
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	((AlertDialog)dialog).getButton(which).setVisibility(View.GONE);
		            }
		        })
		        .show();
			}
			  };

	@Override
	public void onClick(View arg) {
		switch (arg.getId()) {
		case R.id.btnpay:
			setValues();
			if(validation() == true)
				progressDialog = ProgressDialog.show(Paypal.this,
			              "", "Loading...");
			    backgroundThread = new BackgroundThread();
			    backgroundThread.start();
			break;
		case R.id.btnback:
				finish();	
			break;
		default:
			break;
		}
		
	}
			  
}
