package com.example.sagar.urcatters.SubClass;

import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.example.sagar.urcatters.Adapter.FavListAdapter;
import com.example.sagar.urcatters.Adapter.FavListAdapter.MyViewHolder;
import com.example.sagar.urcatters.Adapter.FavoritesRecyclerItemTouchHelper;
import com.example.sagar.urcatters.Adapter.FavoritesRecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Database.Breakfast_Database;
import com.example.sagar.urcatters.Database.Favorites_Database;
import com.example.sagar.urcatters.Model.cart_item;
import java.util.ArrayList;
import java.util.List;

public class Fav_Page extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    private FavListAdapter adapter;
    Breakfast_Database breakfast_database;
    Favorites_Database favorites_database;
    private List<cart_item> listItem;
    private RecyclerView recyclerView;

    public class GridSpacingItemDecoration extends ItemDecoration {
        private boolean includeEdge;
        private int spacing;
        private int spanCount;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % this.spanCount;
            if (this.includeEdge) {
                outRect.left = this.spacing - ((this.spacing * column) / this.spanCount);
                outRect.right = ((column + 1) * this.spacing) / this.spanCount;
                if (position < this.spanCount) {
                    outRect.top = this.spacing;
                }
                outRect.bottom = this.spacing;
                return;
            }
            outRect.left = (this.spacing * column) / this.spanCount;
            outRect.right = this.spacing - (((column + 1) * this.spacing) / this.spanCount);
            if (position >= this.spanCount) {
                outRect.top = this.spacing;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0290R.layout.fav_recylerview);
        this.favorites_database = new Favorites_Database(getApplicationContext());
        this.breakfast_database = new Breakfast_Database(getApplicationContext());
        this.listItem = new ArrayList();
        Cursor res = this.favorites_database.read();
        while (res.moveToNext()) {
            Favorites_Database favorites_Database = this.favorites_database;
            String string = res.getString(res.getColumnIndex("name"));
            Favorites_Database favorites_Database2 = this.favorites_database;
            this.listItem.add(new cart_item(string, res.getString(res.getColumnIndex("shiftName"))));
        }
        this.adapter = new FavListAdapter(getApplication(), this.listItem);
        this.recyclerView = (RecyclerView) findViewById(C0290R.id.favRecyclerView);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.adapter);
        new ItemTouchHelper(new FavoritesRecyclerItemTouchHelper(0, 4, this)).attachToRecyclerView(this.recyclerView);
    }

    public void onSwiped(ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MyViewHolder) {
            String name = ((cart_item) this.listItem.get(viewHolder.getAdapterPosition())).getCartOrderName();
            final cart_item deletedItem = (cart_item) this.listItem.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            this.adapter.removeItem(viewHolder.getAdapterPosition());
            if (Long.valueOf(this.favorites_database.delete(name)).longValue() == -1) {
                Toast.makeText(this, "Data not Deleted", 0).show();
            } else {
                Toast.makeText(this, "Data Deleted", 0).show();
            }
            Snackbar snackbar = Snackbar.make(findViewById(C0290R.id.fav_rec_view), name + " removed from cart!", 0);
            snackbar.setAction((CharSequence) "UNDO", new OnClickListener() {
                public void onClick(View view) {
                    Fav_Page.this.adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor((int) InputDeviceCompat.SOURCE_ANY);
            snackbar.show();
        }
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics()));
    }
}
