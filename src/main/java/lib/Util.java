package lib;

import org.apache.commons.lang3.StringUtils;

public class Util {
    public static boolean isValidString(String s) {
        return !StringUtils.isBlank(s);
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[0-9]{10}$";
        return phoneNumber.matches(regex);
    }
}
