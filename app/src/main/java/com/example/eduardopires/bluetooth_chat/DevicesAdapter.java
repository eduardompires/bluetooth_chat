package com.example.eduardopires.bluetooth_chat;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 212571132 on 10/31/16.
 */

public class DevicesAdapter extends SimpleSectionedAdapter<DevicesAdapter.ItemViewHolder>{

    private List<BluetoothDevice> bondedDevices = new ArrayList<BluetoothDevice>();
    private List<BluetoothDevice> availableDevices = new ArrayList<BluetoothDevice>();
    private Context context;

    public DevicesAdapter(Context context, List<BluetoothDevice> bondedDevices, List<BluetoothDevice> availableDevices) {
        this.context = context;
        this.bondedDevices = bondedDevices;
        this.availableDevices = availableDevices;
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
        return section == 0 ? "Dispositivos pareados" : "Dispositivos disponíveis";
    }

    @Override
    protected int getSectionCount() {
        return 2;
    }

    @Override
    protected int getItemCountForSection(int section) {
        switch (section) {
            case 0:
                return bondedDevices.size();
            case 1:
                return availableDevices.size();
        }
        return 0;
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pairing_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int section, int position) {
        String deviceName = "";
        switch (section) {
            case 0:
                deviceName = bondedDevices.get(position).getName();
                break;
            case 1:
                deviceName = availableDevices.get(position).getName();
                break;
        }

        holder.textView.setText(deviceName);
    }

    public void updateBondedDevices(List<BluetoothDevice> bondedDevices) {
        this.bondedDevices = bondedDevices;
        notifyDataSetChanged();
    }

    public void updateAvailableDevices(List<BluetoothDevice> availableDevices) {
        this.availableDevices = availableDevices;
        notifyDataSetChanged();
    }

    protected static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text_name);
        }
    }
}
