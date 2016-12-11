package com.example.eduardopires.bluetooth_chat;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConversationActivity extends AppCompatActivity implements ChatController.ChatListener {

    protected static final UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    protected BluetoothDevice device;
    protected ChatController chat;

    private EditText etMessageToSend;
    private ImageButton btnSendMessage;
    private RecyclerView conversationRecylerView;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        device = getIntent().getParcelableExtra("device");
        etMessageToSend = (EditText) findViewById(R.id.et_message);
        btnSendMessage = (ImageButton) findViewById(R.id.btn_send_message);
        conversationRecylerView = (RecyclerView) findViewById(R.id.conversation_list);
        conversationRecylerView.setLayoutManager(new LinearLayoutManager(this));
        conversationRecylerView.setHasFixedSize(true);

        btnSendMessage.setEnabled(false);

        adapter = new MessageAdapter(this, messages);

        try {
            if (device != null) {
                getSupportActionBar().setTitle("Conectado: " + device.getName());
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();

                chat = new ChatController(this, socket);
                chat.start();
                btnSendMessage.setEnabled(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToSendString = etMessageToSend.getText().toString();
                try {
                    chat.sendMessage(messageToSendString);
                    etMessageToSend.setText("");
                    Message message = new Message(messageToSendString, true);
                    adapter.updateMessageList(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chat != null) {
            chat.stop();
        }
    }

    @Override
    public void onMessageReceived(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(msg, false);
                adapter.updateMessageList(message);
            }
        });
    }
}
