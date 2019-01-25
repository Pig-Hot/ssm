package utils;

/**
 * Created by zhuran on 2019/1/16 0016
 * 字符串工具类
 */
public class StringUtils {
    public static boolean isEmpty(String string){
        return string == null || "".equals(string);
    }

    public static String fristCharToLowerCase(String str) {
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }
}
