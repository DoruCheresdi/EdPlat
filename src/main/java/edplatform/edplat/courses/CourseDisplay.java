package edplatform.edplat.courses;

import lombok.Data;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Class for displaying information about a course
 */
@Data
public class CourseDisplay {

    public CourseDisplay(Long id, String courseName, String description, String image, Timestamp createdAt) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.image = image;
        this.createdAt = createdAt;
    }

    private Long id;

    private String courseName;

    private String description;

    private String image;

    private Timestamp createdAt;

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
