package edu.ncsu.csc.iTrust2.web;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.common.DBUtils;
import edu.ncsu.csc.iTrust2.models.Inventory;
import edu.ncsu.csc.iTrust2.models.Recipe;
import edu.ncsu.csc.iTrust2.services.InventoryService;
import edu.ncsu.csc.iTrust2.services.RecipeService;

/**
 * Tests Make Coffee functionality.
 *
 * @author Elizabeth Gilbert (evgilber@ncsu.edu)
 * @author Kai Presler-Marshall (kpresle@ncsu.edu)
 */

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class MakeCoffeeTest extends SeleniumTest {

    /** The URL for CoffeeMaker - change as needed */
    private String             baseUrl;
    private final StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private InventoryService   inventoryService;

    @Autowired
    private RecipeService      recipeService;

    @Autowired
    private DataSource         dataSource;

    @Override
    @Before
    public void setUp () throws Exception {
        super.setUp();

        DBUtils.resetDB( dataSource );

        /* Create lots of inventory to use */
        final Inventory ivt = inventoryService.getInventory();

        ivt.setChocolate( 500 );
        ivt.setCoffee( 500 );
        ivt.setMilk( 500 );
        ivt.setSugar( 500 );

        inventoryService.save( ivt );

        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 20, TimeUnit.SECONDS );

    }

    /**
     * Helper to create a recipe to make
     *
     * @return the name of the recipe
     * @throws Exception
     *             if there was an issue in submitting the recipe
     */
    private void createRecipe ( final String name, final int price, final int amtCoffee, final int amtMilk,
            final int amtSugar, final int amtChocolate ) throws Exception {

        final Recipe e = recipeService.findByName( name );
        if ( null != e ) {
            recipeService.delete( e );
        }

        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.setCoffee( amtCoffee );
        recipe.setMilk( amtMilk );
        recipe.setSugar( amtSugar );
        recipe.setChocolate( amtChocolate );
        recipeService.save( recipe );

    }

    /**
     * Looks through the list of available recipes and selects the specified
     * recipe
     *
     * @param name
     * @return true if found and selected, false if not
     * @throws InterruptedException
     */
    private boolean selectRecipe ( final String name ) throws InterruptedException {
        final List<WebElement> list = driver.findElements( By.name( "name" ) );
        Thread.sleep( 3000 );

        // Select the recipe
        for ( final WebElement we : list ) {
            if ( name.equals( we.getAttribute( "value" ) ) ) {
                we.click();
                return true;
            }
        }

        return false;
    }

    /**
     * Create valid coffee
     *
     * @throws Exception
     *
     */
    private void makeCoffee ( final String recipeName, final int price, final int amtCoffee, final int amtMilk,
            final int amtSugar, final int amtChocolate, final int paid, final String expectedMessage )
            throws Exception {
        createRecipe( recipeName, price, amtCoffee, amtMilk, amtSugar, amtChocolate );

        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Make Coffee" ) ).click();

        selectRecipe( recipeName );

        try {
            driver.findElement( By.name( "amtPaid" ) ).clear();
            driver.findElement( By.name( "amtPaid" ) ).sendKeys( paid + "" );
        }
        catch ( final Exception e ) {
            System.out.println( driver.getCurrentUrl() );
            System.out.println( driver.getPageSource() );
            Assert.fail();
        }

        // Submit
        System.out.println( recipeName + " " + price + " " + amtCoffee + " " + amtMilk + " " + " " + amtSugar + " "
                + amtChocolate + " " + paid + " " + expectedMessage );
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
        Thread.sleep( 1000 );

        // Make sure the proper message was displayed.
        assertTextPresent( expectedMessage, driver );
    }

    /**
     * Test for making coffee (valid) Expect to get an appropriate success
     * message.
     *
     * @throws Exception
     */
    @Test
    public void testValidMakeCoffee () throws Exception {
        makeCoffee( "Coffee", 60, 0, 3, 7, 2, 60, "Coffee was made" );
        makeCoffee( "Coffee", 60, 5, 0, 7, 2, 60, "Coffee was made" );
        makeCoffee( "Coffee", 60, 5, 3, 0, 2, 60, "Coffee was made" );
        makeCoffee( "Coffee", 60, 5, 3, 0, 2, 60, "Coffee was made" );
        makeCoffee( "Coffee", 60, 5, 3, 7, 0, 60, "Coffee was made" );
        makeCoffee( "Coffee", 60, 5, 3, 7, 2, 100, "Coffee was made" );
        makeCoffee( "Coffee", 60, 5, 3, 7, 2, 61, "Coffee was made" );
    }

    /**
     * Test for making coffee (invalid) Expect to get an appropriate failure
     * message
     *
     * @throws Exception
     */
    @Test
    public void testInvalidMakeCoffee () throws Exception {
        makeCoffee( "Coffee", 60, 0, 3, 7, 2, 59, "Error while making recipe" );
        makeCoffee( "Coffee", 60, 5, 0, 7, 2, -1, "Error while making recipe" );
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
