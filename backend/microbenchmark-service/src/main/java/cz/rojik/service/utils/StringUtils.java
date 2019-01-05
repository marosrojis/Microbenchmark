package cz.rojik.service.utils;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class StringUtils {

    public static String insertBracket(String string) {
        return "\\[" + string + "\\]";
    }
}
