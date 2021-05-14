
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ferfl
 */
public class SQLStatement {
       
    //String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", host, port, db);

    static final String CONN_URL = "jdbc:mysql://localhost:3306/document?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "Fer";              /* cuenta de usuario: "root" */
    static final String PASSWD = "";        /* PASSWORD de la cuenta */
    Connection conn = null;

    
    public SQLStatement(String PRE_STMT) {
        
        try {
            
            // Registro del "driver"
            //System.out.print( "Loading driver... " );
            Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
            //System.out.println( "loaded" );
            
            // Establece la connección con el servidor del SGBD
            //System.out.print("Connecting to the database... ");
            
            try {
                conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
                //System.out.println( "connected" );
                // Do something with the Connection
                // Creación de la consulta
                PreparedStatement stmt = conn.prepareStatement( PRE_STMT );

                // Ejecución del update
                stmt.executeUpdate( PRE_STMT );
                // Cerrar y liberación de la memoria
                
                stmt.close();
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                //System.out.println("SQLState: " + ex.getSQLState());
                //System.out.println("VendorError: " + ex.getErrorCode());
            }                       
        } catch( Exception e ) {            
            System.err.println( "failed" );
            e.printStackTrace( System.err );
        }
    }
    
    public void SQLStatementClose() throws SQLException
    {
        conn.close();
    }
    
    public void commit() throws SQLException
    {
        conn.commit();
    }
    
    public void SQLState(String PRE_STMT) throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement( PRE_STMT );
        // Ejecución del update
        stmt.executeUpdate( PRE_STMT );
    }
}
