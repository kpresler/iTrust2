package edu.ncsu.csc.iTrust2.services;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.Recipe;
import edu.ncsu.csc.iTrust2.repositories.RecipeRepository;

/**
 * The RecipeService is used to handle CRUD operations on the Recipe model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Recipe by name.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class RecipeService extends Service {

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    protected JpaRepository getRepository () {
        return recipeRepository;
    }

    public Recipe findByName ( final String name ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );

        final ExampleMatcher matcher = ExampleMatcher.matching().withMatcher( "name",
                ExampleMatcher.GenericPropertyMatchers.exact() );

        final Example<Recipe> example = Example.of( recipe, matcher );

        try {
            return recipeRepository.findOne( example ).get();
        }
        catch ( final NoSuchElementException nsee ) {
            return null;
        }
    }

}
