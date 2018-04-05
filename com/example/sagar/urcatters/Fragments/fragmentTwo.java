package com.example.sagar.urcatters.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sagar.urcatters.Adapter.URAdapter_basic;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Model.RecyclerItem;
import java.util.ArrayList;
import java.util.List;

public class fragmentTwo extends Fragment {
    private List<RecyclerItem> listItem;
    private RecyclerView recyclerView;
    private URAdapter_basic urAdapter_basic;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0290R.layout.fragment_2, container, false);
        this.listItem = new ArrayList();
        this.urAdapter_basic = new URAdapter_basic(this.listItem, getActivity());
        this.recyclerView = (RecyclerView) rootView.findViewById(C0290R.id.recyclerView);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.urAdapter_basic);
        prepareAlbums();
        return rootView;
    }

    private void prepareAlbums() {
        int[] covers = new int[]{C0290R.drawable.bfimage, C0290R.drawable.lunchndinner, C0290R.drawable.panipuri, C0290R.drawable.juice};
        this.listItem.add(new RecyclerItem("Break Fast", covers[0]));
        this.listItem.add(new RecyclerItem("Lunch & Dinner", covers[1]));
        this.listItem.add(new RecyclerItem("Snacks", covers[2]));
        this.listItem.add(new RecyclerItem("Others", covers[3]));
        this.urAdapter_basic.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics()));
    }
}
