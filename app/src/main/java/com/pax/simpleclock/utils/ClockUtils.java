package com.pax.simpleclock.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;

import com.pax.simpleclock.R;
import com.pax.simpleclock.ui.AlarmActivity;
import com.pax.simpleclock.ui.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhanzc on 2018/3/2.
 */

public class ClockUtils {
    public static void keepAlive(Service service, String time, String content) {
        Notification notification = new Notification.Builder(service)
                /**设置通知左边的大图标**/
                //.setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.status)
                /**通知首次出现在通知栏，带上升动画效果的**/
                //.setTicker("通知来了")
                /**设置通知的标题**/
                .setContentTitle(time)
                /**设置通知的内容**/
                .setContentText(content)
                /**通知产生的时间，会在通知信息里显示，设为0则不显示时间**/
                .setWhen(0)
                /**设置该通知优先级**/
                .setPriority(Notification.PRIORITY_MAX)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(false)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(true)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                //.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(service, 1, new Intent(service, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
        service.startForeground(android.os.Process.myPid(), notification);
    }

    public static void setUpAlarm(Context context, long alarmTimeMillis) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT < 19){
            am.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }else{
            am.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }
    }

    public static long getTimeMillis(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis();
    }

    private static final String FORMAT_TIME = "yyyy-MM-dd HH:mm";
    public static String getFormattedTime(long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIME);
        return format.format(new Date(timeMillis));
    }
}
