package core;

import tool.Inputter;
import tool.Validation;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TraditionalFeastOrderManager implements Function{
    public ArrayList<Customer> customers = new ArrayList<>();
    public Inputter inputter = new Inputter();
    FeastMenuList feastMenuList = new FeastMenuList();
    public Scanner sc = new Scanner(System.in);
    public ArrayList<Order> orders = new ArrayList<>();

    @Override
    public void registerCustomer() {
        String customerCode;
        String name;
        String phoneNumber;
        String email, confirm;
        do {
            //input customer code
            while (true) {
                customerCode = inputter.inputStringAndLoop("Enter customer code: ", "Invalid customer code\nStart with C, K, G and follow by 4 digits", Validation.CUSTOMER_CODE_VALID);
                if (checkDuplicateCustomerID(customerCode)) {
                    System.out.println("Customer code was duplicated");
                    System.out.println("Try another one");
                    continue;
                }
                break;
            }
            //input name;
            while (true) {
                name = inputter.inputString("Enter customer name: ");
                if (name.length() < 2 || name.length() > 25) {
                    System.out.println("Name length must be within 2 to 25 character");
                    continue;
                }
                if (name.trim().isEmpty()) {
                    System.out.println("Name must be non-empty string");
                    continue;
                }
                break;
            }
            //input phone number
            while (true) {
                phoneNumber = inputter.inputStringAndLoop("Enter customer phone number: ", "Phone number must belong to Vietnam network operator", Validation.PHONE_NUMBER_VALID);
                if (phoneNumber.isEmpty()) {
                    System.out.println("Phone number must be non-empty string");
                    continue;
                }
                break;
            }
            //input email
            while (true) {
                email = inputter.inputStringAndLoop("Enter customer email: ", "Email address must follow email standard format(e.g., example@domain.com)", Validation.EMAIL_VALID);
                if (email.isEmpty()) {
                    System.out.println("Email must be non-empty string");
                    continue;
                }
                break;

            }
            customers.add(new Customer(customerCode, name, phoneNumber, email));
            System.out.println("Register new customer successfully");
            confirm = inputter.inputStringAndLoop("Do you want to continue to register new customer(Y/N): ", "Invalid confirm (Y or N)", Validation.YES_NO_VALID);
        } while (confirm.equalsIgnoreCase("y"));

    }

    @Override
    public void updateCustomerInfo() {
        String newName, newPhoneNumber, newEmail, customerID, confirm;
        Customer customerUpdate;
        do {
            customerID = inputter.inputStringAndLoop("Enter customer code to update information: ", "Invalid customer code\nStart with C, K, G and follow by 4 digits", Validation.CUSTOMER_CODE_VALID);
            customerUpdate = findCustomerByCustomerCode(customerID);
            if (customerUpdate != null) {
                System.out.println("If you leave with blank, program will keep current value");
                //update name
                newName = inputter.inputString("Enter new customer name: ");
                if (!newName.isEmpty()) {
                    while (newName.length() < 2 || newName.length() > 25) {
                        System.out.println("Name length must within 2 to 25 character length");
                        System.out.println("Please try again");
                        newName = inputter.inputString("Enter new customer name: ");
                    }
                    customerUpdate.setName(newName);
                }
                //update phone number
                newPhoneNumber = inputter.inputString("Enter new customer phone number: ");
                if (!newPhoneNumber.isEmpty()) {
                    while (true) {
                        if (!newPhoneNumber.matches(Validation.PHONE_NUMBER_VALID)) {
                            System.out.println("Phone number must belong to Vietnam network operator");
                            break;
                        }
                    }
                    customerUpdate.setName(newName);
                }
                //update email
                newEmail = inputter.inputString("Enter new customer email: ");
                if (!newEmail.isEmpty()) {
                    while (true) {
                        if (!newEmail.matches(Validation.EMAIL_VALID)) {
                            System.out.println("Must follow standard email formatting (e.g., example@domain.com).");
                            continue;
                        }
                        break;
                    }
                    customerUpdate.setEmail(newEmail);
                }
                System.out.println("Update customer information successfully");
            }
            else {
                System.out.println("This customer does not exist.");
            }
            confirm = inputter.inputStringAndLoop("You you want to update new customer information(Y/N): ", "Invalid confirm(Y or N)", Validation.YES_NO_VALID);
        } while (confirm.equalsIgnoreCase("y"));
    }

    @Override
    public void searchCustomerByName() {
        if (customers.isEmpty()) {
            System.out.println("Customer list is empty");
            return;
        }
        String name = inputter.inputString("Enter name or a part of name to find customer information: ");
        boolean matchFound = false;
        System.out.println("------------------------------------------------------------------");
        System.out.println("Code    | Customer Name        | Phone      | Email  ");
        System.out.println("------------------------------------------------------------------");
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.printf("%-8s|%-3s,%-17s| %-12s |%s", customer.getCustomerCode(), getLastName(customer.getName()), getNameWithoutLastName(customer.getName()), (customer.getPhoneNumber()), customer.getEmail());
                matchFound = true;
                System.out.println();
            }
        }
        System.out.println("------------------------------------------------------------------");
        if (!matchFound) {
            System.out.println("No one matches the search criteria!");
        }
    }

    @Override
    public void displayFeastMenu() {
        feastMenuList.loadFromFile("D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\FeastMenu.csv");
        feastMenuList.sort(Comparator.comparingDouble(FeastMenu::getPrice));
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("List of Set Menus for ordering party:");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (FeastMenu feastMenu : feastMenuList) {
            System.out.println("Code        :" + feastMenu.getCodeSetMenu());
            System.out.println("Name        :" + feastMenu.getName());
            System.out.println("Price       :" + feastMenu.getPrice());
            System.out.println("Ingredients : \n" + feastMenu.getIngredients().replace("#", "\n").replace("\"", ""));
            System.out.println("--------------------------------------------------------------------------------------------------");
        }
    }

    @Override
    public void placeFeastOrder() throws IOException {
        feastMenuList.loadFromFile("D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\FeastMenu.csv");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String customerCode;
        String codeSetMenu;
        double totalCost;
        int numberOfTable, orderID = 1, orderIDAfterUpdate;
        Date eventDate;
        Customer customerPlaceFeastOrder;
        FeastMenu feastMenuPlaceFeastOrder;
        //update order ID from file
        for (Order order : orders) {
            while (orderID <= order.getOrderID()) {
                orderID++;
            }
        }
        orderIDAfterUpdate = orderID;
        while (true) {
            customerCode = inputter.inputStringAndLoop("Enter customer code: ", "Invalid customer code\nStart with C, K, G and follow by 4 digits", Validation.CUSTOMER_CODE_VALID);
            if (customerCode.isEmpty()) {
                System.out.println("Can not be empty");
                continue;
            }
            break;
        }
        if (checkDuplicateCustomerID(customerCode)) {
            codeSetMenu = inputter.inputStringAndLoop("Enter code of set menu: ", "Invalid code of set menu(PW001-PW006)", Validation.CODE_SET_MENU_VALID);
            numberOfTable = inputter.inputIntegerAndLoop("Enter number of table: ", "Number of table must be greater than 0", 0, 1000);
            while (true) {
                eventDate = inputter.inputDateAndLoop("Enter preferred event date: ", "invalid date format(Day/Month/Year)");
                if (eventDate.before(new Date())) {
                    System.out.println("The preferred event date must be in the future.");
                    continue;
                }
                break;
            }
            feastMenuPlaceFeastOrder = findFeastMenuByCode(codeSetMenu);
            totalCost = numberOfTable * feastMenuPlaceFeastOrder.getPrice();
            orders.add(new Order(orderIDAfterUpdate, customerCode, codeSetMenu, numberOfTable, eventDate, totalCost));
            System.out.println("Place feast order successfully");
            System.out.println("----------------------------------------------------------------");
            System.out.printf("Customer order information [Order ID: %d]\n", orderID);
            System.out.println("----------------------------------------------------------------");
            System.out.println("Code          : " + customerCode);
            customerPlaceFeastOrder = findCustomerByCustomerCode(customerCode);
            System.out.println("Customer name : " + getLastName(customerPlaceFeastOrder.getName()) + ", " + getNameWithoutLastName(customerPlaceFeastOrder.getName()));
            System.out.println("Phone number  : " + customerPlaceFeastOrder.getPhoneNumber());
            System.out.println("Email         : " + customerPlaceFeastOrder.getEmail());
            System.out.println("----------------------------------------------------------------");
            System.out.println("Code of Set Menu: " + codeSetMenu.toUpperCase());
            for (FeastMenu feastMenu : feastMenuList) {
                if (feastMenu.getCodeSetMenu().equalsIgnoreCase(codeSetMenu)) {
                    System.out.println("Set menu name   : " + feastMenuPlaceFeastOrder.getName());
                    System.out.println("Event date      : " + simpleDateFormat.format(eventDate));
                    System.out.println("Number of tables: " + numberOfTable);
                    System.out.println("Price           : "+ feastMenuPlaceFeastOrder.getPrice());
                    System.out.println("Ingredients: \n" + feastMenuPlaceFeastOrder.getIngredients().replace("#", "\n").replace("\"", ""));
                    System.out.println("----------------------------------------------------------------");

                    System.out.printf("Total cost      : %.2f\n", totalCost);
                    break;
                }
            }
            System.out.println("----------------------------------------------------------------");
        }

        else {
            System.out.println("This customer haven't register yet");
        }
    }

    @Override
    public void updateOrderInfo() throws ParseException {
        feastMenuList.loadFromFile("D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\FeastMenu.csv");
        String newCodeSetMenu, confirm;
        int newNumberOfTable, orderID;
        Date newEventDate;
        Order orderUpdate;
        FeastMenu feastMenuUpdate;
        //Enter orderID
        do {
            while (true) {
                try {
                    System.out.print("Enter order ID to update information: ");
                    orderID = Integer.parseInt(sc.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Order ID is integer number");
                }
            }
            //Find order detail
            orderUpdate = findOrderByID(orderID);
            if (orderUpdate == null) {
                System.out.println("This Order does not exist.");
                return;
            }
            //Update code of set menu
            newCodeSetMenu = inputter.inputString("Enter new code of set menu: ");
            if (!newCodeSetMenu.isEmpty() && newCodeSetMenu.matches(Validation.CODE_SET_MENU_VALID)) {
                orderUpdate.setCodeSetMenu(newCodeSetMenu);
            }
            //update new number of table
            String numberString = inputter.inputString("Enter new number of tables: ");
            if (!numberString.isEmpty()) {
                try {
                    newNumberOfTable = Integer.parseInt(numberString);
                    if (newNumberOfTable > 0) orderUpdate.setNumberOfTable(newNumberOfTable);
                    else System.out.println("Number of tables must be greater than 0.");
                } catch (NumberFormatException e) {
                    System.out.println("Number of tables must be an integer.");
                }
            }
            newEventDate = inputter.inputDate("Enter new event date: ");
            if (newEventDate != null && newEventDate.after(new Date())) {
                orderUpdate.setEventDate(newEventDate);
            } else if (newEventDate != null) {
                System.out.println("New event date must in future");
            }
            feastMenuUpdate = findFeastMenuByCode(orderUpdate.getCodeSetMenu());
            if (feastMenuUpdate != null) {
                orderUpdate.setTotalCost(orderUpdate.getNumberOfTable() * feastMenuUpdate.getPrice());
            }
            System.out.println("Update order information successfully");
            confirm = inputter.inputStringAndLoop("Do you want to continue with another update(Y/N): ", "Invalid confirm(Y or N)", Validation.YES_NO_VALID);
        } while (confirm.equalsIgnoreCase("y"));
    }

    @Override
    public void saveToFile() {
        int choice = 0;
        do {
            try {
                System.out.println("+-------Save to file-------+");
                System.out.println("|1. Save customer list     |");
                System.out.println("|2. Save order list        |");
                System.out.println("|3. Stop saving            |");
                System.out.println("+--------------------------+");
                System.out.print("Your choose is: ");
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        saveCustomerListToFile();
                        break;
                    case 2:
                        saveFeastMenuOrderToFile();
                        break;
                    case 3:
                        System.out.println("Stop saving");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Choice must be integer number");
            }
        } while (choice >= 1 && choice <= 2);
    }
    public void saveFeastMenuOrderToFile() {
        if (orders.isEmpty()) {
            System.out.println("Order list is empty");
        }
        else {
            try {
                PrintWriter printWriterFeastOrderList = new PrintWriter("D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\FeastOrderService.txt");
                for (Order order : orders) {
                    printWriterFeastOrderList.println(order.toString());
                }
                System.out.println("Save to file successfully");
                printWriterFeastOrderList.close();
            } catch (IOException e) {
                System.out.println("Can not save to file");
            }
        }
    }
    public void saveCustomerListToFile() {
        if (customers.isEmpty()) {
            System.out.println("Customer list is empty");
        }
        else {
            try {
                try (PrintWriter printWriterCustomerList = new PrintWriter("D:\\BuiGiaHuy\\CN3\\LAB221\\Code\\SuTV\\TraditionalFeastOrderManagement\\src\\data\\Customer.txt")) {
                    for (Customer customer : customers) {
                        printWriterCustomerList.println(customer.toString());
                    }
                    System.out.println("Save to file successfully");
                }
            } catch (IOException e) {
                System.out.println("Can not save to file");
            }
        }
    }
    public void readFromFileCustomer(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        if (!file.exists()) {
            System.out.println("Customer.txt not exist");
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                String customerCode = tokens[0].trim();
                String name = tokens[1].trim();
                String phoneNumber = tokens[2].trim();
                String email = tokens[3].trim();
                Customer customer1 = new Customer(customerCode, name, phoneNumber, email);
                customers.add(customer1);
            }
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }
    }
    public void readFromFileFeastMenuOrder(String fileName) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setLenient(false);
        File feastMenuOrder = new File(fileName);
        FileReader fileReader = new FileReader(feastMenuOrder);
        if (!feastMenuOrder.exists()) {
            System.out.println(feastMenuOrder.getName() + " not exist");
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(fileReader);){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    String[] tokens = line.split(",");
                    if (tokens.length < 6) {
                        System.out.println("Invalid line format: " + line);
                        continue;
                    }
                    int orderID = Integer.parseInt(tokens[0].trim());
                    String customerCode = tokens[1].trim();
                    String codeSetMenu = tokens[2].trim();
                    int numberOfTable = Integer.parseInt(tokens[3].trim());
                    String eventDate = tokens[4].trim();
                    double totalCost = Double.parseDouble(tokens[5].trim());
                    Order order = new Order(orderID, customerCode, codeSetMenu, numberOfTable, simpleDateFormat.parse(eventDate), totalCost);
                    orders.add(order);
                } catch (ParseException e)  {
                    System.out.println("Error to parse date");
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }

    }
    @Override
    public void displayCustomerAndOrderList() {
        int choice = 0;
        do {
            try {
                System.out.println("+---------Display---------+");
                System.out.println("|1. Display customer list |");
                System.out.println("|2. Display order list    |");
                System.out.println("|3. Stop display          |");
                System.out.println("+-------------------------+");
                System.out.print("Your choose is: ");
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        displayCustomerList();
                        break;
                    case 2:
                        displayOrderList();
                        break;
                    case 3:
                        System.out.println("Stop display");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Choice must be integer number");
            }
        } while (choice >= 1 && choice <= 2);
    }

    @Override
    public void menu() {
        System.out.println("+--------------Traditional Feast Order--------------+");
        System.out.println("| 1. Register customers.                            |");
        System.out.println("| 2. Update customer information.                   |");
        System.out.println("| 3. Search for customer information by name.       |");
        System.out.println("| 4. Display feast menus.                           |");
        System.out.println("| 5. Place a feast order.                           |");
        System.out.println("| 6. Update order information.                      |");
        System.out.println("| 7. Save data to file.                             |");
        System.out.println("| 8. Display Customer or Order lists.               |");
        System.out.println("| 9. Quit.                                          |");
        System.out.println("+---------------------------------------------------+");
        System.out.print("Your choice is: ");
    }

    public void displayCustomerList() {

        if (!customers.isEmpty()) {
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("Code    | Customer Name        | Phone      | Email  ");
            System.out.println("-------------------------------------------------------------------------------");
            for (Customer customer : customers) {
                System.out.printf("%-6s|%-5s,%-18s|%-12s|%s", customer.getCustomerCode(), getLastName(customer.getName()), getNameWithoutLastName(customer.getName()), customer.getPhoneNumber(), customer.getEmail());
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------------------------");
        }else {
            System.out.println("No data in system");
        }
    }
    public void displayOrderList() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (!orders.isEmpty()) {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("ID    | Event date |Customer ID| Set Menu| Price    | Tables |       Cost");
            System.out.println("-------------------------------------------------------------------------");
            for (Order order : orders) {
                System.out.printf("%-6s|%-12s|%-11s|%-9s|%8.2f|%8d|%f\n", order.getOrderID(), simpleDateFormat.format(order.getEventDate()), order.getCustomerCode(), order.getCodeSetMenu(), (order.getTotalCost() / order.getNumberOfTable()), order.getNumberOfTable(), order.getTotalCost());
            }
            System.out.println("-------------------------------------------------------------------------");
        }
        else {
            System.out.println("No data in system");
        }
    }
    public boolean checkDuplicateCustomerID(String customerCode) {
        for (Customer customer : customers) {
            if (customer.getCustomerCode().equalsIgnoreCase(customerCode)) {
                return true;
            }
        }
        return false;
    }
    public Customer findCustomerByCustomerCode(String customerCode) {
        for (Customer customer : customers) {
            if (customer.getCustomerCode().equalsIgnoreCase(customerCode)) {
                return customer;
            }
        }
        return null;
    }
    public String getLastName(String name) {
        String[] partName = name.split(" ");
        return partName[partName.length - 1];
    }
    public String getNameWithoutLastName(String name) {
        String result = "";
        String[] partName = name.split(" ");
        for (int i = 0; i < partName.length - 1; i++) {
            result += partName[i] + " ";
        }
        return result;
    }
    public FeastMenu findFeastMenuByCode(String codeOfSetMenu) {
        for (FeastMenu feastMenu : feastMenuList) {
            if (feastMenu.getCodeSetMenu().equalsIgnoreCase(codeOfSetMenu)) {
                return feastMenu;
            }
        }
        return null;
    }
    public Order findOrderByID(int id) {
        for (Order order : orders) {
            if (order.getOrderID() == id) {
                return order;
            }
        }
        return null;
    }
}