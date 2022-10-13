package edplatform.edplat.entities.authority;

import edplatform.edplat.entities.users.User;

import java.util.Optional;

public interface AuthorityService {

    public Optional<Authority> findByName(String name);

    /**
     * Method to give a certain authority to a user, also persisting it. Creates authority if it
     * doesn't exist in the database
     * @param user to be given an authority
     * @param authorityName name of the authority to give to the user
     */
    public void giveAuthorityToUser(User user, String authorityName);

    /**
     * Creates enrolled authority for course and persists it
     * @param courseId id of the course whose authority must be created
     */
    public Authority createEnrolledAuthority(Long courseId);

    /**
     * Method to delete an authority
     * @param authorityName authority to be deleted
     */
    public void deleteAuthority(String authorityName);
}
