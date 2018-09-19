package DAL.Contexts;

import java.sql.Connection;
import java.sql. DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionContext {

    private Connection Connection;
    private String ConnectionString;

    public DatabaseConnectionContext() {
        ConnectionString =  "jdbc:sqlserver://kiddyapi.database.windows.net:1433;database=Semester3;user=nickburgt@kiddyapi;password=Wachtwoord12@;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        try {
            Connection = DriverManager.getConnection(ConnectionString);
        } catch(SQLException e) {
            System.out.println("Cant connect to database");
        }
    }

    public Connection GetDatabaseConnection() {
        return Connection;
    }
}
