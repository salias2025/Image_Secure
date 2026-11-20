package cli;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import model.ImageFile;
import model.ImageMetadata;
import metadata.ExtractorFactory;
import metadata.MetadataExtractor;
import scanner.ImageScanner;
import scanner.ImageFileFilter;
import steganography.Steganography;

public class CLI {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Erreur : aucun paramètre fourni.");
            System.out.println("Utilisez -h ou --help pour afficher l'aide.");
            return;
        }

        if (args[0].equals("-h") || args[0].equals("--help")) {
            afficherAide();
            return;
        }

        if (args[0].equals("-d")) {
            if (args.length < 2) {
                System.out.println("Le paramètre -d nécessite un chemin de dossier.");
                return;
            }
            listerImages(Path.of(args[1]));
            return;
        }

        if (args[0].equals("-f")) {

            if (args.length < 2) {
                erreur("Le paramètre -f nécessite un chemin de fichier.");
            }

            Path fichier = Path.of(args[1]);

            if (args.length >= 4 && args[2].equals("-s")) {
                cacherMessage(fichier, args[3]);
                return;
            }

            if (args.length >= 3 && args[2].equals("-e")) {
                extraireMessage(fichier);
                return;
            }

            if (args.length == 2) {
                afficherMetadonnees(fichier);
                return;
            }

            System.out.println("Option inconnue après -f. Utilisez -s \"message\" ou -e.");
            return;
        }

        // Option inconnue
        System.out.println("Option inconnue : " + args[0]);
    }

//helper methods
    private static void afficherAide() {
        System.out.println("PHOTO MANAGER - MODE CONSOLE");
        System.out.println();
        System.out.println("Utilisation :");
        System.out.println();
        System.out.println("  java -jar cli.jar -h | --help");
        System.out.println("      Affiche cette aide.");
        System.out.println();
        System.out.println("  java -jar cli.jar -d <dossier>");
        System.out.println("      Parcourt le dossier (et sous-dossiers) et liste");
        System.out.println("      tous les fichiers PNG et JPEG trouvés.");
        System.out.println();
        System.out.println("  java -jar cli.jar -f <fichier>");
        System.out.println("      Affiche les métadonnées de l'image (dimensions,");
        System.out.println("      appareil, date, GPS, etc.).");
        System.out.println();
        System.out.println("  java -jar cli.jar -f <fichier> -s \"texte\"");
        System.out.println("      Cache le texte ASCII dans l'image (PNG avec alpha).");
        System.out.println("      Une copie est créée à côté du fichier original.");
        System.out.println();
        System.out.println("  java -jar cli.jar -f <fichier> -e");
        System.out.println("      Extrait le texte dissimulé dans l'image.");
        System.out.println();
        System.out.println("  java -jar gui.jar");
        System.out.println("      Lance l'interface graphique.");
    }

    private static void listerImages(Path dossier) {

        if (!Files.isDirectory(dossier)) {
            erreur("Le chemin n'est pas un dossier valide : " + dossier);
        }

        ImageScanner scanner = new ImageScanner();
        ArrayList<ImageFile> images = scanner.scannerDossier(dossier);

        System.out.println("LISTE DES IMAGES");
        System.out.println();
        System.out.println("Dossier  : " + dossier);
        System.out.println("Trouvées : " + images.size() + " image(s)");
        System.out.println();

        if (images.isEmpty()) {
            System.out.println("Aucune image PNG ou JPEG trouvée.");
        } else {
            int i = 1;
            for (ImageFile image : images) {
                System.out.println(i + ". " + image.getFileName());
                System.out.println("   Chemin : " + image.getPath());
                System.out.println("   Format : " + image.getFormat()
                        + "  |  Taille : " + image.getFormattedSize()
                        + "  |  Modifié le : " + image.getDateModification());
                System.out.println();
                i++;
            }
        }
    }

    private static ImageFile construireImageFile(Path fichier) {

        if (!Files.isRegularFile(fichier)) {
            erreur("Fichier introuvable : " + fichier);
            return null;
        }

        ImageFileFilter filtre = new ImageFileFilter();

        if (!filtre.accepter(fichier)) {
            erreur("Format non supporté (JPEG ou PNG attendu) : " + fichier);
            return null;
        }

        String typeMime = filtre.detecterTypeMime(fichier);
        String format = "image/png".equals(typeMime) ? "PNG" : "JPEG";

        try {
            long taille = Files.size(fichier);
            FileTime date = Files.getLastModifiedTime(fichier);
            LocalDateTime dateModification = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

            return new ImageFile(fichier, format, taille, dateModification, null);

        } catch (Exception e) {
            erreur("Impossible de lire le fichier : " + e.getMessage());
            return null;
        }
    }

    private static void erreur(String message) {
        System.err.println("Erreur : " + message);
        System.err.println("Utilisez -h ou --help pour afficher l'aide.");
        System.exit(1);
    }

    private static void afficherMetadonnees(Path fichier) {

        ImageFile image = construireImageFile(fichier);
        if (image == null) return;

        ExtractorFactory factory = new ExtractorFactory();
        MetadataExtractor extractor = factory.creer(image);

        if (extractor == null) {
            erreur("Aucun extracteur disponible pour ce format.");
        }

        try {
            ImageMetadata metadata = extractor.extraire(image);
            image.setMetadata(metadata);

            System.out.println("MÉTADONNÉES DE L'IMAGE");
            System.out.println();
            System.out.println("Fichier       : " + image.getFileName());
            System.out.println("Chemin        : " + image.getPath());
            System.out.println("Format        : " + image.getFormat());
            System.out.println("Taille        : " + image.getFormattedSize());
            System.out.println("Modifié le    : " + image.getDateModification());
            System.out.println();
            System.out.println("Caractéristiques :");
            System.out.println("  Dimensions  : " + metadata.getLargeur() + " x " + metadata.getHauteur() + " px");
            System.out.println("  Canal alpha : " + (metadata.hasAlpha() ? "oui" : "non"));
            System.out.println();
            System.out.println("Appareil photo :");
            System.out.println("  Marque      : " + (metadata.getMarqueAppareil() != null ? metadata.getMarqueAppareil() : "—"));
            System.out.println("  Modèle      : " + (metadata.getModelAppareil() != null ? metadata.getModelAppareil() : "—"));
            System.out.println("  Date prise  : " + (metadata.getDatePriseVue() != null ? metadata.getDatePriseVue() : "—"));
            System.out.println();
            System.out.println("Géolocalisation :");
            if (metadata.hasGps()) {
                System.out.println("  Latitude    : " + metadata.getLatitude());
                System.out.println("  Longitude   : " + metadata.getLongitude());
            } else {
                System.out.println("  Aucune donnée GPS disponible");
            }
            System.out.println();

        } catch (RuntimeException e) {
            erreur("Erreur lors de l'extraction des métadonnées : " + e.getMessage());
        }
    }

    private static void cacherMessage(Path fichier, String message) {

        ImageFile image = construireImageFile(fichier);
        if (image == null) return;

        if (!"PNG".equalsIgnoreCase(image.getFormat())) {
            erreur("Le fichier doit être une image PNG (composante Alpha requise).");
        }

        Steganography steganography = new Steganography();

        System.out.println("DISSIMULATION DE MESSAGE");
        System.out.println();

        try {
            ImageFile copie = steganography.cacherMessage(image, message);

            System.out.println("Message caché avec succès !");
            System.out.println();
            System.out.println("Fichier original : " + image.getPath());
            System.out.println("Copie créée      : " + copie.getPath());
            System.out.println("Longueur message : " + message.length() + " caractère(s)");

        } catch (IllegalArgumentException e) {
            erreur("Impossible de cacher le message : " + e.getMessage());
        } catch (RuntimeException e) {
            erreur("Erreur : " + e.getMessage());
        }

        System.out.println();
    }

    private static void extraireMessage(Path fichier) {

        ImageFile image = construireImageFile(fichier);
        if (image == null) return;

        Steganography steganography = new Steganography();

        System.out.println("EXTRACTION DE MESSAGE");
        System.out.println();

        try {
            String message = steganography.extraireMessage(image);

            System.out.println("Message extrait avec succès !");
            System.out.println();
            System.out.println("Contenu du message :");
            System.out.println();
            System.out.println(message);
            System.out.println();
            System.out.println("Longueur : " + message.length() + " caractère(s)");

        } catch (IllegalArgumentException e) {
            erreur("Aucun message trouvé : " + e.getMessage());
        } catch (RuntimeException e) {
            erreur("Erreur lors de l'extraction : " + e.getMessage());
        }

        System.out.println();
    }
}