package com.squadigital.securityApp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squaddigital.securityApp.util.SecurityAppUtil;

public class UpdateContacts extends BaseSecurityApp implements OnClickListener {

	private EditText editTextName;
	private EditText editTextEmail;
	private EditText editTextPhone;

	private Button btnDelete;
	private Button btnEdit;
	private Button btnBack;

	private String name;
	private String email;
	private String phone;
	private String contactId;
	private String message;

	AgencyList agencyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatecontacts);

		Bundle bundle = this.getIntent().getExtras();
		contactId = bundle.getString("contactid");
		
		editTextName = (EditText) findViewById(R.id.editTextName);
		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextPhone = (EditText) findViewById(R.id.editTextPhone);

		btnDelete = (Button) findViewById(R.id.button1);
		btnEdit = (Button) findViewById(R.id.buttonEdit);
		btnBack = (Button) findViewById(R.id.buttonback);

		btnDelete.setOnClickListener(this);
		btnEdit.setOnClickListener(this);
		btnBack.setOnClickListener(this);

		connectWebservice();

		editTextName.setText(name);
		editTextEmail.setText(email);
		editTextPhone.setText(phone);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			showDeleteMessage();
		}
		else if (v.getId() == R.id.buttonEdit) {
			getInputValues();
			if (validation() == true) {
				updateContacts();
			}
		}
		else if (v.getId() == R.id.buttonback){
			finish();
		}
	}


	public void connectWebservice() {
		SecurityAppUtil.fetchXml(Global.url + "clients/editClientContact/" + contactId);
		agencyList = MyXMLHandler.agencyList;
		String[] nameList = new String[agencyList.getName().size()];
		nameList = agencyList.getName().toArray(nameList);
		String[] emailList = new String[agencyList.getEmail().size()];
		emailList = agencyList.getEmail().toArray(emailList);
		String[] phoneList = new String[agencyList.getPhone().size()];
		phoneList = agencyList.getPhone().toArray(phoneList);

		for (int i = 0; i < nameList.length; i++) {
			name = nameList[i];
			email = emailList[i];
			phone = phoneList[i];

		}

	}

	public void updateContacts() {
		SecurityAppUtil.fetchXml(Global.url + "clients/updateClientContact/" + contactId + "/" + name + "/" + email + "/" + phone);
		agencyList = MyXMLHandler.agencyList;
		String[] statusList = new String[agencyList.getStatus().size()];
		statusList = agencyList.getStatus().toArray(statusList);
		String[] messageList = new String[agencyList.getMessage().size()];
		messageList = agencyList.getMessage().toArray(messageList);
		for (int i = 0; i < statusList.length; i++) {
			String status = statusList[i];
			if (status.equals("true")) {
				message = messageList[i];
				showMessage(message);
				startActivity(new Intent(getApplicationContext(),
						EditContacts.class));
			}
		}
	}

	public void deleteContacts() {
		SecurityAppUtil.fetchXml(Global.url + "clients/deleteClientContact/" + contactId);
		agencyList = MyXMLHandler.agencyList;
		String[] statusList = new String[agencyList.getStatus().size()];
		statusList = agencyList.getStatus().toArray(statusList);
		String[] messageList = new String[agencyList.getMessage().size()];
		messageList = agencyList.getMessage().toArray(messageList);
		for (int i = 0; i < statusList.length; i++) {
			String status = statusList[i];
			if (status.equals("false")) {
				message = messageList[i]; // only show the message.
				showDialog(message);
			} else if (status.equals("true")) {
				message = messageList[i];
				showMessage(message);
				startActivity(new Intent(getApplicationContext(),
						EditContacts.class));
			}
		}
	}

	public void getInputValues() {
		name = editTextName.getText().toString();
		email = editTextEmail.getText().toString();
		phone = editTextPhone.getText().toString();
	}

	private boolean validation() {
		if (name.toString().equals("")) {
			showMessage("Please fill Name!");
			return false;
		} else if (email.toString().equals("")) {
			showMessage("Please fill Email Address!");
			return false;
		} else if (phone.toString().equals("")) {
			showMessage("Please fill phone!");
			return false;
		}

		else {
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

	private void showDialog(String msg) {
		new AlertDialog.Builder(this).setMessage(msg).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuaccount, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			startActivity(new Intent(getApplicationContext(), Home.class));
			break;
		case R.id.account:
//			startActivity(new Intent(getApplicationContext(), Account.class));
			break;
		case R.id.profile:
			startActivity(new Intent(getApplicationContext(),
					ManageProfile.class));
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

	protected void showDeleteMessage() {
		new AlertDialog.Builder(this)
				.setTitle("Delete contact")
				.setMessage("Are you Sure you want to delete this contact")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((AlertDialog) dialog).getButton(which)
										.setVisibility(View.GONE);
								deleteContacts();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();

	}

}
