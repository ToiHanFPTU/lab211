package core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private int orderID;
    private String customerCode;
    private String codeSetMenu;
    private int numberOfTable;
    private Date eventDate;
    private double totalCost;

    public Order(int orderID, String customerCode, String codeSetMenu, int numberOfTable, Date eventDate, double totalCost) {
        this.orderID = orderID;
        this.customerCode = customerCode;
        this.codeSetMenu = codeSetMenu;
        this.numberOfTable = numberOfTable;
        this.eventDate = eventDate;
        this.totalCost = totalCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCodeSetMenu() {
        return codeSetMenu;
    }

    public void setCodeSetMenu(String codeSetMenu) {
        this.codeSetMenu = codeSetMenu;
    }

    public int getNumberOfTable() {
        return numberOfTable;
    }

    public void setNumberOfTable(int numberOfTable) {
        this.numberOfTable = numberOfTable;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    public String toString() {
        return orderID + "," + customerCode + "," + codeSetMenu + "," + numberOfTable + "," + simpleDateFormat.format(eventDate) + "," + totalCost;
    }
}
