package com.fanwe.lib.touchhelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.fanwe.lib.touchhelper.FGestureDetector;
import com.fanwe.lib.touchhelper.FScroller;
import com.fanwe.lib.touchhelper.FTouchHelper;

public abstract class FGestureFrameLayout extends FrameLayout
{
    public FGestureFrameLayout(Context context)
    {
        super(context);
        init();
    }

    public FGestureFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FGestureFrameLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final FTouchHelper mTouchHelper = new FTouchHelper();
    private FGestureDetector mGestureDetector;
    private FScroller mScroller;

    private ViewConfiguration mViewConfiguration;

    private void init()
    {
        mGestureDetector = new FGestureDetector(getContext(), new FGestureDetector.Callback()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                return FGestureFrameLayout.this.onGestureDown(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                return FGestureFrameLayout.this.onGestureScroll(e2);
            }

            @Override
            public void onFinishEvent(MotionEvent event, float velocityX, float velocityY)
            {
                FGestureFrameLayout.this.onGestureFinish(event, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return FGestureFrameLayout.this.onGestureSingleTapUp(e);
            }
        });
    }

    protected final FTouchHelper getTouchHelper()
    {
        return mTouchHelper;
    }

    protected final FScroller getScroller()
    {
        if (mScroller == null)
        {
            mScroller = new FScroller(getContext());
        }
        return mScroller;
    }

    protected final ViewConfiguration getViewConfiguration()
    {
        if (mViewConfiguration == null)
        {
            mViewConfiguration = ViewConfiguration.get(getContext());
        }
        return mViewConfiguration;
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();
        if (getScroller().computeScrollOffset())
        {
            onComputeScroll(getScroller().getDeltaX(), getScroller().getDeltaY());
            invalidate();
        } else
        {
            onComputeScrollFinish();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (mTouchHelper.isNeedIntercept())
        {
            return true;
        }

        mTouchHelper.processTouchEvent(event);

        boolean intercept = false;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (shouldInterceptTouchEvent(event))
                {
                    intercept = true;
                }
                break;
        }

        if (intercept)
        {
            mTouchHelper.setNeedIntercept(true);
            FTouchHelper.requestDisallowInterceptTouchEvent(this, true);
            return true;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 是否要开始手势拦截({@link MotionEvent#ACTION_DOWN}和{@link MotionEvent#ACTION_MOVE}事件触发)
     *
     * @param event
     * @return
     */
    protected boolean shouldInterceptTouchEvent(MotionEvent event)
    {
        return false;
    }

    /**
     * 按下事件回调，默认消费处理此事件
     *
     * @param event true-消费处理此事件
     * @return
     */
    protected boolean onGestureDown(MotionEvent event)
    {
        return true;
    }

    /**
     * 手势滚动回调
     *
     * @param event
     * @return
     */
    protected abstract boolean onGestureScroll(MotionEvent event);

    /**
     * 手势结束回调，收到{@link MotionEvent#ACTION_UP}或者{@link MotionEvent#ACTION_CANCEL}
     *
     * @param event
     * @param velocityX x方向速度
     * @param velocityY y方向速度
     */
    protected abstract void onGestureFinish(MotionEvent event, float velocityX, float velocityY);

    /**
     * 点击事件回调
     *
     * @param event
     * @return
     */
    protected boolean onGestureSingleTapUp(MotionEvent event)
    {
        return false;
    }

    protected abstract void onComputeScroll(int dx, int dy);

    protected abstract void onComputeScrollFinish();
}
