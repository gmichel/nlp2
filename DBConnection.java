package gem.util.database;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by VHACONMICHEG on 6/4/2017.
 */
public class DBConnection {
    Connection conn = null;
    public Connection getDataBaseConnection()
    {
        Configurations configs = new Configurations();

        try {
            PropertiesConfiguration config =
                    configs.properties(new File("config.properties"));
            conn = DriverManager.getConnection(config.getString("dbURL"), config.getString("userName"), config.getString("password"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }


        /*
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ytex","ytex","ytex");
        } catch (SQLException sqlex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, sqlex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        return conn;
    }

    public void close() {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}