package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

}
