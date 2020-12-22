package edu.ncsu.csc.iTrust2.services;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.repositories.UserRepository;

@Component
@Transactional
public class UserService extends Service {

    @Autowired
    private UserRepository repository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public User findByName ( final String username ) {
        final User user = new User();
        user.setUsername( username );

        final ExampleMatcher matcher = ExampleMatcher.matching().withMatcher( "username",
                ExampleMatcher.GenericPropertyMatchers.exact() );

        final Example<User> example = Example.of( user, matcher );

        try {
            return repository.findOne( example ).get();
        }
        catch ( final NoSuchElementException nsee ) {
            return null;
        }
    }

}
