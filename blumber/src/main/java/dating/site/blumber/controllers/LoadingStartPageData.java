package dating.site.blumber.controllers;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Component
@RequestMapping("/loadingpage")
public class LoadingStartPageData {

    //Get user gender and key
    public String userGenderNow;
    public String userKey;
    @PostMapping
    public String getUserGenderAndKey(@RequestParam("storedGender") String storedGender,
                                      @RequestParam("storedUserKey") String storedUserKey) {
        userGenderNow = storedGender;
        userKey = storedUserKey;
        return "index";
    }

    //Data
    protected String firstUserKey;
    protected String secondUserKey;
    /*---------------------*/
    String userGender;
    String userName;
    String aboutUser;
    String userContactLinkName;
    String userContactLink;
    byte[] photoBytes;
    /*---------------------*/
    String userGender2;
    String userName2;
    String aboutUser2;
    String userContactLinkName2;
    String userContactLink2;
    byte[] photoBytes2;


    @Autowired
    private DataSource dataSource;

    NewProfile newProfile = new NewProfile();  

    @GetMapping
    public String newProfile(Model model, LoadingStartPageData loadingStartPageData) {

        if (userGenderNow == null || userGenderNow.isEmpty() || userGenderNow.equals("")) {
            userGenderNow = "Man";
        }

        String sql = "SELECT * FROM blumbertable WHERE userGender != '" + userGenderNow + "' AND userKey != '" + userKey + "' ORDER BY rand() LIMIT 1;";
        String sql2 = "SELECT * FROM blumbertable WHERE userGender != '" + userGenderNow + "' AND userKey != '" + userKey + "' AND userKey != '" + firstUserKey + "' ORDER BY rand() LIMIT 1;";

        for (int i = 0; i < 2; i++) {
            try (Connection connection = dataSource.getConnection()) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(i == 0 ? sql : sql2);

                while (resultSet.next()) {
                    if (i == 0) {
                        firstUserKey = resultSet.getString("userKey");
                        userGender = resultSet.getString("userGender");
                        userName = resultSet.getString("userName");
                        aboutUser = resultSet.getString("aboutUser");
                        userContactLinkName = resultSet.getString("userContactLinkName");
                        userContactLink = resultSet.getString("userContactLink");

                        Blob blob = resultSet.getBlob("userPhoto");
                        photoBytes = null;
                        if (blob != null) {
                            photoBytes = blob.getBytes(1, (int) blob.length());
                        }

                        addAttributes(i, model);
                    } else {
                        while (true) {
                            secondUserKey = resultSet.getString("userKey");
                            if (!secondUserKey.equals(firstUserKey)) {
                                userGender2 = resultSet.getString("userGender");
                                userName2 = resultSet.getString("userName");
                                aboutUser2 = resultSet.getString("aboutUser");
                                userContactLinkName2 = resultSet.getString("userContactLinkName");
                                userContactLink2 = resultSet.getString("userContactLink");

                                Blob blob = resultSet.getBlob("userPhoto");
                                photoBytes2 = null;
                                if (blob != null) {
                                    photoBytes2 = blob.getBytes(1, (int) blob.length());
                                }

                                break;
                            }
                        }

                        addAttributes(i, model);  
                    }
                }
                
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
            }
        }

        newProfile.setUserKeys(loadingStartPageData);

        return "index";
    }

    public void addAttributes(int i, Model model) {
        model.addAttribute(i == 0 ? "userGender" : "userGender2", 
        i == 0 ? userGender : userGender2);
        model.addAttribute(i == 0 ? "userName" : "userName2",
        i == 0 ? userName : userName2);
        model.addAttribute(i == 0 ? "aboutUser" : "aboutUser2",
        i == 0 ? aboutUser : aboutUser2);
        model.addAttribute(i == 0 ? "userContactLinkName" : "userContactLinkName2",
        i == 0 ? userContactLinkName : userContactLinkName2);
        model.addAttribute(i == 0 ? "userContactLink" : "userContactLink2",
        i == 0 ? userContactLink : userContactLink2);

        if (i == 0) {
            if (photoBytes != null) {
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                model.addAttribute("userPhoto", "data:image/jpeg;base64," + base64Photo);
            }
        } else {
            if (photoBytes2 != null) {
                String base64Photo2 = Base64.getEncoder().encodeToString(photoBytes2);
                model.addAttribute("userPhoto2", "data:image/jpeg;base64," + base64Photo2);
            }
        }
    }

    public String getFirstUserKey() {
        return firstUserKey;
    }

    public String getSecondUserKey() {
        return secondUserKey;
    }
}