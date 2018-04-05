package com.example.sagar.urcatters.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Database.Breakfast_Database;
import com.example.sagar.urcatters.Model.cart_item;
import java.util.List;

public class FavListAdapter extends Adapter<MyViewHolder> {
    Breakfast_Database breakfast_database;
    private List<cart_item> cartList;
    private Context context;

    public class MyViewHolder extends ViewHolder {
        public ImageView moveToCart;
        public TextView order_Title;
        public TextView shift_name;
        public RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            this.order_Title = (TextView) view.findViewById(C0290R.id.name);
            this.shift_name = (TextView) view.findViewById(C0290R.id.shiftName);
            this.moveToCart = (ImageView) view.findViewById(C0290R.id.moveToCart_icon);
            this.viewBackground = (RelativeLayout) view.findViewById(C0290R.id.view_background);
            this.viewForeground = (RelativeLayout) view.findViewById(C0290R.id.view_foreground);
        }
    }

    public FavListAdapter(Context context, List<cart_item> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0290R.layout.fav_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        final cart_item item = (cart_item) this.cartList.get(position);
        holder.order_Title.setText(item.getCartOrderName());
        holder.shift_name.setText(item.getCartShiftName());
        this.breakfast_database = new Breakfast_Database(this.context);
        holder.moveToCart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Long.valueOf(FavListAdapter.this.breakfast_database.create(item.getCartOrderName(), item.getCartShiftName())).longValue() == -1) {
                    Toast.makeText(FavListAdapter.this.context, item.getCartOrderName() + " : Not Added to Cart", 0).show();
                } else {
                    Toast.makeText(FavListAdapter.this.context, item.getCartOrderName() + " : Added to Cart", 0).show();
                }
            }
        });
    }

    public int getItemCount() {
        return this.cartList.size();
    }

    public void removeItem(int position) {
        this.cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(cart_item item, int position) {
        this.cartList.add(position, item);
        notifyItemInserted(position);
    }
}
