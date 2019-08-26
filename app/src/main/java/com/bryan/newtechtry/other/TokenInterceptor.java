package com.bryan.newtechtry.other;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private static final String TAG = "TokenInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        int retryCount=0;
        boolean expired=false;

        while(true){
            Response response=chain.proceed(request);
            /**
             * 在这里根据response,判断token（一般放header里）是否过期,过期则同步再请求token,不过期直接返回reqsonse
             * 拿到新token,再重构 request (参考okttp内置RetryAndFollowUpInterceptor)
             */
            expired=true;
            if(expired){
                retryCount++;
                if(retryCount>=3){
                    return response;
                }
                //必须关闭，否则报错
                response.close();
            }else {
                return response;
            }



        }



    }
}
