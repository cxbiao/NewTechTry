package com.bryan.newtechtry.other;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.bryan.newtechtry.R;

public class PhoneListenerActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_listener);
		//finish();
	}

	public void startPhoneListen(View v){
		Intent service=new Intent(this,MyPhoneService.class);
		startService(service);
	}
    
	public void stopPhoneListen(View v){
		Intent service=new Intent(this,MyPhoneService.class);
        stopService(service);
	}
}
