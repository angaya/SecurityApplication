package com.squadigital.securityApp.activities;

import com.squaddigital.securityApp.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ProgressBar;

public class SplashScreen extends BaseSecurityApp {
	
	private ProgressBar progressBar;
	
	private final int SPLASH_DISPLAY_LENGTH = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.splashscreen);
		
        setProgressBarVisibility(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				SplashScreen.this.finish();
				// Create an Intent that will start the main activity.
				Intent mainIntent = new Intent(SplashScreen.this, SecurityAppActivity.class);
				SplashScreen.this.startActivity(mainIntent);
			}
		}, SPLASH_DISPLAY_LENGTH);
	}
	
	
	

}
