package com.example.sagar.urcatters.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Database.Breakfast_Database;

public class All_Cart_Content extends AppCompatActivity {
    Breakfast_Database bf_database;
    CardView confirm_breakfast_cardView;
    TextView confirm_breakfast_textView;
    CardView confirm_chats_cardView;
    TextView confirm_chats_textView;
    CardView confirm_dinner_cardView;
    TextView confirm_dinner_textView;
    CardView confirm_juice_cardView;
    TextView confirm_juice_textView;
    CardView confirm_lunch_cardView;
    TextView confirm_lunch_textView;

    class C02651 implements OnClickListener {
        C02651() {
        }

        public void onClick(View v) {
            All_Cart_Content.this.startActivity(new Intent(All_Cart_Content.this, Breakfast_Cart.class));
        }
    }

    class C02662 implements OnClickListener {
        C02662() {
        }

        public void onClick(View v) {
            All_Cart_Content.this.startActivity(new Intent(All_Cart_Content.this, Lunch_Cart.class));
        }
    }

    class C02673 implements OnClickListener {
        C02673() {
        }

        public void onClick(View v) {
            All_Cart_Content.this.startActivity(new Intent(All_Cart_Content.this, Dinner_Cart.class));
        }
    }

    class C02684 implements OnClickListener {
        C02684() {
        }

        public void onClick(View v) {
            All_Cart_Content.this.startActivity(new Intent(All_Cart_Content.this, Juice_Cart.class));
        }
    }

    class C02695 implements OnClickListener {
        C02695() {
        }

        public void onClick(View v) {
            All_Cart_Content.this.startActivity(new Intent(All_Cart_Content.this, Chats_Cart.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0290R.layout.cart_all_content);
        this.bf_database = new Breakfast_Database(getApplicationContext());
        int bf_count = this.bf_database.getCount("Breakfast");
        this.confirm_breakfast_cardView = (CardView) findViewById(C0290R.id.confirm_breakfast_cardView);
        this.confirm_lunch_cardView = (CardView) findViewById(C0290R.id.confirm_lunch_cardView);
        this.confirm_dinner_cardView = (CardView) findViewById(C0290R.id.confirm_dinner_cardView);
        this.confirm_juice_cardView = (CardView) findViewById(C0290R.id.confirm_juice_cardView);
        this.confirm_chats_cardView = (CardView) findViewById(C0290R.id.confirm_chats_cardView);
        this.confirm_breakfast_textView = (TextView) findViewById(C0290R.id.confirm_breakfast_textView);
        this.confirm_lunch_textView = (TextView) findViewById(C0290R.id.confirm_lunch_textView);
        this.confirm_dinner_textView = (TextView) findViewById(C0290R.id.confirm_dinner_textView);
        this.confirm_juice_textView = (TextView) findViewById(C0290R.id.confirm_juice_textView);
        this.confirm_chats_textView = (TextView) findViewById(C0290R.id.confirm_chats_textView);
        this.confirm_breakfast_textView.setText(this.bf_database.getCount("Breakfast") + " items");
        this.confirm_lunch_textView.setText(this.bf_database.getCount("Lunch") + " items");
        this.confirm_dinner_textView.setText(this.bf_database.getCount("Dinner") + " items");
        this.confirm_juice_textView.setText(this.bf_database.getCount("Juice") + " items");
        this.confirm_chats_textView.setText(this.bf_database.getCount("Chats") + " items");
        this.confirm_breakfast_cardView.setOnClickListener(new C02651());
        this.confirm_lunch_cardView.setOnClickListener(new C02662());
        this.confirm_dinner_cardView.setOnClickListener(new C02673());
        this.confirm_juice_cardView.setOnClickListener(new C02684());
        this.confirm_chats_cardView.setOnClickListener(new C02695());
    }
}
