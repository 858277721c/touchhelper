package com.fanwe.lib.touchhelper;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * 手势识别类
 */
public class FGestureDetector extends GestureDetector
{
    public FGestureDetector(Context context, Callback listener)
    {
        super(context, listener);
        init(context, listener);
    }

    public FGestureDetector(Context context, Callback listener, Handler handler)
    {
        super(context, listener, handler);
        init(context, listener);
    }

    public FGestureDetector(Context context, Callback listener, Handler handler, boolean unused)
    {
        super(context, listener, handler, unused);
        init(context, listener);
    }

    private Callback mCallback;
    private VelocityTracker mVelocityTracker;

    private void init(Context context, Callback listener)
    {
        mCallback = listener;
    }

    public final VelocityTracker getVelocityTracker()
    {
        if (mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
        return mVelocityTracker;
    }

    public final void releaseVelocityTracker()
    {
        if (mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        final boolean result = super.onTouchEvent(ev);

        getVelocityTracker().addMovement(ev);
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_UP:
                getVelocityTracker().computeCurrentVelocity(1000);
                mCallback.onActionUp(ev, getVelocityTracker().getXVelocity(), getVelocityTracker().getYVelocity());
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
            default:
                break;
        }

        return result;
    }

    public static class Callback extends SimpleOnGestureListener
    {
        public void onActionUp(MotionEvent event, float velocityX, float velocityY)
        {
        }
    }
}
