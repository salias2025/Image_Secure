

package scanner;

import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.io.IOException;
import java.util.stream.Stream;

import model.ImageFile;

public class ImageScanner {

    private ImageFileFilter filtre;

    public ImageScanner() {
        filtre = new ImageFileFilter();
    }

    public ArrayList<ImageFile> scannerDossier(Path path) {

        ArrayList<ImageFile> images = new ArrayList<>();

        // Vérifier que le chemin correspond à un dossier
        if (path == null || !Files.isDirectory(path)) {
            return images;
        }

        // Parcourir le dossier et tous ses sous-dossiers
        try  {
                 Stream<Path> fichiers = Files.walk(path);
            // Convertir le Stream en liste pour utiliser la boucle for
            for (Path fichier : fichiers.toList()) {

                // Vérifier si le fichier est une image acceptée
                if (filtre.accepter(fichier)) {

                    try {

                        // Récupérer la taille du fichier
                        long taille = Files.size(fichier);

                        // Récupérer la date de modification
                        FileTime date = Files.getLastModifiedTime(fichier);

                        LocalDateTime dateModification =
                            LocalDateTime.ofInstant(
                                date.toInstant(),
                                ZoneId.systemDefault()
                            );

                        // Convertir le type MIME en format simple
                        String typeMime = filtre.detecterTypeMime(fichier);

                        String format;

                        if ("image/png".equals(typeMime)) {
                            format = "PNG";
                        } else {
                            format = "JPEG";
                        }

                        // Créer l'objet ImageFile
                        ImageFile image = new ImageFile(
                            fichier,
                            format,
                            taille,
                            dateModification,
                            null
                        );

                        // Ajouter l'image à la liste
                        images.add(image);

                    } catch (IOException e) {

                        System.out.println(
                            "Impossible de lire le fichier : " + fichier
                        );
                    }
                }
            }

        } catch (IOException e) {

            System.out.println(
                "Erreur lors du parcours du dossier : " + path
            );
        }

        return images;
    }
}
