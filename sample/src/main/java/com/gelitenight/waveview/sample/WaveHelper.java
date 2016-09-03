package com.gelitenight.waveview.sample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.gelitenight.waveview.library.WaveView;

import java.util.ArrayList;
import java.util.List;

public class WaveHelper {
    private WaveView mWaveView;

    private AnimatorSet mAnimatorSet;

    public WaveHelper(WaveView waveView) {
        mWaveView = waveView;
    }

    public void start(float oldLevel, float newLevel) {
        List<Animator> animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveBackShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveBackShiftRatio", 0f, 1f);
        waveBackShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveBackShiftAnim.setDuration(5000);
        waveBackShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveBackShiftAnim);

        ObjectAnimator waveForeShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveForeShiftRatio", 0f, 1f);
        waveForeShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveForeShiftAnim.setDuration(3000);
        waveForeShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveForeShiftAnim);

        // vertical animation.
        // water level increases from 0 to center of WaveView
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", oldLevel, newLevel);
        waterLevelAnim.setDuration(4000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waterLevelAnim);

        // amplitude animation.
        // wave grows big then grows small, repeatedly
//        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
//                mWaveView, "amplitudeRatio", 0.02f, 0.05f);
//        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
//        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
//        amplitudeAnim.setDuration(5000);
//        amplitudeAnim.setInterpolator(new LinearInterpolator());
//        animators.add(amplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);

        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    public void cancel() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }
}
