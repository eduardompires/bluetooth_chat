package com.example.eduardopires.bluetooth_chat;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

/**
 * Created by 212571132 on 10/31/16.
 */

public class PairingAdapter extends SectionedRecyclerViewAdapter {
    @Override
    public int getSectionCount() {
        return 0;
    }

    @Override
    public int getItemCount(int section) {
        return 0;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
