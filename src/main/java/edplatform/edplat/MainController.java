package edplatform.edplat;

import edplatform.edplat.courses.Course;
import edplatform.edplat.courses.CourseRepository;
import edplatform.edplat.users.User;
import edplatform.edplat.users.UserRepository;
import edplatform.edplat.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path="/")
public class MainController {

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }
}
