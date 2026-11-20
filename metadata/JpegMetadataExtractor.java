package metadata;

// Imports de nos propres classes
import model.ImageMetadata;
import model.ImageFile;

// Imports pour lire l'image
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Imports de la bibliothèque Metadata Extractor
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.lang.GeoLocation;

// Imports pour manipuler les dates
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;



public class JpegMetadataExtractor implements MetadataExtractor {

    public JpegMetadataExtractor(){

    };

    @Override
    public ImageMetadata extraire(ImageFile image) {

        try{

            File fichier=image.getPath().toFile();
            // On lit l'image pour récupérer ses dimensions et son canal Alpha
            BufferedImage imageLue = ImageIO.read(fichier);
            
            
            int largeur = imageLue.getWidth();
            int hauteur = imageLue.getHeight();
            boolean hasAlpha = imageLue.getColorModel().hasAlpha();
            
            // On demande à Metadata Extractor de lire les métadonnées du JPEG
            Metadata metadata = ImageMetadataReader.readMetadata(fichier);

            // Valeurs par défaut si les métadonnées sont absentes
            String marque = null;
            String modele = null;
            LocalDateTime datePriseVue = null;
            double latitude = 0;
            double longitude = 0;

             // on extrait d'abord la marque et le model 
            
             ExifIFD0Directory appareil = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            
            if (appareil != null) {

                marque = appareil.getString(ExifIFD0Directory.TAG_MAKE);
                modele = appareil.getString(ExifIFD0Directory.TAG_MODEL);
            }
 
            //puis on extrait la date de prise de vue
     ExifSubIFDDirectory dpv =metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (dpv != null) {
                Date date = dpv.getDateOriginal();

             
                if (date != null) {
                    //on convertit la vrai date et heure en un instant puis on les adapte selon le fuseau horaire de l'ordi 
                    //puis on va convertir en un objet LocalDateTime qui a le format 	2025-08-15T14:30
                    //T est un separarateur entre date et time 
                    datePriseVue = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }
            }

            GpsDirectory gps =metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (gps != null) {
                //la classe GeoLocation contient uniquement les deux attributs latitude and longitude 
                GeoLocation position = gps.getGeoLocation();

                if (position != null) {
                    latitude = position.getLatitude();
                    longitude = position.getLongitude();
                }
            }

            return new ImageMetadata(largeur,hauteur,hasAlpha,marque,modele,datePriseVue,latitude,longitude);

        }
        catch(IOException | ImageProcessingException e){
                         throw new RuntimeException("Erreur lors de l'extraction des métadonnées jpeg", e);
        }
}
}
