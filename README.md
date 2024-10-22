# Blumber
A simple dating site with a nice and minimalistic interface.

Such technologies as Java, JavaScript, HTML, CSS, MySQL and Spring framework were used in the development.

You should also change the database data that is missing in the `application.properties` (bin\main\application.properties, build\resources\main\application.properties, src\main\resources\application.properties) files to your own. The database name should be `blumberdatabase` and table name `blumbertable`. The table should contain data such as:
`id mediumint`
`userKey varchar(50)`
`userGender varchar(5)`
`userPhoto longblob`
`userName varchar(30)`
`aboutUser varchar(230)`
`userContactLinkName varchar(30)`
`userContactLink varchar(100)`.

To run the project you should open it in the code editor. Go to the `BlumberApplication.java` file (src/main/java/dating/site/blumber/BlumberApplication.java) and run it. Then in your browser go to the link `http://localhost:8080/loadingpage`.
