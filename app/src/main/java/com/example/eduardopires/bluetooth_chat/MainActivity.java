package com.example.eduardopires.bluetooth_chat;

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
import android.view.View;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static int ENABLE_BLUETOOTH = 1;
    private static int SELECT_PAIRED_DEVICE = 2;
    private static int SELECT_DISCOVERED_DEVICE = 3;

    private ViewPager pager;
    private BluetoothAdapter bluetoothAdapter;
    private FloatingActionButton fab;
    private Set<BluetoothDevice> bondedDevices;
    private Set<BluetoothDevice> availableDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        pager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(pager);

        tabLayout.setupWithViewPager(pager);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Snackbar.make(null, "Seu dispositivo não tem suporte a Bluetooth", Snackbar.LENGTH_SHORT).show();
            finish();
        } else if (!bluetoothAdapter.isEnabled()){
            Intent bluetoothIntent = new Intent(BLUETOOTH_SERVICE);
            startActivityForResult(bluetoothIntent, ENABLE_BLUETOOTH);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.startDiscovery();

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(null, "Bluetooth ativado", Snackbar.LENGTH_SHORT).show();
                bondedDevices = bluetoothAdapter.getBondedDevices();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    private void setupViewPager(ViewPager viewPager) {
        BluetoothPagerAdapter adapter = new BluetoothPagerAdapter(getSupportFragmentManager());

        PairingFragment fragment = new PairingFragment();
        fragment.setBondedDevicesName(bondedDevices);

        adapter.addFragment(new PairingFragment(), "Parear");
        adapter.addFragment(new ConversationsFragment(), "Conversas");
        viewPager.setAdapter(adapter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                availableDevices.add(device);
            }
        }
    };
}
