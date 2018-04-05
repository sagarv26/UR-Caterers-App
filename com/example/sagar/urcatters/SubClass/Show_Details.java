package com.example.sagar.urcatters.SubClass;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Database.Breakfast_Database;
import com.example.sagar.urcatters.Database.Favorites_Database;

public class Show_Details extends AppCompatActivity implements OnItemSelectedListener {
    static String menuOptionSelected = null;
    static String orderName = null;
    static String orderOption = null;
    static String orderOptionSelected = null;
    static String shiftName = "Lunch";
    private Button add_cart;
    private Button add_fav;
    private String[] bf_option;
    Breakfast_Database breakfast_database;
    private ImageView detail_image;
    private TextView detail_title;
    Favorites_Database favorites_database;
    final Context mContext = this;
    private Spinner menuSpin;
    int pic;
    private RadioGroup radioButton_1;
    private Spinner varitesSpin;
    private Button what_you_expect;

    class C02941 implements OnCheckedChangeListener {
        C02941() {
        }

        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case C0290R.id.bf_radio:
                    Show_Details.menuOptionSelected = "Breakfast";
                    return;
                case C0290R.id.lunch_radio:
                    Show_Details.menuOptionSelected = "Lunch";
                    return;
                case C0290R.id.dinner_radio:
                    Show_Details.menuOptionSelected = "Dinner";
                    return;
                case C0290R.id.chats_radio:
                    Show_Details.menuOptionSelected = "Chats";
                    return;
                default:
                    return;
            }
        }
    }

    class C02972 implements OnClickListener {

        class C02951 implements DialogInterface.OnClickListener {
            C02951() {
            }

            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(Show_Details.this.mContext, "Cancelled", 0).show();
                dialog.cancel();
            }
        }

        class C02962 implements DialogInterface.OnClickListener {
            C02962() {
            }

            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(Show_Details.this.mContext, "Added", 0).show();
            }
        }

        C02972() {
        }

        public void onClick(View v) {
            View promptsView = LayoutInflater.from(Show_Details.this.mContext).inflate(C0290R.layout.what_you_expect, null);
            Builder alertDialogBuilder = new Builder(Show_Details.this.mContext);
            alertDialogBuilder.setView(promptsView);
            EditText userInput = (EditText) promptsView.findViewById(C0290R.id.what_you_expect_field);
            alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new C02962()).setNegativeButton("Cancel", new C02951());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.getButton(-2).setBackgroundColor(-1);
            alertDialog.getButton(-2).setLeft(10);
            alertDialog.getButton(-1).setTextColor(ViewCompat.MEASURED_STATE_MASK);
            alertDialog.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
            alertDialog.getButton(-1).setBackgroundColor(-1);
        }
    }

    class C02983 implements OnClickListener {
        C02983() {
        }

        public void onClick(View v) {
            Breakfast_Database breakfast_Database = Show_Details.this.breakfast_database;
            if (Breakfast_Database.validate(Show_Details.orderOptionSelected, Show_Details.menuOptionSelected) != 0) {
                Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.orderOptionSelected + " Already Added to Cart", 0).show();
            } else if (Long.valueOf(Show_Details.this.breakfast_database.create(Show_Details.orderOptionSelected, Show_Details.menuOptionSelected)).longValue() == -1) {
                Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.orderOptionSelected + " : Not Added to Cart", 0).show();
            } else {
                Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.orderOptionSelected + " : Added to Cart", 0).show();
            }
        }
    }

    class C02994 implements OnClickListener {
        C02994() {
        }

        public void onClick(View v) {
            if (Long.valueOf(Show_Details.this.favorites_database.create(Show_Details.orderOptionSelected, Show_Details.menuOptionSelected)).longValue() == -1) {
                Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.orderOptionSelected + " : Not Added as Favorites", 0).show();
            } else {
                Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.orderOptionSelected + " : Added as Favorites", 0).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0290R.layout.show_details);
        this.breakfast_database = new Breakfast_Database(getApplicationContext());
        this.favorites_database = new Favorites_Database(getApplicationContext());
        this.detail_image = (ImageView) findViewById(C0290R.id.detail_image);
        this.detail_title = (TextView) findViewById(C0290R.id.detail_title);
        this.add_cart = (Button) findViewById(C0290R.id.add_button);
        this.add_fav = (Button) findViewById(C0290R.id.fav_button);
        this.varitesSpin = (Spinner) findViewById(C0290R.id.option_spinner);
        this.what_you_expect = (Button) findViewById(C0290R.id.button_what_you_expect);
        this.radioButton_1 = (RadioGroup) findViewById(C0290R.id.radio_group_1);
        this.varitesSpin.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        orderName = String.valueOf(intent.getStringExtra("keyTitle"));
        orderOption = String.valueOf(intent.getStringExtra("keyOption"));
        this.bf_option = orderOption.split(",");
        this.detail_title.setText(String.valueOf(intent.getStringExtra("keyTitle")));
        ArrayAdapter aa1 = new ArrayAdapter(this, 17367048, this.bf_option);
        aa1.setDropDownViewResource(17367049);
        this.varitesSpin.setAdapter(aa1);
        this.radioButton_1.setOnCheckedChangeListener(new C02941());
        this.what_you_expect.setOnClickListener(new C02972());
        this.pic = getIntent().getExtras().getInt("keyImage");
        this.detail_image.setImageResource(this.pic);
        this.add_cart.setOnClickListener(new C02983());
        this.add_fav.setOnClickListener(new C02994());
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        orderOptionSelected = this.bf_option[position];
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    protected void onStart() {
        super.onStart();
        this.breakfast_database.openDB();
    }

    protected void onStop() {
        super.onStop();
        this.breakfast_database.closeDB();
    }
}
