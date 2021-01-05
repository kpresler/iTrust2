package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;

import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.enums.Role;

@Entity
public class Personnel extends User {

    private String firstName;
    private String lastName;

    /**
     * For Hibernate
     */
    public Personnel () {

    }

    public Personnel ( final UserForm uf ) {
        super( uf );
        if ( getRoles().contains( Role.ROLE_PATIENT ) ) {
            throw new IllegalArgumentException( "Attempted to create a Personnel record for a non-Patient user!" );
        }
    }

    public String getFirstName () {
        return firstName;
    }

    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName () {
        return lastName;
    }

    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

}
