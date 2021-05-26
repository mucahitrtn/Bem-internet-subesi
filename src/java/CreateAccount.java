
import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name="createaccount")
@SessionScoped

public class CreateAccount implements Serializable{
    
    private String tckno;
    private String name;
    private String lastname;
    private String password;
    private String passwordControl;
    private String birthDate;
    private String securityObject;
    PreparedStatement pstatement= null;
    ResultSet rs= null;
    
    
     public String createAccount() {
        try {
            Connection con = DbHelper.connectDb();       
            pstatement = con.prepareStatement("insert into CUSTOMER (TCKIMLIKNUMARASI, ISIM,SOYISIM,PASSWORD,DOGUMTARIHI,GUVENLIK) values (?,?,?,?,?,?) ");
            pstatement.setString(1, tckno);
            pstatement.setString(2, name);
            pstatement.setString(3, lastname);
            pstatement.setString(4, password);
            pstatement.setString(5, birthDate);
            pstatement.setString(6, securityObject);
            pstatement.executeUpdate();
            
        }
        catch(SQLException e){
            Logger.getLogger(DataBaseBean.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: "+e.getErrorCode());
            
            //HATA VERIP CREATE ACCOUNT SAYFASINA GERI DONMELU
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseBean.class.getName()).log(Level.SEVERE, null, ex);
            
            //HATA VERIP CREATE ACCOUNT SAYFASINA GERI DONMELU
            
        }
        return "index";
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSecurityObject() {
        return securityObject;
    }

    public void setSecurityObject(String securityObject) {
        this.securityObject = securityObject;
    }
}
