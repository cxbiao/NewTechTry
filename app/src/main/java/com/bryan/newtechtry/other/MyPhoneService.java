package com.bryan.newtechtry.other;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneService extends Service {

	private MediaRecorder mediaRecorder;

	private static final String TAG = "MyPhoneService";
	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG,"onBind");
		return null;
	}
    
	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate");
		TelephonyManager telephonyManager=
				(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	class MyPhoneListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
		    switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: //空间
				Log.e(TAG, "电话空闲"+incomingNumber);
				if(mediaRecorder!=null){
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder=null;
					Log.e(TAG, "录音结束，上传到服务器");
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING: //来电状态
				Log.e(TAG, "来电话了"+incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
				Log.e(TAG, "正在通信"+incomingNumber);
				//startRecorder();
				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

		
	}
	
	private void startRecorder() {
		try {
			mediaRecorder=new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(getExternalCacheDir().getAbsolutePath()+"/"+ System.currentTimeMillis()+".mp3");
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}
}
