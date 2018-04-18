package com.pax.simpleclock.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.pax.simpleclock.ClockManager;
import com.pax.simpleclock.R;
import com.pax.simpleclock.adapter.ClockAdapter;
import com.pax.simpleclock.bean.ClockData;
import com.pax.simpleclock.service.ClockService;
import com.pax.simpleclock.utils.ClockUtils;

public class MainActivity extends AppCompatActivity {
    // 1.保存到本地
    // 2.保存到网络
    // 3.现有基础上添加本地文件闹钟
    // 4.现有基础上添加网络闹钟
    // 5.删除网络上的闹钟

    public static final String BROADCAST_FILTER_CLOCK_CHANGED = "clock_changed";

    private ListView lv_clock;
    private ClockAdapter mAdapter;
    private ClockReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initReceiver();
        startService(new Intent(this, ClockService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private void initReceiver() {
        mReceiver = new ClockReceiver();
        IntentFilter filter = new IntentFilter(BROADCAST_FILTER_CLOCK_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_clock = findViewById(R.id.lv_clock);

        mAdapter = new ClockAdapter(this, ClockManager.getInstance().getAllClocks());
        lv_clock.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;

            case R.id.action_add:
                startActivity(new Intent(this, AddClockActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class ClockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
