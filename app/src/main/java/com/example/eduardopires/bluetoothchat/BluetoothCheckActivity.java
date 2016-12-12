package com.example.eduardopires.bluetoothchat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static android.R.attr.name;
import static android.R.attr.permission;

/**
 * Created by eduardoPires on 11/12/2016.
 */

public class BluetoothCheckActivity extends AppCompatActivity {
    protected static final String TAG = "livroandroid";
    protected BluetoothAdapter btfAdapter;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        btfAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btfAdapter == null) {
            Toast.makeText(this, "Bluetooth não disponível neste dispositivo.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (btfAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth está ligado!", Toast.LENGTH_LONG).show();
        } else {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 0);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (btfAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth foi ligado!", Toast.LENGTH_LONG).show();
        }
    }
}
