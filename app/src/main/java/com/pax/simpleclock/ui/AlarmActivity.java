package com.pax.simpleclock.ui;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pax.simpleclock.R;

import java.lang.reflect.Field;

/**
 * Created by zhanzc on 2018/3/2.
 */

public class AlarmActivity extends Activity {
    private Ringtone mRingtone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // todo： 点亮屏幕
        playTone();
    }

    /*private void playTone() {
        // 上班时间到了，响铃并显示“可以下班啦！”
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mRingtone = RingtoneManager.getRingtone(this, uri);
        mRingtone.play();
    }*/

    // 播放res/raw文件夹下的音频
    private void playTone() {
        Uri uri = Uri.parse("android.resource://com.pax.simpleclock/" + R.raw.bark);
        mRingtone = RingtoneManager.getRingtone(this, uri);
        // 设置静音模式也播放声音
        mRingtone.setStreamType(AudioManager.STREAM_ALARM);
        setRingtoneRepeat(mRingtone);
        mRingtone.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRingtone != null && mRingtone.isPlaying()) {
            mRingtone.stop();
        }
    }

    private void setRingtoneRepeat(Ringtone ringtone) {
        Class<Ringtone> clazz =Ringtone.class;
        try {
            Field field = clazz.getDeclaredField("mLocalPlayer");//返回一个 Field 对象，它反映此 Class 对象所表示的类或接口的指定公共成员字段（※这里要进源码查看属性字段）
            field.setAccessible(true);
            MediaPlayer target = (MediaPlayer) field.get(ringtone);//返回指定对象上此 Field 表示的字段的值
            target.setLooping(true);//设置循环
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
