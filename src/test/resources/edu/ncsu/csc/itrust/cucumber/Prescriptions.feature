Feature: Manage Prescriptions	
	As an HCP
	I want to record prescriptions for patients
	So that I have record of past prescriptions and they can be fulfilled
	As a patient
	I want to view my past prescriptions
	So that I can make sure they match my expectations
	As an admin
	I want to manage the list of available medications
	So that all records are up to date and HCPs can prescribe the latest medications
	
	
Scenario Outline: Add New Drug
    Given An Admin exists in iTrust2
    When I log in as admin
	When I choose to add a new drug
	And submit the values for NDC <ndc>, name <name>, and description <description>
	Then the drug <name> is successfully added to the system
	
Examples:
	| ndc          | name         | description       |
	| 1234-5678-90 | Test Product | Strong antiseptic |