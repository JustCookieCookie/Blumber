package dating.site.blumber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Component
class LoadingPage {

    private final NewProfile newProfile;

    @Autowired
    public LoadingPage(NewProfile newProfile) {
        this.newProfile = newProfile;
    }

    @GetMapping("/")
    public String helpNewProfile(LoadingStartPageData loadigStartPageData) {
        newProfile.setUserKeys(loadigStartPageData);
        return "index";
    }
}