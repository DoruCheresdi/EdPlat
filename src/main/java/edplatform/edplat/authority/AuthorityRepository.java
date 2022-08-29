package edplatform.edplat.authority;

import edplatform.edplat.courses.Course;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Course, Long> {
}
