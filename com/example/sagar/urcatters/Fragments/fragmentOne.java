package com.example.sagar.urcatters.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.example.sagar.urcatters.Adapter.flipperAdapter;
import com.example.sagar.urcatters.Adapter.viewPagerAdapter;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Model.GridViewCustomList;

public class fragmentOne extends Fragment {
    private CardView BFCardView;
    private AdapterViewFlipper adapterView;
    private ImageView[] dots;
    private int dotscount;
    private GridView gdView1;
    private GridView gdView2;
    private GridView gdView3;
    private int[] imageArray = new int[]{C0290R.drawable.bfimage1, C0290R.drawable.bfimage2, C0290R.drawable.bfimage3, C0290R.drawable.bfimage4};
    private CardView lunchCardView;
    private ViewPager mainImageViewPager;
    private LinearLayout sliderDotspanel;
    private CardView snacksCardView;

    class C04371 implements OnPageChangeListener {
        C04371() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            for (int i = 0; i < fragmentOne.this.dotscount; i++) {
                fragmentOne.this.dots[i].setImageDrawable(ContextCompat.getDrawable(fragmentOne.this.getContext(), C0290R.drawable.nonactive_dot));
            }
            fragmentOne.this.dots[position].setImageDrawable(ContextCompat.getDrawable(fragmentOne.this.getContext(), C0290R.drawable.active_dot));
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0290R.layout.fragment_1, container, false);
        this.BFCardView = (CardView) rootView.findViewById(C0290R.id.BFCardView);
        this.lunchCardView = (CardView) rootView.findViewById(C0290R.id.lunchCardView);
        this.snacksCardView = (CardView) rootView.findViewById(C0290R.id.snacksCardView);
        this.sliderDotspanel = (LinearLayout) rootView.findViewById(C0290R.id.layoutDots);
        viewPagerAdapter imageAdapter = new viewPagerAdapter(getActivity());
        this.mainImageViewPager = (ViewPager) rootView.findViewById(C0290R.id.mainImageViewPager);
        this.adapterView = (AdapterViewFlipper) rootView.findViewById(C0290R.id.adapterFlipper);
        this.gdView1 = (GridView) rootView.findViewById(C0290R.id.gdView1);
        this.gdView2 = (GridView) rootView.findViewById(C0290R.id.gdView2);
        this.gdView3 = (GridView) rootView.findViewById(C0290R.id.gdView3);
        GridViewCustomList gdList = new GridViewCustomList(getContext(), this.imageArray);
        this.gdView1.setAdapter(gdList);
        this.gdView2.setAdapter(gdList);
        this.gdView3.setAdapter(gdList);
        this.mainImageViewPager.setAdapter(imageAdapter);
        this.adapterView.setAdapter(new flipperAdapter(getActivity(), this.imageArray));
        this.adapterView.setAutoStart(true);
        this.dotscount = imageAdapter.getCount();
        this.dots = new ImageView[this.dotscount];
        for (int i = 0; i < this.dotscount; i++) {
            this.dots[i] = new ImageView(getContext());
            this.dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), C0290R.drawable.nonactive_dot));
            LayoutParams params = new LayoutParams(-2, -2);
            params.setMargins(8, 0, 8, 0);
            this.sliderDotspanel.addView(this.dots[i], params);
        }
        this.dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), C0290R.drawable.active_dot));
        this.mainImageViewPager.addOnPageChangeListener(new C04371());
        return rootView;
    }
}
