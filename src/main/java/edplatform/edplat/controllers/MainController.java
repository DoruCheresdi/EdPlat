package edplatform.edplat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path="/")
public class MainController {

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }
}
