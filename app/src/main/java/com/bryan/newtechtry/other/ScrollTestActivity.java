package com.bryan.newtechtry.other;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bryan.newtechtry.R;

/**
 * Author：Cxb on 2016/12/28 17:21
 */

public class ScrollTestActivity extends AppCompatActivity {


    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        scrollView= (ScrollView) findViewById(R.id.scrollView);

        String m= Build.MODEL;
        String d= Build.VERSION.RELEASE;
    }

    public void up(View v){
        if(ViewCompat.canScrollVertically(scrollView,-1)){
            Toast.makeText(this, "可以向上", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "不可以向上", Toast.LENGTH_SHORT).show();
        }
    }


    public void down(View v){
        if(ViewCompat.canScrollVertically(scrollView,1)){
            Toast.makeText(this, "可以向下", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "不可以向下", Toast.LENGTH_SHORT).show();
        }
    }
}
