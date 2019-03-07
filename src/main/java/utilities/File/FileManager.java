package utilities.File;

import junit.framework.AssertionFailedError;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileManager {
    /**
     * Allows to delete a given file
     * @param filePath of the file to be deleted
     */
    public static void deleteFile(String filePath){
        try{
            Files.delete(Paths.get(filePath));
        } catch (Exception e){
            throw new AssertionFailedError(String.format("There was an error deleting the file: %s, error: %s",filePath,e.toString()));
        }
    }

}