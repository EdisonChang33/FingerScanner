package com.example.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import com.example.helper.DensityUtils;

/**
 * @author EdisonChang
 */
public class WaveDrawable extends Drawable {

    private Paint wavePaint;
    private int color;
    private int radius;
    private long animationTime = 1000;

    protected float waveScale;
    protected float waveStrokeWidth;
    protected int alpha;
    protected int repeatCount;

    protected int strokeSize;
    private Interpolator interpolator;

    private Animator animator;
    private AnimatorSet animatorSet;
    private WaveAnimatorListener waveAnimatorListener;

    public WaveDrawable(Context context, int color, int radius, long animationTime) {
        this(context, color, radius);
        this.animationTime = animationTime;
    }

    public WaveDrawable(Context context, int color, int radius) {
        this.color = color;
        this.radius = radius;
        this.waveScale = 1f;
        strokeSize = DensityUtils.dp2px(context.getApplicationContext(), 15);
        this.waveStrokeWidth = (float) strokeSize;

        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animatorSet = new AnimatorSet();

    }

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();

        // circle
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setColor(color);
        wavePaint.setStrokeWidth(waveStrokeWidth);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius * waveScale, wavePaint);
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidateSelf();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void startAnimation() {
        animator = generateAnimation();
        animator.start();
    }

    public void setRepeatCount(int repeatCount){
        this.repeatCount = repeatCount;
    }

    public void stopAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.end();
        }
    }

    public boolean isAnimationRunning() {
        if (animator != null) {
            return animator.isRunning();
        }
        return false;
    }

    public void setWaveAnimatorListener(WaveAnimatorListener waveAnimatorListener){
        this.waveAnimatorListener = waveAnimatorListener;
    }
    @Override
    public void setColorFilter(ColorFilter cf) {
        wavePaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return wavePaint.getAlpha();
    }

    protected void setWaveScale(float waveScale) {
        this.waveScale = waveScale;
        invalidateSelf();
    }

    protected float getWaveScale() {
        return waveScale;
    }

    protected void setWaveStrokeWidth(float strokeWidth) {
        this.waveStrokeWidth = strokeWidth;
        invalidateSelf();
    }

    protected float getWaveStrokeWidth() {
        return waveStrokeWidth;
    }

    private Animator generateAnimation() {

        ObjectAnimator waveAnimator = ObjectAnimator.ofFloat(this, "waveScale", 1f, 3f);
        waveAnimator.setDuration(animationTime);
        if (interpolator != null) {
            waveAnimator.setInterpolator(interpolator);
        }

        waveAnimator.setRepeatCount(repeatCount);
        waveAnimator.setRepeatMode(Animation.INFINITE);

        ObjectAnimator strokeAnimator = ObjectAnimator.ofFloat(this, "waveStrokeWidth", strokeSize, 0f);
        strokeAnimator.setDuration(animationTime);
        if (interpolator != null) {
            strokeAnimator.setInterpolator(interpolator);
        }
        strokeAnimator.setRepeatCount(repeatCount);
        strokeAnimator.setRepeatMode(Animation.INFINITE);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (waveAnimatorListener != null){
                    waveAnimatorListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.playTogether(waveAnimator, strokeAnimator);
        return animatorSet;
    }

    public interface WaveAnimatorListener{
        void onAnimationEnd();
    }
}
