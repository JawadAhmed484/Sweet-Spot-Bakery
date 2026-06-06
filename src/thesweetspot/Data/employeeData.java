/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thesweetspot.Data;

import thesweetspot.*;
import java.sql.Date;

public class employeeData {
    
    private Integer employeeId;
    private String em_username;
    private Integer itemsSold;
    private Integer customersHandled;
    private Double todaySales;
    private Date todayDate;
    
    public employeeData(Integer employeeId, String emUsername , Integer itemsSold, 
                       Integer customersHandled, Double todaySales, Date todayDate) {
        this.employeeId = employeeId;
        this.em_username = emUsername;
        this.itemsSold = itemsSold;
        this.customersHandled = customersHandled;
        this.todaySales = todaySales;
        this.todayDate = todayDate;
    }
    
    public Integer getEmployeeId() {
        return employeeId;
    }
    
    public String getEmUsername() {
        return em_username;
    }
    
    public Integer getItemsSold() {
        return itemsSold;
    }
    
    public Integer getCustomersHandled() {
        return customersHandled;
    }
    
    public Double getTodaySales() {
        return todaySales;
    }
    
    public Date getTodayDate() {
        return todayDate;
    }
}
