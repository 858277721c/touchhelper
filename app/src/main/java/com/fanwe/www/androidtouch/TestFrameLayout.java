package com.fanwe.www.androidtouch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TestFrameLayout extends FrameLayout
{
    public TestFrameLayout(@NonNull Context context)
    {
        super(context);
    }

    public TestFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        final boolean result = super.onInterceptTouchEvent(ev);
        MotionEventLogger.i("onInterceptTouchEvent:" + result, ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        final boolean result = super.onTouchEvent(event);
        MotionEventLogger.i("onTouchEvent:" + result, event);
        return result;
    }
}
