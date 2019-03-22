package com.puerto7070.epsilon;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class sensor_proximidad {
    SensorManager sensorManager;
    Sensor proximitySensor;
    SensorEventListener proximitySensorListener;
    public static final String TAG="HOLA";
    //constructor de clase
    public sensor_proximidad(SensorManager sensorManager, Sensor proximitySensor, SensorEventListener proximitySensorListener)
    {
        this.sensorManager = sensorManager;
        this.proximitySensor = proximitySensor;
        this.proximitySensorListener = proximitySensorListener;

    }
    //detecta si el sensor de proximidad está disponible
    public void check_sensor(Context context)
    {
        final Context my_context = context;
        if (proximitySensor == null) {
            Toast toast =
                    Toast.makeText(my_context,
                            "El sensor está ocupado vuelve a intentarlo", Toast.LENGTH_SHORT);

            toast.show();
        }
    }
    //suscribe el sensor a eventos y lo revisa cada 2 segundos
    public void registerListener(Context context)
    {
        final Context my_context = context;
        proximitySensorListener = new SensorEventListener() {
            //cuando el sensor detecta un cambio
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
                if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    // Detected something nearby
                    Toast toast =
                            Toast.makeText(my_context,
                                    "No tapes el sensor payaso", Toast.LENGTH_SHORT);

                    toast.show();

                } else {
                    // Nothing is nearby
                    Toast toast =
                            Toast.makeText(my_context,
                                    "Gracias", Toast.LENGTH_SHORT);

                    toast.show();

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        // Register it, specifying the polling interval in
        // microseconds
        sensorManager.registerListener(proximitySensorListener,
                proximitySensor, 2 * 1000 * 1000);
    }
    //dessuscribe el sensor de los eventos
    public void unregisterListener()
    {
        sensorManager.unregisterListener(proximitySensorListener);
    }


}
