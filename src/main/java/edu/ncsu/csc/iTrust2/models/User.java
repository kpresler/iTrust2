package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.enums.Role;

/**
 * Basic class for a User in the system. This User class is a shared type that
 * is used for all users of the iTrust2 system and handles basic functionality
 * such as authenticating the user in the system. For specific rolls, an
 * additional record, such as a Patient or Personnel record, is created and
 * references the User object for that user. This allows the iTrust2 system to
 * keep only what information is needed for a particular type of user.
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
public class User extends DomainObject {

    /**
     * The UID of the user
     */
    private static final long serialVersionUID = 1L;

    /** For Hibernate */
    public User () {
    }

    /**
     * All-argument constructor for User
     *
     * @param username
     *            Username
     * @param password
     *            The _already encoded_ password of the user.
     * @param role
     *            Role of the user
     * @param enabled
     *            1 if the user is enabled 0 if not
     */
    public User ( final String username, final String password, final Role role, final Integer enabled ) {
        setUsername( username );
        setPassword( password );
        setRole( role );
        setEnabled( enabled );
    }

    /**
     * Create a new user based off of the UserForm
     *
     * @param form
     *            the filled-in user form with user information
     */
    public User ( final UserForm form ) {
        setUsername( form.getUsername() );
        if ( !form.getPassword().equals( form.getPassword2() ) ) {
            throw new IllegalArgumentException( "Passwords do not match!" );
        }
        final PasswordEncoder pe = new BCryptPasswordEncoder();
        setPassword( pe.encode( form.getPassword() ) );
        setEnabled( null != form.getEnabled() ? 1 : 0 );
        setRole( Role.valueOf( form.getRole() ) );

    }

    /**
     * The username of the user
     */
    @Id
    @Length ( max = 20 )
    private String  username;

    /**
     * The password of the user
     */
    private String  password;

    /**
     * Whether or not the user is enabled
     */
    @Min ( 0 )
    @Max ( 1 )
    private Integer enabled;

    /**
     * The role of the user
     */
    @Enumerated ( EnumType.STRING )
    private Role    role;

    /**
     * Get the username of this user
     *
     * @return the username of this user
     */
    public String getUsername () {
        return username;
    }

    /**
     * Set the username of this user
     *
     * @param username
     *            the username to set this user to
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * Get the password of this user
     *
     * @return the password of this user
     */
    public String getPassword () {
        return password;
    }

    /**
     * Set the password of this user
     *
     * @param password
     *            the password to set this user to
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * Get whether or not the user is enabled
     *
     * @return Whether or not the user is enabled
     */
    public Integer getEnabled () {
        return enabled;
    }

    /**
     * Set whether or not the user is enabled
     *
     * @param enabled
     *            Whether or not the user is enabled
     */
    public void setEnabled ( final Integer enabled ) {
        this.enabled = enabled;
    }

    /**
     * Get the role of this user
     *
     * @return the role of this user
     */
    public Role getRole () {
        return role;
    }

    /**
     * Set the role of this user
     *
     * @param role
     *            the role to set this user to
     */
    public void setRole ( final Role role ) {
        this.role = role;
    }

    /**
     * Get the hashCode of this user
     *
     * @return the hashCode of this user
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( enabled == null ) ? 0 : enabled.hashCode() );
        result = prime * result + ( ( password == null ) ? 0 : password.hashCode() );
        result = prime * result + ( ( role == null ) ? 0 : role.hashCode() );
        result = prime * result + ( ( username == null ) ? 0 : username.hashCode() );
        return result;
    }

    /**
     * Whether or not this user is equal to the passed User
     *
     * @param obj
     *            the user to compate this user to
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final User other = (User) obj;
        if ( enabled == null ) {
            if ( other.enabled != null ) {
                return false;
            }
        }
        else if ( !enabled.equals( other.enabled ) ) {
            return false;
        }
        if ( password == null ) {
            if ( other.password != null ) {
                return false;
            }
        }
        else if ( !password.equals( other.password ) ) {
            return false;
        }
        if ( role != other.role ) {
            return false;
        }
        if ( username == null ) {
            if ( other.username != null ) {
                return false;
            }
        }
        else if ( !username.equals( other.username ) ) {
            return false;
        }
        return true;
    }

    /**
     * Get the id of this user (aka, the username)
     */
    public String getId () {
        return getUsername();
    }

    /**
     * True if the user is an HCP, OD, OPH, or Virologist
     *
     * @return true if the user role matches HCP, OD, Virologist, or OPH
     */
    public boolean isDoctor () {
        return ( role == Role.ROLE_HCP || role == Role.ROLE_OD || role == Role.ROLE_OPH
                || role == Role.ROLE_VIROLOGIST );
    }

}
