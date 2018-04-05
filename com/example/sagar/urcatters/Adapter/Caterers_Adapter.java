package com.example.sagar.urcatters.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Model.DetailItem;
import com.example.sagar.urcatters.SubClass.Show_Details_Breakfast;
import java.util.List;

public class Caterers_Adapter extends Adapter<ViewHolder> {
    public static final String Key_Image = "keyImage";
    public static final String Key_Option = "keyOption";
    public static final String Key_Title = "keyTitle";
    public List<DetailItem> listItem;
    public Context mContext;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ImageView order_image;
        public TextView order_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.order_image = (ImageView) itemView.findViewById(C0290R.id.orderImage_basic);
            this.order_name = (TextView) itemView.findViewById(C0290R.id.orderName_basic);
            itemView.setOnClickListener(new OnClickListener(Caterers_Adapter.this) {
                public void onClick(View v) {
                    Caterers_Adapter.this.ChangePage((DetailItem) Caterers_Adapter.this.listItem.get(ViewHolder.this.getAdapterPosition()));
                }
            });
        }
    }

    public Caterers_Adapter(List<DetailItem> listItem, Context mContext) {
        this.listItem = listItem;
        this.mContext = mContext;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0290R.layout.ur_recyclerview_basic, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        DetailItem item = (DetailItem) this.listItem.get(position);
        holder.order_name.setText(item.getOrderName());
        Glide.with(this.mContext).load(Integer.valueOf(item.getOrderThumbnail())).into(holder.order_image);
        holder.order_image.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Caterers_Adapter.this.ChangePage((DetailItem) Caterers_Adapter.this.listItem.get(position));
            }
        });
    }

    public int getItemCount() {
        return this.listItem.size();
    }

    public void ChangePage(DetailItem item) {
        Intent intent = new Intent(this.mContext, Show_Details_Breakfast.class);
        intent.setFlags(402653184);
        Bundle bundle = new Bundle();
        intent.putExtra("keyTitle", item.getOrderName());
        intent.putExtra("keyOption", item.getOrderOption());
        bundle.putInt("keyImage", item.getOrderThumbnail());
        intent.putExtras(bundle);
        this.mContext.startActivity(intent);
    }
}
