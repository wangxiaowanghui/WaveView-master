package com.gelitenight.waveview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.gelitenight.waveview.library.WaveView;

public class MainActivity extends AppCompatActivity {

    private WaveHelper mWaveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WaveView waveView = (WaveView) findViewById(R.id.wave);

        mWaveHelper = new WaveHelper(waveView);

        ((RadioGroup) findViewById(R.id.shapeChoice))
            .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                }
            });

        ((SeekBar) findViewById(R.id.seekBar))
            .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        CompoundButtonCompat.setButtonTintList(
            (RadioButton) findViewById(R.id.colorDefault),
            getResources().getColorStateList(android.R.color.white));
        CompoundButtonCompat.setButtonTintList(
            (RadioButton) findViewById(R.id.colorRed),
            getResources().getColorStateList(R.color.red));
        CompoundButtonCompat.setButtonTintList(
            (RadioButton) findViewById(R.id.colorGreen),
            getResources().getColorStateList(R.color.green));
        CompoundButtonCompat.setButtonTintList(
            (RadioButton) findViewById(R.id.colorBlue),
            getResources().getColorStateList(R.color.blue));

        ((RadioGroup) findViewById(R.id.colorChoice))
            .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                }
            });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start(0.7f,0.1f);
    }
}
