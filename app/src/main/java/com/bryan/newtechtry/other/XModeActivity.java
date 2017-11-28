package com.bryan.newtechtry.other;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bryan.newtechtry.R;
import com.bryan.newtechtry.utils.KLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Cxb on 2016/3/2 11:44
 */
public class XModeActivity extends Activity {


    ImageView ivmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        ivmode= findViewById(R.id.iv_mode);

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("status",null);
            jsonObject.put("name","111");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String abc=jsonObject.toString();
        KLog.json(abc);
        KLog.e("abc");
    }


    public void onMode(View v){
        ivmode.getDrawable().setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_ATOP);
    }


    public void onunMode(View v){
        ivmode.getDrawable().clearColorFilter();
    }
}
