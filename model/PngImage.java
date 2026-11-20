package model ;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class PngImage  extends  ImageFile{
       public PngImage(Path path,String format , long size,LocalDateTime dateModification, ImageMetadata metadata){
                 super( path,format ,  size, dateModification, metadata);
       }
}