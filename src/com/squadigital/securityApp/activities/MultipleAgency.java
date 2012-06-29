package com.squadigital.securityApp.activities;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;

public class MultipleAgency extends BaseSecurityApp implements
		View.OnClickListener {

	private Button btnBack;
	private Button btnApply;

	private RadioGroup radioPlanGroup;

	private ImageView imageViewAgencyFirst;
	private ImageView imageViewAgencySecond;

	private TextView tvAgencyOnePlanOne;
	private TextView tvAgencyOnePlanTwo;
	private TextView tvAgencyOnePlanThree;
	private TextView tvAgencyTwoPlanOne;
	private TextView tvAgencyTwoPlanTwo;
	private TextView tvAgencyTwoPlanThree;
	private TextView tvAgencyOnePriceOne;
	private TextView tvAgencyOnePriceTwo;
	private TextView tvAgencyOnePriceThree;
	private TextView tvAgencyTwoPriceOne;
	private TextView tvAgencyTwoPriceTwo;
	private TextView tvAgencyTwoPriceThree;
	private TextView tvAgencyOnePeriodOne;
	private TextView tvAgencyOnePeriodTwo;
	private TextView tvAgencyOnePeriodThree;
	private TextView tvAgencyTwoPeriodOne;
	private TextView tvAgencyTwoPeriodTwo;
	private TextView tvAgencyTwoPeriodThree;

	private String url;
	private String agencyId;
	private String selectedPlan;
	private String firstAgencyPlanName;
	private String secondAgencyPlanName;
	private String agencyName;
	AgencyList agencyList = null;

	ProgressDialog progressDialog;
	ApplyThread applyThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multipleagency);

		SharedPreferences sharedPreferencesAgencyId = PreferenceManager
				.getDefaultSharedPreferences(this);
		agencyId = sharedPreferencesAgencyId.getString("AGENCYID", "null");

		initializeUI();

		btnBack = (Button) findViewById(R.id.btnBack);
		btnApply = (Button) findViewById(R.id.btnApply);
		btnBack.setOnClickListener(this);
		btnApply.setOnClickListener(this);

		url = Global.url + "plans/comparePlan/3/4";
		urlconnection(url);
		agencyList = MyXMLHandler.agencyList;
		if (getIntent().hasExtra("BitmapImageOne") && getIntent().hasExtra("BitmapImageTwo")) {
			Bitmap bitmapFirst = (Bitmap) getIntent().getParcelableExtra("BitmapImageOne");
			Bitmap bitmapSecond = (Bitmap) getIntent().getParcelableExtra("BitmapImageTwo");
			imageViewAgencyFirst.setImageBitmap(bitmapFirst);
			imageViewAgencySecond.setImageBitmap(bitmapSecond);
		}
		labelUI();
	}

	public void urlconnection(String url) {
		try {
			URL sourceUrl = new URL(url);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			MyXMLHandler myXMLHandler = new MyXMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(sourceUrl.openStream()));
		} catch (Exception e) {
			System.out.println("XML parsing Excpetion = " + e);
		}

	}

	public void initializeUI() {
		tvAgencyOnePlanOne = (TextView) findViewById(R.id.textView20);
		tvAgencyOnePlanTwo = (TextView) findViewById(R.id.textView21);
		tvAgencyOnePlanThree = (TextView) findViewById(R.id.textView22);
		tvAgencyTwoPlanOne = (TextView) findViewById(R.id.textView23);
		tvAgencyTwoPlanTwo = (TextView) findViewById(R.id.textView24);
		tvAgencyTwoPlanThree = (TextView) findViewById(R.id.textView26);
		tvAgencyOnePriceOne = (TextView) findViewById(R.id.textView27);
		tvAgencyOnePriceTwo = (TextView) findViewById(R.id.textView28);
		tvAgencyOnePriceThree = (TextView) findViewById(R.id.textView29);
		tvAgencyTwoPriceOne = (TextView) findViewById(R.id.textView30);
		tvAgencyTwoPriceTwo = (TextView) findViewById(R.id.textView31);
		tvAgencyTwoPriceThree = (TextView) findViewById(R.id.textView32);
		tvAgencyOnePeriodOne = (TextView) findViewById(R.id.textView34);
		tvAgencyOnePeriodTwo = (TextView) findViewById(R.id.textView35);
		tvAgencyOnePeriodThree = (TextView) findViewById(R.id.textView36);
		tvAgencyTwoPeriodOne = (TextView) findViewById(R.id.textView37);
		tvAgencyTwoPeriodTwo = (TextView) findViewById(R.id.textView38);
		tvAgencyTwoPeriodThree = (TextView) findViewById(R.id.textView39);

		imageViewAgencyFirst = (ImageView) findViewById(R.id.imageViewAgencyFirst);
		imageViewAgencySecond = (ImageView) findViewById(R.id.imageViewAgencySecond);

		radioPlanGroup = (RadioGroup) findViewById(R.id.radioGroup);
	}

	public void labelUI() {
		tvAgencyOnePlanOne.setText(agencyList.getPlanname().get(0));
		tvAgencyOnePlanTwo.setText(agencyList.getPlanname().get(1));
		tvAgencyTwoPlanOne.setText(agencyList.getPlanname().get(2));
		tvAgencyTwoPlanTwo.setText(agencyList.getPlanname().get(3));
		tvAgencyTwoPlanThree.setText(agencyList.getPlanname().get(4));
		tvAgencyOnePriceOne.setText(agencyList.getPrice().get(0));
		tvAgencyOnePriceTwo.setText(agencyList.getPrice().get(1));
		tvAgencyTwoPriceOne.setText(agencyList.getPrice().get(2));
		tvAgencyTwoPriceTwo.setText(agencyList.getPrice().get(3));
		tvAgencyTwoPriceThree.setText(agencyList.getPrice().get(4));
		tvAgencyOnePeriodOne.setText(agencyList.getPeriod().get(0));
		tvAgencyOnePeriodTwo.setText(agencyList.getPeriod().get(1));
		tvAgencyTwoPeriodOne.setText(agencyList.getPeriod().get(2));
		tvAgencyTwoPeriodTwo.setText(agencyList.getPeriod().get(3));
		tvAgencyTwoPeriodThree.setText(agencyList.getPeriod().get(4));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnApply:
			progressDialog = ProgressDialog.show(MultipleAgency.this, "",
					"Loading...");
			applyThread = new ApplyThread();
			applyThread.start();

			break;
		default:
			break;
		}
	}

	private class ApplyThread extends Thread {
		@Override
		public void run() {
			url = Global.url + "plans/comparePlan/3/4";
			urlconnection(url);
			agencyList = MyXMLHandler.agencyList;
			String agencyName = null;
			String planName = null;
			int selectedId;
				for (int i = 0; i < agencyList.getAgencyname().size(); i++) {
					selectedId = radioPlanGroup.indexOfChild(findViewById(radioPlanGroup.getCheckedRadioButtonId()));
					if (selectedId == (i)) {
						agencyName = agencyList.getAgencyname().get(i);
						planName = agencyList.getPlanname().get(i);
					}

				}
				if(agencyName != null && agencyId != null){
					Intent intent = new Intent(MultipleAgency.this, Register.class);
					Bundle bundle = new Bundle();
					bundle.putString("agencyname", agencyName);
					bundle.putString("planname", planName);
					intent.putExtras(bundle);
					startActivity(intent);
					
				}
				
			
			handler.sendEmptyMessage(0);
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			setProgressBarIndeterminateVisibility(false);
			progressDialog.hide();
			progressDialog.dismiss();

		}

	};

	public void savepreferences() {
		SharedPreferences sharedPreferencesSelectedPlan = PreferenceManager
				.getDefaultSharedPreferences(MultipleAgency.this);
		SharedPreferences.Editor editor = sharedPreferencesSelectedPlan.edit();
		editor.putString("SelectedPlan", selectedPlan);

		editor.commit();
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

	public String selectedPlan() {

		return null;
	}

}
