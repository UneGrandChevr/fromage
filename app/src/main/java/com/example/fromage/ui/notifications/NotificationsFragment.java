package com.example.fromage.ui.notifications;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fromage.ItemLayout;
import com.example.fromage.MyAdapterItemLayout;
import com.example.fromage.MyAdapterRecetteListLayout;
import com.example.fromage.R;
import com.example.fromage.RecetteListLayout;
import com.example.fromage.databinding.FragmentNotificationsBinding;
import com.example.fromage.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private RecyclerView recyclerView;
    private MyAdapterRecetteListLayout adapter;
    private List<RecetteListLayout> recetteListLayouts;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Charger les recettes
        loadRecette(root);

        // Barre de recherche (même layout que celui affiché)
        EditText searchBar = root.findViewById(R.id.editTextText);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadRecette(View root) {
        recyclerView = root.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Données à afficher
        recetteListLayouts = new ArrayList<RecetteListLayout>();

        recetteListLayouts.add(new RecetteListLayout(R.drawable.icon_camembert, "Camembert",
                "Le camembert est un fromage français\n" +
                "à pâte molle et croûte fleurie, fabriqué\n" +
                "à base de lait de vache. Il est\n" +
                "crémeux, parfumé et connu pour son\n" +
                "goût doux et légèrement boisé.",
                R.raw.camembert));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.tomme_de_brebis_icon, "Tomme de Brebis",
                "La tomme de brebis est un fromage\n" +
                "à pâte pressée non cuite, élaboré à\n" +
                "partir de lait de brebis. Elle offre une\n" +
                "texture souple et un goût typique, à\n" +
                "la fois doux, fruité et légèrement\n" +
                "rustique.",
                R.raw.tomme_de_brebis));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.reblochon_icon, "Reblochon",
                "Le reblochon est un fromage\n" +
                "savoyard à pâte pressée non cuite\n" +
                "et croûte lavée, fabriqué avec du lait\n" +
                "de vache. Il est onctueux, doux et\n" +
                "développe des arômes de noisette.",
                R.raw.reblochon));
        recetteListLayouts.add(new RecetteListLayout(R.drawable.gruyere_icon, "Gruyère",
                "Le gruyère est un fromage suisse à\n" +
                "pâte pressée cuite, produit avec du\n" +
                "lait de vache. Il se distingue par sa\n" +
                "texture ferme, sa pâte sans trous et\n" +
                "son goût fruité aux notes\n" +
                "légèrement salées.",
                R.raw.gruyere));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.brie_icon, "Brie",
                "Le brie est un fromage français à\n" +
                        "pâte molle et à croûte fleurie, produit\n" +
                        "avec du lait de vache. Il se distingue\n" +
                        "par sa pâte souple et crémeuse,\n" +
                        "sa croûte blanche caractéristique et\n" +
                        "son goût doux et légèrement\n" +
                        "beurré.",
                R.raw.brie));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.roquefort_icon, "Roquefort",
                "Le roquefort est un fromage français à\n" +
                        "pâte persillée et à croûte naturelle,\n" +
                        "produit avec du lait de brebis. Il se\n" +
                        "distingué par ses veines bleues,\n" +
                        "sa pâte moelleuse et son goût\n" +
                        "puissant et salé, caractéristique\n" +
                        "des fromages affinés en cave.",
                R.raw.roquefort));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.morbier_icon, "Morbier",
                "Le morbier est un fromage français à\n" +
                        "pâte pressée non cuite et à croûte\n" +
                        "naturelle, produit avec du lait de\n" +
                        "vache. Il se distingue par sa ligne\n" +
                        "noire au milieu de la pâte et son\n" +
                        "goût doux, crémeux et légèrement\n" +
                        "noisetté.",
                R.raw.morbier));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.icon_comte, "Comté",
                "Le comté est un fromage français à\n" +
                        "pâte pressée cuite et à croûte\n" +
                        "naturelle, produit avec du lait de\n" +
                        "vache. Il se distingue par sa pâte\n" +
                        "ferme et souple, son goût fruité et\n" +
                        "noisetté, et ses arômes riches et\n" +
                        "complexes.",
                R.raw.comte));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.tomme_des_pyrennees_icon, "Tomme des Pyrénées",
                "La tomme des Pyrénées est un fromage\n" +
                        "français à pâte pressée non cuite et\n" +
                        "à croûte naturelle, produit avec du\n" +
                        "lait de brebis ou de vache. Elle se\n" +
                        "distingué par sa pâte semi-ferme, sa\n" +
                        "saveur douce et typée, et ses arômes\n" +
                        "de montagne.",
                R.raw.tomme_des_pyrenees));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.munster_icon, "Munster",
                "Le munster est un fromage français à\n" +
                        "pâte molle et à croûte lavée, produit\n" +
                        "avec du lait de vache. Il se distingue\n" +
                        "par sa croûte orangée, sa pâte\n" +
                        "souple et son goût puissant et\n" +
                        "aromatique.",
                R.raw.munster));

        recetteListLayouts.add(new RecetteListLayout(R.drawable.parmesan_icon, "Parmesan",
                "Le parmesan est un fromage italien à\n" +
                        "pâte dure et à croûte naturelle,\n" +
                        "produit avec du lait de vache. Il se\n" +
                        "distingué par sa texture granuleuse,\n" +
                        "son affinage long et son goût riche,\n" +
                        "fruité et savoureux.",
                R.raw.parmesan));

        adapter = new MyAdapterRecetteListLayout(recetteListLayouts,getContext());
        recyclerView.setAdapter(adapter);



    }


}