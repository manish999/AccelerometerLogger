package com.rampgreen.acceldatacollector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Listener implements SensorEventListener {

    private long startTime;
    private int numSamples;
    private boolean isActive = true;
    private double samplingRate = 0.0;
    private DummySectionFragment accelerometerTest;
    
    public Listener(DummySectionFragment accelerometerTest) {
        this.accelerometerTest = accelerometerTest;
    }
    public double getSamplingRate() {
        return samplingRate;
    }
    public void startRecording() {
        startTime = System.currentTimeMillis();
        numSamples = 0;
        isActive = true;
    }
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    	Log.e("SENSOR CHANGED", isActive +"");
        if (isActive) {
            numSamples++;
            long now = System.currentTimeMillis();
            if (now >= startTime + 5000) {
                samplingRate = numSamples / ((now - startTime) / 1000.0);                
                isActive = false;
                accelerometerTest.displayRates();
            }
        }
    }
}
