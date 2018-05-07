/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.touchhelper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class FGestureManager
{
    private final ViewGroup mViewGroup;
    private FScroller mScroller;
    private ViewConfiguration mViewConfiguration;
    private VelocityTracker mVelocityTracker;
    private final FTouchHelper mTouchHelper = new FTouchHelper()
    {
        @Override
        public void setNeedIntercept(boolean needIntercept)
        {
            super.setNeedIntercept(needIntercept);
            getCallback().onNeedInterceptChanged(needIntercept);
        }
    };

    private Callback mCallback;

    public FGestureManager(ViewGroup viewGroup)
    {
        mViewGroup = viewGroup;
    }

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

    public Context getContext()
    {
        return mViewGroup.getContext();
    }

    public FTouchHelper getTouchHelper()
    {
        return mTouchHelper;
    }

    public FScroller getScroller()
    {
        if (mScroller == null)
        {
            mScroller = new FScroller(getContext());
        }
        return mScroller;
    }

    public ViewConfiguration getViewConfiguration()
    {
        if (mViewConfiguration == null)
        {
            mViewConfiguration = ViewConfiguration.get(getContext());
        }
        return mViewConfiguration;
    }

    /**
     * 是否拦截事件
     *
     * @param intercept
     */
    public void interceptTouchEvent(boolean intercept)
    {
        mTouchHelper.setNeedIntercept(intercept);
    }

    private VelocityTracker getVelocityTracker()
    {
        if (mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
        return mVelocityTracker;
    }

    private void releaseVelocityTracker()
    {
        if (mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 外部调用
     *
     * @param event
     * @return
     */
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (mTouchHelper.isNeedIntercept())
        {
            return true;
        }

        mTouchHelper.processTouchEvent(event);
        getVelocityTracker().addMovement(event);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
            default:
                if (getCallback().shouldInterceptTouchEvent(event))
                {
                    interceptTouchEvent(true);
                    return true;
                }
                break;
        }

        return false;
    }

    /**
     * 外部调用
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        getVelocityTracker().addMovement(event);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
            default:
                if (mTouchHelper.isNeedCosume())
                {
                    final boolean consume = getCallback().onConsumeEvent(event);
                    mTouchHelper.setNeedCosume(consume);
                } else
                {
                    if (getCallback().shouldConsumeTouchEvent(event))
                    {
                        mTouchHelper.setNeedCosume(true);
                    } else
                    {
                        mTouchHelper.setNeedCosume(false);
                    }
                }
                break;
        }

        return mTouchHelper.isNeedCosume();
    }

    /**
     * 外部调用
     */
    public void computeScroll()
    {
        final int dx = getScroller().getDeltaX();
        final int dy = getScroller().getDeltaY();
        final boolean computeScrollOffset = getScroller().computeScrollOffset();

        if (computeScrollOffset)
        {
            getCallback().onComputeScroll(dx, dy, false);
            mViewGroup.invalidate();
        } else
        {
            getCallback().onComputeScroll(dx, dy, true);
        }
    }

    public interface Callback
    {
        /**
         * 是否开始拦截事件({@link #onInterceptTouchEvent(MotionEvent)}方法触发)
         *
         * @param event
         * @return
         */
        boolean shouldInterceptTouchEvent(MotionEvent event);

        void onNeedInterceptChanged(boolean needIntercept);

        /**
         * 是否开始消费事件
         *
         * @param event
         * @return
         */
        boolean shouldConsumeTouchEvent(MotionEvent event);

        /**
         * 事件回调
         *
         * @param event
         * @return
         */
        boolean onConsumeEvent(MotionEvent event);

        /**
         * 手势结束回调，收到{@link MotionEvent#ACTION_UP}或者{@link MotionEvent#ACTION_CANCEL}
         *
         * @param event
         * @param velocityX x方向速度
         * @param velocityY y方向速度
         */
        void onGestureFinish(MotionEvent event, float velocityX, float velocityY);

        void onComputeScroll(int dx, int dy, boolean finish);

        Callback DEFAULT = new Callback()
        {
            @Override
            public boolean shouldInterceptTouchEvent(MotionEvent event)
            {
                return false;
            }

            @Override
            public void onNeedInterceptChanged(boolean needIntercept)
            {
            }

            @Override
            public boolean shouldConsumeTouchEvent(MotionEvent event)
            {
                return false;
            }

            @Override
            public boolean onConsumeEvent(MotionEvent event)
            {
                return false;
            }

            @Override
            public void onGestureFinish(MotionEvent event, float velocityX, float velocityY)
            {
            }

            @Override
            public void onComputeScroll(int dx, int dy, boolean finish)
            {
            }
        };
    }
}
