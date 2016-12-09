package com.example.eduardopires.bluetooth_chat;


import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.example.eduardopires.bluetooth_chat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class PairingFragment extends Fragment {

    private RecyclerView recyclerView;
    private PairingAdapter adapter;

    private List<String> bondedDevicesName = new ArrayList<String>();
    private List<String> availableDevicesName= new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_paring, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        adapter = new PairingAdapter(getContext(), bondedDevicesName, availableDevicesName);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setBondedDevicesName(Set<BluetoothDevice> bondedDevices) {
        for (BluetoothDevice device: bondedDevices) {
            String name = device.getName();
            bondedDevicesName.add(name);
        }
    }

    public void setAvailableDevicesName(Set<BluetoothDevice> availableDevices) {
        for (BluetoothDevice device: availableDevices) {
            String name = device.getName();
            bondedDevicesName.add(name);
        }
    }

}
