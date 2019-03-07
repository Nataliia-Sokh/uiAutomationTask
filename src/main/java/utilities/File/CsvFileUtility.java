package utilities.File;

import junit.framework.AssertionFailedError;

import java.io.*;

public final class CsvFileUtility {

    /**
     * Gets the headers of a given Csv File
     * @param pathFile of the Csv
     * @return
     */
    public static String[] getCsvHeaders(String pathFile){
        String csvFile = pathFile;
        String line;
        String csvSplitBy = ",";
        String[] csvHeaders;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
            line = bufferedReader.readLine();
            line = line.replace(System.getProperty("line.separator"),"");
            csvHeaders = line.split(csvSplitBy);
            csvHeaders = removeDoubleQuotes(csvHeaders);
            return csvHeaders;
        } catch (IOException e) {
            throw new AssertionFailedError(String.format("There was an error when getting the headers of the Csv file: %s"
                    ,csvFile));
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                throw new AssertionFailedError(String.format("There was an error when  closing the buggered stream of Csv file: %s"
                        ,csvFile));
            }
        }
    }

    /**
     *Removes the double quote of an array of strings
     * @param words, String Array
     * @return String Array
     */
    private static String[] removeDoubleQuotes(String[] words){
        for(int i=0; i<words.length; i++){
            words[i] = words[i].replace("\"", "");
        }
        return words;
    }
}
