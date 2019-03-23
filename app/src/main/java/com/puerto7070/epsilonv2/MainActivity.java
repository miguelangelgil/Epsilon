package com.puerto7070.epsilonv2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.FirebaseApp;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends Activity implements Listener{

    //region Variables NFC

    public static final String TAG = MainActivity.class.getSimpleName();

    public EditText mEtMessage;
    public Button mBtRead;
    public TextView tViCrono;
    private NFCReadFragment mNfcReadFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;

    //endregion

    // region Variables CRONO

    private EditText mEtAlarmSeconds;
    public Button mBtSetAlarm;
    private List<cronometro> cronometros;
    //endregion


    cronometro my_cronometro;
    View info_layout;
    Button mBtShowInfo;
    Button mBtHideInfo;

    //region Variables Proximidad
    SensorManager sensorManager = null;

    Sensor proximitySensor = null;

    SensorEventListener proximitySensorListener = null;


    sensor_proximidad my_sensor_proximidad;

    //endregion

    //region Variables CreateTask

    public EditText task_name_edit_text;
    public EditText project_name_edit_text;
    public Button open_create_task_menu;
    public Button create_task;
    public Button cancel;

//endregion

    TextView cronometro = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        cronometro = findViewById(R.id.cronometro);
        my_cronometro = new cronometro("Nombre del cronómetro", cronometro, this); new Thread(my_cronometro).start();
        //region Control de tiempo
        //my_control_tiempo = new control_tiempo(my_cronometro, getApplicationContext());
        //new Thread(my_control_tiempo).start();

        //end Control de tiempo

        //region NFC
        initViews();
        initNFC();
        //endregion

        //region Proximidad

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        my_sensor_proximidad = new sensor_proximidad(sensorManager, proximitySensor, proximitySensorListener,this );
        my_sensor_proximidad.check_sensor(getApplicationContext());

        //endregion

        tViCrono = findViewById(R.id.cronometro);

        initCreateTaskViews();


    }
    public void Active_cronometro(){ if(my_cronometro==null){ my_cronometro = new cronometro("Nombre del cronómetro", cronometro, this); new Thread(my_cronometro).start();}else{my_cronometro.pause();}}
    public void Desactive_cronometro(){my_cronometro.pause();}

    //region Metodos NFC
    private void initViews() {

        mEtMessage = (EditText) findViewById(R.id.et_message);
        mBtRead = (Button) findViewById(R.id.btn_read);

        mBtRead.setOnClickListener(view -> showReadFragment());

        mEtAlarmSeconds = (EditText) findViewById(R.id.et_seconds);
        mBtSetAlarm = (Button) findViewById(R.id.bt_seconds);

        mBtSetAlarm.setOnClickListener(view -> startCrono());//setAlarm(Integer.parseInt(mEtAlarmSeconds.getText().toString())));

        info_layout = findViewById(R.id.info_layout);
        mBtShowInfo = findViewById(R.id.bt_show_info);

        mBtShowInfo.setOnClickListener(view -> showInfo());

        mBtHideInfo = findViewById(R.id.bt_hide_info);

        mBtHideInfo.setOnClickListener(view -> hideInfo());
    }

    private void showInfo()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_enter);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                info_layout.setVisibility(View.VISIBLE);

            }
            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                //info_layout.setVisibility(View.GONE);
            }
        });
        info_layout.startAnimation(animation);
    }

    private void hideInfo()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_exit);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                info_layout.setVisibility(View.GONE);
            }
        });
        info_layout.startAnimation(animation);
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    private void setAlarm(int seconds)
    {
        final String TAG = "MyActivity";
        Log.d(TAG, String.valueOf(seconds));
        if(seconds > 0)
        {
            cronometro cron = new cronometro("cronometro", this, seconds);
            new Thread(cron).start();
        }
    }

    private void startCrono()
    {
        my_cronometro = new cronometro("Nombre del cronómetro", tViCrono, this, true);
        new Thread(my_cronometro).start();
    }

    public void sendNotification(View view) {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//

        mNotificationManager.notify(001, mBuilder.build());
    }

    private void showReadFragment() {

        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        if (mNfcReadFragment == null) {

            mNfcReadFragment = NFCReadFragment.newInstance();
        }
        mNfcReadFragment.show(getFragmentManager(),NFCReadFragment.TAG);

    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }

    //endregion

    @Override
    protected void onResume() {
        super.onResume();

        //region On Resume NFC

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

        //endregion

        //region On Resume proximidad

        my_sensor_proximidad.registerListener(getApplicationContext());

        //endregion
        Desactive_cronometro();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //region On Pause NFC
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);

        //endregion

        //region On Pause proximidad

        my_sensor_proximidad.unregisterListener();

        //endregion
    }

    @Override
    protected void onNewIntent(Intent intent) {

        //region On New Intent NFC
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            //TODO Qué pasa cuando se detecta el NFC
            Active_cronometro();


            if (isDialogDisplayed) {

                if (isWrite) {

                    String messageToWrite = mEtMessage.getText().toString();

                } else {

                    mNfcReadFragment = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    mNfcReadFragment.onNfcDetected(ndef);
                }
            }
        }

        //endregion
    }

    private void initCreateTaskViews(){
        project_name_edit_text = (EditText) findViewById(R.id.project_name);
        task_name_edit_text    = (EditText) findViewById(R.id.task_name);

        open_create_task_menu  = (Button) findViewById(R.id.create_task);
        create_task            = (Button) findViewById(R.id.create_task_button);
        cancel                 = (Button) findViewById(R.id.cancel);

        mBtRead.setOnClickListener(view -> showReadFragment())
        open_create_task_menu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                project_name_edit_text.setVisibility(View.VISIBLE);
                project_name_edit_text.setText(null);
                task_name_edit_text.setVisibility(View.VISIBLE);
                task_name_edit_text.setText(null);
                create_task.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                project_name_edit_text.setVisibility(View.GONE);
                task_name_edit_text.setVisibility(View.GONE);
                create_task.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
        });

        create_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String given_project_name = project_name_edit_text.getText().toString();
                String given_task_name = task_name_edit_text.getText().toString();


                //Checks if the message has anything.
                if (given_project_name.length() == 0 || given_task_name.length() == 0) {
                    return;
                }

                TaskManager.CreateTask(given_task_name, given_project_name);

                //TODO Algo tiene que pasar en el layout para que se actualice con la nueva task creada

                project_name_edit_text.setVisibility(View.GONE);
                task_name_edit_text.setVisibility(View.GONE);
                create_task.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);

            }
        });



    }

}
