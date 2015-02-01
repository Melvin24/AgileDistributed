package myproj;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class mySQL {

    
    Connection conn = null;
    public static Connection ConnectDb() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://csmysql.cs.cf.ac.uk:3306/c1214944", "c1214944", "melvinrjohn");
            //System.out.println(conn);
            return conn;
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        } 
    }

}
