package com.squadigital.securityApp.activities;

import com.squaddigital.securityApp.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Settings extends BaseSecurityApp implements OnClickListener {
	
	private ImageView imageViewLogout;
	
	private LinearLayout linearLayoutViewProfile;
	private LinearLayout linearLayoutDashboard;
	
	private Button btntrack;
	private Button btnupdate;
	private Button btnunsubscribe;
	
	boolean trackme;
	boolean update;
	boolean unsubscribe;
	
	
	
	private Button btnAgency;
	private Button btnPayment;
	private Button btnPlan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		SharedPreferences sharedPreferencesTrackMeStatus = PreferenceManager.getDefaultSharedPreferences(this);
		trackme = sharedPreferencesTrackMeStatus.getBoolean("trackMe", false);
		
		linearLayoutDashboard = (LinearLayout)findViewById(R.id.linearLayoutdashboard);
		linearLayoutViewProfile = (LinearLayout)findViewById(R.id.linearviewprofile);
		
		btntrack = (Button)findViewById(R.id.buttontrack);
		btnupdate = (Button)findViewById(R.id.buttonupdate);
		btnunsubscribe = (Button)findViewById(R.id.buttonunsubscribe);
		
		if(trackme){
			btntrack.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_on));	
		}
		
		btntrack.setOnClickListener(this);
		btnupdate.setOnClickListener(this);
		btnunsubscribe.setOnClickListener(this);
		
		linearLayoutDashboard.setOnClickListener(this);
		linearLayoutViewProfile.setOnClickListener(this);
		
		btnAgency = (Button)findViewById(R.id.btneditagency);
		btnPayment = (Button)findViewById(R.id.btneditpayment);
		btnPlan = (Button)findViewById(R.id.btneditplan);
		imageViewLogout =(ImageView)findViewById(R.id.imageViewLogout);
		
		btnAgency.setOnClickListener(this);
		btnPayment.setOnClickListener(this);
		btnPlan.setOnClickListener(this);
		imageViewLogout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttontrack:
			if(!trackme){
				btntrack.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_on));
				trackme = true;
			} else{
				btntrack.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_off));
				trackme = false;
			}
			saveTrackMeStatus();
			break;
		case R.id.buttonupdate:
			if(!update){
				btnupdate.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_on));
				update = true;
			} else{
				btnupdate.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_off));
				update = false;
			}
			break;
		case R.id.buttonunsubscribe:
			if(!unsubscribe){
				unsubscribe();
			} else{
				btnunsubscribe.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_off));
				unsubscribe = false;
			}
			break;
		case R.id.linearLayoutdashboard:
			startActivity(new Intent(getApplicationContext(),Home.class));
			break;
		case R.id.linearviewprofile:
			startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
			break;
		case R.id.btneditagency:
			showDialog("Agency");
			break;
		case R.id.btneditpayment:
			showDialog("Payment");
			break;
		case R.id.btneditplan:
			showDialog("Plan");
			break;
		case R.id.imageViewLogout:
			showMessage();
			break;
		default:
			break;
		}
		
	}
	
	private void showDialog(String title) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title);
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}
	
	private void unsubscribe() {
		new AlertDialog.Builder(this)
				.setTitle("Unsubscribe")
				.setMessage("Are you sure you want to Unsubsribe?")
				.setCancelable(true)
				.setPositiveButton("YES",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((AlertDialog) dialog).getButton(which)
										.setVisibility(View.INVISIBLE);
								btnunsubscribe.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_on));
								unsubscribe = true;
							}
						})
				.setNegativeButton("No",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).show();

	}
	
	private void saveTrackMeStatus(){
		SharedPreferences sharedPreferencesTrackMeStatus = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferencesTrackMeStatus.edit();
		editor.putBoolean("trackMe", trackme);
		editor.commit();
	}
	
	
}
