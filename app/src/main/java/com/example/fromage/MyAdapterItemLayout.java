package com.example.fromage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterItemLayout extends RecyclerView.Adapter<MyAdapterItemLayout.ViewHolder> {

    private List<ItemLayout> itemList;

    public MyAdapterItemLayout(List<ItemLayout> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView subtitleView;
        Button retirerBtn, passerBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.CheeseIcon);
            titleView = itemView.findViewById(R.id.TitreCheese);
            subtitleView = itemView.findViewById(R.id.ActionCheese);
            retirerBtn = itemView.findViewById(R.id.buttonRetirer);
            passerBtn = itemView.findViewById(R.id.buttonPasser);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemLayout item = itemList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.titleView.setText(item.getTitle());

        int joursRestants = item.joursRestants();
        if (joursRestants > 0) {
            holder.subtitleView.setText(item.getCurrentEtape().action +" dans " + joursRestants + " jours");
            holder.retirerBtn.setVisibility(View.VISIBLE);
            holder.passerBtn.setVisibility(View.GONE);
        } else {
            holder.subtitleView.setText("Étape prête : " + item.getCurrentEtape().action);
            holder.passerBtn.setVisibility(View.VISIBLE);

            if (!item.isNotifiedForCurrentEtape()) {
                // Vérifier préférence utilisateur (notifications activées)
                boolean isNotifEnabled = true;
                try {
                    isNotifEnabled = holder.itemView.getContext()
                            .getSharedPreferences("settings", Context.MODE_PRIVATE)
                            .getBoolean("notifications_enabled", true);
                } catch (Exception ignored) {
                }

                if (isNotifEnabled) {
                    NotificationHelper.sendNotification(
                            holder.itemView.getContext(),
                            "Étape prête pour " + item.getTitle(),
                            item.getTitle() + " : " + item.getCurrentEtape().action
                    );
                }
                // Marquer comme notifié pour ne pas renvoyer
                item.setNotifiedForCurrentEtape(true);
            }
        }

        // Bouton "Passer"
        holder.passerBtn.setOnClickListener(v -> {
            item.passerEtape();
            item.setNotifiedForCurrentEtape(false);
            notifyItemChanged(position);
        });

        // Bouton "Retirer"
        holder.retirerBtn.setOnClickListener(v -> {
            Context context = v.getContext();
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Voulez-vous vraiment retirer " + item.getTitle() + " de votre cave ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            MaCave.removeFromList(itemList.get(currentPosition));
                            itemList.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                        })
                        .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                        .setCancelable(true)
                        .show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
}