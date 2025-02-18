package core;

import java.io.IOException;
import java.text.ParseException;

public interface Function {
    void registerCustomer();
    void updateCustomerInfo();
    void searchCustomerByName() throws IOException;
    void displayFeastMenu() throws IOException;
    void placeFeastOrder() throws IOException;
    void updateOrderInfo() throws ParseException;
    void saveToFile();
    void displayCustomerAndOrderList();
    void menu();
}