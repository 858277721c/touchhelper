package com.fanwe.lib.touchhelper;

import android.view.MotionEvent;

public class FSimpleTouchHelper extends FTouchHelper
{

    private Callback mCallback;

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    public Callback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = Callback.DEFAULT;
        }
        return mCallback;
    }

    /**
     * 外部调用
     *
     * @param event
     * @return
     */
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (isNeedIntercept())
        {
            return true;
        }

        processTouchEvent(event);

        final int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
        {
            // 不处理
        } else
        {
            if (getCallback().shouldInterceptTouchEvent(event))
            {
                setNeedIntercept(true);
                return true;
            }
        }

        return false;
    }


    public interface Callback
    {
        /**
         * 是否开始拦截事件
         *
         * @param event
         * @return
         */
        boolean shouldInterceptTouchEvent(MotionEvent event);

        Callback DEFAULT = new Callback()
        {
            @Override
            public boolean shouldInterceptTouchEvent(MotionEvent event)
            {
                return false;
            }
        };
    }
}
