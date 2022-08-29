package edplatform.edplat.entities.authority;

import edplatform.edplat.entities.courses.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Optional<Authority> findByName(String name);
}
