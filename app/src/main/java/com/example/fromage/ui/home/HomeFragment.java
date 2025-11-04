package com.example.fromage.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fromage.ItemLayout;
import com.example.fromage.MaCave;
import com.example.fromage.MyAdapterItemLayout;
import com.example.fromage.R;
import com.example.fromage.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private MyAdapterItemLayout adapter;
    private List<ItemLayout> itemList;

    // TextViews
    private TextView humiditeText;
    private TextView temperatureText;
    private TextView refroidisseurText;
    private TextView chauffageText;
    private TextView ventilationText;
    private TextView connectionStatusText;

    // Handler pour mise à jour toutes les secondes
    private final Handler handler = new Handler();
    private Runnable updateTask;

    // Le compte Adafruit
    private static final String ADAFRUIT_USERNAME = "username";
    private static final String ADAFRUIT_KEY = "key";
    private static final String ADAFRUIT_FEED_TEMP = "temp";
    private static final String ADAFRUIT_FEED_HUM = "hum";

    // Valeurs locales si pas de connexion
    private double currentTemp = 0.0;
    private double currentHum = 0.0;
    private boolean connectionOK = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialisation des views
        humiditeText = root.findViewById(R.id.Humiditetext);
        temperatureText = root.findViewById(R.id.Temperaturetext);
        refroidisseurText = root.findViewById(R.id.ReffroidisseurText);
        chauffageText = root.findViewById(R.id.chauffageText);
        ventilationText = root.findViewById(R.id.ventilationText);
        connectionStatusText = root.findViewById(R.id.connectionStatusText);

        itemList = new ArrayList<>();
        loadHome(root);

        // Tâche répétée chaque seconde
        updateTask = new Runnable() {
            @Override
            public void run() {
                fetchAdafruitData();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateTask);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(updateTask);
        binding = null;
    }

    private void loadHome(View root) {
        recyclerView = root.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (MaCave.getListFromages() != null) {
            itemList.addAll(MaCave.getListFromages());
        }

        adapter = new MyAdapterItemLayout(itemList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * code qui essaie de récupérer les données d’Adafruit.io, sinon affiche des valeurs à 0.
     */
    private void fetchAdafruitData() {
        new Thread(() -> {
            try {
                double temperature = getFeedValue(ADAFRUIT_FEED_TEMP);
                double humidite = getFeedValue(ADAFRUIT_FEED_HUM);

                connectionOK = true;
                currentTemp = temperature;
                currentHum = humidite;

            } catch (Exception e) {
                // Si on échoue, on garde les dernières valeurs ou met à 0
                connectionOK = false;
                currentTemp = 0;
                currentHum = 0;
            }

            // Mise à jour de l’UI (toujours sur le thread principal)
            requireActivity().runOnUiThread(this::updateUI);
        }).start();
    }

    /**
     * met à jour l'interface selon les valeurs actuelles (même si offline)
     */
    private void updateUI() {
        temperatureText.setText(String.format("Température : %.1f°C", currentTemp));
        humiditeText.setText(String.format("Humidité : %.1f%%", currentHum));

        if (!connectionOK) {
            connectionStatusText.setText("Déconnecté");
            connectionStatusText.setTextColor(0xFFFF0000);
            refroidisseurText.setText("Refroidisseur : --");
            chauffageText.setText("Chauffage : --");
            ventilationText.setText("Ventilation : --");
        } else {
            connectionStatusText.setText("Connecté");
            connectionStatusText.setTextColor(0xFF00FF00); // Vert
            refroidisseurText.setText(currentTemp > 10 ? "Refroidisseur : Activé" : "Refroidisseur : Arrêté");
            chauffageText.setText(currentTemp < 8 ? "Chauffage : Activé" : "Chauffage : Arrêté");
            ventilationText.setText(currentHum > 80 ? "Ventilation : Activée" : "Ventilation : Arrêtée");
        }
    }

    /**
     * recupere la dernière valeur d’un feed Adafruit.io
     */
    private double getFeedValue(String feedName) throws Exception {
        String urlString = "https://io.adafruit.com/api/v2/" + ADAFRUIT_USERNAME + "/feeds/" + feedName + "/data?limit=1";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-AIO-Key", ADAFRUIT_KEY);
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) throw new Exception("Erreur HTTP : " + responseCode);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) response.append(line);
        reader.close();

        JSONArray jsonArray = new JSONArray(response.toString());
        JSONObject first = jsonArray.getJSONObject(0);
        return Double.parseDouble(first.getString("value"));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}
