package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorNameTextView;
    private TextView sensorValueTextView;
    private TextView sensorValueTextView1;
    private TextView sensorValueTextView2;
    private TextView sensorValueTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorNameTextView = findViewById(R.id.sensor_name_label);
        sensorValueTextView = findViewById(R.id.sensor_value_label);
        sensorValueTextView1 = findViewById(R.id.sensor_value_label_1);
        sensorValueTextView2 = findViewById(R.id.sensor_value_label_2);
        sensorValueTextView3 = findViewById(R.id.sensor_value_label_3);

        Intent intent = getIntent();
        int sensorId = intent.getIntExtra(SensorActivity.SENSOR_ID_KEY, -1);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorId);

        if(sensor == null){
            sensorNameTextView.setText(R.string.missingSensor);
            sensorValueTextView1 = null;
            sensorValueTextView2 = null;
            sensorValueTextView3 = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float currentValue1 = event.values[0];
        float currentValue3 = 0;
        float currentValue2 = 0;
        if(event.values.length >= 3)
        {
            currentValue2 = event.values[1];
            currentValue3 = event.values[2];
        }
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:

            case Sensor.TYPE_GYROSCOPE:

            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorNameTextView.setText(getResources().getString(R.string.detailSensorName, sensor.getName()));
                sensorValueTextView.setText(R.string.manyValues);
                sensorValueTextView1.setText(getResources().getString(R.string.detailSensorValue1, currentValue1));
                sensorValueTextView2.setText(getResources().getString(R.string.detailSensorValue2, currentValue2));
                sensorValueTextView3.setText(getResources().getString(R.string.detailSensorValue3, currentValue3));
                break;

            default:
                sensorNameTextView.setText(R.string.missingSensor);
                sensorValueTextView.setVisibility(View.INVISIBLE);
                sensorValueTextView1.setVisibility(View.INVISIBLE);
                sensorValueTextView2.setVisibility(View.INVISIBLE);
                sensorValueTextView3.setVisibility(View.INVISIBLE);
                break;

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}