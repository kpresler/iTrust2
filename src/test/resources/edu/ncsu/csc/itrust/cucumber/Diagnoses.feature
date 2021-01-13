#Author tvbarnet
Feature: Diagnoses in iTrust2
	As an iTrust2 HCP
	I want to add a diagnosis to an office visit
	So that a record exists of when a Patient is diagnosed with illnesses
	As a Patient
	I want to view my current and previous diagnoses
	So that I know my history
	As an Admin
	I want to add new diagnoses to the system
	So that they can be tracked
	
	
#admin add diagnoses	
Scenario Outline: Add a diagnosis to the system, then delete it
    Given An Admin exists in iTrust2
    When I log in as admin
    When I navigate to the list of diagnoses
    When I enter the info for a diagnosis with code: <code>, and description: <description>
    Then The diagnosis is added sucessfully
    And The diagnosis info is correct
    When I delete the new code
    Then The code is deleted
Examples:
	| code   | description |
	| K35    | Acute Appendicitis|


#admin add invalid diagnoses	
Scenario Outline: Add an invalid diagnosis to the system
    Given An Admin exists in iTrust2
    When I log in as admin
    When I navigate to the list of diagnoses
    When I enter the info for a diagnosis with code: <code>, and description: <description>
    Then The diagnosis is not added
Examples:
	| first    | last     | code   | description |
	|Al        | Minister |K3      |Acute Appendicitis|
	|Al        | Minister |35A      |Acute Appendicitis|
	|Al        | Minister |K       |Acute Appendicitis|
	|Al        | Minister |K35.55555|Acute Appendicitis|
	|Al        | Minister |K35      |12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901|