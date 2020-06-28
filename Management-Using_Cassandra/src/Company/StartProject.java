
package Company;

import Forms.formLogin;
import Forms.frmDepartement;

public class StartProject {

    public static void main(String[] args) {
        formLogin frm = new formLogin() ;
        frm.setVisible(true);
        
        // Insert User To Database 
        /*
        boolean insert = db.go.runNonQuery("INSERT INTO departement  VALUES (1,'Admin','123')") ;
        if (insert){System.out.println("true")  ;} 
        else{System.out.println("false")  ;}
        */
    
    }
    
}
