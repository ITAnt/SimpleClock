package com.pax.simpleclock;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import com.pax.simpleclock.bean.ClockData;
import com.pax.simpleclock.ui.MainActivity;
import com.pax.simpleclock.utils.ClockUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zhanzc on 2018/3/2.
 */

public class ClockManager {
    private static final int CLOCk_ADD = 1;
    private static final int CLOCk_DEL = -1;

    // 解决并发访问的问题
    private CopyOnWriteArrayList<ClockData> mClockDataList;
    private static ClockManager manager;
    private Realm realm;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CLOCk_ADD:
                    ClockData addData = (ClockData) msg.obj;
                    realm.beginTransaction();
                    realm.insertOrUpdate(addData);
                    realm.commitTransaction();
                    break;

                case CLOCk_DEL:
                    ClockData delData = (ClockData) msg.obj;
                    realm.beginTransaction();
                    realm.where(ClockData.class).equalTo("id", delData.getId()).findAll().deleteFirstFromRealm();
                    realm.commitTransaction();
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    private ClockManager() {
        initClockData();
    }

    private void initClockData() {
        mClockDataList = new CopyOnWriteArrayList<>();
        realm = Realm.getDefaultInstance();
        List<ClockData> clockDataList = realm.where(ClockData.class).findAll();
        List<ClockData> tempClockDataList = realm.copyFromRealm(clockDataList);
        if (tempClockDataList != null) {
            mClockDataList.addAll(tempClockDataList);
        }
        sortClock();
    }

    public static ClockManager getInstance() {
        if (manager == null) {
            initManager();
        }

        return manager;
    }

    private static synchronized void initManager() {
        if (manager == null) {
            manager = new ClockManager();
        }
    }

    private void sortClock() {
        if (mClockDataList == null || mClockDataList.isEmpty()) {
            return;
        }
        List temp = Arrays.asList(mClockDataList.toArray());
        Collections.sort(temp, new Comparator<ClockData>() {
            @Override
            public int compare(ClockData clock1, ClockData clock2) {
                return clock1.getAlarmTime() > clock2.getAlarmTime() ? 1 : -1;
            }
        });

        mClockDataList.clear();
        mClockDataList.addAll(temp);
    }

    public void addAlarm(Context context, ClockData clockData) {
        mClockDataList.add(clockData);

        // CopyOnWriteArrayList，是线程安全的集合，但不支持直接排序
        sortClock();

        context.sendBroadcast(new Intent(MainActivity.BROADCAST_FILTER_CLOCK_CHANGED));
        Message message = Message.obtain();
        message.what = CLOCk_ADD;
        message.obj = clockData;
        mHandler.sendMessage(message);
        // todo: 默认保存到网络
    }

    public void deleteAlarm(Context context, int index) {
        if (mClockDataList != null && mClockDataList.size() > index) {
            ClockData clockData = mClockDataList.get(index);
            mClockDataList.remove(index);
            context.sendBroadcast(new Intent(MainActivity.BROADCAST_FILTER_CLOCK_CHANGED));
            Message message = Message.obtain();
            message.what = CLOCk_DEL;
            message.obj = clockData;
            mHandler.sendMessage(message);
            // todo: 默认从网络删除
        }
    }

    public ClockData getRecentClock() {
        return (mClockDataList == null || mClockDataList.isEmpty()) ? null : mClockDataList.get(0);
    }

    public List<ClockData> getAllClocks() {
        return mClockDataList;
    }
}
