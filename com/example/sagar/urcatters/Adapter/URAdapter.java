package com.example.sagar.urcatters.Adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Model.RecyclerItem;
import java.util.List;

public class URAdapter extends Adapter<viewHolder> {
    public static final String Key_Image = "keyImage";
    public static final String Key_Title = "keyTitle";
    private List<RecyclerItem> listItem;
    private Context mContext;

    public class viewHolder extends ViewHolder {
        public ImageView order_Thumbnail;
        public TextView order_Title;
        public ImageView overflow;

        public viewHolder(View itemView) {
            super(itemView);
            this.order_Title = (TextView) itemView.findViewById(C0290R.id.orderName);
            this.order_Thumbnail = (ImageView) itemView.findViewById(C0290R.id.orderImage);
            this.overflow = (ImageView) itemView.findViewById(C0290R.id.orderMenu);
            itemView.setOnClickListener(new OnClickListener(URAdapter.this) {
                public void onClick(View v) {
                }
            });
        }
    }

    public URAdapter(List<RecyclerItem> listItem, Context mContext) {
        this.listItem = listItem;
        this.mContext = mContext;
    }

    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(C0290R.layout.ur_recyclerview, parent, false));
    }

    public void onBindViewHolder(final viewHolder holder, final int position) {
        RecyclerItem item = (RecyclerItem) this.listItem.get(position);
        holder.order_Title.setText(item.getOrderName());
        Glide.with(this.mContext).load(Integer.valueOf(item.getOrderThumbnail())).into(holder.order_Thumbnail);
        holder.overflow.setOnClickListener(new OnClickListener() {

            class C04361 implements OnMenuItemClickListener {
                C04361() {
                }

                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case C0290R.id.menu_save:
                            Toast.makeText(URAdapter.this.mContext, "Saved", 0).show();
                            break;
                        case C0290R.id.menu_delete:
                            URAdapter.this.listItem.remove(position);
                            URAdapter.this.notifyDataSetChanged();
                            Toast.makeText(URAdapter.this.mContext, "Deleted", 0).show();
                            break;
                    }
                    return false;
                }
            }

            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(URAdapter.this.mContext, holder.overflow);
                popupMenu.inflate(C0290R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new C04361());
                popupMenu.show();
            }
        });
    }

    public int getItemCount() {
        return this.listItem.size();
    }
}
