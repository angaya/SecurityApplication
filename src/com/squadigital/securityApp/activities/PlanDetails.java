package com.squadigital.securityApp.activities;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squadigital.securityApp.activities.SecurityAppActivity.BackgroundThread;

public class PlanDetails extends BaseSecurityApp implements OnClickListener {

	AgencyList agencyList = null;

	private ImageView imageView1;
	private ImageView imageView2;

	private Button btnCompare;
	private Button btnSelect;

	private URL myUrl;

	private TextView tv1;
	private TextView tv2;

	private CheckBox checkBox1;
	private CheckBox checkBox2;

	private String url;
	private String[] ListAgency;
	private String[] agencyId;
	private String[] logoUrl;
	private String logourl;
	private String agencyid;
	private String agencyIdSecond;
	private String agencyName;
	private String agencyNameSecond;
	private String id;
	
	ProgressDialog progressDialog;
	ComparePlanThread comparePlanThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plandetail);

		Bundle bundle = this.getIntent().getExtras();
		ListAgency = bundle.getStringArray("agencyList");
		agencyId = bundle.getStringArray("agencyId");
		logoUrl = bundle.getStringArray("logourl");

		tv1 = (TextView) findViewById(R.id.textView2);
		tv2 = (TextView) findViewById(R.id.textView3);

		imageView1 = (ImageView) findViewById(R.id.imageView2);
		imageView2 = (ImageView) findViewById(R.id.imageView3);

		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		checkBox2 = (CheckBox) findViewById(R.id.checkBox2);

		checkBox1.setOnClickListener(this);
		checkBox2.setOnClickListener(this);

		btnCompare = (Button) findViewById(R.id.buttoncompare);
		btnSelect = (Button) findViewById(R.id.buttonselect);

		btnCompare.setOnClickListener(this);
		btnSelect.setOnClickListener(this);
		for (int i = 0; i < ListAgency.length; i++) {
			tv1.setText(displaysText(0));

			drawImage(0, imageView1);
			tv2.setText(ListAgency[1]);
			// tv2.setText(sitesList.getAgencyname().get(1));
			drawImage(1, imageView2);
		}
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

	private void drawImage(int i, ImageView imageView) {
		try {
			myUrl = new URL(logoUrl[i]);
			Drawable myImage = Drawable.createFromStream(myUrl.openStream(),
					"src");
			imageView.setImageDrawable(myImage);
			imageView.setMinimumHeight(20);
			imageView.setMinimumWidth(20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String displaysText(int i) {
		return ListAgency[i];
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttoncompare) {
			if (checkBox1.isChecked() && checkBox2.isChecked()) {
				setProgressBarIndeterminateVisibility(true);
			    progressDialog = ProgressDialog.show(PlanDetails.this,
			              "", "Please wait...");
			    comparePlanThread = new ComparePlanThread();
			    comparePlanThread.start();
			} else {
				showMessage("Select two agencies");
			}

		} else if (v.getId() == R.id.buttonselect) {
			fetchXml(Global.url + "plans/agencyList");
			agencyList = MyXMLHandler.agencyList;
			if ((checkBox1.isChecked())) {
				for (int i = 0; i < agencyList.getId().size(); i++) {
					agencyName = agencyList.getAgencyname().get(0);
					agencyid = agencyList.getAgencyid().get(0);
					id = agencyList.getId().get(i);
					logourl = agencyList.getLogourl().get(0);
					// for (int j = 0; j < sitesList.getAgencyname().size();
					// j++) {
					// if (agencyName.equals(sitesList.getAgencyname().get(j)))
					// {
					// agencyId = sitesList.getAgencyid().get(j);
					// }
					// }
					// return;
				}
			}

			else if ((checkBox2.isChecked())) {
				fetchXml(Global.url + "plans/agencyList");
				agencyList = MyXMLHandler.agencyList;
				for (int i = 0; i < agencyList.getId().size(); i++) {
					agencyName = agencyList.getAgencyname().get(1);
					id = agencyList.getId().get(i);
					for (int j = 0; j < agencyList.getAgencyname().size(); j++) {
						if (agencyName.equals(agencyList.getAgencyname().get(j))) {
							agencyid = agencyList.getAgencyid().get(j);
						}
					}
				}
				logourl = agencyList.getLogourl().get(1);
			}

			if ((checkBox1.isChecked() && !checkBox2.isChecked())
					|| (!checkBox1.isChecked() && checkBox2.isChecked())) {
				startActivity(new Intent(getApplicationContext(),
						SecurityDetails.class));
			} else {
				showMessage("Select one agency");
			}
			savepreferences();
		}
	}

	private void showMessage(String msg) {
		new AlertDialog.Builder(this)
				.setMessage(msg)
				.setPositiveButton("OK",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((AlertDialog) dialog).getButton(which)
										.setVisibility(View.GONE);
							}
						}).show();
	}

	public void savepreferences() {
		SharedPreferences sharedPreferencesAgencyId = PreferenceManager
				.getDefaultSharedPreferences(PlanDetails.this);
		SharedPreferences.Editor editor = sharedPreferencesAgencyId.edit();
		if (agencyNameSecond != null) {
			editor.putString("AGENCYNAMESECOND", agencyNameSecond);
		}
		editor.putString("LOGOURL", logourl);
		editor.putString("AGENCYNAME", agencyName);
		editor.putString("AGENCYID", agencyid);
		editor.putString("ID", id);
		editor.commit();
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
	
	public class ComparePlanThread extends Thread{
		@Override
		public void run() {
			
			url = Global.url + "plans/comparePlan/3/4";
			urlconnection(url);
			agencyList = MyXMLHandler.agencyList;
			passToNextActivity();
			comparePlanHandler.sendEmptyMessage(0); 
		}
		  }
	
	Handler comparePlanHandler = new Handler(){
		 
		 @Override
			public void handleMessage(Message msg) {
		//	 setProgressBarIndeterminateVisibility(false);
			 progressDialog.hide();
			 progressDialog.dismiss();
			}
		 
	 };
	 
	 public Bitmap drawImage(String url) {
			Bitmap bitmap = null;
			try {
				URL myUrl;
				myUrl = new URL(url);
				Drawable drawable = Drawable.createFromStream(myUrl.openStream(), "src");
				bitmap = ((BitmapDrawable)drawable).getBitmap();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		
		public void passToNextActivity(){
			Intent intent = new Intent(this, MultipleAgency.class);
			intent.putExtra("BitmapImageOne",drawImage(agencyList.getLogourl().get(0)));
			intent.putExtra("BitmapImageTwo",drawImage(agencyList.getLogourl().get(3)));
			Bundle bundle = new Bundle();
			bundle.putStringArray("agencyList", ListAgency);
			startActivity(intent);
		}

}
