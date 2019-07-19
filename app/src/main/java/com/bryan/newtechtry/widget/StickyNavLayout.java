package com.bryan.newtechtry.widget;

import android.content.Context;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Author：Cxb on 2016/12/28 17:39
 */

public class StickyNavLayout extends LinearLayout implements NestedScrollingParent {


    private static final String TAG = "StickyNavLayout";

    private int mHeaderHeight;

    private Scroller mScroller;

    public StickyNavLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(context);
    }

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(context);

    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeaderHeight=getChildAt(0).getMeasuredHeight();
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll: ");
        return nestedScrollAxes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.e(TAG, "onNestedScrollAccepted: " );
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.e(TAG, "onStopNestedScroll: ");
        super.onStopNestedScroll(child);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll: ");


        //down - //up+
        boolean hiddenTop = dy > 0 && getScrollY() < mHeaderHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop)
        {
            Log.e(TAG, "onNestedPreScroll: "+dy );
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll: ");
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling: ");
        //down - //up+
        if (getScrollY() >= mHeaderHeight) return false;
        Log.e(TAG, "getScrollY: "+getScrollY() );
        fling((int) velocityY);
        return true;
    }



    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling: ");
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    public void fling(int velocityY)
    {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mHeaderHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y)
    {
        if (y < 0)
        {
            y = 0;
        }
        if (y > mHeaderHeight)
        {
            y = mHeaderHeight;
        }
        if (y != getScrollY())
        {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }
}
