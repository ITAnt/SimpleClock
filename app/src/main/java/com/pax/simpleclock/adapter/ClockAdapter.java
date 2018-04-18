package com.pax.simpleclock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.pax.simpleclock.ClockManager;
import com.pax.simpleclock.R;
import com.pax.simpleclock.bean.ClockData;
import com.pax.simpleclock.utils.ClockUtils;

import java.util.List;

/**
 * Created by zhanzc on 2018/3/5.
 */

public class ClockAdapter extends BaseAdapter {
    private Context context;
    private List<ClockData> mClockDataList;

    public ClockAdapter(Context context, List<ClockData> clocks) {
        this.context = context;
        this.mClockDataList = clocks;
    }

    @Override
    public int getCount() {
        return mClockDataList == null ? 0 : mClockDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mClockDataList == null ? null : mClockDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clock, null);

            mViewHolder = new ViewHolder();
            mViewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            mViewHolder.tv_message = convertView.findViewById(R.id.tv_message);
            mViewHolder.tv_delete = convertView.findViewById(R.id.tv_delete);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ClockData clockData = mClockDataList.get(position);
        if (clockData != null) {
            mViewHolder.tv_time.setText(ClockUtils.getFormattedTime(clockData.getAlarmTime()));
            mViewHolder.tv_message.setText(clockData.getMessage());
            mViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClockManager.getInstance().deleteAlarm(context, position);
                }
            });
        }
        return convertView;
    }

    private ViewHolder mViewHolder;
    private static class ViewHolder {
        TextView tv_time;
        TextView tv_message;
        TextView tv_delete;
    }
}
