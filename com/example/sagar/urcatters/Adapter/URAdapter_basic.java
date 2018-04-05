package com.example.sagar.urcatters.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Model.RecyclerItem;
import com.example.sagar.urcatters.SubClass.Breakfast_Page;
import com.example.sagar.urcatters.SubClass.lunch_Page;
import java.util.List;

public class URAdapter_basic extends Adapter<viewHolder> {
    private List<RecyclerItem> listItem;
    private Context mContext;

    public class viewHolder extends ViewHolder {
        public ImageView order_Thumbnail_basic;

        public viewHolder(View itemView) {
            super(itemView);
            this.order_Thumbnail_basic = (ImageView) itemView.findViewById(C0290R.id.orderImage_common);
        }
    }

    public URAdapter_basic(List<RecyclerItem> listItem, Context mContext) {
        this.listItem = listItem;
        this.mContext = mContext;
    }

    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(C0290R.layout.common_recyclerview, parent, false));
    }

    public void onBindViewHolder(viewHolder holder, int position) {
        final RecyclerItem item = (RecyclerItem) this.listItem.get(position);
        Glide.with(this.mContext).load(Integer.valueOf(item.getOrderThumbnail())).into(holder.order_Thumbnail_basic);
        holder.order_Thumbnail_basic.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                switch (item.getOrderThumbnail()) {
                    case C0290R.drawable.bfimage:
                        Toast.makeText(URAdapter_basic.this.mContext, "Break Fast", 0).show();
                        URAdapter_basic.this.mContext.startActivity(new Intent(URAdapter_basic.this.mContext, Breakfast_Page.class));
                        return;
                    case C0290R.drawable.lunchndinner:
                        Toast.makeText(URAdapter_basic.this.mContext, "Lunch and Dinner", 0).show();
                        URAdapter_basic.this.mContext.startActivity(new Intent(URAdapter_basic.this.mContext, lunch_Page.class));
                        return;
                    case C0290R.drawable.panipuri:
                        Toast.makeText(URAdapter_basic.this.mContext, "Snacks", 0).show();
                        URAdapter_basic.this.mContext.startActivity(new Intent(URAdapter_basic.this.mContext, Breakfast_Page.class));
                        return;
                    default:
                        Toast.makeText(URAdapter_basic.this.mContext, "Others ", 0).show();
                        return;
                }
            }
        });
    }

    public int getItemCount() {
        return this.listItem.size();
    }
}
