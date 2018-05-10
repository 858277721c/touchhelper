package com.fanwe.www.androidtouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity
{
    private TestFrameLayout mTestFrameLayout;
    private Button mButton;
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);
        mTestFrameLayout = findViewById(R.id.fl_test);
        mButton = findViewById(R.id.btn);
        mTextView = findViewById(R.id.textview);
    }
}
