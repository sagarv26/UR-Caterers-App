package com.example.sagar.urcatters.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Cart.All_Cart_Content;
import com.example.sagar.urcatters.Fragments.fragmentOne;
import com.example.sagar.urcatters.Fragments.fragmentThree;
import com.example.sagar.urcatters.Fragments.fragmentTwo;
import com.example.sagar.urcatters.SubClass.Add_Profile;
import com.example.sagar.urcatters.SubClass.Breakfast_Page;
import com.example.sagar.urcatters.SubClass.Cart_Page;
import com.example.sagar.urcatters.SubClass.Fav_Page;
import com.example.sagar.urcatters.SubClass.WebActivity;
import com.example.sagar.urcatters.SubClass.lunch_Page;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    public static final String Key_Image = "keyImage";
    public static final String Key_Option = "keyOption";
    public static final String Key_Title = "keyTitle";
    private ViewPager fragmentViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;

    class C02561 implements OnClickListener {
        C02561() {
        }

        public void onClick(View view) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, All_Cart_Content.class));
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new fragmentOne();
                case 1:
                    return new fragmentTwo();
                case 2:
                    return new fragmentThree();
                default:
                    return null;
            }
        }

        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Menu";
                case 2:
                    return "About";
                default:
                    return null;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0290R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(C0290R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon((int) C0290R.drawable.ic_menu_share);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.fragmentViewPager = (ViewPager) findViewById(C0290R.id.fragmentViewPager);
        this.fragmentViewPager.setAdapter(this.mSectionsPagerAdapter);
        this.tabLayout = (TabLayout) findViewById(C0290R.id.tabs);
        this.tabLayout.setupWithViewPager(this.fragmentViewPager);
        setupTabIcons();
        ((FloatingActionButton) findViewById(C0290R.id.fav_button)).setOnClickListener(new C02561());
        DrawerLayout drawer = (DrawerLayout) findViewById(C0290R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, C0290R.string.navigation_drawer_open, C0290R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ((NavigationView) findViewById(C0290R.id.nav_view)).setNavigationItemSelectedListener(this);
    }

    private void setupTabIcons() {
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(C0290R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0290R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0290R.id.action_settings) {
            return true;
        }
        if (id == C0290R.id.action_search) {
            startActivity(new Intent(this, Fav_Page.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0290R.id.nav_breakfast) {
            startActivity(new Intent(this, Breakfast_Page.class));
        } else if (id == C0290R.id.nav_profile) {
            startActivity(new Intent(this, Add_Profile.class));
        } else if (id == C0290R.id.nav_lunch) {
            startActivity(new Intent(this, lunch_Page.class));
        } else if (id == C0290R.id.nav_snacks) {
            startActivity(new Intent(this, All_Cart_Content.class));
        } else if (id == C0290R.id.nav_addContact) {
            Intent contactIntent = new Intent("android.intent.action.INSERT");
            contactIntent.setType("vnd.android.cursor.dir/raw_contact");
            contactIntent.putExtra("name", "UR Caterers").putExtra("phone", "8722548007").putExtra("com.android.contacts.action.ATTACH_IMAGE", "@drawable/ic_contact_phone_black");
            startActivityForResult(contactIntent, 1);
        } else if (id == C0290R.id.nav_cart) {
            startActivity(new Intent(this, Cart_Page.class));
        } else if (id == C0290R.id.nav_fav) {
            startActivity(new Intent(this, Fav_Page.class));
        } else if (id == C0290R.id.nav_fb) {
            startActivity(new Intent(this, WebActivity.class));
        } else if (id == C0290R.id.nav_share) {
            Intent share = new Intent("android.intent.action.SEND");
            share.setType("text/plain");
            share.putExtra("android.intent.extra.TEXT", "UR Caterers : Best In Market");
            startActivity(share);
        } else if (id == C0290R.id.nav_send) {
            startActivity(new Intent(this, Cart_Page.class));
        }
        ((DrawerLayout) findViewById(C0290R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }
}
