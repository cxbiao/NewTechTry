package com.bryan.newtechtry.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author：Cxb on 2016/11/25 16:23
 */

public class MyView extends View {

    private static final String TAG = "MyView";
    private Paint mPaint;
    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(20);
    }


    private int width;
    private int height;

    private float downX;
    private float downY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int wmode= MeasureSpec.getMode(widthMeasureSpec);
        int hmode= MeasureSpec.getMode(heightMeasureSpec);

        if(wmode== MeasureSpec.EXACTLY){
            width= MeasureSpec.getSize(widthMeasureSpec);
        }else {
            width=200;
        }

        if(hmode== MeasureSpec.EXACTLY){
            height= MeasureSpec.getSize(heightMeasureSpec);
        }else{
            height=200;
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {



        canvas.translate(width/2,height/2);


        Matrix invert=new Matrix();


        canvas.getMatrix().invert(invert);
        float[] dest={downX,downY};
        invert.mapPoints(dest);
        //按照canvas的变化将touch坐标转换到canvas坐标系

        Log.e(TAG,"destx="+dest[0]+";desty="+dest[1]);
        canvas.drawText("this is text",0,0,mPaint);


        canvas.drawCircle(dest[0],dest[1],20,mPaint);


        Log.e(TAG,canvas.getMatrix().toString());
        Log.e(TAG,getMatrix().toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x=event.getX();
                float rawX=event.getRawX();
                float y=event.getY();
                float rawY=event.getRawY();
                downX=rawX;
                downY=rawY;
                invalidate();
                Log.e(TAG,"y="+y+";rawY="+rawY);
                Log.e(TAG,"x="+x+";rawX="+rawX);
                break;
            case MotionEvent.ACTION_UP:
                downX=downY=-1;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
