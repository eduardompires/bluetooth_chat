package com.example.eduardopires.bluetooth_chat;

import java.util.Random;

/**
 * Created by alex1 on 11/12/2016.
 */

public class Message {
    public String body;
    public boolean isMine;

    public Message(String body, boolean isMine) {
        this.body = body;
        this.isMine = isMine;
    }
}
