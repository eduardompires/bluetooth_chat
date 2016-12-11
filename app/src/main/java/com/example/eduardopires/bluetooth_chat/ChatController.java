package com.example.eduardopires.bluetooth_chat;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by alex1 on 11/12/2016.
 */

public class ChatController {
    private static String TAG = "ChatController";
    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;
    private ChatListener listener;
    private boolean running;

    public interface ChatListener {
        void onMessageReceived(String msg);
    }

    public ChatController(ChatListener listener, BluetoothSocket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.running = true;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                running = true;
                byte[] bytes = new byte[1024];
                int length;
                while (running){
                    try {
                        length = in.read(bytes);
                        String msg = new String(bytes, 0, length);
                        Log.d(TAG, "Mensagem: " + msg);
                        listener.onMessageReceived(msg);
                    } catch (Exception e) {
                        running = false;
                        e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
