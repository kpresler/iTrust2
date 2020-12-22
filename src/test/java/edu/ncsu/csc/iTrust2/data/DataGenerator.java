package edu.ncsu.csc.iTrust2.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.UserService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class DataGenerator {

    @Autowired
    private UserService service;

    @Test
    public void createUsers () {

        final User admin = new User( new UserForm( "admin", "123456", Role.ROLE_ADMIN, 1 ) );

        final User doc = new User( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );

        service.save( admin );

        service.save( doc );

    }

}
