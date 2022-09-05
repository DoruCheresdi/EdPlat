package edplatform.edplat.security;

import edplatform.edplat.entities.authority.Authority;
import edplatform.edplat.entities.authority.AuthorityRepository;
import edplatform.edplat.entities.users.User;
import edplatform.edplat.entities.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * Method to give a certain authority to a user. Creates authority if it
     * doesn't exist in the database
     * @param user to be given an authority
     * @param authorityName name of the authority to give to the user
     */
    public void giveAuthorityToUser(User user, String authorityName) {
        Optional<Authority> optionalAuthority = authorityRepository.findByName(authorityName);

        if (optionalAuthority.isPresent()) {
            // authority is already present in DB, add the authority to the user:
            Authority authority = optionalAuthority.get();
            user.getAuthorities().add(authority);
        } else {
            // create authority in DB:
            Authority authority = new Authority(authorityName);
            authorityRepository.save(authority);

            // add it to the user:
            user.getAuthorities().add(authority);
        }
        userRepository.save(user);
    }
}
