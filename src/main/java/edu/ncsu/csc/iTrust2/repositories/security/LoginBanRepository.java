package edu.ncsu.csc.iTrust2.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.security.LoginBan;

public interface LoginBanRepository extends JpaRepository<LoginBan, Long> {

    public boolean existsByIp ( String ipAddress );

    public boolean existsByUser ( User user );

    public long deleteByIp ( String ipAddress );

    public long deleteByUser ( User user );

}
