package com.bryan.newtechtry.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcast extends BroadcastReceiver {


	private static final String TAG = "MyPhoneService";

	@Override
	public void onReceive(Context context, Intent intent) {

		//TelephonyManager telephonyManager=
		//		(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//Log.e(TAG, "state:" + telephonyManager.getCallState());
		Log.e(TAG,intent.getAction());
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Log.e(TAG, "call OUT:" + phoneNumber);
		}
		Intent service=new Intent(context,MyPhoneService.class);
		context.startService(service);
	}

}
