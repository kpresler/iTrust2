package edu.ncsu.csc.iTrust2.cucumber;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class CommonStepDefs extends CucumberTest {

    @Given ( "An Admin exists in iTrust2" )
    public void adminExists () {
        final User u = new Personnel( new UserForm( "admin", "123456", Role.ROLE_ADMIN, 1 ) );

        userService.save( u );

    }

    /**
     * Admin log in
     */
    @When ( "I log in as admin" )
    public void loginAdmin () {
        attemptLogout();

        driver.get( BASE_URL );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

}
