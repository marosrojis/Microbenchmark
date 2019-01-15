package cz.rojik.service.utils;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class StringUtils {

    /**
     * Insert boundary bracket to mark.
     * Default java class contains boundary bracket around mark.
     * @param string mark to border with bracket
     * @return mark with bracket
     */
    public static String insertBracket(String string) {
        return "\\[" + string + "\\]";
    }
}
