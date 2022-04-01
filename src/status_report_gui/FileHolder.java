import java.io.File;

class FileHolder {
   private File the_file;
   public File getFile() {
      return the_file;
   }
   public void setFile(File given_file) {
      the_file = given_file;
   }
}