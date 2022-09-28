package edplatform.edplat.entities.courses;

public interface CourseService {

    /**
     * Retrieved a course from db by id
     * @param id id of the course to be retrieved
     */
    public void findById(Long id);

    /**
     * Saves a course entity to the DB, course should have a name
     * @param course entity to be saved
     */
    public void save(Course course);
}
