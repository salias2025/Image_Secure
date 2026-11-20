package model;

import java.time.LocalDateTime;

public class ImageMetadata {
    private int largeur;
    private int hauteur;
    private boolean hasAlpha;
    private String marqueAppareil;
    private String modelAppareil;
    private LocalDateTime datePriseVue;
    private double latitude;
    private  double longitude;

    public ImageMetadata( int largeur, int hauteur, boolean hasAlpha,String marqueAppareil,String modelAppareil,
     LocalDateTime datePriseVue,double latitude,double longitude){
        this.largeur=largeur;
        this.hauteur=hauteur;
        this.hasAlpha=hasAlpha;
        this.marqueAppareil=marqueAppareil;
        this.modelAppareil=modelAppareil;
        this.datePriseVue=datePriseVue;
        this.latitude=latitude;
        this.longitude=longitude;
         }

         public int getLargeur(){
            return largeur;
         }
         public int getHauteur(){
            return hauteur;
         }
         public boolean hasAlpha(){
            return hasAlpha;
         }
         public String getMarqueAppareil(){
            return marqueAppareil;
         }
         public String getModelAppareil(){
            return modelAppareil;
         }
         public LocalDateTime getDatePriseVue(){
            return datePriseVue;
         }
         public double getLatitude(){
            return latitude;
         }
         public double getLongitude(){
            return longitude;
         }

         public void setLargeur(int largeur) {
            this.largeur = largeur;
         }

        public void setHauteur(int hauteur) {
          this.hauteur = hauteur;
        }

       public void setHasAlpha(boolean hasAlpha) {
         this.hasAlpha = hasAlpha;
       }

      public void setMarqueAppareil(String marqueAppareil) {
        this.marqueAppareil = marqueAppareil;
       }

     public void setModelAppareil(String modelAppareil) {
       this.modelAppareil = modelAppareil;
    }

    public void setDatePriseVue(LocalDateTime datePriseVue) {
       this.datePriseVue = datePriseVue;
     }

   public void setPositionGps(double latitude ,double longitude){
    this.latitude=latitude;
    this.longitude=longitude;
   }
   public boolean hasGps(){
            return latitude != 0 && longitude != 0;
   }
   @Override 
   
   
   public String toString() {
    return "ImageMetadata{" +
            "largeur=" + largeur +
            ", hauteur=" + hauteur +
            ", hasAlhpa=" + hasAlpha+
            ", marqueAppareil='" + marqueAppareil + '\'' +
            ", modelAppareil='" + modelAppareil + '\'' +
            ", datePriseVue=" + datePriseVue +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
}
    

         
}
