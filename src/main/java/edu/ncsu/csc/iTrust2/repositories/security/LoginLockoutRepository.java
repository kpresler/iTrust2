package edu.ncsu.csc.iTrust2.repositories.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.security.LoginLockout;

public interface LoginLockoutRepository extends JpaRepository<LoginLockout, Long> {

    public List<LoginLockout> findByIp ( String ipAddress );

    public long removeByIp ( String ipAddress );

    public List<LoginLockout> findByUser ( User user );

    public long removeByUser ( User user );

}
