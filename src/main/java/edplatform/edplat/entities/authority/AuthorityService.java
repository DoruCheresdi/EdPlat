package edplatform.edplat.entities.authority;

import edplatform.edplat.entities.users.User;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {

    Optional<Authority> findByName(String name);

    /**
     * Loads all authorities of a user
     * @param email email of the user
     * @return list of all authorities of the user
     */
    List<Authority> findAllByUserEmail(String email);

    /**
     * Method to give a certain authority to a user, also persisting it. Creates authority if it
     * doesn't exist in the database
     * @param user to be given an authority
     * @param authorityName name of the authority to give to the user
     */
    void giveAuthorityToUser(User user, String authorityName);

    /**
     * Creates enrolled authority for course and persists it
     * @param courseId id of the course whose authority must be created
     */
    Authority createEnrolledAuthority(Long courseId);

    /**
     * Method to delete an authority
     * @param authorityName authority to be deleted
     */
    void deleteAuthority(String authorityName);
}
