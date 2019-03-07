package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;


public class TextImpl extends ElementImpl {

    private static final Logger logger = Logger.getLogger(TextImpl.class.getName());

    public TextImpl(By by) {
        super(by);
    }

    public TextImpl(String text) {
        super(By.xpath(getXpathExpression(text)));
    }

    public TextImpl(String text, Query searchArea) {
        super(By.xpath(getXpathExpression(text)), searchArea);
    }

    private static String getXpathExpression(String text) {
        if (text.contains("'") || text.contains("\"")) {
            return xpathLiteralExpression(text);
        } else {
            return "//*[contains(translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+text.toLowerCase()+"')]";
        }
    }

    private static String xpathLiteralExpression(String value) {
        // construct an expression that concatenates all non-double-quote substrings with
        // the quotes, e.g.:
        //
        //    concat("foo",'"',"bar")

        List<String> parts = new ArrayList<>();
        if (value.contains("\"")) {
            for (String str : value.split("\""))
            {
                if (!Strings.isNullOrEmpty(str))
                    parts.add('"' + str + '"');

                parts.add("'\"'");
            }
        } else if (value.contains("'")) {
            for (String str : value.split("'"))
            {
                if (!Strings.isNullOrEmpty(str)) {
                    parts.add('"' + str + '"');
                }

                parts.add("\"'\"");
            }
        }

        // Then remove the extra '"' after the last component.
        parts.remove(parts.size() - 1);

        // Finally, put it together into a concat() function call.
        return "//*[text()=concat(" + String.join(",", parts) + ")]";
    }
}
