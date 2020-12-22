package edu.ncsu.csc.iTrust2.cucumber;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import edu.ncsu.csc.iTrust2.cucumber.utils.SharedInventoryData;
import edu.ncsu.csc.iTrust2.models.Inventory;
import edu.ncsu.csc.iTrust2.models.Recipe;
import edu.ncsu.csc.iTrust2.services.InventoryService;
import edu.ncsu.csc.iTrust2.services.RecipeService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * StepDefs (Cucumber) test class for interacting with the Inventory model. This
 * performs a number of tests to ensure that the inventory is changed in the
 * expected manner
 *
 * @author Kai Presler-Marshall
 * @author Sarah Elder
 *
 */

public class InventoryStepDefs {
    private final SharedInventoryData inventoryData;

    @Autowired
    protected InventoryService        inventoryService;

    @Autowired
    protected RecipeService           recipeService;

    /**
     * Constructor
     *
     * @param sid
     *            SharedInventoryData; basically a backup copy of the inventory
     *            to make sure that changes made to the "real" one are what is
     *            expected
     */
    public InventoryStepDefs () {
        this.inventoryData = new SharedInventoryData();

    }

    /**
     * The CoffeeMaker has no (direct) way to remove ingredients from the
     * Inventory it stores, this allows us to effectively do so by creating a
     * recipe with the amount of ingredients to remove, and then making coffee
     * with the Recipe just created
     *
     * @param removeCoffee
     *            Amount of Coffee to remove from the Inventory
     * @param removeMilk
     *            Amount of Milk to remove from the Inventory
     * @param removeSugar
     *            Amount of Sugar to remove from the Inventory
     * @param removeChocolate
     *            Amount of Chocolate to remove from the Inventory
     */
    public void removeInventoryHelper ( final int removeCoffee, final int removeMilk, final int removeSugar,
            final int removeChocolate ) {

        final Inventory currentInventory = inventoryService.getInventory();

        final Recipe r = new Recipe();
        r.setCoffee( removeCoffee );
        r.setMilk( removeMilk );
        r.setSugar( removeSugar );
        r.setChocolate( removeChocolate );

        currentInventory.useIngredients( r );
    }

    /**
     * This Cucumber "Given" step ensures that the Recipe has the amount of
     * ingredients specified. This is used to ensure that preconditions for the
     * tests are satisfied
     *
     * @param originalCoffee
     *            Amount of Coffee that the inventory will be set to have
     * @param originalMilk
     *            Amount of Milk that the inventory will be set to have
     * @param originalSugar
     *            Amount of Sugar that the inventory will be set to have
     * @param originalChocolate
     *            Amount of Chocolate that the inventory will be set to have
     */
    @Given ( "^there is (-?\\d+) coffee, (-?\\d+) milk, (-?\\d+) sugar, and (-?\\d+) chocolate in the CoffeeMaker$" )
    public void initialInventory ( final int originalCoffee, final int originalMilk, final int originalSugar,
            final int originalChocolate ) {
        inventoryService.deleteAll();
        recipeService.deleteAll();

        final Inventory i = inventoryService.getInventory();
        i.addIngredients( originalCoffee, originalMilk, originalSugar, originalChocolate );
        inventoryService.save( i );

        inventoryData.originalCoffee = originalCoffee;
        inventoryData.originalMilk = originalMilk;
        inventoryData.originalSugar = originalSugar;
        inventoryData.originalChocolate = originalChocolate;
    }

    /**
     * Add the specified amounts of ingredients to the CoffeeMaker's inventory.
     * This is required to pass.
     *
     * @param amtCoffee
     *            Amount of Coffee to add to the Inventory.
     * @param amtMilk
     *            Amount of Milk to add to the Inventory.
     * @param amtSugar
     *            Amount of Sugar to add to the Inventory.
     * @param amtChocolate
     *            Amount of Chocolate to add to the Inventory.
     */
    @When ( "^I add (-?\\d+) coffee, (-?\\d+) milk, (-?\\d+) sugar, and (-?\\d+) chocolate$" )
    public void addInventory ( final int amtCoffee, final int amtMilk, final int amtSugar, final int amtChocolate ) {
        inventoryData.newCoffee = amtCoffee;
        inventoryData.newMilk = amtMilk;
        inventoryData.newSugar = amtSugar;
        inventoryData.newChocolate = amtChocolate;
        try {
            final Inventory inventory = inventoryService.getInventory();
            inventory.addIngredients( amtCoffee, amtMilk, amtSugar, amtChocolate );
            inventoryService.save( inventory );
        }
        catch ( final Exception e ) {
            Assert.fail( "Inventory not added. InventoryException thrown" );
        }
    }

    /**
     * Add the specified amount of ingredients to the CoffeeMaker's inventory.
     * This is for testing with invalid values and is expected to fail.
     *
     * @param amtCoffee
     *            Amount of Coffee to add to the Inventory.
     * @param amtMilk
     *            Amount of Milk to add to the Inventory.
     * @param amtSugar
     *            Amount of Sugar to add to the Inventory.
     * @param amtChocolate
     *            Amount of Chocolate to add to the Inventory.
     */
    @When ( "^I attempt to add (-?\\d+) coffee, (-?\\d+) milk, (-?\\d+) sugar, and (-?\\d+) chocolate$" )
    public void invalidAddInventory ( final int amtCoffee, final int amtMilk, final int amtSugar,
            final int amtChocolate ) {
        try {
            final Inventory inventory = inventoryService.getInventory();
            inventory.addIngredients( amtCoffee, amtMilk, amtSugar, amtChocolate );
            inventoryService.save( inventory );
            Assert.fail( "Inventory added without throwing an error." );
        }
        catch ( final Exception e ) {
            inventoryData.errorMessage = e.getMessage();
            Assert.assertTrue( "Adding Inventory throws error", true );
        }
    }

    /**
     * Set the CoffeeMaker's Inventory to have the amount of ingredients
     * specified. This will completely replace the values that were already
     * stored.
     *
     * @param amtCoffee
     *            Amount of Coffee to set the inventory to contain.
     * @param amtMilk
     *            Amount of Milk to set the inventory to contain.
     * @param amtSugar
     *            Amount of Sugar to set the inventory to contain.
     * @param amtChocolate
     *            Amount of Chocolate to set the inventory to contain.
     */
    @When ( "^I update it to be (-?\\d+) coffee, (-?\\d+) milk, (-?\\d+) sugar, and (-?\\d+) chocolate$" )
    public void updateInventory ( final int amtCoffee, final int amtMilk, final int amtSugar, final int amtChocolate ) {
        inventoryData.newCoffee = amtCoffee;
        inventoryData.newMilk = amtMilk;
        inventoryData.newSugar = amtSugar;
        inventoryData.newChocolate = amtChocolate;

        try {
            final Inventory inventory = inventoryService.getInventory();
            inventory.setCoffee( amtCoffee );
            inventory.setMilk( amtMilk );
            inventory.setSugar( amtSugar );
            inventory.setChocolate( amtChocolate );
            inventoryService.save( inventory );
        }
        catch ( final Exception e ) {
            Assert.fail( "Inventory not added. InventoryException thrown" );
        }
    }

    /**
     * Verify that the CoffeeMaker's Inventory was not updated and contains the
     * same values that were already stored in the "backup" Shared Inventory
     * Data.
     */
    @Then ( "^the inventory of the CoffeeMaker is not updated$" )
    public void inventoryNotUpdated () {

        final Inventory inventory = inventoryService.getInventory();
        final int coffee2 = inventory.getCoffee();
        final int milk2 = inventory.getMilk();
        final int sugar2 = inventory.getSugar();
        final int chocolate2 = inventory.getChocolate();

        // Verify that the inventory is unchanged
        Assert.assertEquals( "Coffee not correct", inventoryData.originalCoffee, coffee2 );
        Assert.assertEquals( "Milk not correct", inventoryData.originalMilk, milk2 );
        Assert.assertEquals( "Sugar not correct", inventoryData.originalSugar, sugar2 );
        Assert.assertEquals( "Chocolate not correct", inventoryData.originalChocolate, chocolate2 );

    }

    /**
     * Verify that the CoffeeMaker's Inventory has been updated and that it
     * stores the values in the SharedInventoryData
     */
    @Then ( "^the inventory of the CoffeeMaker is successfully added$" )
    public void inventoryAdded () {
        // calculate what the inventory SHOULD be
        final int expectedCoffee = inventoryData.originalCoffee + inventoryData.newCoffee;
        final int expectedMilk = inventoryData.originalMilk + inventoryData.newMilk;
        final int expectedSugar = inventoryData.originalSugar + inventoryData.newSugar;
        final int expectedChocolate = inventoryData.originalChocolate + inventoryData.newChocolate;

        final Inventory inventory = inventoryService.getInventory();
        final int coffee2 = inventory.getCoffee();
        final int milk2 = inventory.getMilk();
        final int sugar2 = inventory.getSugar();
        final int chocolate2 = inventory.getChocolate();

        // Verify that the inventory is correct
        Assert.assertEquals( "Coffee not added correctly", expectedCoffee, coffee2 );
        Assert.assertEquals( "Milk not added correctly", expectedMilk, milk2 );
        Assert.assertEquals( "Sugar not added correctly", expectedSugar, sugar2 );
        Assert.assertEquals( "Chocolate not added correctly", expectedChocolate, chocolate2 );

    }

    /**
     * Verify that the CoffeeMaker's Inventory has been updated and that it
     * stores the values in the SharedInventoryData
     */
    @Then ( "^the inventory of the CoffeeMaker is successfully updated$" )
    public void inventoryUpdated () {

        final Inventory inventory = inventoryService.getInventory();
        final int coffee2 = inventory.getCoffee();
        final int milk2 = inventory.getMilk();
        final int sugar2 = inventory.getSugar();
        final int chocolate2 = inventory.getChocolate();

        // Verify that the inventory is correct
        Assert.assertEquals( "Coffee not added correctly", inventoryData.newCoffee, coffee2 );
        Assert.assertEquals( "Milk not added correctly", inventoryData.newMilk, milk2 );
        Assert.assertEquals( "Sugar not added correctly", inventoryData.newSugar, sugar2 );
        Assert.assertEquals( "Chocolate not added correctly", inventoryData.newChocolate, chocolate2 );

        inventoryData.newCoffee = 0;
        inventoryData.newMilk = 0;
        inventoryData.newSugar = 0;
        inventoryData.newChocolate = 0;
        inventoryData.originalCoffee = 0;
        inventoryData.originalMilk = 0;
        inventoryData.originalSugar = 0;
        inventoryData.originalChocolate = 0;
        inventoryData.errorMessage = "";
    }

    /**
     * Ensure that an error was thrown while attempting to perform an action
     *
     * @param error
     *            The error message to check
     */
    @Then ( "^an error occurs for (.+)$" )
    public void errorThrown ( final String error ) {
        Assert.assertTrue( !error.isEmpty() );
    }

}
