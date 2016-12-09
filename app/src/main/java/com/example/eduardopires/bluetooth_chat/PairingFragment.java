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

    private List<BluetoothDevice> bondedDevices = new ArrayList<BluetoothDevice>();
    private List<BluetoothDevice> availableDevices= new ArrayList<BluetoothDevice>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_paring, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        adapter = new PairingAdapter(getContext(), bondedDevices, availableDevices);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setBondedDevices(List<BluetoothDevice> bondedDevices) {
        if (bondedDevices != null) {
            this.bondedDevices = bondedDevices;
        }
    }

    public void setAvailableDevices(List<BluetoothDevice> availableDevices) {
        if (availableDevices != null) {
            this.availableDevices = availableDevices;
        }
    }

    public void updateLists(List<BluetoothDevice> availableDevices, List<BluetoothDevice> bondedDevices) {
        adapter.updateAvailableDevices(availableDevices);
        adapter.updateBondedDevices(bondedDevices);
    }

}
