package tool;

public class Validation {
    public static final String PHONE_NUMBER_VALID = "^(086|096|097|098|032|033|034|035|036|037|038|039|091|094|088|081|082|083|084|085|090|093|089|070|076|077|078|079|092|056|058|099|059|087)\\d{7}$";
    public static final String EMAIL_VALID = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,4}$";
    public static final String CUSTOMER_CODE_VALID = "^[CcGgKk]\\d{4}";
    public static final String CODE_SET_MENU_VALID = "^[Pp][Ww]00[1-6]$";
    public static final String DATE_FORMAT_VALID = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
    public static final String YES_NO_VALID = "^[YyNn]$";
    public static boolean isValid(String data, String pattern) {
        return data.matches(pattern);
    }
}
