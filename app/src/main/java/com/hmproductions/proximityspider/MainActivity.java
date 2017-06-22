package com.hmproductions.proximityspider;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private static final String LOG_TAG = "Proximity Sensor Range:";

    TextView proximity_textView, secondsRemaining_textView, alarm_textView;
    ProgressBar progressBar;

    private static SensorManager mSensorManager;
    private static Sensor mSensor;
    CountDownTimer timer;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secondsRemaining_textView = (TextView)findViewById(R.id.secTextView);
        proximity_textView = (TextView)findViewById(R.id.proximity_textView);

        alarm_textView = (TextView)findViewById(R.id.alarm_textView);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mMediaPlayer = MediaPlayer.create(this, R.raw.default_alarm);
        mMediaPlayer.setLooping(true);

        CountDownTo10Seconds();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if(event.values[0] == 0) {
                proximity_textView.setText("NEAR");
                progressBar.setProgress(0);
                timer.start();
            }
            else {
                proximity_textView.setText("FAR");
                if(mMediaPlayer.isPlaying())
                    mMediaPlayer.pause();
                timer.cancel();
                alarm_textView.setTextColor(Color.parseColor("#000000"));
                alarm_textView.setTextSize(24);
                progressBar.setProgress(0);
            }
        }
    }

    private void CountDownTo10Seconds()
    {
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining_textView.setText(String.valueOf((1000+millisUntilFinished)/1000));
                progressBar.setProgress((int) ((10000 - millisUntilFinished) / 1000));
            }

            @Override
            public void onFinish() {
                mMediaPlayer.start();
                alarm_textView.setTextColor(Color.parseColor("#FF0000"));
                alarm_textView.setTextSize(30);
                progressBar.setProgress(progressBar.getMax());
                secondsRemaining_textView.setText(String.valueOf(10));
            }
        };
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }
}
