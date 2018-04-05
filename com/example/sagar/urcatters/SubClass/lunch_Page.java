package com.example.sagar.urcatters.SubClass;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.sagar.urcatters.Adapter.Caterers_Adapter_Lunch;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Model.DetailItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class lunch_Page extends AppCompatActivity {
    private ArrayList bf_Name;
    private ArrayList bf_option;
    private Caterers_Adapter_Lunch caterers_adapter;
    private Context context;
    int[] covers;
    private List<DetailItem> listItem;
    private Properties f19p;
    private PropertyFile propertyFile;
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
        setContentView((int) C0290R.layout.lunch_page);
        setSupportActionBar((Toolbar) findViewById(C0290R.id.toolbar));
        initCollapsingToolbar();
        this.context = this;
        this.listItem = new ArrayList();
        this.bf_Name = new ArrayList();
        this.bf_option = new ArrayList();
        this.caterers_adapter = new Caterers_Adapter_Lunch(this.listItem, getApplicationContext());
        this.recyclerView = (RecyclerView) findViewById(C0290R.id.lunch_recycler_view);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(20), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.caterers_adapter);
        this.propertyFile = new PropertyFile(this.context);
        this.f19p = this.propertyFile.getProperties("breakfast.properties");
        for (int i = 1; i <= 4; i++) {
            String name = this.f19p.getProperty("bfName" + i);
            String option = this.f19p.getProperty("bfOption" + i);
            this.bf_Name.add(name);
            this.bf_option.add(option);
        }
        this.covers = new int[]{C0290R.drawable.bfimage1, C0290R.drawable.bfimage2, C0290R.drawable.bfimage3, C0290R.drawable.bfimage4};
        prepareAlbums();
        try {
            Glide.with((FragmentActivity) this).load(Integer.valueOf(C0290R.drawable.lunchndinner)).into((ImageView) findViewById(C0290R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(C0290R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(C0290R.id.appbar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (this.scrollRange == -1) {
                    this.scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (this.scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Lunch and Dinner");
                    this.isShow = true;
                } else if (this.isShow) {
                    collapsingToolbar.setTitle(" ");
                    this.isShow = false;
                }
            }
        });
    }

    private void prepareAlbums() {
        for (int k = 0; k <= 3; k++) {
            this.listItem.add(new DetailItem(this.bf_Name.get(k).toString(), this.covers[k], this.bf_option.get(k).toString()));
        }
        this.caterers_adapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics()));
    }
}
