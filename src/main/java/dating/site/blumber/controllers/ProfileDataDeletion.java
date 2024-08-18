package dating.site.blumber.controllers;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settingsdelete")
public class ProfileDataDeletion {

    @Autowired
    private DataSource dataSource;

    @PostMapping
    public String getData(@RequestParam("storedUserKey") String storedUserKey) {
        
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("USE blumberdatabase;");

            String sql = "DELETE FROM blumbertable WHERE userKey = '" + storedUserKey + "';";

            statement.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println("SQL Error:" + e.getMessage());
        }

        return "settings";
    }

}