package com.fanwe.www.androidtouch;

import android.util.Log;
import android.view.MotionEvent;

public class MotionEventLogger
{
    public static final String TAG = MotionEventLogger.class.getSimpleName();

    public static void i(String msg, MotionEvent event)
    {
        if (msg == null)
        {
            return;
        }

        Log.i(TAG, getLogString(msg, event));
    }

    public static void e(String msg, MotionEvent event)
    {
        if (msg == null)
        {
            return;
        }

        Log.e(TAG, getLogString(msg, event));
    }

    public static String getLogString(String msg, MotionEvent event)
    {
        String eventName = "";
        if (event != null)
        {
            final int action = event.getAction();
            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    eventName = "ACTION_DOWN ";
                    break;
                case MotionEvent.ACTION_MOVE:
                    eventName = "ACTION_MOVE ";
                    break;
                case MotionEvent.ACTION_UP:
                    eventName = "ACTION_UP ";
                    break;
                case MotionEvent.ACTION_CANCEL:
                    eventName = "ACTION_CANCEL ";
                    break;
                default:
                    eventName = "ACTION_DEFAULT ";
                    break;
            }
        }
        return eventName + " " + msg;
    }
}
