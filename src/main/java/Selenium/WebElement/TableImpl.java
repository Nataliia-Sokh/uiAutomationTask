package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import Selenium.WebElement.Interface.Table;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableImpl extends ElementImpl implements Table {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(TableImpl.class.getName());

    public TableImpl(By by) {
        super(by);
    }

    public TableImpl(String captionOrHeader) {
        super(possibleTags, captionOrHeader);
    }

    public TableImpl(Query searchArea) {
        super(possibleTags, searchArea);
    }

    public TableImpl(WebElement element) {
        super(element);
    }

    @Override
    public List<Map<String, String>> getAsMaps() {
        logger.debug("Getting HTML table as a list of maps.");
        //Return List
        List<Map<String, String>> mapList = new ArrayList<>();
        //Get all the headers
        List<WebElement> headers = findElements(By.tagName("th"));
        //Get all the rows
        List<WebElement> rows = findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            Map<String, String> dataMap = new HashMap<>();
            List<WebElement> columns = row.findElements(By.tagName("td"));
            int index = 0;
            for (WebElement column : columns) {
                String header = headers.get(index).getText();
                String value = column.getText();
                dataMap.put(header, value);
                index++;
            }
            mapList.add(dataMap);
        }
        logger.info("Converted HTML table as a list of maps.");
        logger.debug(System.lineSeparator() + mapList);
        return mapList;
    }

    @Override
    protected List<WebElement> getElementsByVisibleText(List<String> elementTags, String captionOrHeader) {
        List<WebElement> returnElements = new ArrayList<>();

        logger.debug("Trying to find table using visible text: " + captionOrHeader);

        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            for (WebElement element : elements) {
                String tableCaption = element.findElement(By.tagName("caption")).getText();
                if (tableCaption != null) {
                    if (tableCaption.toLowerCase().equals(captionOrHeader.toLowerCase())) {
                        logger.debug("Table found using visible text: " + captionOrHeader);
                        returnElements.add(element);
                    }
                }

                if (returnElements.size() == 0) {
                    List<WebElement> headers = element.findElements(By.tagName("th"));
                    headers.stream().filter(header -> header.getText().toLowerCase().equals(captionOrHeader.toLowerCase())).forEach(header -> {
                        logger.debug("Table found using visible text: " + captionOrHeader);
                        returnElements.add(element);
                    });
                }
            }
        }

        if (returnElements.size() > 0) {
            return returnElements;
        } else {
            throw new NoSuchElementException("Unable to find element with text: " + captionOrHeader);
        }
    }

    @Override
    protected WebElement getElementByVisibleText(List<String> elementTags, String captionOrHeader) {
        logger.debug("Trying to find table using visible text: " + captionOrHeader);

        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            for (WebElement element : elements) {
                String tableCaption = element.findElement(By.tagName("caption")).getText();
                if (tableCaption != null) {
                    if (tableCaption.toLowerCase().equals(captionOrHeader.toLowerCase())) {
                        logger.debug("Table found using visible text: " + captionOrHeader);
                        return element;
                    }
                }

                List<WebElement> headers = element.findElements(By.tagName("th"));
                for (WebElement header : headers) {
                    if (header.getText().toLowerCase().trim().equals(captionOrHeader.toLowerCase())) {
                        logger.debug("Table found using visible text: " + captionOrHeader);
                        return element;
                    }
                }
            }
        }

        throw new NoSuchElementException("Unable to find element with text: " + captionOrHeader);
    }

    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("table");

        return tags;
    }

    public void clickOnTableElement(String value) {
        boolean clicked = false;
        //Get the table rows
        List<WebElement> tableRows = getTableRows();
        //Iterate each row to find the columns
        for (WebElement row : tableRows) {
            //Get all the columns
            List<WebElement> rowColumns = row.findElements(By.tagName("td"));
            //Iterate each column
            for (WebElement column : rowColumns) {
                //If the text for the column matches the value than click on it
                if (column.getText().equalsIgnoreCase(value)) {
                    column.findElement(By.tagName("a")).click();
                    clicked = true;
                    waitUntilPageFinishLoading();
                    break;
                }
            }
        }

        Assert.assertTrue("Unable to click on " + value, clicked);
    }

    public void verifyValueIsOnTheTable(String value) {
        boolean doesValueExist = false;
        //Get the table rows
        List<WebElement> tableRows = getTableRows();
        //Iterate each row to find the columns
        for (WebElement row : tableRows) {
            //Get all the columns
            List<WebElement> rowColumns = row.findElements(By.tagName("td"));
            //Iterate each column
            for (WebElement column : rowColumns) {
                //If the text for the column matches the value than click on it
                if (column.getText().equalsIgnoreCase(value)) {
                    doesValueExist = true;
                    break;
                }
            }
        }

        Assert.assertTrue(value + " not found on the table", doesValueExist);
    }

    public void verifyValueIsNotOnTheTable(String value) {
        boolean doesValueExist = false;
        //Get the table rows
        List<WebElement> tableRows = getTableRows();
        //Iterate each row to find the columns
        for (WebElement row : tableRows) {
            //Get all the columns
            List<WebElement> rowColumns = row.findElements(By.tagName("td"));
            //Iterate each column
            for (WebElement column : rowColumns) {
                //If the text for the column matches the value than click on it
                if (column.getText().equalsIgnoreCase(value)) {
                    doesValueExist = true;
                    break;
                }
            }
        }

        Assert.assertFalse(value + " was found on the table", doesValueExist);
    }

    public String clickOnTheFirstElementOfAColumn(String columnName) {
        //Get the first row of the table
        WebElement firstRow = getTableRows().get(0);
        //Get all the columns of the table
        List<WebElement> columns = firstRow.findElements(By.tagName("td"));
        //Click the first element of the column
        WebElement columnEl = columns.get(getColumnIndex(columnName));
        //Get the column text value to return
        String columnText = columnEl.getText();
        //Click the column
        columnEl.findElement(By.tagName("a")).click();
        waitUntilPageFinishLoading();

        return columnText;

    }

    private List<WebElement> getTableRows() {
        waitUntilPageFinishLoading();
        //Get the table body
        WebElement tableBody = this.findElement(By.tagName("tbody"));
        //Get the table rows

        return tableBody.findElements(By.tagName("tr"));
    }

    private int getColumnIndex(String columnName) {
        waitUntilPageFinishLoading();
        //Get the table head
        WebElement tableBody = this.findElement(By.tagName("thead"));
        //Get the table rows
        List<WebElement> tableRows = tableBody.findElements(By.tagName("tr"));
        //Start index to locate the column
        int index = 0;
        //Iterate each row to find the columns
        for (WebElement row : tableRows) {
            //Get all the columns
            List<WebElement> rowColumns = row.findElements(By.tagName("th"));
            //Iterate each column
            for (WebElement column : rowColumns) {
                //If the text for the column matches the value than click on it
                if (column.getText().equalsIgnoreCase(columnName)) {
                    return index;
                }
                index++;
            }
        }

        throw new NotFoundException("Unable to find column " + columnName);
    }
}
