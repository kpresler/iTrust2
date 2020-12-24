package edu.ncsu.csc.iTrust2.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.security.LoginAttempt;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    /* No idea if any of these will work */

    public long countByIp ( String ipAddress );

    public long deleteByIp ( String ipAddress );

    public long countByUser ( User user );

    public long deleteByUser ( User user );

}
