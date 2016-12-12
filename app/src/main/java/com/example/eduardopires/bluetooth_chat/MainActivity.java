package com.example.eduardopires.bluetooth_chat;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int ENABLE_BLUETOOTH = 0;

    private BluetoothAdapter bluetoothAdapter;
    private FloatingActionButton refreshFab;
    private FloatingActionButton serverFab;
    private FloatingActionMenu menuFab;
    private List<BluetoothDevice> bondedDevices = new ArrayList<BluetoothDevice>();
    private List<BluetoothDevice> availableDevices = new ArrayList<BluetoothDevice>();
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private DevicesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bondedDevices = new ArrayList<>(bluetoothAdapter.getBondedDevices());
        Log.d("Bonded Devices", bluetoothAdapter.getBondedDevices().toString());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new DevicesAdapter(this, bondedDevices, availableDevices, onDeviceClickListener());
        recyclerView.setAdapter(adapter);

        if (bluetoothAdapter == null) {
            Snackbar.make(null, "Seu dispositivo não tem suporte a Bluetooth", Snackbar.LENGTH_SHORT).show();
            finish();
        } else if (bluetoothAdapter.isEnabled()){
            Snackbar.make(findViewById(R.id.root_view), "Bluetooth ligado", Snackbar.LENGTH_SHORT);
        } else {
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, ENABLE_BLUETOOTH);
        }

        menuFab = (FloatingActionMenu) findViewById(R.id.fab);
        menuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuFab.toggleMenu(true);
            }
        });

        refreshFab = (FloatingActionButton) findViewById(R.id.refresh_fab);

        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.startDiscovery();
                dialog = ProgressDialog.show(MainActivity.this, "BluetoothChat", "Buscando dispositivos...", false, true);
                registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
            }
        });

        serverFab = (FloatingActionButton) findViewById(R.id.server_fab);

        serverFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothChatServerActivity serverActivity = new BluetoothChatServerActivity();
                serverActivity.setAdapter(bluetoothAdapter);

                startActivity(new Intent(MainActivity.this, serverActivity.getClass()));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(findViewById(R.id.root_view), "Bluetooth ativado", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    availableDevices.add(device);
                    Log.d("Available Devices", availableDevices.toString());
                } else {
                    bondedDevices.add(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Snackbar.make(findViewById(R.id.root_view), "Busca iniciada", Snackbar.LENGTH_SHORT);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Snackbar.make(findViewById(R.id.root_view), "Busca finalizada", Snackbar.LENGTH_SHORT);
                updateLists(availableDevices, bondedDevices);
                dialog.dismiss();
            }
        }
    };

    public void updateLists(List<BluetoothDevice> availableDevices, List<BluetoothDevice> bondedDevices) {
        adapter.updateAvailableDevices(availableDevices);
        adapter.updateBondedDevices(bondedDevices);
    }

    private DevicesAdapter.OnDeviceClick onDeviceClickListener() {
        return new DevicesAdapter.OnDeviceClick() {
            @Override
            public void onClick(View view, int idx) {
                Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                intent.putExtra("device", bondedDevices.get(idx));
                startActivity(intent);
            }
        };
    }
}
