
package Entity;

import javax.swing.JTable;
import Mycommon.Table ;

public class Departement implements mainData {
    private int DeptNo ;
    private String DeptName ;
    private String Location ;

    public int getDeptNo() {
        return DeptNo;
    }

    public void setDeptNo(int DeptNo) {
        this.DeptNo = DeptNo;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    @Override
    public void add() {
        
        String insert = " insert into departement values (" + DeptNo +",'" + DeptName + "','" + Location + "')" ;
        boolean isAdd = db.go.runNonQuery(insert) ;
        if ( isAdd ){Mycommon.Tools.msgBox("Employe Is Added");}
        
    }

    @Override
    public void update() {
        
        String update = " update departement set deptname ='" + DeptName + "',Location = '" + Location + "' where deptno = "+ DeptNo ;
        boolean isUpdate = db.go.runNonQuery(update) ;
        if ( isUpdate ){Mycommon.Tools.msgBox("Employe Is Updated");}
        
        
    }

    @Override
    public void delete() {

        String delete = " Delete from departement where deptno = '"+ DeptNo +"' " ;
        boolean isDelete = db.go.runNonQuery(delete) ;
        if ( isDelete ){Mycommon.Tools.msgBox("Employe Is Deleted");}
        
            
    }

    @Override
    public String getAutoNumber() {
        
        return db.go.getAutoNumber("Departement" , "deptno") ;
        
    }

    @Override
    public void getAllrows(JTable table) {

        db.go.fillToJTable("departement", table);
        
    }

    @Override
    public void getOneRow(JTable table) {

        String strSelect = "select * from departement where deptno =" + DeptNo ;
        db.go.fillToJTable(strSelect, table);

    }
    
    public void getOneRow_By_Name(JTable table) {

        String strSelect = "select * from departement where deptname ='" + DeptName+"'" ;
        db.go.fillToJTable(strSelect, table);

    }
        
    public void getOneRow_By_Loc(JTable table) {

        String strSelect = "select * from departement where location ='" + Location +"'";
        db.go.fillToJTable(strSelect, table);

    }

    @Override
    public void getCustomRows(String statement, JTable table) {
        
        db.go.fillToJTable(statement, table);
        
    }

    @Override
    public String getValueByName(String name) {
        
        String strSelect = "select deptno from departement where deptname ='" + DeptName +"'" ; 
        String strVal = (String)db.go.getTableData(strSelect).Items[0][0] ;
        return strVal  ;
    }

    @Override
    public String getNameByValue(String Value) {

        String strSelect = "select deptname from departement where deptno ='" + DeptNo +"'" ; 
        String strName = (String)db.go.getTableData(strSelect).Items[0][0] ;
        return strName ;

    }
    
    
}
