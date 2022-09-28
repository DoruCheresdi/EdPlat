package edplatform.edplat.entities.courses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public void findById(Long id) {

    }

    public void save(Course course) {
        // add the time it was added to the course:
        Timestamp courseCreatedAt = new Timestamp(System.currentTimeMillis());
        course.setCreatedAt(courseCreatedAt);

        log.info("Creating course with name {} at timestamp {}",
                course.getCourseName(), courseCreatedAt);

        courseRepository.save(course);
    }
}
