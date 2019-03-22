package com.puerto7070.epsilon;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager = null;

    Sensor proximitySensor = null;

    SensorEventListener proximitySensorListener = null;


    sensor_proximidad my_sensor_proximidad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        my_sensor_proximidad = new sensor_proximidad(sensorManager, proximitySensor, proximitySensorListener );
        my_sensor_proximidad.check_sensor(getApplicationContext());

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        my_sensor_proximidad.registerListener(getApplicationContext());

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        my_sensor_proximidad.unregisterListener();
    }
}
