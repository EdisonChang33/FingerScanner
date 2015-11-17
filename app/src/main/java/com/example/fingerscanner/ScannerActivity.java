package com.example.fingerscanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.helper.DensityUtils;
import com.example.helper.LongTouchListener;
import com.example.widget.WaveDrawable;

/**
 * @author EdisonChang
 */
public class ScannerActivity extends ActionBarActivity {

    private static final int TYPE_SCAN = 0;
    private static final int TYPE_FIND = 1;

    private FrameLayout layoutScan;
    private FrameLayout layoutFind;
    private int animationType;
    private LongTouchListener mLongTouchListener;
    private TranslateAnimation scanTranslateAnimation;
    private WaveDrawable waveDrawble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        mLongTouchListener = new LongTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initContent();
    }

    @Override
    protected void onPause() {

        if (waveDrawble != null)
            waveDrawble.stopAnimation();

        super.onPause();
    }

    private void initContent() {
        layoutScan = (FrameLayout) findViewById(R.id.user_scan);
        layoutFind = (FrameLayout) findViewById(R.id.user_find);

        mLongTouchListener.setTouchListener(new LongTouchListener.TouchListener() {
            @Override
            public void handleLongTouchEven() {
                if (animationType == TYPE_FIND) {
                }
            }

            @Override
            public void handleTouchEven() {
                if (animationType == TYPE_SCAN) {
                    animationType = TYPE_FIND;
                    playAnimation();
                }
            }

            @Override
            public void handleReleaseEven() {
                if (animationType == TYPE_FIND) {
                    animationType = TYPE_SCAN;
                    if (waveDrawble != null)
                        waveDrawble.stopAnimation();
                    playAnimation();
                }
            }
        });
        layoutScan.setOnTouchListener(mLongTouchListener);
        layoutFind.setOnTouchListener(mLongTouchListener);
        playAnimation();
    }

    private void playAnimation() {
        switch (animationType) {
            case TYPE_SCAN:
                layoutScan.setVisibility(View.VISIBLE);
                layoutFind.setVisibility(View.GONE);
                playScanAnimation();
                break;
            case TYPE_FIND:
                layoutScan.setVisibility(View.GONE);
                layoutFind.setVisibility(View.VISIBLE);
                playFindAnimation();
                break;
            default:
                break;
        }
    }

    private void playScanAnimation() {
        ImageView imageLine = (ImageView) layoutScan.findViewById(R.id.user_scan_2);
        scanTranslateAnimation = new TranslateAnimation(0.0f, 0.0f, 10.0f, DensityUtils.dp2px(getApplicationContext(), 110));
        scanTranslateAnimation.setDuration(3000);
        scanTranslateAnimation.setRepeatMode(Animation.RESTART);
        scanTranslateAnimation.setRepeatCount(Animation.INFINITE);
        imageLine.setAnimation(scanTranslateAnimation);
        scanTranslateAnimation.startNow();
    }

    private void playFindAnimation() {
        ImageView imageViewFind = (ImageView) layoutFind.findViewById(R.id.user_find_1);
        final ImageView imageViewWave = (ImageView) layoutFind.findViewById(R.id.user_find_wave);
        if (waveDrawble == null) {
            waveDrawble = new WaveDrawable(this, Color.parseColor("#ff6262"), DensityUtils.dp2px(getApplicationContext(), 40));
        }

        waveDrawble.setInterpolator(new LinearInterpolator());
        waveDrawble.setRepeatCount(Animation.INFINITE);
        waveDrawble.setWaveAnimatorListener(new WaveDrawable.WaveAnimatorListener() {
            @Override
            public void onAnimationEnd() {

            }
        });
        imageViewWave.setImageDrawable(waveDrawble);
        imageViewWave.setVisibility(View.GONE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(500);
        imageViewFind.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewWave.setVisibility(View.VISIBLE);
                waveDrawble.startAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        alphaAnimation.startNow();
    }

}
