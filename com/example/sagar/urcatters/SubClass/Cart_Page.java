package com.example.sagar.urcatters.SubClass;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.example.sagar.urcatters.Adapter.CartListAdapter;
import com.example.sagar.urcatters.Adapter.CartListAdapter.MyViewHolder;
import com.example.sagar.urcatters.Adapter.RecyclerItemTouchHelper;
import com.example.sagar.urcatters.Adapter.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Database.Breakfast_Database;
import com.example.sagar.urcatters.Model.cart_item;
import java.util.ArrayList;
import java.util.List;

public class Cart_Page extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    String[] Storage_Permission = new String[]{"android.permission.SEND_SMS", "android.permission.CALL_PHONE"};
    private CartListAdapter adapter;
    Breakfast_Database bf_database;
    private BottomNavigationView bottomNav;
    private int countOrderedList = 0;
    private List<cart_item> listItem;
    private String ordered_list = "UR Caterers : Best In Market \n\nUser Ordered List\n\n";
    private RecyclerView recyclerView;

    class C04391 implements OnNavigationItemSelectedListener {
        C04391() {
        }

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case C0290R.id.actionOnClick1:
                    Toast.makeText(Cart_Page.this.getApplication(), "Call to Confirm..", 0).show();
                    Intent share = new Intent("android.intent.action.SEND");
                    share.setType("text/plain");
                    share.putExtra("android.intent.extra.TEXT", Cart_Page.this.ordered_list);
                    Cart_Page.this.startActivity(share);
                    break;
                case C0290R.id.actionOnClick2:
                    Toast.makeText(Cart_Page.this.getApplication(), "Mail to Confirm..", 0).show();
                    break;
                case C0290R.id.actionOnClick3:
                    SmsManager.getDefault().sendTextMessage("7019161658", null, Cart_Page.this.ordered_list, null, null);
                    Toast.makeText(Cart_Page.this.getApplicationContext(), "SMS SENT", 0).show();
                    break;
            }
            return true;
        }
    }

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
        setContentView((int) C0290R.layout.cart_recylerview);
        this.bottomNav = (BottomNavigationView) findViewById(C0290R.id.bottomNav);
        this.bf_database = new Breakfast_Database(getApplicationContext());
        this.listItem = new ArrayList();
        Cursor res = this.bf_database.read();
        while (res.moveToNext()) {
            this.countOrderedList++;
            StringBuilder append = new StringBuilder().append(this.ordered_list).append(this.countOrderedList).append(". ");
            Breakfast_Database breakfast_Database = this.bf_database;
            append = append.append(res.getString(res.getColumnIndex("shiftName"))).append(" : ");
            breakfast_Database = this.bf_database;
            this.ordered_list = append.append(res.getString(res.getColumnIndex("name"))).append("\n").toString();
            Breakfast_Database breakfast_Database2 = this.bf_database;
            String string = res.getString(res.getColumnIndex("name"));
            breakfast_Database = this.bf_database;
            this.listItem.add(new cart_item(string, res.getString(res.getColumnIndex("shiftName"))));
        }
        this.adapter = new CartListAdapter(getApplication(), this.listItem);
        this.recyclerView = (RecyclerView) findViewById(C0290R.id.cartRecyclerView);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.adapter);
        new ItemTouchHelper(new RecyclerItemTouchHelper(0, 4, this)).attachToRecyclerView(this.recyclerView);
        this.bottomNav.setOnNavigationItemSelectedListener(new C04391());
        if (ContextCompat.checkSelfPermission(this, "android.permission.SEND_SMS") != 0) {
            ActivityCompat.requestPermissions(this, this.Storage_Permission, 1);
        }
    }

    public void onSwiped(ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MyViewHolder) {
            String name = ((cart_item) this.listItem.get(viewHolder.getAdapterPosition())).getCartOrderName();
            final cart_item deletedItem = (cart_item) this.listItem.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            this.adapter.removeItem(viewHolder.getAdapterPosition());
            if (Long.valueOf(this.bf_database.delete(name)).longValue() == -1) {
                Toast.makeText(this, "Data not Deleted", 0).show();
            } else {
                Toast.makeText(this, "Data Deleted", 0).show();
            }
            Snackbar snackbar = Snackbar.make(findViewById(C0290R.id.cart_rec_view), name + " removed from cart!", 0);
            snackbar.setAction((CharSequence) "UNDO", new OnClickListener() {
                public void onClick(View view) {
                    Cart_Page.this.adapter.restoreItem(deletedItem, deletedIndex);
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
