
package db;

import Mycommon.Table;
import Mycommon.Tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



public class go {
    
    private static String url = "" ;
    private static String dbName = "mydata" ;
    private static Connection con ;
    
    //to prepare URL 
    private static void setURL(){
        url = "jdbc:mysql://localhost:3306/" + dbName 
                + "?useUnicode=true&characterEncoding=UTF-8" ;
    }    
    
    private static void setConnection(){
        
        try {
            setURL();
            con = DriverManager.getConnection(url, "root", "") ;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            //Tools.msgBox(ex.getMessage());
        }
        
    }
    
    public static boolean checkUserAndPass(String username , String password){
        try{
            setConnection();
            Statement stmt = con.createStatement();
            String strCheck="select * from users where userName ='" + username + "'and pass= '"+ password + "'";
            stmt.executeQuery(strCheck) ;
            
            while (stmt.getResultSet().next()){
                con.close() ;
                return true ;
            }
            
            con.close() ;
            
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return false ;
    }
    
    //Insert Update Delete NOT select
    public static boolean runNonQuery(String sqlStatement){
        
        try{
            setConnection();
            Statement stmt = con.createStatement() ;
            stmt.execute(sqlStatement) ;
            con.close();
            return true ;
        }
        catch(SQLException ex){
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            return false ;
        }
        
    }
    
    public static String getAutoNumber(String tableName , String columnName){
        
        try {
            setConnection() ;
            Statement stmt = con.createStatement() ;
            String strAuto = " select max("+columnName+")+1 as autonum from "+tableName ;
            stmt.executeQuery(strAuto) ;
            String Num = "" ;
            while(stmt.getResultSet().next()){
                Num = stmt.getResultSet().getString("autonum") ;
            }
            con.close();
            if (Num == null || "".equals(Num)) {
                return "1" ;
            }
            else{
                return Num ;
            }
            
        } catch (SQLException ex) {
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            return "0" ;
        }
        
    }
    
    
    public static Table getTableData(String statement){
        
        try {
            setConnection() ;
            Statement stmt = con.createStatement() ;
            ResultSet rs ;
            rs = stmt.executeQuery(statement) ;
            ResultSetMetaData rsmd = rs.getMetaData() ;
            int c = rsmd.getColumnCount() ;
            Table table =new Table(c) ;
            
            while(rs.next()){
                Object Row[] = new Object[c] ;
                for (int i = 0  ; i < c ; i++ ){
                    Row[i] = rs.getString(i + 1) ;
                }table.addNewRow(Row);
            }
            con.close() ;
            return table ;
            
         } catch (SQLException ex) {
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            return new Table(0) ;
        }
    }
    
    
    public static void fillCombo(String tableName, String columnName, JComboBox combo  ){
        try{
            setConnection() ;
            Statement stmt = con.createStatement() ;
            ResultSet rs ;
            
            String strSelect = "select " + columnName + " from " +  tableName ;  
            rs = stmt.executeQuery(strSelect) ;
            rs.last() ;
            int c = rs.getRow() ;
            rs.beforeFirst(); 
            String Values[] = new String[c] ; 
            
            int i = 0 ;
            while(rs.next()){
                Values[i] = rs.getString(1) ;
                i+=1 ;
            }
            con.close(); 
            combo.setModel(new DefaultComboBoxModel(Values));
              
        }
        catch(SQLException ex){
            System.out.println("----------Error Synthaxe----------");
            System.out.println(ex.getMessage()) ;
            System.out.println("----------------------------------");
            
        }
        
    }
    //243
    public static void fillToJTable(String tableNameOrSelectStatement, JTable table ){
        
        try{
            setConnection() ;
            Statement stmt = con.createStatement() ;
            ResultSet rs ;
            String SPart = tableNameOrSelectStatement.substring(0, 7) ;
            if ("select ".equals(SPart)){
                rs = stmt.executeQuery(tableNameOrSelectStatement) 
                        ;}
            else {
                rs = stmt.executeQuery("select * from " +tableNameOrSelectStatement) 
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
            con.close() ;
        }
        catch(SQLException Ex){
            System.out.println("----------Error Synthaxe----------");
            System.out.println(Ex.getMessage()) ;
            System.out.println("----------------------------------");
        }
    }
        
    
   
}
