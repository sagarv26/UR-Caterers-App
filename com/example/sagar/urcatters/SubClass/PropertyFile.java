package com.example.sagar.urcatters.SubClass;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {
    private Context context;
    private Properties properties = new Properties();

    public PropertyFile(Context context) {
        this.context = context;
    }

    public Properties getProperties(String FileName) {
        try {
            this.properties.load(this.context.getAssets().open(FileName));
        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.toString());
        }
        return this.properties;
    }
}
