package hospital;
import java.sql.*;

public class NewSqlClass {
    
    private static Connection con;

    public static void ConnectToSQL() {
        try {
            if (con == null || con.isClosed()) {
                String url = "jdbc:sqlserver://localhost:1433;"
                           + "databaseName=Hospital;"
                           + "encrypt=false;"
                           + "trustServerCertificate=true;";
                con = DriverManager.getConnection(url, "nedal", "0000");
                System.out.println("✅ CONNECTED");
            }
        } catch (SQLException ex) {
            System.out.println("❌ Connection Error: " + ex.getMessage());
        }
    }

    public static ResultSet getData(String sql) {
        try {
            ConnectToSQL();
            Statement stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                ResultSet.CONCUR_READ_ONLY
            );
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println("❌ getData Error: " + e.getMessage());
            return null;
        }
    }

    public static boolean executeNonQuery(String sql) {
        try {
            ConnectToSQL();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("❌ executeNonQuery Error: " + e);
            return false;
        }
    }
}