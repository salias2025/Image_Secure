package model;

import java.nio.file.Path;
import java.time.LocalDateTime;


public class ImageFile{
   private Path path;
   private String format ;
   private long size;
   private  LocalDateTime dateModification;
   private ImageMetadata metadata;

   public  ImageFile(Path path,String format , long size,LocalDateTime dateModification, ImageMetadata metadata){
           this.path=path;
           this.format=format;
           this.size=size;
           this.dateModification=dateModification;
           this.metadata=metadata;

        }
        public void setMetadata(ImageMetadata metadata) {
            this.metadata = metadata;
        }
        public Path getPath(){
                  return path;
        }
        public String getFormat(){
                  return format;
        }
        public long getSize(){
                  return size;
        }
        public LocalDateTime getDateModification(){
                  return dateModification;
        }
        public ImageMetadata getMetadata(){
                  return metadata;
        }

        public boolean hasMetadata(){
            if (metadata!=null){
                return true;
            }else {
                return  false;
            }
        }
        public String getFileName() {
          return path.getFileName().toString();
        }
        public String getFormattedSize() {
          double taille = size;

            if (size < 1024) {
               return size + " octets";
           } else if (size < 1024 * 1024) {
                return String.format("%.2f Ko", taille / 1024);
           } else {
                return String.format("%.2f Mo", taille / (1024 * 1024));
           }
         }
          @Override
           public String toString() {
            return "ImageFile{" +"fileName='" + getFileName() + '\'' + ", format='" + format + '\'' + ", size='" + getFormattedSize() + '\'' +
               ", dateModification=" + dateModification +", hasMetadata=" + hasMetadata() + '}';
           }
          
           @Override
        public boolean equals(Object obj) {

            if (this == obj) {
             return true;
            }

            if (obj == null) {
              return false;
            }

            if (!(obj instanceof ImageFile)) {
             return false;
           }

            ImageFile image = (ImageFile) obj;

           return path.equals(image.path)
               && format.equals(image.format)  && size == image.size && dateModification.equals(image.dateModification)  && (metadata == null
                ? image.metadata == null
                : metadata.equals(image.metadata));
            }
          
        @Override
        public int hashCode() {
         return java.util.Objects.hash(
        path, format, size, dateModification, metadata
       );
}
             
         
        }