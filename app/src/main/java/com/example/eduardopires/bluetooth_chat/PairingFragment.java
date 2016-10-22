package com.example.eduardopires.bluetooth_chat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eduardopires.bluetooth_chat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PairingFragment extends Fragment {


    public PairingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paring, container, false);
    }

}
