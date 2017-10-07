package com.komect.showroom.util;

import java.util.List;
import java.util.Map;

/**
 * 检查字符串和其它数据集合是否为空
 */
public class EmptyUtils {
    /**
     * 判断字符串是否为空,包括null和空字符串
     *
     * @param str 字符串
     * @return 空返回true，否则false
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空,包括null、空字符串和"null"
     *
     * @param str 字符串
     * @return 空返回true，否则false
     */
    public static boolean isEmpty2(String str) {
        return str == null || str.length() == 0 || "null".equalsIgnoreCase(str);
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }
}
