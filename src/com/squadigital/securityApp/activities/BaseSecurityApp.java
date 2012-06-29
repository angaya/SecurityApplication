package com.squadigital.securityApp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public abstract class BaseSecurityApp extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void logout() {
		SharedPreferences sharedPreferencesUserId = PreferenceManager
				.getDefaultSharedPreferences(this);
		sharedPreferencesUserId.edit().clear().commit();
		Intent intent = new Intent(getApplicationContext(),
				SecurityAppActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	protected void showMessage() {
		new AlertDialog.Builder(this)
				.setTitle("Sign out")
				.setMessage("Are you sure you want to exit?")
				.setCancelable(true)
				.setPositiveButton("YES",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((AlertDialog) dialog).getButton(which)
										.setVisibility(View.INVISIBLE);
								logout();
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

	protected void startHelp() {
		startActivity(new Intent(getApplicationContext(), Help.class));

	}

}
