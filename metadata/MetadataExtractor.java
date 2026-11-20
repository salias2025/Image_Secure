package metadata;

import model.ImageMetadata;
import model.ImageFile;

public interface MetadataExtractor {
    public ImageMetadata extraire(ImageFile image);

    
}
