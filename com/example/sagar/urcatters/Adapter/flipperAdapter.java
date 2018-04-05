package com.example.sagar.urcatters.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.sagar.urcatters.C0290R;

public class flipperAdapter extends BaseAdapter {
    int[] images;
    LayoutInflater inflater;
    Context mContext;

    public flipperAdapter(Context mContext, int[] images) {
        this.mContext = mContext;
        this.images = images;
        this.inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return this.images.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = this.inflater.inflate(C0290R.layout.image_content, null);
        ((ImageView) view.findViewById(C0290R.id.slideImageView)).setImageResource(this.images[position]);
        return view;
    }
}
