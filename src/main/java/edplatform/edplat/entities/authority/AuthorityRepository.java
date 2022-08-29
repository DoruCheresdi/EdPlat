package edplatform.edplat.entities.authority;

import edplatform.edplat.entities.courses.Course;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Course, Long> {
}
