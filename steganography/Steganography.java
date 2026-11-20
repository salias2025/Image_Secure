package steganography;
import java.nio.file.Path;
import model.ImageFile;
import model.ImageMetadata;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.ZoneId;

public class Steganography {

    public static final String TAG_DEBUT="START";
    public static final String TAG_FIN="END";


public  ImageFile cacherMessage(ImageFile image,String message){
    try{
     ImageFile copie=copierImage(image);

   BufferedImage bf =ImageIO.read(copie.getPath().toFile());
   String messageComplet = TAG_DEBUT + message + TAG_FIN;

   byte[] donnees = messageComplet.getBytes(StandardCharsets.US_ASCII);

   long capacite = (long) bf.getWidth() * bf.getHeight();

   if (donnees.length > capacite) {
            throw new IllegalArgumentException("Image trop petite pour cacher ce message.");
        }
    
    if (!bf.getColorModel().hasAlpha()) {
            throw new IllegalArgumentException("L'image doit posséder une composante Alpha.");
    }

    for (int i = 0; i < donnees.length; i++) {

            int octet = donnees[i] & 0xFF;
            ecrireSurPixel(bf, i, octet);

        }
    ImageIO.write(bf, "png", copie.getPath().toFile());
  

    return copie;
    }
    catch (IOException e) {
        throw new RuntimeException("Impossible de cacher le message.", e);
    }
    
      
}

public String extraireMessage(ImageFile imageFile) {

    try {
        BufferedImage bufferedImage = ImageIO.read(imageFile.getPath().toFile());

        if (bufferedImage == null) {
            throw new IllegalArgumentException("Image illisible.");
        }

        StringBuilder resultat = new StringBuilder();
        StringBuilder fenetre = new StringBuilder();

        boolean debutTrouve = false;

        long nombrePixels = (long) bufferedImage.getWidth() * bufferedImage.getHeight();

        for (int i = 0; i < nombrePixels; i++) {

            int octet = lireDuPixel(bufferedImage, i);
            char caractere = (char) octet;

            if (!debutTrouve) {

                fenetre.append(caractere);

                if (fenetre.length() > TAG_DEBUT.length()) {
                    fenetre.deleteCharAt(0);
                }

                if (fenetre.toString().equals(TAG_DEBUT)) {
                    debutTrouve = true;
                    fenetre.setLength(0);
                }

            } else {

                resultat.append(caractere);
                fenetre.append(caractere);

                if (fenetre.length() > TAG_FIN.length()) {
                    fenetre.deleteCharAt(0);
                }

                if (fenetre.toString().equals(TAG_FIN)) {
                    resultat.setLength(resultat.length() - TAG_FIN.length());
                    return resultat.toString();
                }
            }
        }

        throw new IllegalArgumentException("Aucun message valide trouvé.");

    } catch (IOException e) {
        throw new RuntimeException("Impossible de lire l'image.", e);
    }
}
private  void ecrireSurPixel(BufferedImage image,int position,int octet){

    int largeur = image.getWidth();

    int x = position % largeur;
    int y = position / largeur;

    int pixel = image.getRGB(x, y);

    int alpha = (pixel >> 24) & 0xFF;
    int rouge = (pixel >> 16) & 0xFF;
    int vert = (pixel >> 8) & 0xFF;
    int bleu = pixel & 0xFF;

    
    int bitsAlpha = (octet >> 6) & 3;  
    int bitsRouge = (octet >> 4) & 3;     
    int bitsVert = (octet >> 2) & 3;   
    int bitsBleu = octet & 3;

    alpha = (alpha & 252) | bitsAlpha;
    rouge = (rouge & 252) | bitsRouge;
    vert = (vert & 252) | bitsVert;
    bleu = (bleu & 252) | bitsBleu;

    pixel = (alpha << 24) | (rouge << 16) | (vert << 8) | bleu;
    image.setRGB(x, y, pixel);



}
private int lireDuPixel(BufferedImage image,int position){
       
    int largeur = image.getWidth();

    int x = position % largeur;
    int y = position / largeur;

    int pixel = image.getRGB(x, y);

    int alpha = (pixel >> 24) & 0xFF;
    int rouge = (pixel >> 16) & 0xFF;
    int vert = (pixel >> 8) & 0xFF;
    int bleu = pixel & 0xFF;

    int bitsAlpha = alpha & 3;
    int bitsRouge = rouge & 3;
    int bitsVert = vert & 3;
    int bitsBleu = bleu & 3;

   int resultat = (bitsAlpha << 6) | (bitsRouge << 4) | (bitsVert << 2)| bitsBleu;
      
       return resultat;
}

private ImageFile copierImage(ImageFile image){

    Path chemin=image.getPath();
    Path nouveauChemin = chemin.resolveSibling("copie_" + chemin.getFileName());
    try{
         Files.copy(chemin, nouveauChemin);
            return new ImageFile(nouveauChemin,image.getFormat(),image.getSize(),image.getDateModification(),image.getMetadata());

    }catch(IOException e){
        throw new RuntimeException("Impossible de copier l'image.", e);
    }
}
    
}

