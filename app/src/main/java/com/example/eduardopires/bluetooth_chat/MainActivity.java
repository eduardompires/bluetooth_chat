package com.example.eduardopires.bluetooth_chat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public class Splash extends Activity {
        private final int SPLASH_DISPLAY_LENGTH = 1000;

    }
}
