package com.puerto7070.epsilonv2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MainActivity extends Activity implements Listener{

    //region Variables NFC

    public static final String TAG = MainActivity.class.getSimpleName();

    public EditText mEtMessage;
    public Button mBtRead;

    private NFCReadFragment mNfcReadFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;

    //endregion

    //region Variables Proximidad
    SensorManager sensorManager = null;

    Sensor proximitySensor = null;

    SensorEventListener proximitySensorListener = null;


    sensor_proximidad my_sensor_proximidad;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region NFC
        initViews();
        initNFC();
        //endregion

        //region Proximidad

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        my_sensor_proximidad = new sensor_proximidad(sensorManager, proximitySensor, proximitySensorListener );
        my_sensor_proximidad.check_sensor(getApplicationContext());

        //endregion

        TextView cronometro = findViewById(R.id.cronometro);

        cronometro my_cronometro = new cronometro("Nombre del cronómetro", cronometro);
        new Thread(my_cronometro).start();
    }

    //region Metodos NFC
    private void initViews() {

        mEtMessage = (EditText) findViewById(R.id.et_message);
        mBtRead = (Button) findViewById(R.id.btn_read);

        mBtRead.setOnClickListener(view -> showReadFragment());
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
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

            /*
            if (isDialogDisplayed) {

                if (isWrite) {

                    String messageToWrite = mEtMessage.getText().toString();

                } else {

                    mNfcReadFragment = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    mNfcReadFragment.onNfcDetected(ndef);
                }
            }*/
        }

        //endregion
    }
}
