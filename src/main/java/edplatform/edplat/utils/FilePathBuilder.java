package edplatform.edplat.utils;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.users.User;
import org.springframework.stereotype.Service;

@Service
public class FilePathBuilder {

    /**
     * Returns full path for a submission file.
     * @param assignment
     * @param user
     * @return
     */
    public String getSubmissionFileDirectory(Assignment assignment, User user) {
        return "submissions/" + assignment.getId() + "-" + user.getId();
    }
}
