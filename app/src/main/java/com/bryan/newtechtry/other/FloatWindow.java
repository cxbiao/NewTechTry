package com.bryan.newtechtry.other;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bryan.newtechtry.R;

/**
 * 悬浮窗口
 * Created by Administrator on 2015/11/2.
 */
public class FloatWindow extends Activity {

    private static final String TAG="FloatWindow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_float);

        final Button btn=new Button(this);
        btn.setText("button");
        /**
         * TYPE_PHONE等系统级window需要权限SYSTEM_ALERT_WINDOW，TYPE_TOAST则不需要权限，但在低版本中不能进行交互
         * FLAG_NOT_TOUCH_MODAL  窗口之外的事件交给其他view处理,一般来说要加上这个，否则其他窗口无法接收事件
         */
        final WindowManager.LayoutParams params=new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSPARENT);
    //    params.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
       //         | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
      //        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        params.gravity= Gravity.LEFT| Gravity.TOP;
        params.x=100;
        params.y=300;
        //产生一个新的窗口

        WindowManager windowManager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
       // windowManager.addView(btn, params);


        //随着手指不断移动view
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        params.x = rawX;
                        params.y = rawY;
                        getWindowManager().updateViewLayout(btn, params);
                }
                return false;
            }
        });


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,"float");
                wakeUpAndUnlock(getApplicationContext());
            }
        },5000);

    }
    private Handler mHandler=new Handler();

    public void show(View v){
        Rect rect=new Rect();

        final int[] ls = new int[2];
        findViewById(R.id.tv).getGlobalVisibleRect(rect);
        int aaa=findViewById(R.id.tv).getTop();
        findViewById(R.id.tv).getLocationOnScreen(ls);
        findViewById(R.id.tv).getLocationInWindow(ls);
        System.out.println(rect);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void wakeUpAndUnlock(Context context){
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

}
