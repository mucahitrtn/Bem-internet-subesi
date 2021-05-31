
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "credit")
@RequestScoped
public class Credit {

    private String tckno;
    private PreparedStatement pstatement = null;
    private ResultSet rs = null;
    private float creditFloat;
    private String creditString;
    private float ratefloat;
    private String ratestring;
    private float creditAmountF;
    private String creditAmountS;
    private int monthF;
    private String monthS;
    public float getRatefloat() {
        return ratefloat;
    }
    public void setRatefloat(float ratefloat) {
        this.ratefloat = ratefloat;
    }
    public String getRatestring() {
        return ratestring;
    }
    public void setRatestring(String ratestring) {
        this.ratestring = ratestring;
    }
    public float getCreditAmountF() {
        return creditAmountF;
    }
    public void setCreditAmountF(float creditAmountF) {
        this.creditAmountF = creditAmountF;
    }
    public String getCreditAmountS() {
        return creditAmountS;
    }
    public void setCreditAmountS(String creditAmountS) {
        this.creditAmountS = creditAmountS;
    }
    public int getMonthF() {
        return monthF;
    }
    public void setMonthF(int monthF) {
        this.monthF = monthF;
    }
    public String getMonthS() {
        return monthS;
    }

    public void setMonthS(String monthS) {
        this.monthS = monthS;
    }
    
    
    public Credit() {

    }
    public Credit(String tckno) {
        this.tckno= tckno;
    }
    
    public void creditInfo(){
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("select BORC,FAIZORAN,KREDITUTARI,AY from KREDI where TCKIMLIKNUMARASI=? ");
            pstatement.setString(1, tckno);
            rs = pstatement.executeQuery();

            if (rs.next()) {
                this.creditFloat = rs.getFloat("BORC");
                creditString= String.valueOf(creditFloat);
                System.out.print("Paymentsin icinde "+creditFloat);
                this.ratefloat = rs.getFloat("FAIZORAN");
                ratestring= String.valueOf(ratefloat);
                System.out.print("Paymentsin icinde "+ratefloat);
                this.creditAmountF = rs.getFloat("KREDITUTARI");
                creditAmountS= String.valueOf(creditAmountF);
                System.out.print("Paymentsin icinde "+creditAmountF);
                this.monthF = rs.getInt("AY");
                monthS= String.valueOf(monthF);
                System.out.print("Paymentsin icinde "+monthF);
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    
    public float getCreditFloat() {
        return creditFloat;
    }

    public void setCreditFloat(float creditFloat) {
        this.creditFloat = creditFloat;
    }

    public String getCreditString() {
        return creditString;
    }

    public void setCreditString(String creditString) {
        this.creditString = creditString;
    }
    
}
