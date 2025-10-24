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

    // Handler pour mise à jour toutes les secondes
    private final Handler handler = new Handler();
    private Runnable updateTask;

    // Ton clé Adafruit.io
    private static final String ADAFRUIT_USERNAME = "filmchemf2";
    private static final String ADAFRUIT_KEY = "aio_MpvQ89QPIUx7ChINRYx2biJouaDe";
    private static final String ADAFRUIT_FEED_TEMP = "temp";
    private static final String ADAFRUIT_FEED_HUM = "hum";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialisation des vues
        humiditeText = root.findViewById(R.id.Humiditetext);
        temperatureText = root.findViewById(R.id.Temperaturetext);
        refroidisseurText = root.findViewById(R.id.ReffroidisseurText);
        chauffageText = root.findViewById(R.id.chauffageText);
        ventilationText = root.findViewById(R.id.ventilationText);

        itemList = new ArrayList<>();
        loadHome(root);

        // Tâche de mise à jour Adafruit toutes les secondes
        updateTask = new Runnable() {
            @Override
            public void run() {
                fetchAdafruitData();
                handler.postDelayed(this, 1000); // toutes les secondes
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

    public void loadHome(View root) {
        recyclerView = root.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (MaCave.getListFromages() != null) {
            itemList.addAll(MaCave.getListFromages());
        }

        adapter = new MyAdapterItemLayout(itemList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Récupère les données Adafruit.io pour humidité et température
     */
    private void fetchAdafruitData() {
        new Thread(() -> {
            try {
                // Température
                double temperature = getFeedValue(ADAFRUIT_FEED_TEMP);
                double humidite = getFeedValue(ADAFRUIT_FEED_HUM);

                requireActivity().runOnUiThread(() -> {
                    temperatureText.setText(String.format("Température : %.1f°C", temperature));
                    humiditeText.setText(String.format("Humidité : %.1f%%", humidite));

                    // Logique simple d’état (tu pourras ajuster selon ton système)
                    refroidisseurText.setText(temperature > 10 ? "Refroidisseur : Activé" : "Refroidisseur : Arrêté");
                    chauffageText.setText(temperature < 8 ? "Chauffage : Activé" : "Chauffage : Arrêté");
                    ventilationText.setText(humidite > 80 ? "Ventilation : Activée" : "Ventilation : Arrêtée");
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Fonction pour lire la dernière valeur d’un feed Adafruit.io
     */
    private double getFeedValue(String feedName) throws Exception {
        String urlString = "https://io.adafruit.com/api/v2/" + ADAFRUIT_USERNAME + "/feeds/" + feedName + "/data?limit=1";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-AIO-Key", ADAFRUIT_KEY);

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
