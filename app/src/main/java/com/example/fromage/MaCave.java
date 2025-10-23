package com.example.fromage;

import java.util.ArrayList;
import java.util.List;

public class  MaCave {
    private static List<ItemLayout> listFromages;

    public static void setListFromages(List<ItemLayout> plistFromages){
        listFromages=plistFromages;
    }

    public static void addListFromages(ItemLayout pitem){
        listFromages.add(pitem);
    }

    public static ItemLayout getFromage(int index){
        return listFromages.get(index);
    }

    public static List<ItemLayout> getListFromages(){
        return listFromages;
    }

}
