/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author canis
 */
@ManagedBean(name = "admin")
@RequestScoped
public class Admin implements Serializable{

    private String tckno;

    private String name;
    private String lastname;
    private String password;
    private PreparedStatement pstatement = null;
    private ResultSet rs = null;
    private double maxDeposit;
    private String tcknoDb;
    private double mevduatdb;

    private double borcDb;

    public Admin() {

    }

    public void maxDepositCustomer() {
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("SELECT CUSTOMER.TCKIMLIKNUMARASI, DENEME.MEVDUATYATIRILANPARA, KREDI.KREDIBORC \n" +
"FROM  ((CUSTOMER INNER JOIN KREDI ON CUSTOMER.TCKIMLIKNUMARASI= KREDI.TCKIMLIKNUMARASI)\n" +
"        INNER JOIN DENEME ON CUSTOMER.TCKIMLIKNUMARASI=DENEME.TCKIMLIKNUMARASI) ORDER BY MEVDUATYATIRILANPARA DESC OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY"
                   );
            rs = pstatement.executeQuery();
            System.out.println("1. BOLGE");
            if (rs.next()) {
                System.out.println("Hata");
                this.tcknoDb = rs.getString("TCKIMLIKNUMARASI");
                this.mevduatdb = rs.getDouble("MEVDUATYATIRILANPARA");
                this.borcDb = rs.getDouble("KREDIBORC");
                System.out.println("2.BOLGE");
            }
            else
                System.out.println("Hata");
        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code Admin: " + e.getErrorCode());
        }
    }

    public String authenticateA() throws ClassNotFoundException, SQLException {

        Connection con = DbHelper.connectDb();
        pstatement = con.prepareStatement("select * from ADMIN where TCKNO=? and PASSWORD=?");
        pstatement.setString(1, tckno);
        pstatement.setString(2, password);
        rs = pstatement.executeQuery();

        if (rs.next()) {
            System.out.println("BURADA ADMİIN");
            maxDepositCustomer();
            return "AdminMainpage";
        }
        else{
            return "index";
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

    public double getMaxDeposit() {
        return maxDeposit;
    }

    public void setMaxDeposit(double maxDeposit) {
        this.maxDeposit = maxDeposit;
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

    public String getTcknoDb() {
        return tcknoDb;
    }

    public void setTcknoDb(String tcknoDb) {
        this.tcknoDb = tcknoDb;
    }

    public double getMevduatdb() {
        return mevduatdb;
    }

    public void setMevduatdb(double mevduatdb) {
        this.mevduatdb = mevduatdb;
    }

    public double getBorcDb() {
        return borcDb;
    }

    public void setBorcDb(double borcDb) {
        this.borcDb = borcDb;
    }
}
