
import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@ManagedBean(name = "customer")
@SessionScoped

public class Customer  implements Serializable {

    private String tckno;
    private String name;
    private String lastname;
    private String password;
    private String passwordControl;
    private String security;
    private String birthDate;
    
    
    PreparedStatement pstatement = null;
    ResultSet rs = null;
    
    
    private Payments payments= null;
    private String borc;

    
    private Credit credit=null;
    private String creditDebt;


    private String creditAmount;
    private String creditRate;
    private String creditMonth;
   
    public Customer() {
        System.out.println("customer cons"+borc);
    }
    
    
    public String createAccount() {
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("insert into CUSTOMER (TCKIMLIKNUMARASI, ISIM,SOYISIM,PASSWORD,DOGUMTARIHI,GUVENLIK) values (?,?,?,?,?,?) ");
            pstatement.setString(1, tckno);
            pstatement.setString(2, name);
            pstatement.setString(3, lastname);
            pstatement.setString(4, password);
            pstatement.setString(5, birthDate);
            pstatement.setString(6, security);
            pstatement.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

            //HATA VERIP CREATE ACCOUNT SAYFASINA GERI DONMELU
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);

            //HATA VERIP CREATE ACCOUNT SAYFASINA GERI DONMELU
        }
        return "index";
    }

    public String authenticate() throws ClassNotFoundException, SQLException {

        Connection con = DbHelper.connectDb();
        pstatement = con.prepareStatement("select * from APP.CUSTOMER where TCKIMLIKNUMARASI=? and PASSWORD=?");
        pstatement.setString(1, tckno);
        pstatement.setString(2, password);
        rs = pstatement.executeQuery();
        
        if (rs.next()) {
            setPayments(new Payments(tckno));
            this.borc= getPayments().paymentsCall();
            System.out.println("CUSTOMERDER "+borc);
            customerInformation();
            setCredit(new Credit(tckno));
            credit.creditInfo();
            this.creditDebt=getCredit().getCreditString();
            this.creditAmount=getCredit().getCreditAmountS();
            this.creditRate= getCredit().getRatestring();
            this.creditMonth= getCredit().getMonthS();
            return "mainPage";
        }
        // UYARI VERIP INDEX SAYFASINA GERI DONMELI   
        return "INVALID TCKNO OR PASSWORD";
    }

    public String changePasword() {
        if (passwordControl.equals(password)) {

            try {
                Connection con = DbHelper.connectDb();
                pstatement = con.prepareStatement("select * from APP.CUSTOMER where TCKIMLIKNUMARASI=? and GUVENLIK=?");
                pstatement.setString(1, tckno);
                pstatement.setString(2, security);
                rs = pstatement.executeQuery();

                if (rs.next()) {
                    System.out.println("Basarili");
                } else // UYARI VERIP INDEX SAYFASINA GERI DONMELI   
                {
                    return "INVALID Security word";
                }

                pstatement = con.prepareStatement("UPDATE CUSTOMER SET PASSWORD=? WHERE TCKIMLIKNUMARASI=? ");
                pstatement.setString(1, password);
                pstatement.setString(2, tckno);
                pstatement.executeUpdate();

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);

            } catch (SQLException ex) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);

            }
            return "index";
        } else {

            //ŞIFRELER UYUŞMUYOR HATASI VERMELI
            return "ForgotPassword";
        }

    }
     public void customerInformation() {
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("select ISIM,SOYISIM,PASSWORD,DOGUMTARIHI,GUVENLIK from CUSTOMER where TCKIMLIKNUMARASI=?   ");            
            pstatement.setString(1, tckno);
            rs= pstatement.executeQuery();
            System.out.println("Burada");
            if(rs.next()){
                this.name=rs.getString("ISIM");
                this.lastname=rs.getString("SOYISIM");
                this.password=rs.getString("PASSWORD");
                this.birthDate=rs.getString("DOGUMTARIHI");
                this.security=rs.getString("GUVENLIK");
                System.out.println("Burada");
                System.out.println(tckno);
                System.out.println(name);
                System.out.println(lastname);
                System.out.println(birthDate);System.out.println(security);
                
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);

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

    /**
     * @return the security
     */
    public String getSecurity() {
        return security;
    }

    /**
     * @param security the security to set
     */
        public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public String getCreditDebt() {
        return creditDebt;
    }

    public void setCreditDebt(String creditDebt) {
        this.creditDebt = creditDebt;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getCreditRate() {
        return creditRate;
    }

    public void setCreditRate(String creditRate) {
        this.creditRate = creditRate;
    }

    public String getCreditMonth() {
        return creditMonth;
    }

    public void setCreditMonth(String creditMonth) {
        this.creditMonth = creditMonth;
    }
    public void setSecurity(String security) {
        this.security = security;
    }

    /**
     * @return the birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return the payments
     */
    public Payments getPayments() {
        return payments;
    }

    /**
     * @param payments the payments to set
     */
    public void setPayments(Payments payments) {
        this.payments = payments;
    }
     public String getBorc() {
        return borc;
    }

    public void setBorc(String borc) {
        this.borc = borc;
    }
}
