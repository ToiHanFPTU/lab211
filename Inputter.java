package tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Inputter {
    public SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public Scanner sc = new Scanner(System.in);
    public String inputString(String mess) {
        System.out.print(mess);
        return sc.nextLine();
    }
    public String inputStringAndLoop(String mess, String messError, String pattern) {
        String result;
        while (true) {
            result = inputString(mess);
            if (!Validation.isValid(result, pattern)) {
                System.out.println(messError);
                continue;
            }
            break;
        }
        return result;
    }
    public int inputIntegerAndLoop(String mess, String messError, int min, int max) {
        int result;
        while (true) {
            try {
                System.out.print(mess);
                result = Integer.parseInt(sc.nextLine());
                if (result < min || result > max) {
                    System.out.println(messError);
                    continue;
                }
                break;
            }catch (NumberFormatException e) {
                System.out.println("Must be integer number");
            }
        }
        return result;
    }

    public Date inputDateAndLoop(String mess, String messError) {
        Date result;

        dateFormat.setLenient(false);
        while (true) {
            System.out.print(mess);
            String input = sc.nextLine();
            if (!input.matches(Validation.DATE_FORMAT_VALID)) {
                System.out.println(messError);
                continue;
            }
            try {
                result = dateFormat.parse(input);
                break;
            } catch (Exception e) {
                System.out.println(messError);
            }
        }
        return result;
    }
    public Date inputDate(String mess) {
        while (true) {
            try {
                String input = inputString(mess);
                if (input.isEmpty()) {
                    return null;
                }

                dateFormat.setLenient(false);
                Date result = dateFormat.parse(input);

                if (result.before(new Date())) {
                    System.out.println("Date must be in the future.");
                } else {
                    return result;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter in dd/MM/yyyy format.");
            }
        }
    }

}