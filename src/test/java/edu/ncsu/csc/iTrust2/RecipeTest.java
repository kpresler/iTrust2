package edu.ncsu.csc.iTrust2;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.iTrust2.models.Recipe;
import edu.ncsu.csc.iTrust2.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @Before
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setCoffee( 1 );
        r1.setMilk( 0 );
        r1.setSugar( 0 );
        r1.setChocolate( 0 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setCoffee( 1 );
        r2.setMilk( 1 );
        r2.setSugar( 1 );
        r2.setChocolate( 1 );
        service.save( r2 );

        final List<Recipe> recipes = (List<Recipe>) service.findAll();
        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, recipes.size() );

        Assert.assertEquals( "The retrieved recipe should match the created one", r1, recipes.get( 0 ) );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        r1.setCoffee( -12 );
        r1.setMilk( 0 );
        r1.setSugar( 0 );
        r1.setChocolate( 0 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setCoffee( 1 );
        r2.setMilk( 1 );
        r2.setSugar( 1 );
        r2.setChocolate( 1 );

        final List<Recipe> recipes = new ArrayList<Recipe>();

        recipes.add( r1 );
        recipes.add( r2 );

        try {
            service.saveAll( recipes );
            Assert.assertEquals(
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved",
                    0, service.count() );
        }
        catch ( final Exception e ) {
            Assert.assertTrue( e instanceof ConstraintViolationException );
        }

    }

}
