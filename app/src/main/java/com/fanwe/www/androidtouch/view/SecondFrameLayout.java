package com.fanwe.www.androidtouch.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.fanwe.www.androidtouch.MotionEventLogger;

public class SecondFrameLayout extends FrameLayout
{
    public SecondFrameLayout(@NonNull Context context)
    {
        super(context);
    }

    public SecondFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        final boolean result = super.onInterceptTouchEvent(ev);
        MotionEventLogger.i( "Second onInterceptTouchEvent:" + result, ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        final boolean result = super.onTouchEvent(event);
        MotionEventLogger.i("Second onTouchEvent:" + result, event);
        return result;
    }
}
