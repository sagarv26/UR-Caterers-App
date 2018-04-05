package com.example.sagar.urcatters.SubClass;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.Database.Profile_Database;

public class Add_Profile extends AppCompatActivity {
    EditText address;
    Button btnAdd;
    OnClickListener btnListner = new C02911();
    Button btnUpdate;
    Button btnView;
    EditText city;
    Profile_Database dataBase;
    EditText email;
    EditText mobile;
    EditText name;
    EditText pinCode;
    private Cursor res;

    class C02911 implements OnClickListener {
        C02911() {
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case C0290R.id.addProfileButton:
                    if (Long.valueOf(Add_Profile.this.dataBase.create(Add_Profile.this.getValue(Add_Profile.this.name), Add_Profile.this.getValue(Add_Profile.this.email), Add_Profile.this.getValue(Add_Profile.this.mobile), Add_Profile.this.getValue(Add_Profile.this.address), Add_Profile.this.getValue(Add_Profile.this.city), Add_Profile.this.getValue(Add_Profile.this.pinCode))).longValue() == -1) {
                        Toast.makeText(Add_Profile.this.getApplicationContext(), "Data not Inserted", 0).show();
                        return;
                    } else {
                        Toast.makeText(Add_Profile.this.getApplicationContext(), "Data Inserted", 0).show();
                        return;
                    }
                case C0290R.id.updateProfileButton:
                    if (Long.valueOf(Add_Profile.this.dataBase.update(Add_Profile.this.getValue(Add_Profile.this.name), Add_Profile.this.getValue(Add_Profile.this.email), Add_Profile.this.getValue(Add_Profile.this.mobile), Add_Profile.this.getValue(Add_Profile.this.address), Add_Profile.this.getValue(Add_Profile.this.city), Add_Profile.this.getValue(Add_Profile.this.pinCode))).longValue() == -1) {
                        Toast.makeText(Add_Profile.this.getApplicationContext(), "Data not Inserted", 0).show();
                        return;
                    } else {
                        Toast.makeText(Add_Profile.this.getApplicationContext(), "Data Inserted", 0).show();
                        return;
                    }
                case C0290R.id.viewProfileButton:
                    Add_Profile.this.res = Add_Profile.this.dataBase.read();
                    while (Add_Profile.this.res.moveToNext()) {
                        EditText editText = Add_Profile.this.name;
                        Cursor access$100 = Add_Profile.this.res;
                        Cursor access$1002 = Add_Profile.this.res;
                        Profile_Database profile_Database = Add_Profile.this.dataBase;
                        editText.setText(access$100.getString(access$1002.getColumnIndex("name")));
                        editText = Add_Profile.this.email;
                        access$100 = Add_Profile.this.res;
                        access$1002 = Add_Profile.this.res;
                        profile_Database = Add_Profile.this.dataBase;
                        editText.setText(access$100.getString(access$1002.getColumnIndex("email")));
                        editText = Add_Profile.this.mobile;
                        access$100 = Add_Profile.this.res;
                        access$1002 = Add_Profile.this.res;
                        profile_Database = Add_Profile.this.dataBase;
                        editText.setText(access$100.getString(access$1002.getColumnIndex(Profile_Database.Mob)));
                        editText = Add_Profile.this.city;
                        access$100 = Add_Profile.this.res;
                        access$1002 = Add_Profile.this.res;
                        profile_Database = Add_Profile.this.dataBase;
                        editText.setText(access$100.getString(access$1002.getColumnIndex(Profile_Database.City)));
                        editText = Add_Profile.this.address;
                        access$100 = Add_Profile.this.res;
                        access$1002 = Add_Profile.this.res;
                        profile_Database = Add_Profile.this.dataBase;
                        editText.setText(access$100.getString(access$1002.getColumnIndex(Profile_Database.Address)));
                        editText = Add_Profile.this.pinCode;
                        access$100 = Add_Profile.this.res;
                        access$1002 = Add_Profile.this.res;
                        profile_Database = Add_Profile.this.dataBase;
                        editText.setText(access$100.getString(access$1002.getColumnIndex(Profile_Database.PinCode)));
                        Toast.makeText(Add_Profile.this.getApplicationContext(), "Data Inserted", 0).show();
                    }
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0290R.layout.add_profile);
        this.dataBase = new Profile_Database(getApplicationContext());
        init();
    }

    private void init() {
        this.name = (EditText) findViewById(C0290R.id.name_field);
        this.email = (EditText) findViewById(C0290R.id.email_field);
        this.mobile = (EditText) findViewById(C0290R.id.mobile_field);
        this.address = (EditText) findViewById(C0290R.id.addr_field);
        this.city = (EditText) findViewById(C0290R.id.city_field);
        this.pinCode = (EditText) findViewById(C0290R.id.pin_field);
        this.btnUpdate = (Button) findViewById(C0290R.id.updateProfileButton);
        this.btnAdd = (Button) findViewById(C0290R.id.addProfileButton);
        this.btnView = (Button) findViewById(C0290R.id.viewProfileButton);
        this.btnUpdate.setOnClickListener(this.btnListner);
        this.btnAdd.setOnClickListener(this.btnListner);
        this.btnView.setOnClickListener(this.btnListner);
    }

    private String getValue(EditText text) {
        return text.getText().toString().trim();
    }
}
