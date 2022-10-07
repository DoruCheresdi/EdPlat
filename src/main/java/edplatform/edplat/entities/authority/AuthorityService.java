package edplatform.edplat.entities.authority;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.authority.AuthorityRepository;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
@Slf4j
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    public Optional<Authority> findByName(String name) {
        return authorityRepository.findByName(name);
    }

    /**
     * Method to give a certain authority to a user, also persisting it. Creates authority if it
     * doesn't exist in the database
     * @param user to be given an authority
     * @param authorityName name of the authority to give to the user
     */
    public void giveAuthorityToUser(User user, String authorityName) {
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);

        log.info("Giving authority {} to username {}", authorityName, user.getEmail());

        Authority authority;
        if (optionalAuthority.isPresent()) {
            // authority is already present in DB, add the authority to the user:
            log.info("Authority already present in db, giving it to user");
            authority = optionalAuthority.get();
        } else {
            // create authority in DB:
            log.info("Authority is created in db, giving it to user");
            authority = new Authority(authorityName);
            authority.setUsers(new HashSet<>());
        }

        // add it to the user:
        authority.getUsers().add(user);
        user.getAuthorities().add(authority);
        authorityRepository.save(authority);
    }

    public void deleteAuthority(String authorityName) {
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);

        Authority authority;
        if (optionalAuthority.isEmpty()) {
            // authority is already present in DB, add the authority to the user:
            log.error("Trying to delete nonexistent authority with name: {}", authorityName);
        } else {
            log.info("Deleting authority: {}", authorityName);
            authority = optionalAuthority.get();
            authorityRepository.delete(authority);
        }
    }
}
