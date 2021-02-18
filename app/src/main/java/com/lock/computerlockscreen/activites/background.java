package com.lock.computerlockscreen.activites;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lock.computerlockscreen.R;

import java.time.Instant;
import java.util.ArrayList;

public class background extends AppCompatActivity {
    private GridView gv;
    ArrayList<Integer> array_image = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        getWindow().setFlags(1024,1024);
        array_image.add(R.drawable.bg1);
        array_image.add(R.drawable.bg2);
        array_image.add(R.drawable.bg3);
        array_image.add(R.drawable.bg4);
        array_image.add(R.drawable.bg5);
        array_image.add(R.drawable.bg6);
        array_image.add(R.drawable.bg7);
        array_image.add(R.drawable.bg8);

        gv = (GridView) findViewById(R.id.grid_view);
        gv.setAdapter(new GridAdapter());
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivities(new Intent[]{new Intent(getApplicationContext(), Fullscreen.class).putExtra("img", array_image.get(position))});

            }
        });

    }
    class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return array_image.size();
        }

        @Override
        public Object getItem(int position) {
            return array_image.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.collection_item_list, parent, false);
            ImageView iv = (ImageView) convertView.findViewById(R.id.image_list);
            iv.setImageResource(array_image.get(position));
            return convertView;
        }
    }
    @Override
    protected void onResume() {
        gv.deferNotifyDataSetChanged();
        gv.setAdapter(new GridAdapter());
        super.onResume();
    }
}
