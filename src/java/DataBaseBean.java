import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;


@ManagedBean (name="databasebean")
@SessionScoped

public class DataBaseBean implements Serializable{
    private String tckno;
    private String name;
    private String lastname;
    private String password;
    private String passwordControl;
    PreparedStatement pstatement= null;
    ResultSet rs= null;
    public DataBaseBean() {
        
    }
    
    public String authenticate() throws ClassNotFoundException, SQLException{
        
       
        Connection con= DbHelper.connectDb();
        pstatement=con.prepareStatement("select * from APP.CUSTOMER where TCKIMLIKNUMARASI=? and PASSWORD=?");
        pstatement.setString(1, tckno);
        pstatement.setString(2, password);
        rs= pstatement.executeQuery();
        
        if(rs.next()){
            return "mainPage";
        }
        // UYARI VERIP INDEX SAYFASINA GERI DONMELI   
        return "INVALID TCKNO OR PASSWORD";
    }
    
    public String changePasword() {
        if (passwordControl.equals(password)) {
            try {
                Connection con = DbHelper.connectDb();
                pstatement = con.prepareStatement("UPDATE CUSTOMER SET PASSWORD=? WHERE TCKIMLIKNUMARASI=? ");
                pstatement.setString(1, password);
                pstatement.setString(2, tckno);
                pstatement.executeUpdate();

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DataBaseBean.class.getName()).log(Level.SEVERE, null, ex);

            } catch (SQLException ex) {
                Logger.getLogger(DataBaseBean.class.getName()).log(Level.SEVERE, null, ex);

            }
            return "index";
        }
        else{
            
                //ŞIFRELER UYUŞMUYOR HATASI VERMELI
            
                return "ForgotPassword";
        }

    }

    
    public String getTckno() {
        return tckno;
    }
    public void setTckno(String tckno) {
        this.tckno = tckno;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordControl() {
        return passwordControl;
    }
    public void setPasswordControl(String passwordControl) {
        this.passwordControl = passwordControl;
    }
    
    
    
}
