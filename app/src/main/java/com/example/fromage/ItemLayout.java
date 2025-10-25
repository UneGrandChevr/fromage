    package com.example.fromage;

    import java.util.ArrayList;
    import java.util.List;

    public class ItemLayout {
        private int imageResId;
        private String title;
        private String subtitle;
        private List<Etape> etapes;

        private int currentEtapeIndex = 0;
        private long dateDebut;

        private boolean notifiedForCurrentEtape = false;

        public ItemLayout(int imageResId, String title, ArrayList<Etape> pEtapes) {
            this.imageResId = imageResId;
            this.title = title;
            this.subtitle = pEtapes.get(0).action;
            this.etapes = pEtapes;
            this.dateDebut = System.currentTimeMillis();
        }

        public Etape getCurrentEtape() {
            if (currentEtapeIndex < etapes.size()) {
                return etapes.get(currentEtapeIndex);
            }
            return null;
        }

        public void passerEtape() {
            if (currentEtapeIndex < etapes.size() - 1) {
                currentEtapeIndex++;
                subtitle = etapes.get(currentEtapeIndex).action;
                dateDebut = System.currentTimeMillis(); // reset du timer pour la prochaine étape
            } else {
                subtitle = "Affinage terminé !";
            }
        }

        public int joursRestants() {
            Etape e = getCurrentEtape();
            if (e == null) return 0;
            int joursEcoules = calculerJoursDepuisDebut();
            return Math.max(e.jours - joursEcoules, 0);
        }

        private int calculerJoursDepuisDebut() {
            long maintenant = System.currentTimeMillis();
            long diffMillis = maintenant - dateDebut;
            return (int) (diffMillis / (1000 * 60 * 60 * 24));
        }

        public int getImageResId() {
            return imageResId;
        }

        public boolean isNotifiedForCurrentEtape() {
            return notifiedForCurrentEtape;
        }

        public void setNotifiedForCurrentEtape(boolean value) {
            this.notifiedForCurrentEtape = value;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
        public Etape getEtape(int index){
            return etapes.get(index);
        }


        public static class Etape{
            int jours;
            String action;

            public Etape(int jours, String action) {
                this.jours = jours;
                this.action = action;
            }
        }
    }
