package com.lock.computerlockscreen.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lock.computerlockscreen.R;
import com.lock.computerlockscreen.model.Notification;

import java.util.ArrayList;

public class CustomNotificationAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<Notification> notifications;

    public long getItemId(int i) {
        return 0;
    }

    public CustomNotificationAdapter(Context context, ArrayList<Notification> arrayList) {
        this.mcontext = context;
        this.notifications = arrayList;
    }

    public int getCount() {
        if (this.notifications != null) {
            return this.notifications.size();
        }
        return 0;
    }

    public Object getItem(int i) {
        return this.notifications.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = ((LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.notification_list_items, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tv_text = (TextView) view.findViewById(R.id.tv_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        view.setOnLongClickListener((View.OnLongClickListener) null);
        view.setLongClickable(false);
        if (this.notifications.get(i).icon != null) {
            viewHolder.iv_icon.setImageBitmap(this.notifications.get(i).icon);
        }
        viewHolder.tv_title.setText(this.notifications.get(i).tv_title);
        viewHolder.tv_text.setText(this.notifications.get(i).tv_text);
        return view;
    }

    private static class ViewHolder {
        ImageView iv_icon;
        TextView tv_text;
        TextView tv_title;

        private ViewHolder() {
        }
    }
}
