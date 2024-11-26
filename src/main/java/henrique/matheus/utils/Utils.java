package henrique.matheus.utils;

public class Utils {
    public static String onlyAlphabetAndNumbers(String value) {
        return value.replaceAll("[^a-zA-Z çáéíóúàèìòùâêîôûãõñ0-9,.]", "");
    }

    public static String removeXBrackets(String value) {
        return value.replaceAll("\\[x\\]", "");
    }

    public static boolean stringIsInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
    