package com.bryan.newtechtry.other;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bryan.newtechtry.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class TokenRefreshActivity extends AppCompatActivity {

    private OkHttpClient mClient;

    private static final String TAG = "TokenRefreshActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        mClient= new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(logging)
                .build();

    }

    public void token(View v){
        Request request=new Request.Builder().url("http://www.baidu.com").build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,response.body().string());
            }
        });
    }
}
