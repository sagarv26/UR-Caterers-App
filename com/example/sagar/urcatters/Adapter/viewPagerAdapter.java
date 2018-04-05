package com.example.sagar.urcatters.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.sagar.urcatters.C0290R;

public class viewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] slideImage = new Integer[]{Integer.valueOf(C0290R.drawable.bfimage), Integer.valueOf(C0290R.drawable.lunchndinner), Integer.valueOf(C0290R.drawable.panipuri), Integer.valueOf(C0290R.drawable.juice)};

    public viewPagerAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return this.slideImage.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        this.layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        View view = this.layoutInflater.inflate(C0290R.layout.image_content, null);
        ((ImageView) view.findViewById(C0290R.id.slideImageView)).setImageResource(this.slideImage[position].intValue());
        ((ViewPager) container).addView(view, 0);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
