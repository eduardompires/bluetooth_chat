package com.example.eduardopires.bluetooth_chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 212571132 on 12/12/16.
 */

public class BluetoothChatServerActivity extends ConversationActivity implements ChatController.ChatListener {
    private static final int DURATION_SECONDS = 300;
    private static final UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private boolean running;
    private BluetoothServerSocket serverSocket;
    private BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DURATION_SECONDS);
        startActivity(discoverableIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new ChatThread().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
        }
    }

    private class ChatThread extends Thread {
        @Override
        public void run() {
            try {
                serverSocket = adapter.listenUsingRfcommWithServiceRecord("Chat", uuid);
                BluetoothSocket socket = serverSocket.accept();
                if (socket != null) {
                    final BluetoothDevice device = socket.getRemoteDevice();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSupportActionBar().setTitle(device.getName());
                            findViewById(R.id.btn_send_message).setEnabled(true);
                        }
                    });

                    chat = new ChatController(BluetoothChatServerActivity.this, socket);
                    chat.start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapter(BluetoothAdapter adapter) {
        this.adapter = adapter;
    }
}
