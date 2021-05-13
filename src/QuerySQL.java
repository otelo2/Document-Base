import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ferfl
 */
public class QuerySQL {
   
    //String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", host, port, db);

    static final String CONN_URL = "jdbc:mysql://localhost:3306/document?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "Fer";              /* cuenta de usuario: "root" */
    static final String PASSWD = "";        /* PASSWORD de la cuenta */
    //static final String PRE_STMT = "select * from freqtt";
    
    public QuerySQL(String PRE_STMT) {
        
        try {
            
            // Registro del "driver"
            //System.out.print( "Loading driver... " );
            Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
            //System.out.println( "loaded" );
            
            // Establece la connección con el servidor del SGBD
            //System.out.print("Connecting to the database... ");
            
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
                //System.out.println( "connected" );
                // Do something with the Connection
                // Creación de la consulta
                PreparedStatement stmt = conn.prepareStatement( PRE_STMT );

                // Ejecución de la consulta
                ResultSet rset = stmt.executeQuery();

                // Presentación del resultado
                //System.out.println( "Results:" );
                dumpResultSet( rset );
                System.out.println( "" );

                // Cerrar y liberación de la memoria
                rset.close();
                stmt.close();
                conn.close();
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
    
    private void dumpResultSet(ResultSet rset) throws SQLException {
        
        ResultSetMetaData rsetmd = rset.getMetaData();
        int i = rsetmd.getColumnCount();
        
        while (rset.next()) {
            for (int j = 1; j <= i; j++) {
                System.out.print(rset.getString(j) + "\t");
            }
            System.out.println();
        }
    }
}