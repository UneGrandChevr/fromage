package com.example.fromage.ui.home;

import android.os.Bundle;
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
import com.example.fromage.MyAdapterItemLayout;
import com.example.fromage.R;
import com.example.fromage.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private MyAdapterItemLayout adapter;
    private List<ItemLayout> itemList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        loadHome(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadHome(View root) {
        recyclerView = root.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Données à afficher
        itemList = new ArrayList<ItemLayout>();
        itemList.add(new ItemLayout(R.drawable.icon_comte, "Comté", "Retourner dans 12 jours."));
        itemList.add(new ItemLayout(R.drawable.brie_icon, "Brie", "Fin de l'affinage dans 5 jours."));
        itemList.add(new ItemLayout(R.drawable.roquefort_icon, "Roquefort", "Fin de l'affinage!"));

        adapter = new MyAdapterItemLayout(itemList);
        recyclerView.setAdapter(adapter);
    }
}