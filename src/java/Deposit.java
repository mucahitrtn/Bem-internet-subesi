/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author canis
 */
@ManagedBean(name = "deposit")
@RequestScoped
public class Deposit implements Serializable {

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
    

    Customer customer = null;

    public Deposit() {
    }

    public Deposit(String tckno) {
        this.tckno = tckno;
    }
    
    
    public void depositInfo() {
        try {
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("select MEVDUATYATIRILANPARA, MEVDUATYATIRMATARIHI, MEVDUATGUN, MEVDUATORAN, MEVDUATKAR from DENEME where TCKIMLIKNUMARASI= ?");
            pstatement.setString(1, tckno);
            rs = pstatement.executeQuery();

            if (rs.next()) {
                this.depositF = rs.getFloat("MEVDUATYATIRILANPARA");
                depositS = String.valueOf(depositF);
                System.out.print("Paymentsin icinde " + depositF);
                this.depositDate = rs.getString("MEVDUATYATIRMATARIHI");
                this.depositMonthF = rs.getFloat("MEVDUATGUN");
                depositMonthS = String.valueOf(depositMonthF);
                System.out.print("Paymentsin icinde " + depositMonthS);
                this.depositRateF = rs.getFloat("MEVDUATORAN");
                depositRateS = String.valueOf(depositRateF);
                System.out.print("Paymentsin icinde " + depositRateS);
                this.depositProfitF = rs.getFloat("MEVDUATKAR");
                depositProfitS = String.valueOf(depositProfitF);
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error Code: " + e.getErrorCode());

        }
    }

    public void createAccountDeposit() {
        try {
            System.out.println("CREATE ACCOUNT CALISIYOR DENEME DENEME");
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("insert into APP.DENEME (TCKIMLIKNUMARASI, MEVDUATYATIRILANPARA, MEVDUATYATIRMATARIHI, MEVDUATGUN, MEVDUATORAN, MEVDUATKAR) values (? , default, ?, default, default, default)  ");
            System.out.println("CREATE ACCOUNT CALISIYOR DENEME DENEME");
            pstatement.setString(1, tckno);
            System.out.println(tckno);
            pstatement.setString(2, "2021-06-08");
            pstatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error Code Mevduat: " + ex.getErrorCode());
        }

    }

    private double profitDeposit;

    public double getProfitDeposit() {
        return profitDeposit;
    }

    public void setProfitDeposit(double profitDeposit) {
        this.profitDeposit = profitDeposit;
    }

    public double calculateDeposit(String depositAmountS, String totalDayS) {

        double depositAmountD = Double.parseDouble(depositAmountS);
        int totalDayI = Integer.parseInt(totalDayS);
        profitDeposit = depositAmountD * 18.25 * totalDayI / 36500;
        profitDeposit = profitDeposit - profitDeposit * 5 / 100;

        return profitDeposit;
    }

    public void depositToDatabase(String depositAmountS, String totalDayS) {

        double depositAmountD = Double.parseDouble(depositAmountS);
        int totalDayI = Integer.parseInt(totalDayS);
        double tempProfit = calculateDeposit(depositAmountS, totalDayS);

        try {
            System.out.println("database ici Deposit: " + depositAmountD);
            System.out.println(totalDayI);
            System.out.println("Profit: " + profitDeposit);
            System.out.println(depositAmountD);
            System.out.println(tckno);
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Connection con = DbHelper.connectDb();
            pstatement = con.prepareStatement("update DENEME SET MEVDUATYATIRILANPARA=?+MEVDUATYATIRILANPARA, MEVDUATYATIRMATARIHI=?, MEVDUATGUN=?+MEVDUATGUN, MEVDUATORAN=18.25, MEVDUATKAR=?+MEVDUATKAR where TCKIMLIKNUMARASI=?");
            pstatement.setString(5, tckno);
            pstatement.setDouble(1, depositAmountD);
            pstatement.setString(2, date.format(formatter));
            pstatement.setDouble(3, totalDayI);
            pstatement.setDouble(4, tempProfit);
            pstatement.executeUpdate();

        } catch (SQLException ex) {
            System.err.println(ex.getErrorCode());
            System.err.println(ex.getMessage());
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
