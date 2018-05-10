package com.fanwe.www.androidtouch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TestTextView extends android.support.v7.widget.AppCompatTextView
{
    public TestTextView(Context context)
    {
        super(context);
    }

    public TestTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = false;

        final int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN)
        {
            result = true;
        } else if (action == MotionEvent.ACTION_MOVE)
        {
            result = true;
        }

        return super.onTouchEvent(event) || result;
    }
}
