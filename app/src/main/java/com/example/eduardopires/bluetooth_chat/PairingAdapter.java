package com.example.eduardopires.bluetooth_chat;

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

public class PairingAdapter extends SimpleSectionedAdapter<PairingAdapter.ItemViewHolder>{

    private List<String> bondedDevices = new ArrayList<String>();
    private List<String> availableDevices = new ArrayList<String>();
    private Context context;

    public PairingAdapter(Context context, List<String> bondedDevices, List<String> availableDevices) {
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
                deviceName = bondedDevices.get(position);
                break;
            case 1:
                deviceName = availableDevices.get(position);
                break;
        }

        holder.textView.setText(deviceName);
    }


    protected static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text_name);
        }
    }
}
