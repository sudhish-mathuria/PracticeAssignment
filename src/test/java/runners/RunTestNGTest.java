/*
 * @RunTestNGTest.java@
 * Created on Sep 19, 2021
 *
 * Copyright (c) 2021 E2open, Inc.
 * All Rights Reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF E2open
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 */
/**
 *
 */
package runners;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import utilities.BrowserManager;

/**
 * @author sudhish mathuria
 *
 */
@CucumberOptions(features = "src/test/resources/features", glue = "steps")
public class RunTestNGTest extends AbstractTestNGCucumberTests {
  @AfterClass(alwaysRun = true)
  @AfterSuite(alwaysRun = true)
  public void closeAllBrowsers () {
    System.out.println("Inside closeAllBrowsers() method in RunTestNGTest runner class.");
    BrowserManager.closeAllBrowsers();
  }
}
