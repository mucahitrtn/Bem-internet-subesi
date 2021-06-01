import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DbHelper {
    
    public static Connection connectDb() {
        
        Connection conn= null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn= DriverManager.getConnection("jdbc:derby://localhost:1527/addressbook","APP","APP");
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(DbHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getErrorCode());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
   