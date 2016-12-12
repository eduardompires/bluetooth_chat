package com.example.eduardopires.bluetoothchat;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by eduardoPires on 11/12/2016.
 */

public class ChatController {
    private static final String TAG = "chat";
    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;
    private ChatListener listener;
    private boolean running;

    public interface ChatListener {
        public void onMessageReceived(String msg);
    }

    public ChatController(BluetoothSocket socket, ChatListener listener) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.listener = listener;
        this.running = true;
    }

    // Start reading InputStream
    public void start() {
        new Thread() {
            @Override
            public void run() {
                running = true;
                //Read
                byte[] bytes = new byte[1024];
                int length;
                //loop to receive messages
                while (running) {
                    try {
                        Log.d(TAG, "Aguardando mensagem");
                        //Reads the message
                        length = in.read(bytes);
                        String msg = new String(bytes, 0, length);
                        Log.d(TAG, "Mensagem: " + msg);
                        //Received the message
                        listener.onMessageReceived(msg);
                    } catch (Exception e) {
                        running = false;
                        Log.e(TAG, "Error: " + e.getMessage(), e);
                    }
                }
            }
        }.start();
    }

    public void sendMessage(String msg) throws IOException {
        if (out != null) {
            out.write(msg.getBytes());
        }
    }

    public void stop() {
        running = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
        }
    }
}