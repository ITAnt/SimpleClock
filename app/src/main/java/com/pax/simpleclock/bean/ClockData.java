package com.pax.simpleclock.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zhanzc on 2018/3/1.
 */

//@RealmClass
public class ClockData extends RealmObject implements Serializable {
    @PrimaryKey
    private long id;
    private long alarmTime;
    private String message;

    /**
     * 0：一次
     * 1：每周一
     * 2：每周二
     * 3：每周三
     * 4：每周四
     * 5：每周五
     * 6：每周六
     * 7：每周日
     * 8：每天
     * 9：每月
     * 10：每年
     */
    private int repeatMode;

    public ClockData() {
    }

    public ClockData(long alarmTime, String message, int repeatMode) {
        id = System.currentTimeMillis();
        this.alarmTime = alarmTime;
        this.message = message;
        this.repeatMode = repeatMode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }
}
