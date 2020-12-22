package edu.ncsu.csc.iTrust2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.iTrust2.models.Inventory;
import edu.ncsu.csc.iTrust2.models.Recipe;
import edu.ncsu.csc.iTrust2.services.InventoryService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @Before
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.setChocolate( 500 );
        ivt.setCoffee( 500 );
        ivt.setMilk( 500 );
        ivt.setSugar( 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testUpdateInventory () {

        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( 50, 40, 30, 20 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values", 550,
                ivt.getCoffee() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values", 540, ivt.getMilk() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values", 530, ivt.getSugar() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values", 520,
                ivt.getChocolate() );

        try {
            ivt.addIngredients( 10, 20, 30, -40 );
            Assert.fail( "Trying to make an invalid update to the inventory should throw an exception" );
        }
        catch ( final Exception e ) {
            Assert.assertEquals( "Trying to add a negative value to the inventory should result in no updates", 550,
                    ivt.getCoffee() );
            Assert.assertEquals( "Trying to add a negative value to the inventory should result in no updates", 540,
                    ivt.getMilk() );
            Assert.assertEquals( "Trying to add a negative value to the inventory should result in no updates", 530,
                    ivt.getSugar() );
            Assert.assertEquals( "Trying to add a negative value to the inventory should result in no updates", 520,
                    ivt.getChocolate() );
        }

    }

    @Test
    @Transactional
    public void testInventoryConsumed () {

        final Inventory ivt = inventoryService.getInventory();

        final Recipe r1 = new Recipe();

        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setCoffee( 1 );
        r1.setMilk( 2 );
        r1.setSugar( 3 );
        r1.setChocolate( 4 );

        Assert.assertTrue( ivt.enoughIngredients( r1 ) );

        ivt.useIngredients( r1 );

        Assert.assertEquals( "Creating a recipe should partially deplete the inventory", 499, ivt.getCoffee() );
        Assert.assertEquals( "Creating a recipe should partially deplete the inventory", 498, ivt.getMilk() );
        Assert.assertEquals( "Creating a recipe should partially deplete the inventory", 497, ivt.getSugar() );
        Assert.assertEquals( "Creating a recipe should partially deplete the inventory", 496, ivt.getChocolate() );

        r1.setChocolate( 9001 );

        Assert.assertFalse( ivt.enoughIngredients( r1 ) );

        ivt.useIngredients( r1 );

        Assert.assertEquals(
                "Trying to create a recipe with insufficient inventory should result in no changes to the inventory",
                499, ivt.getCoffee() );
        Assert.assertEquals(
                "Trying to create a recipe with insufficient inventory should result in no changes to the inventory",
                498, ivt.getMilk() );
        Assert.assertEquals(
                "Trying to create a recipe with insufficient inventory should result in no changes to the inventory",
                497, ivt.getSugar() );
        Assert.assertEquals(
                "Trying to create a recipe with insufficient inventory should result in no changes to the inventory",
                496, ivt.getChocolate() );

    }
}
