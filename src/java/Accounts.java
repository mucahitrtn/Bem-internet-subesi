
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author canis
 */
@ManagedBean
@RequestScoped

public class Accounts implements Serializable{

    private String tckno;
    private PreparedStatement pstatement = null;
    private ResultSet rs = null;
    
    private double accountAmountF;

    private String accountAmountS;
    
    public Accounts() {
    }
    
    public Accounts(String tckno) {
        this.tckno= tckno;
    }
    
    public void createAccountAccount(){
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("insert into HESAP (TCKIMLIKNUMARASI, BAKIYE) values (?, ?)");
            pstatement.setString(1, tckno);
            pstatement.setDouble(2, 0.0);
            pstatement.executeUpdate();
        } catch (SQLException exc) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, exc);
            System.out.println("Error Code Odemeler: " + exc.getErrorCode());
        }
    }
    
    public String accountAmountInfo(){
          try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("select BAKIYE from HESAP where TCKIMLIKNUMARASI=? ");
            pstatement.setString(1, tckno);
            rs = pstatement.executeQuery();

            if (rs.next()) {
                this.accountAmountF = rs.getFloat("BAKIYE");
                accountAmountS= String.valueOf(accountAmountF);
                System.out.print("ACCOUNT icinde "+ accountAmountF);
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        } 
        return accountAmountS;
    }
    
    public double getAccountAmountF() {
        return accountAmountF;
    }

    public void setAccountAmountF(double accountAmountF) {
        this.accountAmountF = accountAmountF;
    }

    public String getAccountAmountS() {
        return accountAmountS;
    }

    public void setAccountAmountS(String accountAmountS) {
        this.accountAmountS = accountAmountS;
    }
}
