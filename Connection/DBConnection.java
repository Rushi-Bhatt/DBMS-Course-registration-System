package Connection;

import java.sql.*;



public class DBConnection {

    static final String jdbcURL = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";

    public static Connection ConnectDB() throws ClassNotFoundException, SQLException {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            String user = "zndesai";	
            String passwd = "200151915";	
            Connection conn = null;
            
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            
            return conn;
    }

    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }
}