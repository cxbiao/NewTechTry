package com.bryan.newtechtry.other;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bryan.newtechtry.R;

/**锁屏下弹窗
 * Author：Cxb on 2016/12/1 11:04
 */

public class LockWindowActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MyView","LockWindowActivity1");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
               | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_lock_win);
        Log.e("MyView","LockWindowActivity2");
    }

    public void tuclose(View v){
        finish();
    }

    public void tutu(View v){
        Log.e("MyView","tutu");
        Toast.makeText(this, "tutu", Toast.LENGTH_SHORT).show();
    }
}
