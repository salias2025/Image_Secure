package scanner;


import model.ImageFile;
import java.nio.file.Path;
import java.util.ArrayList;

public class TestScanner {

    public static void main(String[] args) {

        // Créer le scanner
        ImageScanner scanner = new ImageScanner();

        // Définir le dossier à scanner
        Path dossier = Path.of(
            "D:/cy cergy paris universite/CATI L2"
        );

        // Scanner le dossier
        ArrayList<ImageFile> images = scanner.scannerDossier(dossier);

        // Afficher le nombre d'images trouvées
        System.out.println("================================");
        System.out.println("       RESULTAT DU SCAN");
        System.out.println("================================");

        System.out.println("Dossier scanné : " + dossier);
        System.out.println("Nombre d'images trouvées : " + images.size());

        System.out.println("--------------------------------");

        // Afficher les informations de chaque image
        for (ImageFile image : images) {
            System.out.println(image);
        }

        System.out.println("================================");
        System.out.println("          FIN DU SCAN");
        System.out.println("================================");
    }
}
