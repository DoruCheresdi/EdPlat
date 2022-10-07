package edplatform.edplat.randomDataGenerator;

import edplatform.edplat.entities.courses.Course;
import edplatform.edplat.entities.courses.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DeleteAllCourses {

    @Autowired
    private CourseService courseService;

    @Test
    @Transactional
    @Rollback(false)
    public void deleteAllCourses() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);;
        Page<Course> listCourses;

        // delete page by page until there are no more courses:
        do {
            listCourses = courseService.findAll(pageable);
            for (Course course :
                    listCourses) {
                courseService.deleteCourse(course);
            }
        } while(listCourses.getTotalElements() > 0);
    }
}
