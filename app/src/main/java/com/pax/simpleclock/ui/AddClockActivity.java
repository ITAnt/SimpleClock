package com.pax.simpleclock.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.pax.simpleclock.ClockManager;
import com.pax.simpleclock.R;
import com.pax.simpleclock.bean.ClockData;
import com.pax.simpleclock.utils.ClockUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhanzc on 2018/3/21.
 *
 */

public class AddClockActivity extends Activity implements View.OnClickListener {
    public static final String CLOCK_DATA = "clock_data";

    private EditText et_description;
    private Button btn_select_time;
    private TextView tv_time;
    private Button btn_add_clock;
    private ClockData clockData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clock);

        clockData = new ClockData();

        et_description = findViewById(R.id.et_description);
        btn_select_time = findViewById(R.id.btn_select_time);
        tv_time = findViewById(R.id.tv_time);
        btn_add_clock = findViewById(R.id.btn_add_clock);

        btn_select_time.setOnClickListener(this);
        btn_add_clock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_time:
                selectTime();
                break;

            case R.id.btn_add_clock:
                if (clockData.getAlarmTime() == 0) {
                    Toast.makeText(this, "请选择事件", Toast.LENGTH_SHORT).show();
                    return;
                }

                String description = et_description.getText().toString();
                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(this, "请输入闹钟描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                clockData.setMessage(description);
                clockData.setId(System.currentTimeMillis());
                ClockManager.getInstance().addAlarm(this, clockData);
                finish();
                break;
        }
    }

    private void selectTime() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        //Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2013,0,1);
        //endDate.set(2020,11,31);

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                clockData.setAlarmTime(date.getTime());
                tv_time.setText(ClockUtils.getFormattedTime(date.getTime()));
            }
        })
        .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
        .setCancelText("取消")//取消按钮文字
        .setSubmitText("确定")//确认按钮文字
        .setTitleSize(20)//标题文字大小
        .setTitleText("")//标题文字
        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
        .isCyclic(true)//是否循环滚动
        .setTitleColor(Color.BLACK)//标题文字颜色
        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
        .setCancelColor(Color.BLUE)//取消按钮文字颜色
        //.setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
        //.setBgColor(0xFF333333)//滚轮背景颜色 Night mode
        .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
        //.setRangDate(startDate, endDate)//起始终止年月日设定
        .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isDialog(true)//是否显示为对话框样式
        .build();

        pvTime.show();
    }
}
