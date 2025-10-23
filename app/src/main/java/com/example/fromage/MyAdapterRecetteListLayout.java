package com.example.fromage;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fromage.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterRecetteListLayout extends RecyclerView.Adapter<MyAdapterRecetteListLayout.ViewHolder> {

    private List<RecetteListLayout> recetteList;
    private Context context;

    private List<RecetteListLayout> fullList; // Copie complète pour filtrage

    public MyAdapterRecetteListLayout(List<RecetteListLayout> recetteList, Context context) {
        this.recetteList = new ArrayList<>(recetteList);
        this.fullList = new ArrayList<>(recetteList);
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView subtitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.CheeseIcon);
            titleView = itemView.findViewById(R.id.TitreCheese);
            subtitleView = itemView.findViewById(R.id.ActionCheese);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recette_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecetteListLayout item = recetteList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.titleView.setText(item.getTitle());
        holder.subtitleView.setText(item.getSubtitle());

        holder.itemView.setOnClickListener(v -> showRecetteDialog(item));
    }


    private void showRecetteDialog(RecetteListLayout item) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_recipe_detail);

        ImageView image = dialog.findViewById(R.id.detail_image);
        TextView title = dialog.findViewById(R.id.detail_title);
        TextView description = dialog.findViewById(R.id.detail_description);
        Button closeBtn = dialog.findViewById(R.id.close_button);
        Button addBtn = dialog.findViewById(R.id.ajouter_recette);

        image.setImageResource(item.getImageResId());
        title.setText(item.getTitle());
        description.setText(item.loadFullText(context)); // tu peux ajouter un getter pour le texte complet

        closeBtn.setOnClickListener(v -> dialog.dismiss());
        addBtn.setOnClickListener(v -> {
            MaCave.addListFromages(new ItemLayout(item.getImageResId(), item.getTitle(), "Fin de l'affinage dans 5 jours."));
        });
        dialog.show();
    }

    public void filter(String text) {
        recetteList.clear();
        if (text.isEmpty()) {
            recetteList.addAll(fullList);
        } else {
            text = text.toLowerCase();
            for (RecetteListLayout item : fullList) {
                if (item.getTitle().toLowerCase().contains(text)) {
                    recetteList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recetteList.size();
    }

    public interface OnRecetteAddListener {
        void onRecetteAdd(RecetteListLayout recette);
    }

    private OnRecetteAddListener addListener;

    public void setOnRecetteAddListener(OnRecetteAddListener listener) {
        this.addListener = listener;
    }
}