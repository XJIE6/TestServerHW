package ru.spbau.mit.kravchenkoyura.testserver.utils;

import java.util.*;

public class ArraySorter {
    static List<Long> times = Collections.synchronizedList(new LinkedList<>());
    public static List<Long> getTimes() {
        return times;
    }
    public static void resetTime() {
        times.clear();
    }

    public static List<Integer> sort(List<Integer> arr) {
        Long time = System.currentTimeMillis();
        for (int i = 0; i < arr.size(); ++i) {
            for (int j = i + 1; j < arr.size(); ++j) {
                Integer x = arr.get(i);
                Integer y = arr.get(j);
                if (x.compareTo(y) > 0) {
                    arr.set(j, x);
                    arr.set(i, y);
                }
            }
        }
        times.add(System.currentTimeMillis() - time);
        return arr;
    }
}
