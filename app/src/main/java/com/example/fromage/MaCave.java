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
}
