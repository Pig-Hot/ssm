package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhuran on 2019/1/18 0018
 */
public class ListAddUtils {
    public static <T> void add(List<T> list, T t) {
        Set<T> set = new HashSet<>(list);
        if (set.add(t)) {
            list.add(t);
        }
    }
}
