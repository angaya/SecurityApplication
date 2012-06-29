package com.squadigital.securityApp.activities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;

public class Register extends BaseSecurityApp implements OnClickListener {

	private String plan;
	public static String planName;
	public static String securityAgency;
	private String details;
	private String agencyid;
	private String agencyName;
	private String plansPrice;
	public static String plansDollarPrice;
	private String name;
	private String pass;
	private String emailAddress;
	private String phoneNumber;
	private String message;
	private String selectedPlan;

	private String[] statusList;
	private String[] messageList;
	private String[] agencyId;
	private String[] id;
	private String[] state;
	private String[] listOfAgencies;
	private String[] price;
	private String[] dollarPrice;
	private String[] period;
	private String[] paymentMethods;

	private int securityAgencyId;

	private Button btnnext;

	private TextView tvplanDetails;

	private EditText userName;
	private EditText password;
	private EditText repassword;
	private EditText email;
	private EditText phone;

	private Spinner spinnerPayMethod;
	private Spinner spinnerplans;
	private Spinner spinnerSecurityAgency;

	private ProgressDialog progressDialog;
	private BackgroundThread backgroundThread;

	AgencyList agencyList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		SharedPreferences sharedPreferencesAgencyId = PreferenceManager.getDefaultSharedPreferences(this);
		agencyName = sharedPreferencesAgencyId.getString("AGENCYNAME", "null");

		agencyid = sharedPreferencesAgencyId.getString("AGENCYID", agencyid);

		Bundle bundle = this.getIntent().getExtras();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
      String [] securityAgencies ={"KK","G4S","Panda","Bm"};
      Spinner spinner;
      spinner = (Spinner) findViewById(R.id.spinnersecurityagency);
      

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,R.layout.spinnerlayout,securityAgencies);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		
		spinner.setAdapter(adapter);

//		selectedPlan = bundle.getString("SelectedPlan");
//		System.out.println("test");
//		if (extras != null && selectedPlan == null) {
//			agencyName = extras.getString("agencyname");
//			if (selectedPlan == null)
//				selectedPlan = extras.getString("planname");
//		}
//
//		if (bundle != null && agencyName == null && agencyid == null) {
//			listOfAgencies = bundle.getStringArray("agencyList");
//			agencyId = bundle.getStringArray("agencyId");
//		} else {
//			fetchXml(Global.url + "plans/agencyList");
//			agencyList = MyXMLHandler.agencyList;
//			listOfAgencies = new String[agencyList.getAgencyname().size()];
//			listOfAgencies = agencyList.getAgencyname().toArray(listOfAgencies);
//
//			agencyId = new String[agencyList.getAgencyid().size()];
//			agencyId = agencyList.getAgencyid().toArray(agencyId);
//		}

		email = (EditText) findViewById(R.id.edittextemail);
		userName = (EditText) findViewById(R.id.editTextusername);
		password = (EditText) findViewById(R.id.editTextpassword);
		repassword = (EditText) findViewById(R.id.editTextrepassword);
		phone = (EditText) findViewById(R.id.editTextphone);
		btnnext = (Button) findViewById(R.id.next);

//		tvplanDetails = (TextView) findViewById(R.id.textView1);
//		tvplanDetails.setVisibility(View.GONE);

//		btnnext.setOnClickListener(this);
//		initializeSpinners();
		
		
		
		
		
		
	}

	public void initializeSpinners() {
		spinnerPayMethod = (Spinner) findViewById(R.id.spinnerpaymethod);
		spinnerplans = (Spinner) findViewById(R.id.spinnerselectplans);

		spinnerSecurityAgency = (Spinner) findViewById(R.id.spinnersecurityagency);

		OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
		spinnerSecurityAgency.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						if (agencyid != null) {
							fetchXml(Global.url + "plans/agencyPlan/"
									+ agencyid);
						}
						for (int i = 0; i < listOfAgencies.length; i++) {
							if (String.valueOf(
									spinnerSecurityAgency.getSelectedItem())
									.equalsIgnoreCase("G4S")) {
								agencyid = "3";
							} else if (String.valueOf(
									spinnerSecurityAgency.getSelectedItem())
									.equalsIgnoreCase("KK Security")) {
								agencyid = "4";
							}
						}
						fetchXml(Global.url + "plans/agencyPlan/" + agencyid);
						agencyList = MyXMLHandler.agencyList;
						state = new String[agencyList.getPlanname().size()];
						state = agencyList.getPlanname().toArray(state);

						if (agencyid.equalsIgnoreCase("null")) {
							populatePlanSpinner(state);
						} else {
							populatePlanSpinner(planArray());
						}

						price = new String[agencyList.getPrice().size()];
						price = agencyList.getPrice().toArray(price);
						dollarPrice = new String[agencyList.getDollarPrice()
								.size()];
						dollarPrice = agencyList.getDollarPrice().toArray(
								dollarPrice);
						id = new String[agencyList.getId().size()];
						id = agencyList.getId().toArray(id);
						period = new String[agencyList.getPeriod().size()];
						period = agencyList.getPeriod().toArray(period);

						tvplanDetails.setVisibility(View.VISIBLE);

						fetchXml(Global.url + "plans/agencyPaymentMethod/"
								+ agencyid);
						agencyList = MyXMLHandler.agencyList;
						paymentMethods = new String[agencyList.getName().size()];
						paymentMethods = agencyList.getName().toArray(
								paymentMethods);
						populatePaymentMethodSpinner(paymentMethods);
						for (int i = 0; i < state.length; i++) {
							if (spinnerplans.getSelectedItem().toString()
									.equalsIgnoreCase("Silver")) {
								String myTest = price[1];
								System.out.println(myTest);
								details = "Ksh " + price[1].toString() + "("
										+ "$" + dollarPrice[1] + ")" + ",  "
										+ period[1].toString() + "Months";
								tvplanDetails.setText(details);
							}
						}
					}

					public void populatePlanSpinner(String[] string) {
						ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
								Register.this,
								android.R.layout.simple_spinner_item,
								planArray());
						adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
						spinnerplans.setAdapter(adapter1);
					}

					public void populatePaymentMethodSpinner(String[] string) {
						ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
								Register.this,
								android.R.layout.simple_spinner_item,
								paymentMethods);
						adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
						spinnerPayMethod.setAdapter(adapter1);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		spinnerPayMethod.setOnItemSelectedListener(onItemSelectedListener);
		spinnerplans.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				for (int i = 0; i < state.length; i++) {

					if (spinnerplans.getSelectedItem().toString()
							.equalsIgnoreCase("Silver")) {
						String myTest = price[1];
						System.out.println(myTest);
						details = "Ksh " + price[1].toString() + "(" + "$"
								+ dollarPrice[1] + ")" + ",  "
								+ period[1].toString() + "Months";
						tvplanDetails.setText(details);
					} else if (spinnerplans.getSelectedItem().toString()
							.equalsIgnoreCase("Gold")) {
						String myTest = price[0];
						System.out.println(myTest);
						details = "Ksh " + price[0].toString() + "(" + "$"
								+ dollarPrice[0] + ")" + ",  "
								+ period[0].toString() + "Months";
						tvplanDetails.setText(details);
					} else if (spinnerplans.getSelectedItem().toString()
							.equalsIgnoreCase("Bronze")) {
						String myTest = price[2];
						System.out.println(myTest);
						details = "Ksh " + price[2].toString() + "(" + "$"
								+ dollarPrice[2] + ")" + ",  "
								+ period[2].toString() + "Months";
						tvplanDetails.setText(details);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ArrayAdapter<String> securityAgencyAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, agencyArray());
		securityAgencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSecurityAgency.setAdapter(securityAgencyAdapter);
	}

	public void startPaypal() {
		Intent intent = new Intent(getApplicationContext(), Paypal.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public boolean isPasswordMinimum() {
		boolean passMinimum = false;
		password = (EditText) findViewById(R.id.editTextpassword);
		String pass = password.getText().toString();
		if (pass.length() < 7) {
			passMinimum = true;
		}
		return passMinimum;
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

	public int paymentMethod(Spinner spinner) {
		String selectedPaymentMethod = spinner.getSelectedItem().toString();
		int paymentId = 0;
		if (selectedPaymentMethod.equalsIgnoreCase("Paypal"))
			paymentId = 1;
		else if (selectedPaymentMethod.equalsIgnoreCase("M-PESA"))
			paymentId = 2;
		else if (selectedPaymentMethod.equalsIgnoreCase("AIRTEL MONEY"))
			paymentId = 3;
		return paymentId;
	}

	private void savepreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(Register.this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("AGENCY", agencyid);
		editor.putString("PLANS", plan);
		editor.putString("USERNAME", userName.getText().toString());
		editor.putString("PHONE", phone.getText().toString());
		editor.putString("PASSWORD", password.getText().toString());
		editor.putString("EMAIL", email.getText().toString());
		editor.putString("PRICEDOLLAR", plansDollarPrice);
		editor.commit();
	}

	public boolean serverValidation() {
		boolean success = false;
		String status = null;
		String link = Global.url + "clients/clientRegister/" + name + "/"
				+ pass + "/" + emailAddress;
		fetchXml(link);
		agencyList = MyXMLHandler.agencyList;
		messageList = new String[agencyList.getMessage().size()];
		messageList = agencyList.getMessage().toArray(messageList);
		statusList = new String[agencyList.getStatus().size()];
		statusList = agencyList.getStatus().toArray(statusList);
		for (int i = 0; i < statusList.length; i++) {
			status = statusList[i];
			if (status.equalsIgnoreCase("false")) {
				for (int j = 0; j < messageList.length; j++) {
					message = messageList[j];
				}
			}
			if (status.equalsIgnoreCase("true")) {
				success = true;
			}

		}
		return success;
	}

	private boolean validation() {
		if (userName.getText().toString().equals("")) {
			showMessage("Please fill Username!");
			return false;
		} else if (email.getText().toString().equals("")) {
			showMessage("Please fill Email!");
			return false;
		} else if (phone.getText().toString().equals("")) {
			showMessage("Please fill Phone Number!");
			return false;
		} else if (password.getText().toString().equals("")) {
			showMessage("Please fill Password!");
			return false;
		} else if (repassword.getText().toString().equals("")) {
			showMessage("Please fill Confirm password!");
			return false;
		} else if (!password.getText().toString()
				.equals(repassword.getText().toString())) {
			showMessage("Password does not match");
			return false;
		}

		// else if (!CheckConnection.isNetworkAvailable(this)) {
		// showMessage("network not available");
		// return false;}
		else {
			return true;
		}

	}

	@Override
	public void onClick(View src) {
		if (src.getId() == R.id.next) {
			if (validation() == true) {
				savepreferences();
				securityAgency = spinnerSecurityAgency.getSelectedItem()
						.toString();
				phoneNumber = phone.getText().toString();
				planName = spinnerplans.getSelectedItem().toString();
				name = userName.getText().toString();
				pass = password.getText().toString();
				emailAddress = email.getText().toString();
				securityAgencyId = paymentMethod(spinnerplans);
				progressDialog = ProgressDialog.show(Register.this, "",
						"Loading...");
				backgroundThread = new BackgroundThread();
				backgroundThread.start();
			}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			startHelp();
			break;
		}
		return true;
	}

	public class BackgroundThread extends Thread {
		@Override
		public void run() {
			if (serverValidation() == true) {
				startPaypal();
			}
			handler.sendEmptyMessage(0);
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			setProgressBarIndeterminateVisibility(false);
			progressDialog.dismiss();
			if (message != null) {
				showMessage(message);
			}

		}
	};

	private String[] movePlanArrayList(String selectedListPlan) {
		if (selectedPlan != null) {
			while (agencyList.getPlanname().indexOf(selectedListPlan) != 0
					&& agencyList.getPlanname().contains(selectedListPlan)) {
				int i = agencyList.getPlanname().indexOf(selectedListPlan);
				Collections.swap(agencyList.getPlanname(), i, i - 1);
			}
			state = new String[agencyList.getPlanname().size()];
			state = agencyList.getPlanname().toArray(state);
		}
		return state;
	}

	private String[] moveAgencyIdList(String myAgencyId) {
		while (agencyList.getAgencyid().indexOf(myAgencyId) != 0) {
			int i = agencyList.getAgencyname().indexOf(myAgencyId);
			Collections.swap(agencyList.getAgencyname(), i, i - 1);
		}
		agencyId = new String[agencyList.getAgencyid().size()];
		agencyId = agencyList.getAgencyid().toArray(agencyId);
		return agencyId;
	}

	private String[] agencyIdArray() {
		if (!agencyid.equals("null"))
			agencyId = moveArrayList(agencyid);
		return listOfAgencies;

	}

	private String[] moveArrayList(String agencyname) {
		if (agencyList != null) {
			ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(listOfAgencies));
			while (arrayList.indexOf(agencyname) != 0) {
				int i = arrayList.indexOf(agencyname);
				Collections.swap(arrayList, i, i - 1);
			}
			listOfAgencies = new String[arrayList.size()];
			listOfAgencies = arrayList.toArray(listOfAgencies);
		}

		return listOfAgencies;
	}

	private String[] agencyArray() {
		if(agencyName != null){
			if (!agencyName.equals("null")){
				listOfAgencies = moveArrayList(agencyName);
			} else {
			}
		}
		return listOfAgencies;

	}

	private String[] planArray() {
		if (selectedPlan != null) {
			String test = selectedPlan;
			System.out.println(test);
			state = movePlanArrayList(selectedPlan);
		}
		return state;
	}

	public class PlanThread extends Thread {
		@Override
		public void run() {

			planHandler.sendEmptyMessage(0);
		}
	}

	Handler planHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			setProgressBarIndeterminateVisibility(false);
			progressDialog.dismiss();
		}

	};

}
