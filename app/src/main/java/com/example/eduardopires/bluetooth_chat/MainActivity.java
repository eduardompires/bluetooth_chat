package com.example.eduardopires.bluetooth_chat;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static int ENABLE_BLUETOOTH = 0;
    private static int SELECT_PAIRED_DEVICE = 1;
    private static int SELECT_DISCOVERED_DEVICE = 2;

    private ViewPager pager;
    private BluetoothAdapter bluetoothAdapter;
    private FloatingActionButton fab;
    private List<BluetoothDevice> bondedDevices = new ArrayList<BluetoothDevice>();
    private List<BluetoothDevice> availableDevices = new ArrayList<BluetoothDevice>();
    private ProgressDialog dialog;
    private PairingFragment fragment = new PairingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        pager = (ViewPager) findViewById(R.id.pager);

        tabLayout.setupWithViewPager(pager);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bondedDevices = new ArrayList<>(bluetoothAdapter.getBondedDevices());
        Log.d("Bonded Devices", bluetoothAdapter.getBondedDevices().toString());

        setupViewPager(pager);

        if (bluetoothAdapter == null) {
            Snackbar.make(null, "Seu dispositivo não tem suporte a Bluetooth", Snackbar.LENGTH_SHORT).show();
            finish();
        } else if (bluetoothAdapter.isEnabled()){
            Snackbar.make(findViewById(R.id.root_view), "Bluetooth ligado", Snackbar.LENGTH_SHORT);
        } else {
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, ENABLE_BLUETOOTH);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.startDiscovery();
                dialog = ProgressDialog.show(MainActivity.this, "BluetoothChat", "Buscando dispositivos...", false, true);
                registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
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

    private void setupViewPager(ViewPager viewPager) {
        BluetoothPagerAdapter adapter = new BluetoothPagerAdapter(getSupportFragmentManager());

        fragment.setBondedDevices(bondedDevices);
        fragment.setAvailableDevices(availableDevices);

        adapter.addFragment(fragment, "Parear");
        adapter.addFragment(new ConversationsFragment(), "Conversas");
        viewPager.setAdapter(adapter);
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
                fragment.updateLists(availableDevices, bondedDevices);
                dialog.dismiss();
            }
        }
    };
}
