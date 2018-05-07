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

        @Override
        public void setNeedConsume(boolean needConsume)
        {
            super.setNeedConsume(needConsume);
            getCallback().onNeedConsumeChanged(needConsume);
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

    public VelocityTracker getVelocityTracker()
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
                    mTouchHelper.setNeedIntercept(true);
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
            case MotionEvent.ACTION_DOWN:
                return getCallback().consumeDownEvent(event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getCallback().onConsumeEventFinish(event);
                releaseVelocityTracker();
                break;
            default:
                if (mTouchHelper.isNeedConsume())
                {
                    final boolean consume = getCallback().onConsumeEvent(event);
                    mTouchHelper.setNeedConsume(consume);
                } else
                {
                    final boolean shouldConsumeTouchEvent = getCallback().shouldConsumeTouchEvent(event);
                    mTouchHelper.setNeedConsume(shouldConsumeTouchEvent);
                }
                break;
        }

        return mTouchHelper.isNeedConsume();
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

        /**
         * 是否需要拦截发生变化
         *
         * @param intercept
         */
        void onNeedInterceptChanged(boolean intercept);

        /**
         * 是否需要消费按下事件，只有此方法返回true，才有后续的移动事件
         *
         * @param event
         * @return
         */
        boolean consumeDownEvent(MotionEvent event);

        /**
         * 是否开始消费事件
         *
         * @param event
         * @return
         */
        boolean shouldConsumeTouchEvent(MotionEvent event);

        /**
         * 是否需要消费发生变化
         *
         * @param consume
         */
        void onNeedConsumeChanged(boolean consume);

        /**
         * 事件回调
         *
         * @param event
         * @return
         */
        boolean onConsumeEvent(MotionEvent event);

        /**
         * 事件结束回调，收到{@link MotionEvent#ACTION_UP}或者{@link MotionEvent#ACTION_CANCEL}
         *
         * @param event
         */
        void onConsumeEventFinish(MotionEvent event);

        void onComputeScroll(int dx, int dy, boolean finish);

        Callback DEFAULT = new Callback()
        {
            @Override
            public boolean shouldInterceptTouchEvent(MotionEvent event)
            {
                return false;
            }

            @Override
            public void onNeedInterceptChanged(boolean intercept)
            {
            }

            @Override
            public boolean consumeDownEvent(MotionEvent event)
            {
                return false;
            }

            @Override
            public boolean shouldConsumeTouchEvent(MotionEvent event)
            {
                return false;
            }

            @Override
            public void onNeedConsumeChanged(boolean consume)
            {
            }

            @Override
            public boolean onConsumeEvent(MotionEvent event)
            {
                return false;
            }

            @Override
            public void onConsumeEventFinish(MotionEvent event)
            {
            }

            @Override
            public void onComputeScroll(int dx, int dy, boolean finish)
            {
            }
        };
    }
}
