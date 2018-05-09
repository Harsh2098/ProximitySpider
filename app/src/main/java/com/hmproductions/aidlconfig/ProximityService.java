package com.hmproductions.aidlconfig;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class ProximityService extends Service implements SensorEventListener {

    private boolean currentDistance;

    private SensorManager mSensorManager;

    private class ProximityImpl extends IProximityInterface.Stub {

        @Override
        public boolean isNear() {
            return currentDistance;
        }
    }

    // Constructor
    public ProximityService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ProximityImpl();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)
            currentDistance = sensorEvent.values[0] == 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager != null) {
            Sensor proximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }
}
