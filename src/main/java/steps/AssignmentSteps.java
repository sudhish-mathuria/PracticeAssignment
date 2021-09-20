/*
 * @AssignmentSteps.java@
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
package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AssignmentPage;
import utilities.BrowserManager;

/**
 * @author sudhish mathuria
 *
 */
public class AssignmentSteps {
  AssignmentPage assignmentPage = new AssignmentPage();
  @Given("user has launched salesforce application url {string} in {string} browser")
  public void user_has_launched_salesforce_application_url_in_browser (
      String url, String browser) {
    BrowserManager.setBrowser(browser);
    BrowserManager.maximizeAndNavigate(url);
  }

  @When("user quick finds and selects {string} under {string} tab")
  public void user_quick_find_s_and_selects_under_tab (
      String component, String tab) {
    assignmentPage.selectTab(tab);
    assignmentPage.quickFinds(component);
    assignmentPage.selectComponent(component);
  }

  @Then("{string} component is loaded in right hand side pane")
  public void component_is_loaded_in_right_hand_side_pane (String component) {
    assignmentPage.isComponentLoaded(component);
  }

  @When("user selects {string} example")
  public void user_selects_example (
      String example) {
    assignmentPage.selectExample(example);
  }

  @When("updates values of data rows")
  public void updates_values_of_data_rows (DataTable dataTable) throws Exception {
    assignmentPage.updateDataRowValues(dataTable);
  }

  @Then("values are updated for data rows")
  public void values_are_updated_for_data_rows (DataTable dataTable) throws Exception {
    assignmentPage.validateUpdatedDataRowValues(dataTable);
  }
}
