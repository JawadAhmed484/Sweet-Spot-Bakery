/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thesweetspot.Data;

import thesweetspot.*;
import java.sql.Date;

/**
 *
 * @author WINDOWS 10
 */
public class customersData {

    private int id;
    private int customer_ID;
    private double total;
    private Date date;
    private String em_username;

    public customersData(int id, int customerID, double total, Date date, String emUsername) {
        this.id = id;
        this.customer_ID = customerID;
        this.total = total;
        this.date = date;
        this.em_username = emUsername;
    }

    // Make sure you have these getter methods for PropertyValueFactory
    public int getId() {
        return id;
    }

    public int getCustomerID() {
        return customer_ID;
    }

    public double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public String getEmUsername() {
        return em_username;
    }
}
