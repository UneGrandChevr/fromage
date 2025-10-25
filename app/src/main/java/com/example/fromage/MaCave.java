package com.example.fromage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MaCave {

    private static List<ItemLayout> listFromages = new ArrayList<>();

    // --- Méthodes existantes ---
    public static void setListFromages(List<ItemLayout> plistFromages) {
        listFromages = plistFromages;
    }

    public static void addListFromages(ItemLayout pitem) {
        if (listFromages == null)
            listFromages = new ArrayList<>();
        listFromages.add(pitem);
    }

    public static ItemLayout getFromage(int index) {
        return listFromages.get(index);
    }

    public static List<ItemLayout> getListFromages() {
        return listFromages;
    }

    public static void removeFromList(ItemLayout item) {
        listFromages.remove(item);
    }

    // --- 🔹 Sauvegarde et chargement persistant ---
    private static final String PREFS_NAME = "MaCavePrefs";
    private static final String KEY_LIST = "listFromages";

    public static void saveToPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(listFromages);
        editor.putString(KEY_LIST, json);
        editor.apply();
    }

    public static void loadFromPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_LIST, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ItemLayout>>() {}.getType();
            listFromages = gson.fromJson(json, type);
        } else {
            listFromages = new ArrayList<>();
        }
    }
}
