package edplatform.edplat.entities.courses.enrollment;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentRequestCreationStrategyFactory {

    private final FreeEnrollmentRequestCreationStrategy freeEnrollmentRequestCreationStrategy;

    private final OwnerDecidesEnrollmentRequestCreationStrategy ownerDecidesEnrollmentRequestCreationStrategy;

    public EnrollmentRequestCreationStrategy creationStrategy(CourseEnrollment.EnrollmentType enrollmentType) {
        switch (enrollmentType) {
            case FREE:
                // users are free to join the course without requiring any permission:
                return freeEnrollmentRequestCreationStrategy;
            case ONE_OWNER_DECIDES:
                // users need to be accepted by at least one owner:
                return ownerDecidesEnrollmentRequestCreationStrategy;
        }
        return null;
    }
}
