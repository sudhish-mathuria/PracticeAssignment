/*
 * @AssignmentPage.java@ Created on Sep 19, 2021
 *
 * Copyright (c) 2021 E2open, Inc. All Rights Reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF E2open The copyright notice above does not
 * evidence any actual or intended publication of such source code.
 *
 */
/**
 *
 */
package pages;

import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

import io.cucumber.datatable.DataTable;
import utilities.BrowserManager;

/**
 * @author sudhish mathuria
 *
 */
public class AssignmentPage {
  public void selectTab (String tab) {
    By tabLinkBy = By.linkText(tab);
    if (BrowserManager.waitForElementReady(tabLinkBy)) {
      BrowserManager.moveToElementAndClick(tabLinkBy);
    } else {
      String message = "Tab: " + tab + " is not supported.";
      BrowserManager.closeAllBrowsers();
      System.out.println(message);
      throw new NoSuchElementException(message);
    }
  }

  public void quickFinds (String component) {
    By quickFindTextBoxBy = By.name("Quick Find");
    if (BrowserManager.waitForElementReady(quickFindTextBoxBy)) {
      BrowserManager.clearElementAndEnterText(quickFindTextBoxBy, component, Keys.ENTER);
    } else {
      String message = "Quick find text box is not displayed on gui.";
      BrowserManager.closeAllBrowsers();
      System.out.println(message);
      throw new NoSuchElementException(message);
    }
  }

  private String componentText = "Component: ";

  public void selectComponent (String component) {
    By componentLinkBy = By.linkText(component);
    if (BrowserManager.waitForElementReady(componentLinkBy)) {
      BrowserManager.moveToElementAndJavaScriptClick(componentLinkBy);
    } else {
      String message = componentText + component + " is not supported.";
      BrowserManager.closeAllBrowsers();
      System.out.println(message);
      throw new NoSuchElementException(message);
    }
  }

  public void isComponentLoaded (String component) {
    if (BrowserManager.waitForElementReady(By.cssSelector("h2[title='" + component + "']"))) {
      System.out.println(componentText + component + " is loaded.");
    } else {
      String message = componentText + component + " is not loaded.";
      BrowserManager.closeAllBrowsers();
      System.out.println(message);
      throw new NoSuchElementException(message);
    }
  }

  public void selectExample (String example) {
    By exampleListBoxBy = By.name("example");
    By exampleOptionBy = By.cssSelector("span[title='" + example + "']");
    BrowserManager.selectOptionFromListBox(exampleListBoxBy, exampleOptionBy, example);
    By runButtonBy = By.xpath("//button[text()='Run']");
    if (BrowserManager.waitForElementReady(runButtonBy)) {
      BrowserManager.moveToElementAndClick(runButtonBy);
      System.out.println("Click performed on run button.");
    } else {
      String message = "Run button is not displayed on gui.";
      BrowserManager.closeAllBrowsers();
      System.out.println(message);
      throw new NoSuchElementException(message);
    }
  }

  private String dataRowLocatorString = "table[role='grid'] > tbody > tr:nth-child($|$)";
  private By rowNumberLabelBy = null;

  public void updateDataRowValues (DataTable dataTable) throws Exception {
    switchToFrame("preview");
    for (Map<Object, Object> data : dataTable.asMaps(String.class, String.class)) {
      dataRowLocatorString =
          dataRowLocatorString.replace("$|$", data.get("dataRowNumber").toString());
      rowNumberLabelBy = By.cssSelector(dataRowLocatorString + " > td:nth-child(1)");
      BrowserManager.waitForElementReady(rowNumberLabelBy, 60);
      String[] newValues = data.get("newValues").toString().split(";");
      updateDataCellValue(dataRowLocatorString, "Label", newValues[0].trim());
      updateDataCellValue(dataRowLocatorString, "Website", newValues[1].trim());
      updateDataCellValue(dataRowLocatorString, "Phone", newValues[2].trim());
      updateCloseAt(dataRowLocatorString, newValues[3].trim());
      updateDataCellValue(dataRowLocatorString, "Balance", newValues[4].trim());
      dataRowLocatorString =
          dataRowLocatorString.replace(data.get("dataRowNumber").toString(), "$|$");
    }
    BrowserManager.switchToDefaultContent();
  }

  private void switchToFrame (String iFrameName) throws Exception {
    By childFrameBy = By.name(iFrameName);
    BrowserManager.waitForElementReady(childFrameBy, 60000);
    if (BrowserManager.getElementsCount(By.cssSelector("iframe[name='preview']")) == 1) {
      BrowserManager.switchToFrame(iFrameName);
      By grandchildFrameBy = By.name(iFrameName);
      BrowserManager.waitForElementReady(grandchildFrameBy, 60000);
      BrowserManager.switchToFrame(iFrameName);
    } else {
      System.out.println(
          "Total number of frames: "
              + BrowserManager.getElementsCount(By.cssSelector("iframe[name='preview']")));
      BrowserManager.closeAllBrowsers();
      throw new Exception("There are more than 1 frame in dom object");
    }
  }

  private void updateCloseAt (String dataRowLocatorString, String closeAt) {
    By closeAtCellButtonBy =
        By.cssSelector(dataRowLocatorString + " > [data-label='CloseAt'] button");
    BrowserManager.waitForElementReady(closeAtCellButtonBy);
    BrowserManager.getElement(closeAtCellButtonBy).click();

    String[] dateTime = closeAt.split("%");
    if (!dateTime[0].isEmpty() && "today".equalsIgnoreCase(dateTime[0])) {
      dateTime[0] = BrowserManager.getSystemDate("MMM dd, yyyy", Locale.US);
      System.out.println("Date: " + dateTime[0]);
    }

    By dateCellTextBoxBy = By.xpath("//label[text()='Date']/..//input");
    BrowserManager.clearElementAndEnterText(dateCellTextBoxBy, dateTime[0].trim(), null);

    BrowserManager.sleep(2500);
    By timeCellTextBoxBy = By.xpath("//label[text()='Time']/..//input");
    BrowserManager.clearElementAndEnterText(timeCellTextBoxBy, dateTime[1].trim(), null);

    BrowserManager.sleep(2500);
    BrowserManager.getElement(rowNumberLabelBy).click();
  }

  private void updateDataCellValue (
      String dataRowLocatorString, String dataCellKey, String dataCellValue) {
    By dataCellButtonBy =
        By.cssSelector(dataRowLocatorString + " > [data-label='" + dataCellKey + "'] button");
    BrowserManager.moveToElementAndClick(dataCellButtonBy);

    By dataCellTextBoxBy = By.xpath("//label[text()='" + dataCellKey + "']/..//input");
    BrowserManager.clearElementAndEnterText(dataCellTextBoxBy, dataCellValue, null);

    BrowserManager.sleep(2500);
    BrowserManager.getElement(rowNumberLabelBy).click();
  }

  public void validateUpdatedDataRowValues (DataTable dataTable) throws Exception {
    switchToFrame("preview");
    for (Map<Object, Object> data : dataTable.asMaps(String.class, String.class)) {
      dataRowLocatorString =
          dataRowLocatorString.replace("$|$", data.get("dataRowNumber").toString());
      rowNumberLabelBy = By.cssSelector(dataRowLocatorString + " > td:nth-child(1)");
      BrowserManager.waitForElementReady(rowNumberLabelBy, 60);
      String[] newValues = data.get("newValues").toString().split(";");
      validateDataCellValue(dataRowLocatorString, "Label", newValues[0].trim());
      validateDataCellValue(dataRowLocatorString, "Website", newValues[1].trim());
      validateDataCellValue(dataRowLocatorString, "Phone", newValues[2].trim());
      if (!newValues[3].isEmpty() && "today".equalsIgnoreCase(newValues[3])) {
        newValues[3] = BrowserManager.getSystemDate("MMM dd, yyyy", Locale.US);
        System.out.println("Date: " + newValues[3]);
      }
      validateDataCellValue(dataRowLocatorString, "CloseAt", newValues[3].trim());
      validateDataCellValue(dataRowLocatorString, "Balance", newValues[4].trim());
      dataRowLocatorString =
          dataRowLocatorString.replace(data.get("dataRowNumber").toString(), "$|$");
    }
    BrowserManager.switchToDefaultContent();
  }

  private boolean validateDataCellValue (
      String dataRowLocatorString, String dataCellKey, String dataCellValue) throws Exception {
    By dataCellBy = By.cssSelector(
        dataRowLocatorString + " > [data-label='" + dataCellKey
        + "'] > lightning-primitive-cell-factory > span > div");
    BrowserManager.waitForElementReady(dataCellBy);
    BrowserManager.sleep(1000);
    String actualValue = BrowserManager.getElement(dataCellBy).getText().trim();
    boolean flag = dataCellValue.equals(actualValue);
    if (!flag) {
      System.out.println(
          "Expected value: " + dataCellValue + "; Actual value: " + actualValue + ". "
              + dataCellValue + " value is not updated for data cell of column: " + dataCellKey
              + ".");
      throw new Exception(
          "Expected value: " + dataCellValue + "; Actual value: " + actualValue + ". "
              + dataCellValue + " value is not updated for data cell of column: " + dataCellKey
              + ".");
    }

    return flag;
  }
}
