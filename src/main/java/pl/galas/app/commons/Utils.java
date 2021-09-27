package pl.galas.app.commons;

import java.util.Collection;

public class Utils {
    public static boolean isNullOrEmpty(Collection resultList) {
        if (resultList == null)
            return true;
        return resultList.size() == 0;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
