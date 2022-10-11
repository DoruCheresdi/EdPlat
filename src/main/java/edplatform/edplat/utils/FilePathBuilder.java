package edplatform.edplat.utils;

import edplatform.edplat.entities.assignment.Assignment;
import edplatform.edplat.entities.users.User;
import org.springframework.stereotype.Service;

@Service
public class FilePathBuilder {

    public String getSubmissionFileDirectory(Assignment assignment, User user) {
        return "submissions/" + assignment.getId() + "-" + user.getId();
    }
}
