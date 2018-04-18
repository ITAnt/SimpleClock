package com.pax.simpleclock.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.pax.simpleclock.ClockManager;
import com.pax.simpleclock.bean.ClockData;
import com.pax.simpleclock.ui.AlarmActivity;
import com.pax.simpleclock.ui.MainActivity;
import com.pax.simpleclock.utils.ClockUtils;

import java.util.Timer;
import java.util.TimerTask;

import static com.pax.simpleclock.ui.MainActivity.BROADCAST_FILTER_CLOCK_CHANGED;

/**
 * Created by zhanzc on 2018/3/1.
 */

public class ClockService extends Service {
    public static final String EXTRA_CLOCK_DATA = "clock";
    private static final int WHAT_CLOCK_COMING = 0;
    private Timer mTimer;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CLOCK_COMING:
                    ClockData clockData = (ClockData) msg.obj;
                    if (clockData != null) {
                        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(EXTRA_CLOCK_DATA, clockData);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setUpNotification();
        setAlarmHeartBeat();
        initReceiver();
        return START_STICKY;
    }

    private static final long MILLIS_DELAY = 0;
    private static final long MILLIS_PERIOD = 10000;
    /**
     * 每隔10秒去检查一下闹钟
     */
    private void setAlarmHeartBeat() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ClockData recentClock = ClockManager.getInstance().getRecentClock();
                if (recentClock != null && (System.currentTimeMillis() >= recentClock.getAlarmTime())) {
                    ClockManager.getInstance().deleteAlarm(getApplicationContext(), 0);
                    Message message = Message.obtain();
                    message.what = WHAT_CLOCK_COMING;
                    message.obj = recentClock;
                    mHandler.sendMessage(message);
                }
            }
        }, MILLIS_DELAY, MILLIS_PERIOD);
    }

    private ClockReceiver mReceiver;
    private class ClockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUpNotification();
        }
    }

    private void setUpNotification() {
        ClockData recentClock = ClockManager.getInstance().getRecentClock();
        if (recentClock != null) {
            ClockUtils.keepAlive(ClockService.this, ClockUtils.getFormattedTime(recentClock.getAlarmTime()), recentClock.getMessage());
        } else {
            ClockUtils.keepAlive(ClockService.this, "简易闹钟", "您的贴身闹钟");
        }
    }

    private void initReceiver() {
        mReceiver = new ClockReceiver();
        IntentFilter filter = new IntentFilter(BROADCAST_FILTER_CLOCK_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}
