package edu.ncsu.csc.iTrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DiagnosesStepDefs extends CucumberTest {

    private static boolean initialized = false;

    private final String   baseUrl     = "http://localhost:8080/iTrust2";

    private void setTextField ( final By byVal, final Object value ) {
        final WebElement elem = driver.findElement( byVal );
        elem.clear();
        elem.sendKeys( value.toString() );
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

    @Then ( "The (.+), (.+), (.+), and (.+) are correct" )
    public void checkList ( final String date, final String hcp, final String description, final String note ) {
        final long time = System.currentTimeMillis();
        waitForAngular();
        while ( System.currentTimeMillis() - time < 5000 ) {
            for ( final WebElement diag : driver.findElements( By.name( "diagnosis" ) ) ) {
                final String text = diag.getText();
                if ( text.contains( date ) && text.contains( hcp ) && text.contains( description )
                        && text.contains( note ) ) {
                    // we found the right diganosis
                    return;
                }
            }
        }
        // fail( "failed to find specified diagnosis" );
    }

    @When ( "I navigate to the list of diagnoses" )
    public void adminNavigate () {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('manageICDCodes').click();" );

    }

    List<String> before;
    List<String> after;
    String       expectedCode;
    String       expectedDescription;

    @When ( "^I enter the info for a diagnosis with code: (.+), and description: (.+)$" )
    public void enterDiagnosisInfo ( final String code, final String description ) {

        expectedCode = code;
        expectedDescription = description;

        waitForAngular();

        before = driver.findElements( By.name( "codeRow" ) ).stream().map( x -> x.getAttribute( "codeid" ) )
                .collect( Collectors.toList() );

        try {
            setTextField( By.name( "code" ), code );
            setTextField( By.name( "description" ), description );
            driver.findElement( By.name( "submit" ) ).click();
            waitForAngular();
            try {
                new WebDriverWait( driver, 10 ).until( ExpectedConditions.or(
                        ExpectedConditions.numberOfElementsToBeMoreThan( By.name( "codeRow" ), before.size() ),
                        ExpectedConditions.textToBePresentInElementLocated( By.id( "errP" ),
                                "Code doesn't meet specifications" ) ) );
            }
            catch ( final Exception e ) {
                // ignore this ~ problems caught in next step
            }
        }
        catch ( final Exception e ) {
            fail( e.getMessage() );
        }
    }

    @Then ( "The diagnosis is added sucessfully" )
    public void checkDiagnosisAdd () {
        waitForAngular();
        after = driver.findElements( By.name( "codeRow" ) ).stream().map( x -> x.getAttribute( "codeid" ) )
                .collect( Collectors.toList() );
        after.removeAll( before );

        waitForAngular();

        assertEquals( 1, after.size() );
    }

    @Then ( "The diagnosis info is correct" )
    public void verifyAddedDiagnosis () {
        waitForAngular();
        // the one left in after is what we expect.
        final WebElement newRow = driver.findElements( By.name( "codeRow" ) ).stream()
                .filter( x -> x.getAttribute( "codeid" ).equals( after.get( 0 ) ) ).findFirst().get();
        assertEquals( expectedCode, newRow.findElement( By.name( "codeCell" ) ).getText() );
        assertEquals( expectedDescription, newRow.findElement( By.name( "descriptionCell" ) ).getText() );
    }

    @Then ( "The diagnosis is not added" )
    public void checkInvalidAdd () {
        waitForAngular();
        try {
            final WebElement err = driver.findElement( By.id( "errP" ) );
            assertTrue( err.getText().contains( "Code doesn't meet specifications" )
                    || err.getText().contains( "Description exceeds character limit of 250" ) );
            after = driver.findElements( By.name( "codeRow" ) ).stream().map( x -> x.getAttribute( "codeid" ) )
                    .collect( Collectors.toList() );
            after.removeAll( before );
            assertEquals( 0, after.size() );
        }
        catch ( final Exception e ) {
            fail( e.getMessage() );
        }
    }

    @When ( "I delete the new code" )
    public void deleteCode () {
        waitForAngular();
        // the one left in after is what we expect.
        final WebElement newRow = driver.findElements( By.name( "codeRow" ) ).stream()
                .filter( x -> x.getAttribute( "codeid" ).equals( after.get( 0 ) ) ).findFirst().get();
        newRow.findElement( By.tagName( "input" ) ).click();
        waitForAngular();
    }

    @When ( "I add a diagnosis without a code" )
    public void addDiagnosisNoCode () {
        waitForAngular();
        driver.findElement( By.name( "GENERAL_CHECKUP" ) ).click();
        setTextField( By.name( "notesEntry" ), "Fun note" );
        driver.findElement( By.name( "fillDiagnosis" ) ).click();
    }

    @Then ( "The code is deleted" )
    public void checkDelete () {
        waitForAngular();

        final List<String> current = driver.findElements( By.name( "codeRow" ) ).stream()
                .map( x -> x.getAttribute( "codeid" ) ).collect( Collectors.toList() );
        assertFalse( current.contains( after.get( 0 ) ) );
    }

    @Then ( "A message is shown that indicates that the code was invalid" )
    public void checkFailMessage () {
        waitForAngular();
        assertTrue( driver.getPageSource().contains( "Diagnosis must be associated with a diagnosis code" ) );
    }

}
