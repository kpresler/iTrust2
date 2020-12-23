package edu.ncsu.csc.iTrust2.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.LogEntry;
import edu.ncsu.csc.iTrust2.repositories.LogEntryRepository;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

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
        final LogEntry entry = new LogEntry();
        entry.setPrimaryUser( user );
        entry.setSecondaryUser( user );

        // Maybe this will work?
        final ExampleMatcher matcher = ExampleMatcher.matchingAny();

        final Example<LogEntry> example = Example.of( entry, matcher );

        final List<LogEntry> byPrimaryUser = repository.findAll( example );

        return byPrimaryUser;
    }

    public List<LogEntry> getByDateRange ( final ZonedDateTime startDate, final ZonedDateTime endDate ) {
        endDate.plusDays( 1 ); // To make inclusive

        final String user = LoggerUtil.currentUser();

        return repository.findByTimeBetween( startDate, endDate ).stream()
                .filter( e -> e.getPrimaryUser().equals( user ) || e.getSecondaryUser().equals( user ) )
                .collect( Collectors.toList() );

    }

}
