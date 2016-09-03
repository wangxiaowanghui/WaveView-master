/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gelitenight.waveview.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {

    // 初试高度占据整个高度的比例
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    // 整个水波在view中的偏移
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    // 前景色和背景色
    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#ff66c1f1");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#ff32adfa");

    // shader containing repeated waves
    private BitmapShader mWaveBackShader;
    private BitmapShader mWaveForeShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewBackPaint;
    private Paint mViewForePaint;

    // 垂直和水平的便宜量
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveBackShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private float mWaveForeShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private float mAmplitudeRatio = 0.05f;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewBackPaint = new Paint();
        mViewBackPaint.setAntiAlias(true);

        mViewForePaint = new Paint();
        mViewForePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createBackShader();
        createForeShader();
    }

    private float getRealCalHeight() {
        return getHeight() - getAmplitude();
    }

    private float getWaterLevel() {
        return (getRealCalHeight()) * DEFAULT_WATER_LEVEL_RATIO + getAmplitude();
    }

    private float getAmplitude() {
        return ScreenUtil.dp2px(getContext().getApplicationContext(), 4);
    }

    /**
     * 创建背景色的shader
     */
    private void createBackShader() {
        double mDefaultAngularFrequency = 2.0f * Math.PI / getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        wavePaint.setColor(DEFAULT_BEHIND_WAVE_COLOR);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (getWaterLevel() + getAmplitude() * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
        }

        // 创建BitmapShader
        mWaveBackShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewBackPaint.setShader(mWaveBackShader);
    }

    /**
     * 创建前景色的shader
     */
    private void createForeShader() {
        double mDefaultAngularFrequency = 2.0f * Math.PI / getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        wavePaint.setColor(DEFAULT_FRONT_WAVE_COLOR);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            // 前景波比背景略低一些
            float beginY = (float) (getWaterLevel() + ScreenUtil.dp2px(getContext().getApplicationContext(), 7) + getAmplitude() * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
        }

        // 创建BitmapShader
        mWaveForeShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewForePaint.setShader(mWaveForeShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mViewBackPaint.getShader() == null) {
            mViewBackPaint.setShader(mWaveBackShader);
        }

        if (mViewForePaint.getShader() == null) {
            mViewForePaint.setShader(mWaveForeShader);
        }

        mShaderMatrix.setScale(1, mAmplitudeRatio / 0.05f, 0, getWaterLevel());
        // 计算横向和纵向的偏移，横向按照mWaveShiftRatio，纵向按照初试绘制高度和要求高度的差值
        mShaderMatrix.postTranslate(
                mWaveBackShiftRatio * getWidth(),
                (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getRealCalHeight());

        // assign matrix to invalidate the shader
        mWaveBackShader.setLocalMatrix(mShaderMatrix);

        mShaderMatrix.setScale(0.5f, mAmplitudeRatio / 0.05f, 0, getWaterLevel());
        // 计算横向和纵向的偏移，横向按照mWaveShiftRatio，纵向按照初试绘制高度和要求高度的差值
        mShaderMatrix.postTranslate(
                mWaveForeShiftRatio * getWidth(),
                (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getRealCalHeight());
        mWaveForeShader.setLocalMatrix(mShaderMatrix);

        canvas.drawRect(0, 0, getWidth(), getHeight(), mViewBackPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mViewForePaint);
    }

    public float getWaveBackShiftRatio() {
        return mWaveBackShiftRatio;
    }

    public void setWaveBackShiftRatio(float waveShiftRatio) {
        if (mWaveBackShiftRatio != waveShiftRatio) {
            mWaveBackShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaveForeShiftRatio() {
        return mWaveForeShiftRatio;
    }

    public void setWaveForeShiftRatio(float waveShiftRatio) {
        if (mWaveForeShiftRatio != waveShiftRatio) {
            mWaveForeShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }
}
