package dating.site.blumber.controllers;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Component
@RequestMapping("/data")
public class NewProfile {

    public String userGenderNow;
    public String userKey;
    public String profileNumber;

    @PostMapping("/post")
    public String getData(@RequestParam("storedGender") String storedGender,
                          @RequestParam("storedUserKey") String storedUserKey,
                          @RequestParam("stroredProfileNumber") String stroredProfileNumber, Model model) {
        userGenderNow = storedGender;
        userKey = storedUserKey;
        profileNumber = stroredProfileNumber;
        return "index";
    }

    //Data    
    @Autowired
    private LoadingStartPageData loadingStartPageData;

    String lastUserKey;
    String lastUserKey2;
    public void setUserKeys(LoadingStartPageData loadingStartPageData) {
        this.lastUserKey = loadingStartPageData.getFirstUserKey();
        this.lastUserKey2 = loadingStartPageData.getSecondUserKey();
    }
    /*---------------------*/
    String userGender;
    String userName;
    String aboutUser;
    String userContactLinkName;
    String userContactLink;
    byte[] photoBytes;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<String> newProfile() {

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        if (lastUserKey == null && lastUserKey2 == null) {
            setUserKeys(loadingStartPageData);
        }

        if (userGenderNow == null || userGenderNow.isEmpty() || userGenderNow.equals("")) {
            userGenderNow = "Man";
        }

        String sql = "SELECT * FROM blumbertable WHERE userGender != '" + userGenderNow + "' AND userKey != '" + userKey + "' AND userKey != '" + lastUserKey + "' AND userKey != '" + lastUserKey2 + "' ORDER BY rand() LIMIT 1;";

        try (Connection connection = dataSource.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                if (profileNumber.equals("1")) {
                    lastUserKey = resultSet.getString("userKey");
                } else {
                    lastUserKey2 = resultSet.getString("userKey");
                }
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
            }
            
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }

        String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
        String jsonProfile = String.format("{\"profileNumber\": \"%s\", \"userGender\": \"%s\", \"userName\": \"%s\", \"aboutUser\": \"%s\", \"userContactLinkName\": \"%s\", \"userContactLink\": \"%s\", \"userPhoto\": \"data:image/jpeg;base64,%s\"}",
        profileNumber, userGender, userName, aboutUser, userContactLinkName, userContactLink, base64Photo);

        return ResponseEntity.ok(jsonProfile);
    }

}