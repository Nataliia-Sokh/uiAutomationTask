package utilities.Date;

import java.time.Year;

public final class DateUtility {

    /**
     * Gets the current year
     * @return String with the current year
     */
    public static String getCurrentYear(){
        return Integer.toString(Year.now().getValue());
    }
}
