package com.squadigital.securityApp.activities;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squadigital.securityApp.activities.SecurityAppActivity.BackgroundThread;

public class SecurityDetails extends BaseSecurityApp implements
		OnClickListener, RadioGroup.OnCheckedChangeListener {

	private static String MONTHS = " months";
	
	private Button btnback;
	private Button btnapplynow;
	private RadioGroup radioPlanGroup;
	private RadioButton radioPlanButton;

	private ImageView imageView;

	private TextView tvContactPerson;
	private TextView tvEmail;
	private TextView tvStreet1;
	private TextView tvPhone;
	private TextView tvPostal;
	private TextView tvPlanName1;
	private TextView tvPlanName2;
	private TextView tvPlanName3;
	private TextView tvPrice1;
	private TextView tvPrice2;
	private TextView tvPrice3;
	private TextView tvPeriod1;
	private TextView tvPeriod2;
	private TextView tvPeriod3;

	private String agencyId;
	private String id;
	private String email;
	private String contactperson;
	private String street1;
	private String street2;
	private String postalcode;
	private String city;
	private String country;
	private String phone1;
	private String phone2;
	private String emergencynumber;
	private String logourl;
	private String planName1;
	private String planName2;
	private String planName3;
	private String price1;
	private String price2;
	private String price3;
	private String period1;
	private String period2;
	private String period3;
	private String selectedPlan;

	AgencyList agencyList = null;
	
	ProgressDialog progressDialog;
	BackgroundThread backgroundThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferencesAgencyId = PreferenceManager
				.getDefaultSharedPreferences(this);
		agencyId = sharedPreferencesAgencyId.getString("AGENCYID", "null");
		id = sharedPreferencesAgencyId.getString("ID", "null");
		logourl = sharedPreferencesAgencyId.getString("LOGOURL", logourl);
		if (agencyId != null) {
			if (agencyId.equals("4")) {
				setContentView(R.layout.detailsecurity2);
				setupComponents();
				connectWebserviceDetails();
				connectWebservicePlan();
				initializeValues();

			} else if (agencyId.equals("3")) {
				setContentView(R.layout.detailsecurity);
				setupComponents();
				connectWebserviceDetails();
				connectWebservicePlan();
				initialiseMinValue();
			}
		}

		setupComponents();
		if (logourl != null)
			drawImage(logourl, imageView);
		btnback = (Button) findViewById(R.id.button1);
		btnapplynow = (Button) findViewById(R.id.button2);

		btnapplynow.setOnClickListener(this);
		btnback.setOnClickListener(this);

	}

	private void initialiseMinValue() {
		tvContactPerson.setText(contactperson);
		tvEmail.setText(email);
		tvStreet1.setText(street1);
		tvPhone.setText(phone1);
		tvPostal.setText(postalcode);
		tvPlanName1.setText(planName1);
		tvPlanName2.setText(planName2);
		tvPrice1.setText(price1);
		tvPrice2.setText(price2);
		tvPeriod1.setText(period1);
		tvPeriod2.setText(period2);
	}

	private void initializeValues() {
		tvContactPerson.setText(contactperson);
		tvEmail.setText(email);
		tvStreet1.setText(street1);
		tvPhone.setText(phone1);
		tvPostal.setText(postalcode);
		tvPlanName1.setText(planName1);
		tvPlanName2.setText(planName2);
		tvPlanName3.setText(planName3);
		tvPrice1.setText(price1);
		tvPrice2.setText(price2);
		tvPrice3.setText(price3);
		tvPeriod1.setText(period1);
		tvPeriod2.setText(period2);
		tvPeriod3.setText(period3);
	}

	private void setupComponents() {
		imageView = (ImageView) findViewById(R.id.imageView1);
		tvContactPerson = (TextView) findViewById(R.id.textView2);
		tvPlanName1 = (TextView) findViewById(R.id.textView10);
		tvEmail = (TextView) findViewById(R.id.textView3);
		tvStreet1 = (TextView) findViewById(R.id.textView4);
		tvPhone = (TextView) findViewById(R.id.textView7);
		tvPostal = (TextView) findViewById(R.id.textView5);
		tvPlanName2 = (TextView) findViewById(R.id.textView11);
		tvPlanName3 = (TextView) findViewById(R.id.textView1);
		tvPrice1 = (TextView) findViewById(R.id.textView12);
		tvPrice2 = (TextView) findViewById(R.id.textView13);
		tvPrice3 = (TextView) findViewById(R.id.textView6);
		tvPeriod1 = (TextView) findViewById(R.id.textView14);
		tvPeriod2 = (TextView) findViewById(R.id.textView15);
		tvPeriod3 = (TextView) findViewById(R.id.textView8);

		radioPlanGroup = (RadioGroup) findViewById(R.id.radioPlanGroup);

		imageView = (ImageView) findViewById(R.id.imageViewLogo);
	}

	public void fetchXml(String url) {
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

	public void connectWebserviceDetails() {
		fetchXml(Global.url + "plans/agencyDetail/" + id);
		agencyList = MyXMLHandler.agencyList;
		for (int i = 0; i < agencyList.getId().size(); i++) {
			email = agencyList.getEmail().get(i);
			contactperson = agencyList.getContactperson();
			street1 = agencyList.getStreet1().get(i);
			street2 = agencyList.getStreet2().get(i);
			postalcode = agencyList.getPostalcode().get(i);
			city = agencyList.getCity().get(i);
			country = agencyList.getCountry().get(i);
			phone1 = agencyList.getPhone1().get(i);
			phone2 = agencyList.getPhone2().get(i);
			emergencynumber = agencyList.getEmergencynumber().get(i);
		}
	}

	public void connectWebservicePlan() {
		fetchXml(Global.url + "plans/agencyPlan/" + agencyId);
		agencyList = MyXMLHandler.agencyList;
		for (int i = 0; i < agencyList.getId().size(); i++) {
			if (agencyList.getId().size() == 2) {
				planName1 = agencyList.getPlanname().get(0);
				price1 = agencyList.getPrice().get(0);
				period1 = agencyList.getPeriod().get(0) + MONTHS;
				planName2 = agencyList.getPlanname().get(1);
				price2 = agencyList.getPrice().get(1);
				period2 = agencyList.getPeriod().get(1)+ MONTHS;
			} else if (agencyList.getId().size() == 3) {
				planName1 = agencyList.getPlanname().get(0);
				price1 = agencyList.getPrice().get(0);
				period1 = agencyList.getPeriod().get(0) + MONTHS;
				planName2 = agencyList.getPlanname().get(1);
				planName3 = agencyList.getPlanname().get(2);
				price2 = agencyList.getPrice().get(1);
				period2 = agencyList.getPeriod().get(1) + MONTHS;
				price3 = agencyList.getPrice().get(2);
				period3 = agencyList.getPeriod().get(2) + MONTHS;
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button2:
			 progressDialog = ProgressDialog.show(SecurityDetails.this,
		              "", "Loading...");
		    backgroundThread = new BackgroundThread();
		    backgroundThread.start();
			
			break;
		case R.id.button1:
			finish();
			break;
		default:
			break;
		}

	}

	public String selectedPlan() {

		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		arg1 = arg0.getCheckedRadioButtonId();
		// if(arg1 == R.id.radioPlan1){
		//
		// }

	}

//	public void savepreferences() {
//		SharedPreferences sharedPreferencesSelectedPlan = PreferenceManager
//				.getDefaultSharedPreferences(SecurityDetails.this);
//		SharedPreferences.Editor editor = sharedPreferencesSelectedPlan.edit();
//		editor.putString("SelectedPlan", selectedPlan);
//		
//		editor.commit();
//	}

	private void drawImage(String url, ImageView imageView) {
		try {
			URL myUrl;
			myUrl = new URL(url);
			Drawable myImage = Drawable.createFromStream(myUrl.openStream(),
					"src");
			imageView.setImageDrawable(myImage);
			imageView.setMinimumHeight(40);
			imageView.setMinimumWidth(20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class BackgroundThread extends Thread{
		@Override
		public void run() {
		{
		int selectedId = radioPlanGroup.indexOfChild(findViewById(radioPlanGroup.getCheckedRadioButtonId()));
		fetchXml(Global.url + "plans/agencyPlan/" + agencyId);
		agencyList = MyXMLHandler.agencyList;
		int size = agencyList.getId().size();
		for (int i = 0; i < size; i++) {
			if (selectedId == i) {
				selectedPlan = agencyList.getPlanname().get(i);
			}
		}
//		savepreferences();
		Intent intent = new Intent(getApplicationContext(), Register.class);
		Bundle bundle = new Bundle();
		bundle.putString("SelectedPlan", selectedPlan);
		intent.putExtras(bundle);
		startActivity(intent);
			}
			 handler.sendEmptyMessage(0); 
		}
		  }
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
		 setProgressBarIndeterminateVisibility(false);
		 progressDialog.hide();
		 progressDialog.dismiss();
		 
		}
		 
		  };

}
