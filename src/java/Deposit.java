/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
@ManagedBean(name="deposit")
@RequestScoped
public class Deposit {
    private String tckno;
    private PreparedStatement pstatement = null;
    private ResultSet rs = null;
    private String depositS;
    private float depositF;
    
    private String depositDate;
    
    private String depositMonthS;
    private float depositMonthF;
    
    private String depositRateS;
    private float depositRateF;
    
    private String depositProfitS;
    private float depositProfitF;
    
    public Deposit() {
    }
    
    public Deposit(String tckno) {
        this.tckno= tckno;
    }
    
    public void depositInfo(){
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("select YATIRILANPARA,YATIRILMATARIHI,AY,FAIZORAN,NETKAR from MEVDUAT where TCKIMLIKNUMARASI=? ");
            pstatement.setString(1, tckno);
            rs = pstatement.executeQuery();

            if (rs.next()) {
                this.depositF = rs.getFloat("YATIRILANPARA");
                depositS= String.valueOf(depositF);
                System.out.print("Paymentsin icinde "+depositF);
                this.depositDate = rs.getString("YATIRILMATARIHI");                
                this.depositMonthF = rs.getFloat("AY");
                depositMonthS= String.valueOf(depositMonthF);
                System.out.print("Paymentsin icinde "+ depositMonthS);
                this.depositRateF = rs.getFloat("FAIZORAN");
                depositRateS= String.valueOf(depositRateF);
                System.out.print("Paymentsin icinde "+depositRateS);
                this.depositProfitF= rs.getFloat("NETKAR");
                depositProfitS= String.valueOf(depositProfitF);
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    
    
    public String getDepositS() {
        return depositS;
    }

    public void setDepositS(String depositS) {
        this.depositS = depositS;
    }

    public float getDepositF() {
        return depositF;
    }

    public void setDepositF(float depositF) {
        this.depositF = depositF;
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getDepositMonthS() {
        return depositMonthS;
    }

    public void setDepositMonthS(String depositMonthS) {
        this.depositMonthS = depositMonthS;
    }

    public float getDepositMonthF() {
        return depositMonthF;
    }

    public void setDepositMonthF(float depositMonthF) {
        this.depositMonthF = depositMonthF;
    }

    public String getDepositRateS() {
        return depositRateS;
    }

    public void setDepositRateS(String depositRateS) {
        this.depositRateS = depositRateS;
    }

    public float getDepositRateF() {
        return depositRateF;
    }

    public void setDepositRateF(float depositRateF) {
        this.depositRateF = depositRateF;
    }

    public String getDepositProfitS() {
        return depositProfitS;
    }

    public void setDepositProfitS(String depositProfitS) {
        this.depositProfitS = depositProfitS;
    }

    public float getDepositProfitF() {
        return depositProfitF;
    }

    public void setDepositProfitF(float depositProfitF) {
        this.depositProfitF = depositProfitF;
    }
    
}
