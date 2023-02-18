package edplatform.edplat.entities.courses.enrollment;

public class CourseEnrollment {

    public enum EnrollmentType {
        // users don't have to be approved:
        FREE,
        // course owners have to approve user's requests to join a course:
        ONE_OWNER_DECIDES
    }
}
