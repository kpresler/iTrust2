package edu.ncsu.csc.iTrust2.repositories.security;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.ncsu.csc.iTrust2.models.security.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    public List<LogEntry> findByTimeBetween ( ZonedDateTime fromDate, ZonedDateTime toDate );

    @Query ( "SELECT le FROM LogEntry le WHERE le.primaryUser = ?1 OR le.secondaryUser = ?1" )
    public List<LogEntry> findByPrimaryUserOrSecondaryUser ( String user );

}
