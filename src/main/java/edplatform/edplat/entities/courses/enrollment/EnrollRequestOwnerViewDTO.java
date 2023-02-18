package edplatform.edplat.entities.courses.enrollment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnrollRequestOwnerViewDTO {

    private final String userId;

    private final String userFullName;

    private final String createdAt;
}
