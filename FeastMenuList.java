package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FeastMenuList extends ArrayList<FeastMenu> {

    /**
     * D:\BuiGiaHuy\CN3\LAB221\Code\SuTV\TraditionalFeastOrderManagement\FeastMenu.csv
     */
    public ArrayList<FeastMenu> feastMenus = new ArrayList<>();

    public void loadFromFile(String fileName) {
        /**
         * 1. Kiểm tra có file hay không 2. Nếu có thì mở file 3. Khi còn đọc đc
         * 1 dòng cắt từng field tạo 1 mountain fill từng list 4. Đóng file
         */
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not exist");
            System.exit(0); //Keu he thong thoat chuong trinh
        }
        FileReader fr;
        BufferedReader br;
        String line;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            line = br.readLine();
            line = line.trim();
            while ((line = br.readLine()) != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                String code = stringTokenizer.nextToken().trim();
                String name = stringTokenizer.nextToken().trim();
                double price = Double.parseDouble(stringTokenizer.nextToken().trim());
                String ingredients = stringTokenizer.nextToken().trim();
                this.add(new FeastMenu(code, name, price, ingredients));
            }
            System.out.println("Data loaded successfully ");
            fr.close();
            br.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
