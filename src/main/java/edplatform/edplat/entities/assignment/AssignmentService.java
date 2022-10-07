package edplatform.edplat.entities.assignment;

import java.util.Optional;

public interface AssignmentService {

    public Optional<Assignment> findById(Long id);

    public void save(Assignment assignment);
}
