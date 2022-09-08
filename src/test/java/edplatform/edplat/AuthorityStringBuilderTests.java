package edplatform.edplat;

import edplatform.edplat.security.AuthorityStringBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthorityStringBuilderTests {

    @Autowired
    private AuthorityStringBuilder authorityStringBuilder;

    @Test
    void shouldGiveRightCourseOwnerAuthority() {
        String authorityString = authorityStringBuilder.getCourseOwnerAuthority("5");
        assertThat(authorityString).isEqualTo("course-5-owner");
    }

    @Test
    void shouldGiveRightCourseEnrolledAuthority() {
        String authorityString = authorityStringBuilder.getCourseEnrolledAuthority("5");
        assertThat(authorityString).isEqualTo("course-5-enrolled");
    }
}
