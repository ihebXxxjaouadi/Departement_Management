
package db;

import Mycommon.Table;
import Mycommon.Tools;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



public class go {
    
    
    private static Cluster cluster;
    private static Session session;

    public static void connectdb(String ServerIp, int port) {
        go.cluster = Cluster.builder().addContactPoint(ServerIp).withPort(port).build();
        final Metadata metadata = cluster.getMetadata();

        for (final Host host : metadata.getAllHosts()) {
            System.out.println("driver version " + host.getCassandraVersion());
        }

        go.session = cluster.connect();
    }

    public static void close() {
        cluster.close();
    }


    public static boolean checkUserAndPass(String username , String password){
        try{
            connectdb("localhost", 9042);
       
            String sqlStatement="select * from users where userName ='" + username + "'and pass= '"+ password + "'";
            
            
            while (session.execute(sqlStatement).getResultSet().next()){
                cluster.close();
                return true ;
            }
            
            cluster.close();

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return false ;
    }
    
    //Insert Update Delete NOT select
    public static boolean runNonQuery(String sqlStatement){
        
        try{
            connectdb("localhost", 9042);
            session.execute(sqlStatement) ;
            close();
            return true ;
        }
        catch(Exception ex){
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            return false ;
        }
        
    }
    
    public static String getAutoNumber(String tableName , String columnName){
        
        try {
            connectdb("localhost", 9042);
            String strAuto = " select max("+columnName+")+1 as autonum from "+tableName ;         
            String Num = "" ;
            while(session.executeQuery(strAuto).getResultSet().next()){
                Num = session.executeQuery(strAuto).getResultSet().getString("autonum") ;
            }
            close();
            if (Num == null || "".equals(Num)) {
                return "1" ;
            }
            else{
                return Num ;
            }
            
        } catch (Exception ex) {
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            return "0" ;
        }
        
    }
    
    
    public static Table getTableData(String statement){
        
        try {
            connectdb("localhost", 9042);
            ResultSet rs ;
            rs = session.execute(statement) ;
            ResultSetMetaData rsmd = rs.getMetaData() ;
            int c = rsmd.getColumnCount() ;
            Table table =new Table(c) ;
            
            while(rs.next()){
                Object Row[] = new Object[c] ;
                for (int i = 0  ; i < c ; i++ ){
                    Row[i] = rs.getString(i + 1) ;
                }table.addNewRow(Row);
            }
            close() ;
            return table ;
            
         } catch (Exception ex) {
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            return new Table(0) ;
        }
    }
    
    
    public static void fillCombo(String tableName, String columnName, JComboBox combo  ){
        try{
            connectdb("localhost", 9042);

            ResultSet rs ;
            String strSelect = "select " + columnName + " from " +  tableName ;  
            rs = session.execute(strSelect) ;
            rs.last() ;
            int c = rs.getRow() ;
            rs.beforeFirst(); 
            String Values[] = new String[c] ; 
            
            int i = 0 ;
            while(rs.next()){
                Values[i] = rs.getString(1) ;
                i+=1 ;
            }
            close(); 
            combo.setModel(new DefaultComboBoxModel(Values));
              
        }
        catch(Exception ex){
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            
        }
        
    }
    //243
    public static void fillToJTable(String tableNameOrSelectStatement, JTable table ){
        
        try{
            connectdb("localhost", 9042);
            ResultSet rs ;
            String SPart = tableNameOrSelectStatement.substring(0, 7) ;
            if ("select ".equals(SPart)){
                rs = session.execute(tableNameOrSelectStatement) 
                        ;}
            else {
                rs = session.execute("select * from " +tableNameOrSelectStatement) 
                        ;}

            ResultSetMetaData rsmd = rs.getMetaData() ;
            //to know how length we have to build columns table 
            int c = rsmd.getColumnCount() ;
            DefaultTableModel tbl = (DefaultTableModel)table.getModel() ;
            //to intialize table to 0 columns 
            tbl.setRowCount(0);
            ////Object Row[]= new Object[c] ;
            Vector Row = new Vector() ;
             //to put columns in array(Object) then add it to table  
            while (rs.next()){             
                ////Row[i] = rs.getString(i + 1) ;
                Row = new Vector(c) ;
                for (int i = 1 ; i <= c ; i++ ){
                    Row.add( rs.getString(i)) ;
                }tbl.addRow(Row);
            }
            
            if (table.getColumnCount() != c){
            System.out.println("JTable Columns Count Not Equal To Querry Count\nJTable Columns Count Is :  "+ table.getColumnCount() +"\nQuery Columns Count Is: " + c ) ;
            }
            close() ;
        }
        catch(Exception Ex){
            System.out.println("----------Error Synthaxe----------");
            System.out.println(Ex.getMessage()) ;
            System.out.println("----------------------------------");
        }
    }
        
    
   
}
