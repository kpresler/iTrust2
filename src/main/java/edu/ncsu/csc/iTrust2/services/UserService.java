package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

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

}
