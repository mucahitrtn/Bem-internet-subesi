/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author canis
 */
@ManagedBean(name="admin")
@RequestScoped
public class Admin {

    private String tckno;

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
    private String name;
    private String lastname;
    private String password;
    private PreparedStatement pstatement = null;
    private ResultSet rs = null;
    
    public Admin() {
    
    }
    
    public String authenticateA() throws ClassNotFoundException, SQLException {

        Connection con = DbHelper.connectDb();
        pstatement = con.prepareStatement("select * from ADMIN where TCKNO=? and PASSWORD=?");
        pstatement.setString(1, tckno);
        pstatement.setString(2, password);
        rs = pstatement.executeQuery();

        if (rs.next()) {
            System.out.println("hlelooooeokd");
            return "AdminMainpage";
        }
          
        return "INVALID TCKNO OR PASSWORD";
    }

}
