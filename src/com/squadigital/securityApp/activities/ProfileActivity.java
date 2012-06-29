package com.squadigital.securityApp.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squaddigital.securityApp.util.SecurityAppUtil;

public class ProfileActivity extends BaseSecurityApp implements OnClickListener {
	
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	private String userId;
	
	private boolean addcontacts;

	AgencyList agencyList;
	
	private String contactList[];

	private LinearLayout linearLayoutDashboard;
	private LinearLayout linearLayoutViewSettings;
	private LinearLayout linearLayoutaddcontacts;
	private LinearLayout linearLayout;
	private LinearLayout linearLayoutroot;  
	private LinearLayout linearLayout1;
	private LinearLayout linearLayout5;
	private LinearLayout linearLayout6;
	private TextView textView;
	private Button button2;

	private Button btnusername;
	private Button btnpassword;
	private Button btnmobileNo;
	private Button btnemail;
	private Button btnreports;
	private Button btnarrow;
 
	private ImageView imageViewLogout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		
		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(this);
		userId = sharedPreferencesUserId.getString("USERID", "null");
		
		linearLayoutDashboard = (LinearLayout)findViewById(R.id.linearLayoutdashboard);
		linearLayoutViewSettings = (LinearLayout)findViewById(R.id.linearLayoutsettings);
		linearLayoutaddcontacts = (LinearLayout)findViewById(R.id.linearLayoutaddcontacts);
		
		btnarrow = (Button)findViewById(R.id.btncontactarrow);
		
		linearLayoutViewSettings.setOnClickListener(this);
		linearLayoutDashboard.setOnClickListener(this);

		imageViewLogout =(ImageView)findViewById(R.id.imageViewLogout);

		btnreports = (Button) findViewById(R.id.btnreportsarrow);
		btnusername = (Button) findViewById(R.id.btneditusername);
		btnpassword = (Button) findViewById(R.id.btneditpassword);
		btnemail = (Button) findViewById(R.id.btneditemail);
		btnmobileNo = (Button) findViewById(R.id.btneditmobileNo);
		
		imageViewLogout.setOnClickListener(this);
		btnusername.setOnClickListener(this);
		btnpassword.setOnClickListener(this);
		btnemail.setOnClickListener(this);
		btnmobileNo.setOnClickListener(this);
		linearLayoutaddcontacts.setOnClickListener(this);
		btnreports.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearLayoutdashboard:
			startActivity(new Intent(getApplicationContext(), Home.class));
			break;
		case R.id.linearLayoutsettings:
			startActivity(new Intent(getApplicationContext(), Settings.class));
			break;
		case R.id.imageViewLogout:
			showMessage();
			break;
		case R.id.btneditusername:
			changeusername();
			break;
		case R.id.btneditpassword:
			changePassword();
			break;
		case R.id.btneditemail:
			changeemail();
			break;
		case R.id.btneditmobileNo:
			changemobile();
			break;
		case R.id.btnreportsarrow:

			break;
		case R.id.linearLayoutaddcontacts:
			linearLayout = (LinearLayout)findViewById(R.id.linearLayout20);
			linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout29);
			linearLayoutroot = (LinearLayout)findViewById(R.id.root);
			String[] emergencyContact = {"alex", "angaya", "musera"};
			if(!addcontacts){
				for(int i = 0; i < emergencyContact.length; i++){
					linearLayout1 = new LinearLayout(this);
					linearLayout1.setOrientation((LinearLayout.HORIZONTAL));
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 2);
					linearLayout1.setLayoutParams(param);
					linearLayout1.setGravity(Gravity.RIGHT);
					linearLayout1.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_advance_bg));
					linearLayout1.setWeightSum(100);
					
					linearLayout5 = new LinearLayout(this);
					linearLayout5.setOrientation((LinearLayout.HORIZONTAL));
					LinearLayout.LayoutParams param5 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 70);
					param5.setMargins(30, 0, 0, 0);
					linearLayout5.setLayoutParams(param5);
					linearLayout5.setGravity(Gravity.CENTER);
					
					linearLayout1.addView(linearLayout5);
					
					textView = new TextView(this);
					textView.setText(emergencyContact[i]);
					LinearLayout.LayoutParams paramTextView=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
					textView.setLayoutParams(paramTextView);
					linearLayout5.addView(textView);
					
					linearLayout6 = new LinearLayout(this);
					linearLayout6.setOrientation((LinearLayout.HORIZONTAL));
					LinearLayout.LayoutParams param6 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 30);
					param6.setMargins(30, 0, 0, 0);
					linearLayout6.setLayoutParams(param6);
					linearLayout6.setGravity(Gravity.CENTER);
					
					linearLayout1.addView(linearLayout6);
					
					
					button2 = new Button(this);
					button2.setText("edit");
					LinearLayout.LayoutParams paramButton=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  //width, height
					button2.setLayoutParams(paramButton);
					button2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							addContact();
						}
					});
				
					button2.setBackgroundDrawable(getResources().getDrawable(R.layout.buttonedit));
					linearLayout6.addView(button2);
					
					linearLayoutroot.addView(linearLayout1);
				}
			
				linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_advance_bg));
				linearLayout.setVisibility(View.VISIBLE);
				Button button = new Button(this);
				button.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				button.setText("Add Contacts");
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					addContact();
					}
				});
				
				button.setBackgroundDrawable(getResources().getDrawable(R.layout.buttonedit));
				linearLayout.addView(button);
				btnarrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_down));
				addcontacts = true;
			} else{
				int childViews, emergencyContactLength, start;
				childViews = linearLayoutroot.getChildCount();
				emergencyContactLength = emergencyContact.length;
				start = childViews - emergencyContactLength;
				linearLayoutroot.removeViews(start, emergencyContactLength);
				btnarrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow));
				addcontacts = false;
				linearLayout.removeAllViewsInLayout();
				linearLayout.setVisibility(View.GONE);
			}
			break;
			
		default:
			break;
		}

	}
	
	public void changemobile(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Edit Phone");
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		final EditText mobileNo = new EditText(this);
		mobileNo.setHint("Mobile No");
		mobileNo.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_CLASS_PHONE);
		linearLayout.addView(mobileNo,layoutParams);
		alert.setView(linearLayout);
		
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				addContact();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
		
	}
	
	public void changeemail(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Edit Email");
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(1);
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		final EditText email = new EditText(this);
		email.setHint("Email");
		email.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		linearLayout.addView(email,layoutParams);
		alert.setView(linearLayout);
		
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
		
	}
	
	
	
	public void changeusername(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Edit Name");
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		final EditText fname = new EditText(this);
		final EditText sname = new EditText(this);
		fname.setHint("first");
		sname.setHint("last");
		fname.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		sname.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		linearLayout.addView(fname, layoutParams);
		linearLayout.addView(sname, layoutParams);
		alert.setView(linearLayout);
		
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
	
	public void changePassword(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Change password");
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 10, 0);
		final EditText oldpwd = new EditText(this);
		final EditText newpwd = new EditText(this);
		final EditText confirmpwd = new EditText(this);
		oldpwd.setHint("Old password");
		newpwd.setHint("New password");
		confirmpwd.setHint("Reenter password");
		oldpwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		newpwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		confirmpwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		linearLayout.addView(oldpwd, layoutParams);
		linearLayout.addView(newpwd, layoutParams);
		linearLayout.addView(confirmpwd, layoutParams);
		alert.setView(linearLayout);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				oldPassword = oldpwd.getText().toString();
				newPassword = newpwd.getText().toString();
				confirmPassword = confirmpwd.getText().toString();
				if (validation() == true){
					connectWebservice();
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
	
	private boolean validation() {		
		if (oldPassword.toString().equals("") && newPassword.toString().equals("") && confirmPassword.toString().equals("")){
			showMessage("Please fill all fields");
			return false;
		}
		else if(oldPassword.toString().equals("")) {
			showMessage("Please fill old password!");
			return false;
			
		} else if (newPassword.toString().equals("")) {
			showMessage("Please fill New password!");
			return false;
		} else if (confirmPassword.toString().equals("")) {
			showMessage("Please fill Confirm password!");
			return false;
		} else if (!newPassword.equals(confirmPassword)) {
			showMessage("Passwords does not match");
			return false;
		} else {
			return true;
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
	
	private void connectWebservice() {
		SecurityAppUtil.fetchXml(Global.url + "clients/changePassword/" + userId + "/" + oldPassword + "/" + newPassword);
		agencyList = MyXMLHandler.agencyList;
		String message = null;
		String status = null;
		String[] statusList = new String[agencyList.getStatus().size()];
		statusList = agencyList.getStatus().toArray(statusList);
		String[] messageList = new String[agencyList.getMessage().size()];
		messageList = agencyList.getMessage().toArray(messageList);
		for (int i = 0; i < statusList.length; i++) {
			status = statusList[i];
			message = messageList[i];
		}
		if (status.equals("true")) {
			showMessage(message);
			startActivity(new Intent(getApplicationContext(), SecurityAppActivity.class));
		} else if (status.equals("false")) {
			showMessage(message);
		}

	}
	
	private String[] contactOfList(){
		SecurityAppUtil.fetchXml(Global.url + "clients/listClientContact/"+userId);
		agencyList = MyXMLHandler.agencyList;
		contactList = new String[agencyList.getName().size()];
		contactList = agencyList.getName().toArray(contactList);
		return contactList;
	}
}