package com.puerto7070.epsilonv2;

import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;


public class cronometro implements Runnable{

    // Atributos privados de la clase
    private TextView etiq; // Etiqueta para mostrar la información
    private String nombrecronometro; // Nombre del cronómetro
    private int segundos, minutos, horas; // Segundos, minutos y horas que lleva activo el cronómetro
    private Handler escribirenUI; // Necesario para modificar la UI
    private Boolean pausado; // Para pausar el cronómetro
    private String salida; // Salida formateada de los datos del cronómetro

    private boolean running;
    private boolean alarm_setted;
    private int alarm_seconds;
    private Context context;
    private int elapsed_minutes;
    private int elapsed_hours;
    private boolean advices;
    private Ringtone r;
    /**
     * Constructor de la clase
     * @param nombre Nombre del cronómetro
     * @param etiqueta Etiqueta para mostrar información
     */
    public cronometro(String nombre, TextView etiqueta, Context ctx)
    {
        etiq = etiqueta;
        salida = "";
        segundos = 0;
        minutos = 0;
        horas = 0;
        nombrecronometro = nombre;
        escribirenUI = new Handler();
        pausado = Boolean.FALSE;
        alarm_setted = Boolean.FALSE;
        running = Boolean.TRUE;
        alarm_seconds = 0;
        context = ctx;
    }

    //Constructor de alarmas
    public cronometro(String nombre, Context ctx, int alarm)
    {
        salida = "";
        segundos = 0;
        minutos = 0;
        horas = 0;
        nombrecronometro = nombre;
        escribirenUI = new Handler();
        pausado = Boolean.FALSE;
        alarm_setted = Boolean.TRUE;
        running = Boolean.TRUE;
        alarm_seconds = alarm;
        context = ctx;
    }

    //Constructor de termómetros
    public cronometro(String nombre, TextView etiqueta, Context ctx, boolean adv)
    {

        etiq = etiqueta;
        salida = "";
        segundos = 0;
        minutos = 0;
        horas = 0;
        nombrecronometro = nombre;
        escribirenUI = new Handler();
        pausado = Boolean.FALSE;
        running = Boolean.TRUE;
        advices = adv;

        context = ctx;
        Calendar calendar = Calendar.getInstance();
        Log.d("EEEEEEEEEEE", String.valueOf((calendar.get(Calendar.HOUR_OF_DAY))));
        Log.d("EEEEEEEEEEE", String.valueOf((calendar.get(Calendar.SECOND))));
        Log.d("EEEEEEEEEEE", String.valueOf((calendar.get(Calendar.MINUTE))));

        r = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
    }

    @Override
    /**
     * Acción del cronómetro, contar tiempo en segundo plano
     */
    public void run()
    {
        try
        {
            while(running)
            {
                Thread.sleep(1000);
                salida = "";
                if( !pausado )
                {
                    segundos++;
                    if(segundos == 60)
                    {
                        segundos = 0;
                        minutos++;
                        if(advices)
                            elapsed_minutes++;
                    }
                    if(minutos == 60)
                    {
                        minutos = 0;
                        horas++;
                        if(advices)
                            elapsed_hours++;
                    }
                    // Formateo la salida
                    salida += "0";
                    salida += horas;
                    salida += ":";
                    if( minutos <= 9 )
                    {
                        salida += "0";
                    }
                    salida += minutos;
                    salida += ":";
                    if( segundos <= 9 )
                    {
                        salida += "0";
                    }
                    salida += segundos;
                    // Modifico la UI
                    if(etiq != null)
                    {
                        try
                        {
                            escribirenUI.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    etiq.setText(salida);
                                }
                            });
                        }
                        catch (Exception e)
                        {
                            Log.i("Cronometro", "Error en el cronometro " + nombrecronometro + " al escribir en la UI: " + e.toString());
                        }
                    }

                    if(advices)
                    {
                        if(segundos == 5)
                            RingTheAlarm();


                    }

                    if(alarm_setted)
                    {
                        alarm_seconds--;
                        if(alarm_seconds == 0)
                        {
                            alarm_setted = Boolean.FALSE;
                            System.exit(0);
                            running = Boolean.FALSE;
                        }

                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            Log.i("Cronometro", "Error en el cronometro " + nombrecronometro + ": " + e.toString());
        }
    }

    public void RingTheAlarm()
    {

        r.play();
    }

    public void PauseTheAlarm(){
        r.stop();
    }
    public void SetAlarm(int seconds)
    {
        alarm_setted = Boolean.TRUE;
        alarm_seconds = seconds;
    }

    public void reiniciar()
    {
        segundos = 0;
        minutos = 0;
        horas = 0;
        pausado = Boolean.FALSE;
    }

    public void pause()
    {
        pausado = !pausado;
    }
}
