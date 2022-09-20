package edplatform.edplat.entities.assignment;

import edplatform.edplat.entities.authority.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AssignmentRepository extends CrudRepository<Assignment, Long> {

    Optional<Assignment> findByAssignmentName(String assignmentName);
}
