package model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestModel {

    public static void main(String[] args) throws Exception {

        // Chemin de la vraie image
        Path chemin = Path.of(
            "D:/photo a faire sortir/Photos anniverssaire Saliha  1 ans/DSC00436.jpg"
        );

        // Vérifier si le fichier existe
        if (!Files.exists(chemin)) {
            System.out.println("Fichier introuvable !");
            return;
        }

        System.out.println("Fichier trouvé avec succès !");

        // Récupérer la taille du fichier
        long taille = Files.size(chemin);

        // Récupérer la date de modification
        FileTime date = Files.getLastModifiedTime(chemin);

        LocalDateTime dateModification = LocalDateTime.ofInstant(
            date.toInstant(),
            ZoneId.systemDefault()
        );

        // Créer les métadonnées de test
        ImageMetadata metadata = new ImageMetadata(
            1920,
            1080,
            true,
            "Canon",
            "EOS 250D",
            LocalDateTime.now(),
            48.8566,
            2.3522
        );

        // Tester ImageMetadata
        System.out.println("\n========== TEST IMAGEMETADATA ==========");

        System.out.println(metadata);
        System.out.println("Largeur : " + metadata.getLargeur());
        System.out.println("Hauteur : " + metadata.getHauteur());
        System.out.println("Alpha : " + metadata.hasAlpha());
        System.out.println("Marque : " + metadata.getMarqueAppareil());
        System.out.println("Modèle : " + metadata.getModelAppareil());
        System.out.println("Date de prise de vue : " + metadata.getDatePriseVue());
        System.out.println("Latitude : " + metadata.getLatitude());
        System.out.println("Longitude : " + metadata.getLongitude());
        System.out.println("Présence GPS : " + metadata.hasGps());

        // Créer un objet ImageFile avec le vrai fichier
        ImageFile image1 = new ImageFile(
            chemin,
            "JPEG",
            taille,
            dateModification,
            metadata
        );

        // Tester ImageFile
        System.out.println("\n========== TEST IMAGEFILE ==========");

        System.out.println(image1);
        System.out.println("Nom : " + image1.getFileName());
        System.out.println("Chemin : " + image1.getPath());
        System.out.println("Format : " + image1.getFormat());
        System.out.println("Taille en octets : " + image1.getSize());
        System.out.println("Taille formatée : " + image1.getFormattedSize());
        System.out.println("Date de modification : " + image1.getDateModification());
        System.out.println("Possède des métadonnées : " + image1.hasMetadata());

        // Tester setMetadata()
        image1.setMetadata(null);

        System.out.println("\n========== TEST SETMETADATA ==========");
        System.out.println("Après suppression des métadonnées : "
                + image1.hasMetadata());

        // Créer un deuxième objet ImageFile
        ImageFile image2 = new ImageFile(
            chemin,
            "JPEG",
            taille,
            dateModification,
            null
        );

        // Tester equals()
        System.out.println("\n========== TEST EQUALS ==========");
        System.out.println("image1 égale image2 : " + image1.equals(image2));

        // Tester hashCode()
        System.out.println("\n========== TEST HASHCODE ==========");
        System.out.println("Hash image1 : " + image1.hashCode());
        System.out.println("Hash image2 : " + image2.hashCode());

        // Tester les setters de ImageMetadata
        metadata.setLargeur(1280);
        metadata.setHauteur(720);

        System.out.println("\n========== TEST SETTERS ==========");
        System.out.println("Nouvelle largeur : " + metadata.getLargeur());
        System.out.println("Nouvelle hauteur : " + metadata.getHauteur());

        System.out.println("\n========== FIN DES TESTS ==========");
    }
}
