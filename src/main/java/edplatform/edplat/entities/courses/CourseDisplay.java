package edplatform.edplat.entities.courses;

import lombok.Data;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * DTO for displaying information about a course for a specific user
 */
@Data
public class CourseDisplay {

    public CourseDisplay(Long id, String courseName, String description,
                         String image, Timestamp createdAt, String userAuthority) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.image = image;
        this.createdAt = createdAt;
        this.userAuthority = userAuthority;
    }

    private final Long id;

    private final String courseName;

    private final String description;

    private final String image;

    private final Timestamp createdAt;

    /**
     * Authority of the user that requested the page over this course:
     */
    private final String userAuthority;

    public String getImagePath() {
        if (image == null || id == null) return null;

        return "/course-photos/" + id + "/" + image;
    }

    public String getSinceCreatedString() {
        // get time since course has been created in pretty format:
        PrettyTime t = new PrettyTime(new Date(System.currentTimeMillis()));
        return t.format(new Date(createdAt.getTime()));
    }
}
