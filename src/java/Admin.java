/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class Admin implements Serializable {

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
            pstatement = con.prepareStatement("SELECT CUSTOMER.TCKIMLIKNUMARASI, DENEME.MEVDUATYATIRILANPARA, KREDI.KREDIBORC \n"
                    + "FROM  ((CUSTOMER INNER JOIN KREDI ON CUSTOMER.TCKIMLIKNUMARASI= KREDI.TCKIMLIKNUMARASI)\n"
                    + "        INNER JOIN DENEME ON CUSTOMER.TCKIMLIKNUMARASI=DENEME.TCKIMLIKNUMARASI) ORDER BY MEVDUATYATIRILANPARA DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY"
            );
            rs = pstatement.executeQuery();
            System.out.println("1. BOLGE");
            if (rs.next()) {
                System.out.println("Hata");
                this.tcknoDb = rs.getString("TCKIMLIKNUMARASI");
                this.mevduatdb = rs.getDouble("MEVDUATYATIRILANPARA");
                this.borcDb = rs.getDouble("KREDIBORC");
                System.out.println("2.BOLGE");
            } else {
                System.out.println("Hata");
            }
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
            sum();
            maxDepositCustomerwithNoCredit();
            return "AdminMainpage";
        } else {
            return "adminLogin";

        }
    }

    public ArrayList<Customer> getcustomerTable() {
        ArrayList<Customer> tmp = new ArrayList<>();
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("SELECT CUSTOMER.TCKIMLIKNUMARASI, CUSTOMER.ISIM, CUSTOMER.SOYISIM, DENEME.MEVDUATYATIRILANPARA, KREDI.KREDIBORC, HESAP.BAKIYE, ODEMELER.BORC\n"
                    + "FROM ((((CUSTOMER INNER JOIN KREDI ON CUSTOMER.TCKIMLIKNUMARASI= KREDI.TCKIMLIKNUMARASI)\n"
                    + "        INNER JOIN DENEME ON CUSTOMER.TCKIMLIKNUMARASI= DENEME.TCKIMLIKNUMARASI)\n"
                    + "        INNER JOIN HESAP ON CUSTOMER.TCKIMLIKNUMARASI= HESAP.TCKIMLIKNUMARASI)\n"
                    + "        INNER JOIN ODEMELER ON CUSTOMER.TCKIMLIKNUMARASI= ODEMELER.TCKIMLIKNUMARASI) ORDER BY BAKIYE DESC");
            rs = pstatement.executeQuery();
            System.out.println("1. BOLGE");
            while (rs.next()) {
                Customer c = new Customer(rs.getString("TCKIMLIKNUMARASI"), rs.getString("ISIM"), rs.getString("SOYISIM"),
                        rs.getDouble("MEVDUATYATIRILANPARA"), rs.getDouble("KREDIBORC"), rs.getDouble("BAKIYE"), rs.getDouble("BORC"));
                tmp.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error Code Admin: " + e.getErrorCode());
        }

        return tmp;
    }

    private String nameInput;

    public String getNameInput() {
        return nameInput;
    }

    public void setNameInput(String nameInput) {
        this.nameInput = nameInput;
    }

    public ArrayList<Customer> gettableSearch() {
        ArrayList<Customer> tmp1 = new ArrayList<>();
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("SELECT CUSTOMER.TCKIMLIKNUMARASI, CUSTOMER.ISIM, CUSTOMER.SOYISIM, KREDI.KREDIBORC, HESAP.BAKIYE \n"
                    + "    FROM ((CUSTOMER INNER JOIN KREDI ON CUSTOMER.TCKIMLIKNUMARASI= KREDI.TCKIMLIKNUMARASI)\n"
                    + "            INNER JOIN HESAP ON CUSTOMER.TCKIMLIKNUMARASI= HESAP.TCKIMLIKNUMARASI) WHERE KREDIBORC=0.0 ORDER BY BAKIYE DESC ");

            rs = pstatement.executeQuery();
            System.out.println("5. BOLGE");
            while (rs.next()) {
                Customer ce = new Customer(rs.getString("TCKIMLIKNUMARASI"), rs.getString("ISIM"), rs.getString("SOYISIM"),
                        rs.getDouble("KREDIBORC"), rs.getDouble("BAKIYE"));
                tmp1.add(ce);
            }

        } catch (SQLException e) {
            System.out.println("Error Code Admin: " + e.getErrorCode());
        }

        return tmp1;
    }

    public String getTckno() {
        return tckno;
    }

    private double sumcrdt;
    private double sumdpst;

    public double getSumcrdt() {
        return sumcrdt;
    }

    public void setSumcrdt(double sumcrdt) {
        this.sumcrdt = sumcrdt;
    }

    public double getSumdpst() {
        return sumdpst;
    }

    public void setSumdpst(double sumdpst) {
        this.sumdpst = sumdpst;
    }

    public double getSumamnt() {
        return sumamnt;
    }

    public void setSumamnt(double sumamnt) {
        this.sumamnt = sumamnt;
    }
    private double sumamnt;
//     + "SELECT SUM(DENEME.MEVDUATYATIRILANPARA) FROM DENEME WHERE DENEME.MEVDUATYATIRILANPARA!=0;\n"
//                    + "SELECT SUM(HESAP.BAKIYE) FROM HESAP WHERE HESAP.BAKIYE!=0;"
//     this.sumdpst = rs.getDouble("SUM(MEVDUATYATIRILANPARA)");
//                this.sumamnt = rs.getDouble("SUM(BAKIYE)");
//                System.out.println("2.BOLGE");

    public void sum() {
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("SELECT SUM(KREDI.KREDIBORC) as kredib FROM KREDI WHERE KREDI.KREDIBORC!=0");
            rs = pstatement.executeQuery();
            System.out.println("1. BOLGE");
            if (rs.next()) {
                this.sumcrdt = rs.getDouble("kredib");
            }

            pstatement = con.prepareStatement("SELECT SUM(DENEME.MEVDUATYATIRILANPARA) as amountd FROM DENEME WHERE DENEME.MEVDUATYATIRILANPARA!=0");
            rs = pstatement.executeQuery();
            System.out.println("1. BOLGE");
            if (rs.next()) {
                this.sumdpst = rs.getDouble("amountd");
            }

            pstatement = con.prepareStatement("SELECT SUM(HESAP.BAKIYE) as amntb FROM HESAP WHERE HESAP.BAKIYE!=0");
            rs = pstatement.executeQuery();
            System.out.println("1. BOLGE");
            if (rs.next()) {
                this.sumamnt = rs.getDouble("amntb");
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code Admin: " + e.getErrorCode());
        }
    }
    
    private String tck;
    private String ism;
    private String sism;
    private double kredibmv;
    private double mevd;
    
    public void maxDepositCustomerwithNoCredit() {
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("SELECT CUSTOMER.TCKIMLIKNUMARASI, CUSTOMER.ISIM, CUSTOMER.SOYISIM, KREDI.KREDIBORC, HESAP.BAKIYE \n"
                    + "    FROM ((CUSTOMER INNER JOIN KREDI ON CUSTOMER.TCKIMLIKNUMARASI= KREDI.TCKIMLIKNUMARASI)\n"
                    + "            INNER JOIN HESAP ON CUSTOMER.TCKIMLIKNUMARASI= HESAP.TCKIMLIKNUMARASI) WHERE KREDIBORC=0.0 ORDER BY BAKIYE DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY"
            );
            rs = pstatement.executeQuery();
            System.out.println("1. BOLGE");
            if (rs.next()) {
                System.out.println("Hata");
                this.tck = rs.getString("TCKIMLIKNUMARASI");
                this.ism = rs.getString("ISIM");
                this.sism = rs.getString("SOYISIM");
                this.kredibmv = rs.getDouble("KREDIBORC");
                this.mevd = rs.getDouble("BAKIYE");
                System.out.println("2.BOLGE");
            } else {
                System.out.println("Hata");
            }
        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code Admin: " + e.getErrorCode());
        }
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
    public String getTck() {
        return tck;
    }

    public void setTck(String tck) {
        this.tck = tck;
    }

    public String getIsm() {
        return ism;
    }

    public void setIsm(String ism) {
        this.ism = ism;
    }

    public String getSism() {
        return sism;
    }

    public void setSism(String sism) {
        this.sism = sism;
    }

    public double getKredibmv() {
        return kredibmv;
    }

    public void setKredibmv(double kredibmv) {
        this.kredibmv = kredibmv;
    }

    public double getMevd() {
        return mevd;
    }

    public void setMevd(double mevd) {
        this.mevd = mevd;
    }
}
