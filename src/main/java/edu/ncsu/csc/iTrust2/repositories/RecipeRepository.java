package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
