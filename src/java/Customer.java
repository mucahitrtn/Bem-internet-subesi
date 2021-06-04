
import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.inject.Named;

@ManagedBean(name = "customer")
@SessionScoped

public class Customer implements Serializable {
    
    private String gidentckno;
    private String gidenpara;
    private String gidenhesap;
    private String borcodeme;
    private double gidentcknoD;
    private double gidenparaD;
    private double gidenhesapD;
    private double borcodemeD;
    
    private String tckno;
    private String name;
    private String lastname;
    private String password;
    private String passwordControl;
    private String security;
    private String birthDate;

    private PreparedStatement pstatement = null;
    private ResultSet rs = null;

    private Payments payments = null;
    private String borc;

    private Credit credit = null;
    private String creditDebt;
    private String creditAmount;
    private String creditRate;
    private String creditMonth;

    private Deposit deposit = null;
    private String depositAmount;
    private String depositDate;
    private String depositMonth;
    private String depositRate;
    private String depositProfit;

    private Accounts accounts = null;
    private String accountAmount;

    public Customer() {

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
            System.out.println("Error Code Customer: " + e.getErrorCode());
        }

        setPayments(new Payments(tckno));
        payments.createAccountPayments();

        setCredit(new Credit(tckno));
        credit.createAccountCredit();

        setDeposit(new Deposit(tckno));
        deposit.createAccountDeposit();

        setAccounts(new Accounts(tckno));
        accounts.createAccountAccount();

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
            this.borc = getPayments().paymentsCall();
            customerInformation();

            setCredit(new Credit(tckno));
            credit.creditInfo();
            this.creditDebt = getCredit().getCreditString();
            this.creditAmount = getCredit().getCreditAmountS();
            this.creditRate = getCredit().getRatestring();
            this.creditMonth = getCredit().getMonthS();

            setDeposit(new Deposit(tckno));
            deposit.depositInfo();
            this.depositAmount = getDeposit().getDepositS();
            this.depositDate = getDeposit().getDepositDate();
            this.depositMonth = getDeposit().getDepositMonthS();
            this.depositProfit = getDeposit().getDepositProfitS();
            this.depositRate = getDeposit().getDepositRateS();

            setAccounts(new Accounts(tckno));
            this.accountAmount = accounts.accountAmountInfo();

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
            rs = pstatement.executeQuery();
            System.out.println("Burada");
            if (rs.next()) {
                this.name = rs.getString("ISIM");
                this.lastname = rs.getString("SOYISIM");
                this.password = rs.getString("PASSWORD");
                this.birthDate = rs.getString("DOGUMTARIHI");
                this.security = rs.getString("GUVENLIK");
                System.out.println("Burada");
                System.out.println(tckno);
                System.out.println(name);
                System.out.println(lastname);
                System.out.println(birthDate);
                System.out.println(security);

            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        }
    }
    
    public void paybill() {
        try {
            double accountAmountD;
            double borcD;
            double asgari;
            borcD = Double.parseDouble(borc);
            accountAmountD = Double.parseDouble(accountAmount);
            asgari = (borcD * 20)/100;
            
            if(borcodemeD >= asgari){
                accountAmountD = accountAmountD - borcodemeD;
                accountAmount = String.valueOf(accountAmountD);
                borcD = borcD - borcodemeD;
                borc = String.valueOf(borcD);
                
                Connection con = DbHelper.connectDb();
                pstatement = con.prepareStatement("UPDATE ODEMELER SET BORC=? WHERE TCKIMLIKNUMARASI=? ");
                pstatement.setDouble(1, borcD);
                pstatement.setString(2, tckno);
                pstatement.executeUpdate();
                
                pstatement = con.prepareStatement("UPDATE HESAP SET BAKIYE=? WHERE TCKIMLIKNUMARASI=? ");
                pstatement.setDouble(1, accountAmountD);
                pstatement.setString(2, tckno);
                pstatement.executeUpdate();
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());
        }
            
    }

    public void moneytransfer() {
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("select BAKIYE from HESAP where TCKIMLIKNUMARASI=? ");
            pstatement.setString(1, gidentckno);
            rs = pstatement.executeQuery();

            if (rs.next()) {
                this.gidenhesapD = rs.getFloat("BAKIYE");
                gidenhesap = String.valueOf(gidenhesapD);
                System.out.print("ACCOUNT icinde " + gidenhesapD);
            }
            double tempbakiye;
            tempbakiye = Double.parseDouble(accountAmount);

            if (tempbakiye >= gidenparaD) {
                gidenhesapD = gidenhesapD + gidenparaD;
                tempbakiye = tempbakiye - gidenparaD;
                gidenhesap = String.valueOf(gidenhesapD);
                accountAmount = String.valueOf(tempbakiye);

                pstatement = con.prepareStatement("UPDATE HESAP SET BAKIYE=? WHERE TCKIMLIKNUMARASI=? ");
                pstatement.setDouble(1, gidenhesapD);
                pstatement.setString(2, gidentckno);
                pstatement.executeUpdate();

                pstatement = con.prepareStatement("UPDATE HESAP SET BAKIYE=? WHERE TCKIMLIKNUMARASI=? ");
                pstatement.setDouble(1, tempbakiye);
                pstatement.setString(2, tckno);
                pstatement.executeUpdate();
                
                
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        }
    }

    public String depositDirected() {
        setDeposit(new Deposit(tckno));
        return "deposit";
    }

    public void calculate() {
        setDeposit(new Deposit(tckno));
        this.profit = deposit.calculateDeposit(depositAmountS, totalDayS);
    }
    
   
    
    public String depositDatabaseCon() {
        double tempDepositAmountD = Double.parseDouble(depositAmountS);
            double tempbakiye;
            tempbakiye = Double.parseDouble(accountAmount);

        if (tempbakiye >= tempDepositAmountD) {
            setDeposit(new Deposit(tckno));
            deposit.depositToDatabase(depositAmountS, totalDayS);
            
            deposit.depositInfo();
            this.depositAmount = getDeposit().getDepositS();
            this.depositDate = getDeposit().getDepositDate();
            this.depositMonth = getDeposit().getDepositMonthS();
            this.depositProfit = getDeposit().getDepositProfitS();
            this.depositRate = getDeposit().getDepositRateS();
            
            try{
                Connection con = DbHelper.connectDb();
                pstatement = con.prepareStatement("UPDATE HESAP SET BAKIYE=BAKIYE-? WHERE TCKIMLIKNUMARASI=? ");
                pstatement.setDouble(1, tempDepositAmountD);
                pstatement.setString(2, tckno);
                pstatement.executeUpdate();
                setAccounts(new Accounts(tckno));
            this.accountAmount = accounts.accountAmountInfo();
            
            }
            catch(SQLException e){
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("Error Code: " + e.getErrorCode());
            }
            
            
        } else {
            return "mainPage";// BAKİYE YETERSİZ UYARISI VERİLMELİ
        }
        return "deposit";
    }

    private String totalDayS;
    private int totalDayI;
    private String depositAmountS;
    private double depositAmountD;
    private double profit;

    public String getTotalDayS() {
        return totalDayS;
    }

    public void setTotalDayS(String totalDayS) {
        this.totalDayS = totalDayS;
    }

    public int getTotalDayI() {
        return totalDayI;
    }

    public void setTotalDayI(int totalDayI) {
        this.totalDayI = totalDayI;
    }

    public String getDepositAmountS() {
        return depositAmountS;
    }

    public void setDepositAmountS(String depositAmountS) {
        this.depositAmountS = depositAmountS;
    }

    public double getDepositAmountD() {
        return depositAmountD;
    }

    public void setDepositAmountD(double depositAmountD) {
        this.depositAmountD = depositAmountD;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
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

    public String getSecurity() {
        return security;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

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

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getDepositMonth() {
        return depositMonth;
    }

    public void setDepositMonth(String depositMonth) {
        this.depositMonth = depositMonth;
    }

    public String getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(String depositRate) {
        this.depositRate = depositRate;
    }

    public String getDepositProfit() {
        return depositProfit;
    }

    public void setDepositProfit(String depositProfit) {
        this.depositProfit = depositProfit;
    }

    public String getGidentckno() {
        return gidentckno;
    }

    public void setGidentckno(String gidentckno) {
        this.gidentckno = gidentckno;
    }

    public String getGidenpara() {
        return gidenpara;
    }

    public void setGidenpara(String gidenpara) {
        this.gidenpara = gidenpara;
    }

    public String getGidenhesap() {
        return gidenhesap;
    }

    public void setGidenhesap(String gidenhesap) {
        this.gidenhesap = gidenhesap;
    }

    public double getGidentcknoD() {
        return gidentcknoD;
    }

    public void setGidentcknoD(double gidentcknoD) {
        this.gidentcknoD = gidentcknoD;
    }

    public double getGidenparaD() {
        return gidenparaD;
    }

    public void setGidenparaD(double gidenparaD) {
        this.gidenparaD = gidenparaD;
    }

    public double getGidenhesapD() {
        return gidenhesapD;
    }

    public void setGidenhesapD(double gidenhesapD) {
        this.gidenhesapD = gidenhesapD;
    }
    
    public String getBorcodeme() {
        return borcodeme;
    }

    public void setBorcodeme(String borcodeme) {
        this.borcodeme = borcodeme;
    }

    public double getBorcodemeD() {
        return borcodemeD;
    }

    public void setBorcodemeD(double borcodemeD) {
        this.borcodemeD = borcodemeD;
    }
}
