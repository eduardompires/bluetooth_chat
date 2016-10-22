package com.example.eduardopires.bluetooth_chat;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        pager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(pager);

        tabLayout.setupWithViewPager(pager);
    }

    public void setupViewPager(ViewPager viewPager) {
        BluetoothPagerAdapter adapter = new BluetoothPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PairingFragment(), "Parear");
        adapter.addFragment(new ConversationsFragment(), "Conversas");
        viewPager.setAdapter(adapter);
    }
}
