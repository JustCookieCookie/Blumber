package dating.site.blumber.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class settingsController {

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
}