package com.fanwe.www.androidtouch.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.fanwe.www.androidtouch.MotionEventLogger;

public class FirstFrameLayout extends FrameLayout
{
    public FirstFrameLayout(@NonNull Context context)
    {
        super(context);
    }

    public FirstFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs)
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
