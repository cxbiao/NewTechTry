package com.bryan.newtechtry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bryan.annotations.OnClick;
import com.bryan.annotations.RandomInt;
import com.bryan.annotations.RandomString;
import com.bryan.annotations_api.RandomUtil;
import com.bryan.newtechtry.loader.SkinInflaterFactory;
import com.bryan.newtechtry.other.PixelActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @RandomInt(minValue = 10,maxValue = 1000)
    int it;
    @RandomString
    String is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory2(new SkinInflaterFactory());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RandomUtil.inject(this);
        Log.i(TAG,"it="+it);
        Log.i(TAG,"is="+is);getDelegate().installViewFactory();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PixelActivity.class));
            }
        });
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Log.w("SkinInflaterFactory2","parent:"+parent+";name="+name);
        return null;
    }

    @OnClick(R.id.btn)
    public void doBtn(){
        Toast.makeText(getApplicationContext(),"btn",Toast.LENGTH_SHORT).show();
    }
}
