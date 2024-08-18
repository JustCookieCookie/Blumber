package dating.site.blumber.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settingsdata")
public class ProfileDataSending {

    int count;

    @Autowired
    private DataSource dataSource;

    @PostMapping
    public String getData(@RequestParam("storedUserKey") String storedUserKey,
                          @RequestParam("storedGender") String storedGender,
                          @RequestParam("storedUserName") String storedUserName,
                          @RequestParam("storedAboutUser") String storedAboutUser,
                          @RequestParam("storedUserContact") String storedUserContact,
                          @RequestParam("storedUserContactText") String storedUserContactText,
                          @RequestParam("storedPhoto") String storedPhoto
                          ) {

        controllUserProfile(storedUserKey);
        
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("USE blumberdatabase;");

            byte[] userPhotoBytes = Base64.getDecoder().decode(storedPhoto.split(",")[1]);

            String sql1 = "INSERT INTO blumbertable (userKey, userGender, userPhoto, userName, aboutUser, userContactLink, userContactLinkName) " +
            "VALUES ('" + storedUserKey + "', '" + storedGender + "', ?, '" + storedUserName + "', '" + storedAboutUser + "', '" + storedUserContact + "', '" + storedUserContactText + "');";
            
            String sql2 = "UPDATE blumbertable SET userGender = '" + storedGender + "', userPhoto = ?, userName = '" + storedUserName + "', aboutUser = '" + storedAboutUser + "', userContactLinkName = '" + storedUserContactText + "', userContactLink = '" + storedUserContact + "' WHERE userKey = '" + storedUserKey + "';";

            try (PreparedStatement preparedStatement = connection.prepareStatement(count == 0 ? sql1 : sql2)) {
                preparedStatement.setBytes(1, userPhotoBytes);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("SQL Error:" + e.getMessage());
        }

        return "settings";
    }

    public void controllUserProfile(String storedUserKey) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM blumbertable WHERE userKey = '" + storedUserKey + "';");

            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("SQL Error:" + e.getMessage());
        }
    }
}