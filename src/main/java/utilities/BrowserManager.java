/*
 * @BrowserManager.java@ Created on Sep 19, 2021
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
package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author sudhish mathuria
 *
 */
public class BrowserManager {
  private static WebDriver driver = null;

  public static void setBrowser (String browser) {
    switch (browser.toLowerCase()) {
      case "chrome":
      case "google chrome":
      case "googlechrome":
        System.setProperty(
            "webdriver.chrome.driver",
            "./src/main/resources/drivers/win32/chromedriver.exe");
        driver = new ChromeDriver();
        break;
      case "firefox":
      case "ff":
      case "mozilla firefox":
      case "mozillafirefox":
        System
        .setProperty(
            "webdriver.gecko.driver",
            "./src/main/resources/drivers/win64/geckodriver.exe");
        driver = new FirefoxDriver();
        break;
      case "ie":
      case "internet explorer":
      case "internetexplorer":
        System
        .setProperty(
            "webdriver.ie.driver",
            "./src/main/resources/drivers/win64/IEDriverServer.exe");
        driver = new InternetExplorerDriver();
        break;
      case "edge":
      case "ms edge":
      case "msedge":
      case "microsoft edge":
      case "microsoftedge":
        System
        .setProperty(
            "webdriver.edge.driver",
            "./src/main/resources/drivers/win64/msedgedriver.exe");
        driver = new EdgeDriver();
        break;
      case "opera":
        System.setProperty(
            "webdriver.opera.driver",
            "./src/main/resources/drivers/win64/operadriver.exe");
        driver = new EdgeDriver();
        break;
      default:
        System.out.println("Browser: " + browser + " is not supported.");
        break;
    }
  }

  public static void maximizeAndNavigate (String url) {
    driver.manage().window().maximize();
    driver.get(url);
  }

  private static int defaultWaitInSeconds = 30;
  public static boolean waitForElementReady (By by) {
    return waitForElementReady(by, defaultWaitInSeconds);
  }

  private static int pollTimeInMilliseconds = 500;
  private static int counterMultiplier = (1000 / pollTimeInMilliseconds);
  private static String errorMessage = "Error message: ";
  public static boolean waitForElementReady (By by, int waitInSeconds) {
    boolean flag = false;
    StringBuilder message = new StringBuilder();
    waitInSeconds *= counterMultiplier;
    for (int counter = 0; counter < waitInSeconds; counter++) {
      try {
        WebElement element = driver.findElement(by);
        flag = element != null && element.isDisplayed() && element.isEnabled();
        if (flag)
          break;
      } catch (Exception e) {
        message.append(Arrays.toString(e.getStackTrace()));
        message.append("\n");
        try {
          Thread.sleep(pollTimeInMilliseconds);
        } catch (InterruptedException e1) {
          message.append(Arrays.toString(e1.getStackTrace()));
          message.append("\n");
          Thread.currentThread().interrupt();
        }
      }
    }
    if ((!flag) && (!(message.toString().isEmpty()))) {
      System.out.println(errorMessage + message.toString());
    }
    return flag;
  }

  public static WebElement getElement (By by) {
    return driver.findElement(by);
  }

  private static String optionText = "Option: ";
  public static boolean selectOptionFromListBox (By listBoxBy, By optionBy, String option) {
    boolean flag = false;
    if (waitForElementReady(listBoxBy)) {
      if (option.equals(getElement(listBoxBy).getText().trim())) {
        System.out.println(optionText + option + " is already selected in list box.");
      } else {
        moveToElement(listBoxBy);
        waitForElementReady(listBoxBy);
        getElement(listBoxBy).click();
        waitForElementReady(optionBy);
        getElement(optionBy).click();
        waitForElementReady(listBoxBy);
        System.out.println(optionText + option + " is selected successfully in list box.");
      }
    } else {
      String message = "List box is not displayed on gui.";
      BrowserManager.closeAllBrowsers();
      System.out.println(message);
      throw new NoSuchElementException(message);
    }
    return flag;
  }

  public static WebElement getClickableElement (By by) {
    return getClickableElement(by, defaultWaitInSeconds);
  }

  public static WebElement getClickableElement (By by, int waitInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, waitInSeconds);
    return wait.until(ExpectedConditions.elementToBeClickable(by));
  }

  public static void javaScriptClick (By by) {
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].click();", getElement(by));
  }

  public static void switchToFrame (String iFrameName) {
    System.out.println("Switched to " + iFrameName + " frame.");
    driver.switchTo().frame(iFrameName);
  }

  public static void switchToDefaultContent () {
    System.out.println("Switched to default content.");
    driver.switchTo().defaultContent();
  }

  public static void sleep (int sleepForSeconds) {
    try {
      Thread.sleep(sleepForSeconds);
    } catch (InterruptedException e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      Thread.currentThread().interrupt();
    }
  }

  public static void moveToElement (By by) {
    Actions builder = new Actions(driver);
    builder.moveToElement(getElement(by));
    builder.build().perform();
  }

  public static String getSystemDate (String dateFormatText, Locale locale) {
    DateFormat dateFormat = new SimpleDateFormat(dateFormatText, locale);
    Date date = new Date();
    return dateFormat.format(date);
  }

  public static int getElementsCount (By by) {
    return driver.findElements(by).size();
  }

  public static void moveToElementAndClick (By by) {
    moveToElement(by);
    waitForElementReady(by);
    getElement(by).click();
  }

  public static void moveToElementAndJavaScriptClick (By by) {
    moveToElement(by);
    waitForElementReady(by);
    javaScriptClick(by);
  }

  public static void clearElementAndEnterText (By by, String text, CharSequence keys) {
    waitForElementReady(by);
    getElement(by).clear();
    if (keys != null) {
      getElement(by).sendKeys(text, keys);
    } else {
      getElement(by).sendKeys(text);
    }
  }

  public static void closeAllBrowsers () {
    if (driver != null) {
      System.out.println("Closing all browser instances.");
      driver.quit();
    } else {
      System.out.println("All browser instances are already closed.");
    }
  }
}
