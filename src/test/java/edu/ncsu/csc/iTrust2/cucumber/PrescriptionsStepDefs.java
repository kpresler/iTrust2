package edu.ncsu.csc.iTrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PrescriptionsStepDefs extends CucumberTest {

    private static final String BASE_URL  = "http://localhost:8080/iTrust2/";
    private static final String VISIT_URL = BASE_URL + "hcp/documentOfficeVisit";
    private static final String VIEW_URL  = BASE_URL + "patient/officeVisit/viewPrescriptions";
    private static final String DRUG_URL  = BASE_URL + "admin/drugs";

    private final String        baseUrl   = "http://localhost:8080/iTrust2";

    private String getUserName ( final String first, final String last ) {
        return first.substring( 0, 1 ).toLowerCase() + last.toLowerCase();
    }

    private void enterValue ( final String name, final String value ) {
        final WebElement field = driver.findElement( By.name( name ) );
        field.clear();
        field.sendKeys( String.valueOf( value ) );
    }

    /**
     * Fills in the date and time fields with the specified date and time.
     *
     * @param date
     *            The date to enter.
     * @param time
     *            The time to enter.
     */
    private void fillInDateTime ( final String dateField, final String date, final String timeField,
            final String time ) {
        fillInDate( dateField, date );
        fillInTime( timeField, time );
    }

    /**
     * Fills in the date field with the specified date.
     *
     * @param date
     *            The date to enter.
     */
    private void fillInDate ( final String dateField, final String date ) {
        driver.findElement( By.name( dateField ) ).clear();
        final WebElement dateElement = driver.findElement( By.name( dateField ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    /**
     * Fills in the time field with the specified time.
     *
     * @param time
     *            The time to enter.
     */
    private void fillInTime ( final String timeField, String time ) {
        // Zero-pad the time for entry
        if ( time.length() == 7 ) {
            time = "0" + time;
        }

        driver.findElement( By.name( timeField ) ).clear();
        final WebElement timeElement = driver.findElement( By.name( timeField ) );
        timeElement.sendKeys( time.replace( ":", "" ).replace( " ", "" ) );
    }

    private void selectItem ( final String name, final String value ) {
        final By selector = By.cssSelector( "input[name='" + name + "'][value='" + value + "']" );
        waitForAngular();
        final WebElement element = driver.findElement( selector );
        element.click();
    }

    private void selectName ( final String name ) {
        final WebElement element = driver.findElement( By.cssSelector( "input[name='" + name + "']" ) );
        element.click();
    }

    @When ( "I choose to add a new drug" )
    public void addDrug () {
        driver.get( DRUG_URL );
    }

    @When ( "^submit the values for NDC (.+), name (.+), and description (.*)$" )
    public void submitDrug ( final String ndc, final String name, final String description )
            throws InterruptedException {

        waitForAngular();
        assertEquals( "Admin Manage Drugs", driver.findElement( By.tagName( "h3" ) ).getText() );

        waitForAngular();
        enterValue( "drug", name );
        enterValue( "code", ndc );
        enterValue( "description", description );
        driver.findElement( By.name( "submit" ) ).click();
    }

    @Then ( "^the drug (.+) is successfully added to the system$" )
    public void drugSuccessful ( final String drug ) throws InterruptedException {
        waitForAngular();
        assertEquals( "", driver.findElement( By.id( "errP" ) ).getText() );

        for ( final WebElement r : driver.findElements( By.name( "drugTableRow" ) ) ) {
            if ( r.getText().contains( drug ) ) {
                r.findElement( By.name( "deleteDrug" ) ).click();
            }
        }
        waitForAngular();

        try {
            assertFalse( driver.findElement( By.tagName( "body" ) ).getText().contains( drug ) );

        }
        catch ( final Exception e ) {
            fail();
        }
    }

}
