package com.fanwe.www.androidtouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.fanwe.library.touchhelper.SDTouchHelper;

public class MainActivity extends AppCompatActivity
{

    private SDTouchHelper mTouchHelper = new SDTouchHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTouchHelper.setDebug(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
