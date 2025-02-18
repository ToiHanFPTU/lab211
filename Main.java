package dispatcher;

import core.FeastMenuList;
import core.TraditionalFeastOrderManager;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String customerFile = "D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\Customer.txt";
        String feastOrderFile = "D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\FeastOrderService.txt";
        String feastMenuList = "D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\FeastMenu.csv";
        File fileCustomer = new File(customerFile);
        File fileFeastOrder = new File(feastOrderFile);
        File fileFeastMenuList = new File(feastMenuList);
        TraditionalFeastOrderManager traditionalFeastOrderManager = new TraditionalFeastOrderManager();
        FeastMenuList feastMenus = new FeastMenuList();
        feastMenus.loadFromFile(feastMenuList);
        traditionalFeastOrderManager.readFromFileCustomer(customerFile);
        traditionalFeastOrderManager.readFromFileFeastMenuOrder(feastOrderFile);
        if (!fileFeastMenuList.exists()) {
            System.out.println("FeastMenu.csv not exist");
            return;
        }
        if (!fileCustomer.exists()) {
            System.out.println("Customer.txt not exist");
            return;
        }
        if (!fileFeastOrder.exists()) {
            System.out.println("FeastOrderService.txt not exist");
            return;
        }
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            try {
                traditionalFeastOrderManager.menu();
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        traditionalFeastOrderManager.registerCustomer();
                        break;
                    case 2:
                        traditionalFeastOrderManager.updateCustomerInfo();
                        break;
                    case 3:
                        traditionalFeastOrderManager.searchCustomerByName();
                        break;
                    case 4:
                        traditionalFeastOrderManager.displayFeastMenu();
                        break;
                    case 5:
                        traditionalFeastOrderManager.placeFeastOrder();
                        break;
                    case 6:
                        traditionalFeastOrderManager.updateOrderInfo();
                        break;
                    case 7:
                        traditionalFeastOrderManager.saveToFile();
                        break;
                    case 8:
                        traditionalFeastOrderManager.displayCustomerAndOrderList();
                        break;
                    case 9:
                        System.out.println("Exit program");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Function not valid");
                }
            } catch (NumberFormatException | ParseException e) {
                System.out.println("Your choice must be integer number");
            }
        } while (choice != 9);
    }
}
