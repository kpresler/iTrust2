package edu.ncsu.csc.iTrust2.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.LogEntry;
import edu.ncsu.csc.iTrust2.repositories.LogEntryRepository;

@Component
@Transactional
public class LogEntryService extends Service {

    @Autowired
    private LogEntryRepository repository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public List<LogEntry> findAllForUser ( final String user ) {
        LogEntry entry = new LogEntry();
        entry.setPrimaryUser( user );

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher( "primaryUser",
                ExampleMatcher.GenericPropertyMatchers.exact() );

        Example<LogEntry> example = Example.of( entry, matcher );

        final List<LogEntry> byPrimaryUser = repository.findAll( example );

        entry = new LogEntry();
        entry.setSecondaryUser( user );

        matcher = ExampleMatcher.matching().withMatcher( "secondaryUser",
                ExampleMatcher.GenericPropertyMatchers.exact() );

        example = Example.of( entry, matcher );

        /* Add in all log entries where our target is the Secondary User too */
        byPrimaryUser.addAll( repository.findAll( example ) );

        return byPrimaryUser;
    }

}
