package edu.ncsu.csc.iTrust2.web;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.common.DBUtils;

/**
 * Tests the update inventory functionality.
 *
 * @author Kai Presler-Marshall (kpresle@ncsu.edu)
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UpdateInventoryTest extends SeleniumTest {

    /** The URL for CoffeeMaker - change as needed */
    private String             baseUrl;
    private final StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private DataSource         dataSource;

    @Override
    @Before
    public void setUp () throws Exception {
        super.setUp();

        DBUtils.resetDB( dataSource );

        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

    }

    /**
     * Test for a adding inventory. Expect to get an appropriate success
     * message.
     *
     * @throws Exception
     */
    @Test
    public void testAddInventory1 () throws Exception {
        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Update Inventory" ) ).click();

        // Enter the amount of each ingredient
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( "5" );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( "3" );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( "7" );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( "2" );

        // Submit the inventory.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        // Make sure the proper message was displayed.
        assertTextPresent( "Inventory Successfully Updated", driver );

        driver.findElement( By.linkText( "Home" ) ).click();
    }

    @Override
    @After
    public void tearDown () {
        final String verificationErrorString = verificationErrors.toString();
        if ( !"".equals( verificationErrorString ) ) {
            fail( verificationErrorString );
        }
    }
}
