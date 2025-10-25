package com.example.fromage;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.fromage.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fromage.databinding.ActivityMainBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String PREFS_NAME = "FromagePrefs";
    private static final String KEY_FROMAGES = "fromages_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Charger les données sauvegardées
        ArrayList<ItemLayout> itemList = loadFromages();

        // Si aucune donnée sauvegardée, créer une liste de base
        if (itemList == null || itemList.isEmpty()) {
            itemList = new ArrayList<>();
            ArrayList<ItemLayout.Etape> etapes = new ArrayList<>();
            etapes.add(new ItemLayout.Etape(2, "Retourner le fromage dans "));
            itemList.add(new ItemLayout(R.drawable.icon_comte, "Comté", etapes));
        }

        // Met à jour MaCave
        MaCave.setListFromages(itemList);

        // Liaison du layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Barre de navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        ).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // Sauvegarde la liste à la fermeture
    @Override
    protected void onPause() {
        super.onPause();
        saveFromages();
    }

    private void saveFromages() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MaCave.getListFromages());
        editor.putString(KEY_FROMAGES, json);
        editor.apply();
    }

    private ArrayList<ItemLayout> loadFromages() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_FROMAGES, null);
        if (json == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemLayout>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
