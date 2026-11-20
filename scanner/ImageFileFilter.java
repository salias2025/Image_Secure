
package scanner;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;

public class ImageFileFilter {

    private ArrayList<String> setExtensions;

    public ImageFileFilter() {

        setExtensions = new ArrayList<>();

        setExtensions.add(".jpg");
        setExtensions.add(".jpeg");
        setExtensions.add(".png");
    }

    public boolean accepter(Path path) {

        if (path == null || !Files.isRegularFile(path)) {
            return false;
        }

        // Récupérer le nom du fichier en minuscules
        String nomFichier = path.getFileName().toString().toLowerCase();

        // Vérifier l'extension
        boolean extensionValide = false;

        for (String extension : setExtensions) {

            if (nomFichier.endsWith(extension)) {
                extensionValide = true;
                break;
            }
        }

        if (!extensionValide) {
            return false;
        }

        // Détecter le type MIME grâce à la signature
        String typeMime = detecterTypeMime(path);

        // Vérifier la correspondance extension / MIME
        if (nomFichier.endsWith(".jpg") || nomFichier.endsWith(".jpeg")) {
            return "image/jpeg".equals(typeMime);
        }

        if (nomFichier.endsWith(".png")) {
            return "image/png".equals(typeMime);
        }

        return false;
    }

    public String detecterTypeMime(Path path) {

        if (path == null || !Files.isRegularFile(path)) {
            return null;
        }

        // Les signatures des formats
        byte[] signatureJPEG = { (byte) 0xFF , (byte) 0xD8, (byte) 0xFF };

        byte[] signaturePNG = { (byte) 0x89,0x50, 0x4E,0x47,0x0D,0x0A,0x1A, 0x0A};

        try (InputStream flux = Files.newInputStream(path)) {

            // Lire les 8 premiers octets du fichier
            byte[] premiersOctets = new byte[8];

            int nombreOctetsLus = flux.read(premiersOctets);

            // Vérifier si le fichier contient suffisamment d'octets
            if (nombreOctetsLus < 3) {
                return null;
            }

            // Vérifier la signature JPEG
            boolean estJPEG = true;

            for (int i = 0; i < signatureJPEG.length; i++) {

                if (premiersOctets[i] != signatureJPEG[i]) {
                    estJPEG = false;
                    break;
                }
            }

            if (estJPEG) {
                return "image/jpeg";
            }

            // Vérifier la signature PNG
            if (nombreOctetsLus >= 8) {

                boolean estPNG = true;

                for (int i = 0; i < signaturePNG.length; i++) {

                    if (premiersOctets[i] != signaturePNG[i]) {
                        estPNG = false;
                        break;
                    }
                }

                if (estPNG) {
                    return "image/png";
                }
            }

            // Signature inconnue
            return "application/octet-stream";

        } catch (IOException e) {

            return null;
        }
    }
}
