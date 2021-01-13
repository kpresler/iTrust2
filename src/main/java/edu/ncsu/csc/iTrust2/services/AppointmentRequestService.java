package edu.ncsu.csc.iTrust2.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.AppointmentRequest;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.repositories.AppointmentRequestRepository;

@Component
@Transactional
public class AppointmentRequestService extends Service {

    @Autowired
    private AppointmentRequestRepository repository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public List<AppointmentRequest> findByPatient ( final User patient ) {
        return repository.findByPatient( patient );
    }

    public List<AppointmentRequest> findByHcp ( final User hcp ) {
        return repository.findByHcp( hcp );
    }

    public List<AppointmentRequest> findByHcpAndPatient ( final User hcp, final User patient ) {
        return repository.findByHcpAndPatient( hcp, patient );
    }

}
