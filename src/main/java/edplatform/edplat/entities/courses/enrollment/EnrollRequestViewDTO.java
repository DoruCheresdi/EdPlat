package edplatform.edplat.entities.courses.enrollment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnrollRequestViewDTO {

    private final String courseName;

    private final String createdAt;
}
