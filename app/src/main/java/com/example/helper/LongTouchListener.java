package com.example.helper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author EdisonChang
 */
public class LongTouchListener implements View.OnTouchListener {

    private static final String TAG = "LongTouchListener";

    private static final long LONG_PRESS_TIME = 1800;
    private static final long PRESS_TIME = 200;

    private int mCurrentInScreenX;
    private int mCurrentInScreenY;
    private int mDownInScreenX;
    private int mDownInScreenY;
    private int mTouchSlop;

    private long mCurrentClickTime;

    private Handler mBaseHandler = new Handler();
    private LongPressedThread mLongPressedThread;
    private PressedThread mPressedThread;
    private ReleaseThread mReleaseThread;
    private TouchListener mTouchListener;

    public LongTouchListener(Context context) {
        mTouchSlop = ViewConfiguration.get(context.getApplicationContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCurrentInScreenX = (int) event.getX();
        mCurrentInScreenY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownInScreenX = (int) event.getX();
                mDownInScreenY = (int) event.getY();
                mCurrentClickTime = System.currentTimeMillis();
                mPressedThread = new PressedThread();
                mLongPressedThread = new LongPressedThread();
                mBaseHandler.postDelayed(mPressedThread, PRESS_TIME);
                mBaseHandler.postDelayed(mLongPressedThread, LONG_PRESS_TIME);
                break;
            case MotionEvent.ACTION_MOVE: {
                boolean xMoved = Math.abs(event.getX() - mDownInScreenX) > mTouchSlop;
                boolean yMoved = Math.abs(event.getY() - mDownInScreenY) > mTouchSlop;

                if (xMoved || yMoved) {
                    mBaseHandler.removeCallbacks(mPressedThread);
                    mBaseHandler.removeCallbacks(mLongPressedThread);
                    mBaseHandler.post(new ReleaseThread());
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:{
                if (!this.isMoved()) {
                    if (System.currentTimeMillis() - mCurrentClickTime <= PRESS_TIME){
                        mBaseHandler.removeCallbacks(mPressedThread);
                    }

                    if (System.currentTimeMillis() - mCurrentClickTime <= LONG_PRESS_TIME) {
                        mBaseHandler.removeCallbacks(mLongPressedThread);
                    }
                } else {
                }
                mBaseHandler.post(new ReleaseThread());
                break;
            }
            default:
                break;
        }
        return true;
    }

    private boolean isMoved() {
        return !(Math.abs(mDownInScreenX - mCurrentInScreenX) <= mTouchSlop && Math.abs(mDownInScreenY - mCurrentInScreenY) <= mTouchSlop);
    }

    public void setTouchListener(TouchListener listener) {
        mTouchListener = listener;
    }

    public class LongPressedThread implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "LongTouchListener LongPressedThread");
            if (mTouchListener != null)
                mTouchListener.handleLongTouchEven();
        }
    }

    public class PressedThread implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "LongTouchListener PressedThread");
            if (mTouchListener != null)
                mTouchListener.handleTouchEven();
        }
    }

    public class ReleaseThread implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "LongTouchListener ReleaseThread");
            if (mTouchListener != null)
                mTouchListener.handleReleaseEven();
        }
    }

    public interface TouchListener {
        void handleLongTouchEven();

        void handleTouchEven();

        void handleReleaseEven();
    }
}
