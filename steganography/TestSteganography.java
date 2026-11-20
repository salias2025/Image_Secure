
package steganography;
import java.nio.file.Path;
import model.ImageFile;
import java.time.ZoneId;
import java.nio.file.Path;
import java.nio.file.Files;

public class TestSteganography {

    public static void main(String[] args) {

        try {
           
            Path chemin = Path.of("D:/cy cergy paris universite/cat.png");

            
            if (!Files.isRegularFile(chemin)) {
                System.out.println("Image introuvable !");
                return;
            }

           
            ImageFile image = new ImageFile(
                chemin,
                Files.probeContentType(chemin),
                Files.size(chemin),
                Files.getLastModifiedTime(chemin).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                null
            );

           
            System.out.println("Format détecté : " + image.getFormat());

            
            Steganography steganography = new Steganography();

           
            String message = "Bonjour, ceci est un test !";

           
            ImageFile copie = steganography.cacherMessage(image, message);

            System.out.println("Message caché avec succès !");
            System.out.println("Chemin de la copie : " + copie.getPath());

           
            String messageExtrait = steganography.extraireMessage(copie);

            
            System.out.println("Message original : " + message);
            System.out.println("Message extrait  : " + messageExtrait);

            if (message.equals(messageExtrait)) {
                System.out.println("TEST REUSSI !");
            } else {
                System.out.println("TEST ECHOUE !");
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
