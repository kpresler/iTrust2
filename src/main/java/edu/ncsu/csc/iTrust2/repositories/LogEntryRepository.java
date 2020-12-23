package edu.ncsu.csc.iTrust2.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    public List<LogEntry> findByTimeBetween ( ZonedDateTime fromDate, ZonedDateTime toDate );

}
