
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;



@ManagedBean(name = "p")
@RequestScoped

public class Payments implements Serializable {

    private String tckno;
    private PreparedStatement pstatement = null;
    private ResultSet rs = null;

    public String getSborc() {
        return sborc;
    }

    public void setSborc(String sborc) {
        this.sborc = sborc;
    }
    private float fborc;
    private String sborc;

    public float getBorc() {
        return fborc;
    }

    public void setBorc(float borc) {
        this.fborc = borc;
    }

    

    public Payments(String tckno) {
        this.tckno = tckno;
        
    }
    public Payments() {
    }
      public void createAccountPayments(){
         try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("insert into ODEMELER (TCKIMLIKNUMARASI, ISIM, BORC) values (?,?,?)");
            pstatement.setString(1, tckno);
            pstatement.setString(2, "Odeme Bulunmamaktadır.");
            pstatement.setDouble(3, 0);
            pstatement.executeUpdate();
        } catch (SQLException exc) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, exc);
            System.out.println("Error Code Odemeler: " + exc.getErrorCode());
        }
    }
    
    public String paymentsCall(){
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("select BORC from ODEMELER where TCKIMLIKNUMARASI=? ");
            pstatement.setString(1, tckno);
            rs = pstatement.executeQuery();

            if (rs.next()) {
                this.fborc = rs.getFloat("BORC");
                sborc= String.valueOf(fborc);
                System.out.print("Paymentsin icinde "+fborc);
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        } 
        return sborc;
    }
}
