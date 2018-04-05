package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class ManifestParser {
    private static final String GLIDE_MODULE_VALUE = "GlideModule";
    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<GlideModule> parse() {
        List<GlideModule> modules = new ArrayList();
        try {
            ApplicationInfo appInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (GLIDE_MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
            return modules;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse GlideModules", e);
        }
    }

    private static GlideModule parseModule(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            try {
                Object module = clazz.newInstance();
                if (module instanceof GlideModule) {
                    return (GlideModule) module;
                }
                throw new RuntimeException("Expected instanceof GlideModule, but found: " + module);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to instantiate GlideModule implementation for " + clazz, e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException("Unable to instantiate GlideModule implementation for " + clazz, e2);
            }
        } catch (ClassNotFoundException e3) {
            throw new IllegalArgumentException("Unable to find GlideModule implementation", e3);
        }
    }
}
