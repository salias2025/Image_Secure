package model;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class JpegImage extends ImageFile{
     public  JpegImage(Path path,String format , long size,LocalDateTime dateModification, ImageMetadata metadata){
        super( path, format ,  size,dateModification,  metadata);
     }
}