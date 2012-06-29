package com.squadigital.securityApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.squaddigital.securityApp.R;

public class SuccessfulRegistration extends BaseSecurityApp implements OnClickListener{
	
	private TextView tvagencyname;
	private TextView tvplan;
	private TextView tvperiod;
	private TextView tvprice;
	private TextView tvlink;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registersuccess);
		
		tvagencyname = (TextView)findViewById(R.id.textView2);
		tvplan       = (TextView)findViewById(R.id.TextView01);
		tvperiod 	 = (TextView)findViewById(R.id.TextView02);
		tvprice 	 = (TextView)findViewById(R.id.textView3);
		tvlink 	 	 = (TextView)findViewById(R.id.textView4);
		
		tvlink.setOnClickListener(this);
		
		tvagencyname.setText(Register.securityAgency);
		tvplan.setText(Register.planName);
//		tvperiod.setText(Register.);
		tvprice.setText(Register.plansDollarPrice);
		
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.textView4)
			startActivity(new Intent(SuccessfulRegistration.this, SecurityAppActivity.class));
	}

	
}
