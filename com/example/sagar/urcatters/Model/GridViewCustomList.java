package com.example.sagar.urcatters.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.sagar.urcatters.C0290R;

public class GridViewCustomList extends BaseAdapter {
    Context context;
    LayoutInflater li = null;
    int[] listImage;

    class viewholder {
        ImageView itemimage;

        viewholder() {
        }
    }

    public GridViewCustomList(Context context, int[] listImage) {
        this.listImage = listImage;
        this.context = context;
        this.li = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.listImage.length;
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder vh = new viewholder();
        convertView = this.li.inflate(C0290R.layout.grid_view_custom_list, null);
        vh.itemimage = (ImageView) convertView.findViewById(C0290R.id.orderImage_CustomList);
        vh.itemimage.setImageResource(this.listImage[position]);
        return convertView;
    }
}
