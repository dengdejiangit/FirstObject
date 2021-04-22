package com.example.administrator.testproject.util;

import java.util.Collection;

public class CollectionsUtil {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static Integer getCount(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return 0;
        }
        return collection.size();
    }
}
