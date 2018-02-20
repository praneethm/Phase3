
package main.database.communication;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mysqlconn
{

 public Mysqlconn()
 {
 }

 public static Connection getConnection()
 {
     //System.out.println("-------- Postgres JDBC Connection Testing ------------");
     try
     {
         Class.forName("org.postgresql.Driver");
     }
     catch(ClassNotFoundException e)
     {
         //System.out.println("Where is your postgres JDBC Driver?");
         e.printStackTrace();
     }
     //System.out.println("Postgres JDBC Driver Registered!");
     Connection connection = null;
     try
     {
    	 //M0rdor
    	 //praneeth
    	 //M0rdor19
         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/MIPCONN", "postgres", "praneeth");
     }
     catch(SQLException e)
     {
        // System.out.println("Connection Failed! Check output console");
         e.printStackTrace();
         return connection;
     }
/*     if(connection != null)
         System.out.println("You made it, take control your database now!");
     else
         System.out.println("Failed to make connection!");*/
     return connection;
 }
}
