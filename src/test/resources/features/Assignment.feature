Feature: Salesforce Testing
	As a salesforce user
  I want to change values of components

Scenario Outline: Change value of a row for data table with inline edit example of datatable component 
	Given user has launched salesforce application url "https://developer.salesforce.com/docs/component-library/documentation/en/lwc" in "<browser>" browser  
	When user quick finds and selects "datatable" under "Component Reference" tab 
	Then "datatable" component is loaded in right hand side pane 
	When user selects "Data Table with Inline Edit" example 
	And updates values of data rows 
	|dataRowNumber|newValues|
	|3|Larry Page;https://google.com;(555)-755-6575;Today%12:57 PM;$1234.56|
	|4|Bill Gates;https://microsoft.com;(555)-755-6576;Sep 1, 2021%23:45 PM;$789.01|
	Then values are updated for data rows 
	|dataRowNumber|newValues|
	|3|Larry Page;https://google.com;(555)-755-6575;Today;$1,234.56|
	|4|Bill Gates;https://microsoft.com;(555)-755-6576;Sep 1, 2021;$789.01|
	
	Examples:
	|browser|
	|firefox|
#	|chrome|
#	|edge|
#	|opera|
#	|safari|
#	|ie|
	