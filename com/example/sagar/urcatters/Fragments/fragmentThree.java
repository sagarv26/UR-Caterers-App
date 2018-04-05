package com.example.sagar.urcatters.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sagar.urcatters.C0290R;
import com.example.sagar.urcatters.SubClass.PropertyFile;
import java.util.Properties;

public class fragmentThree extends Fragment {
    private TextView aboutUs;
    private Context contexts;
    private Properties f17p;
    private PropertyFile propertyFile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C0290R.layout.fragment_3, container, false);
        this.aboutUs = (TextView) rootView.findViewById(C0290R.id.aboutUsDes);
        this.propertyFile = new PropertyFile(getContext());
        this.f17p = this.propertyFile.getProperties("breakfast.properties");
        return rootView;
    }
}
