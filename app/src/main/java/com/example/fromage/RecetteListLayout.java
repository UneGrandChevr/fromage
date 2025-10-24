package com.example.fromage;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RecetteListLayout {
    private int imageResId;
    private String title;
    private String subtitle;
    private int textResId; // <-- référence vers ton fichier texte (ex : R.raw.camembert)

    private ArrayList<ItemLayout.Etape> etapes;

    public RecetteListLayout(int imageResId, String title, String subtitle, int textResId,  ArrayList<ItemLayout.Etape> pEtapes) {
        this.imageResId = imageResId;
        this.title = title;
        this.subtitle = subtitle;
        this.textResId = textResId;
        etapes = pEtapes;
    }

    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public int getTextResId() { return textResId; }

    public ArrayList<ItemLayout.Etape> getEtapes() {
        return etapes;
    }

    // Méthode utilitaire pour lire le contenu du fichier texte
    public String loadFullText(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(textResId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            sb.append("Erreur lors du chargement de la recette.");
        }
        return sb.toString();
    }
}