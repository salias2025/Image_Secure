
package metadata;

import model.ImageFile;
import model.ImageMetadata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestExtractor {

    public static void main(String[] args) throws Exception {

        
        Path chemin = Path.of( "D:/photo a faire sortir/Photos anniverssaire Saliha  1 ans/DSC00436.jpg");

        
        if (!Files.exists(chemin)) {
            System.out.println("Fichier introuvable !");
            return;
        }

        System.out.println("Fichier trouvé avec succès !");

        
        long taille = Files.size(chemin);

        
        FileTime date = Files.getLastModifiedTime(chemin);

        LocalDateTime dateModification = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

        
        ImageFile image = new ImageFile(chemin,"JPEG", taille,dateModification,null);

        // Tester ExtractorFactory
        System.out.println("\n========== TEST EXTRACTOR FACTORY ==========");

        ExtractorFactory factory = new ExtractorFactory();

        // La Factory choisit automatiquement le bon extracteur
        MetadataExtractor extractor = factory.creer(image);

        System.out.println("Extracteur utilisé : "+ extractor.getClass().getSimpleName());

        // Tester l'extraction des métadonnées
        System.out.println("\n========== TEST EXTRACTION METADATA ==========");

        ImageMetadata metadata = extractor.extraire(image);
        image.setMetadata(metadata);

        // Afficher les métadonnées extraites
        System.out.println(metadata);
       

        // Tester ImageFile après extraction
        System.out.println("\n========== TEST IMAGEFILE ==========");

        System.out.println(image);
       

        System.out.println("\n========== FIN DES TESTS ==========");
    }
}
