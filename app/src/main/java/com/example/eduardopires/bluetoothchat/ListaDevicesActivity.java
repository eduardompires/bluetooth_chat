package com.example.eduardopires.bluetoothchat;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by eduardoPires on 11/12/2016.
 */

public class ListaDevicesActivity extends BluetoothCheckActivity implements AdapterView.OnItemClickListener {
        private ProgressDialog dialog;
        protected List<BluetoothDevice> lista;
        private ListView listview;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lista_devices);
            listview = (ListView) findViewById(R.id.listView);
            lista = new ArrayList<BluetoothDevice>(btfAdapter.getBondedDevices());
            this.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        }
        @Override
        protected void onResume() {
            super.onResume();
            if (btfAdapter.isDiscovering()) {
                btfAdapter.cancelDiscovery();
            }
            btfAdapter.startDiscovery();
            dialog = ProgressDialog.show(this, "Exemplo", "Buscando dispositivos Bluetooth...", false, true);
            }
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            private int count;

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        lista.add(device);
                        Toast.makeText(context, "Encontrou: " + device.getName() + ":" + device.getAddress(), Toast.LENGTH_SHORT).show();
                        count++;
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    count = 0;
                    Toast.makeText(context, "Busca iniciada. ", Toast.LENGTH_SHORT).show();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Toast.makeText(context, "Busca finalizada. " + count + " devices encontrados", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    updateLista();
                }
            }
        };

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (btfAdapter != null) {
                btfAdapter.cancelDiscovery();
            }
                this.unregisterReceiver(mReceiver);
        }

        private void updateLista() {
            List<String> nomes = new ArrayList<String>();
            for (BluetoothDevice device : lista) {
                boolean pareado = device.getBondState() == BluetoothDevice.BOND_BONDED;
                nomes.add(device.getName() + " - " + device.getAddress() + (pareado ? " *pareado" : " "));
            }
            int layout = android.R.layout.simple_list_item_1;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout, nomes);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(this);
        }
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int idx, long id) {
            BluetoothDevice device = lista.get(idx);
            Intent intent = new Intent(this, BluetoothCheckActivity.class);
            intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
            startActivity(intent);
        }
}