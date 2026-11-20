package metadata;
import scanner.ImageFileFilter;
import model.ImageFile;
import model.ImageMetadata;

public class ExtractorFactory {
    
    private ImageFileFilter filtre;

    public  ExtractorFactory(){
      filtre =new ImageFileFilter();
    }
    public MetadataExtractor creer(ImageFile image){
        
     try{
        if (image == null || image.getPath() == null) {
            throw new IllegalArgumentException("Image invalide");
        }

        if (!filtre.accepter(image.getPath())) {
            throw new IllegalArgumentException("Format d'image invalide");
        }

         String typeMime = filtre.detecterTypeMime(image.getPath());

         if ("image/jpeg".equals(typeMime)) {
            return new JpegMetadataExtractor();
        }

        if ("image/png".equals(typeMime)) {
            return new PngMetadataExtractor();
        }

        throw new IllegalArgumentException("Format non supporté");

     }
     catch (IllegalArgumentException e) {

        System.out.println("Erreur : " + e.getMessage());
        return null;
    }

    }

}
