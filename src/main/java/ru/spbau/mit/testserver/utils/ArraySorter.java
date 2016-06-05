package ru.spbau.mit.testserver.utils;

import java.util.*;

public class ArraySorter {
    static List<Long> times = Collections.synchronizedList(new LinkedList<>());;
    public static List<Long> getTimes() {
        return times;
    }
    public static void resetTime() {
        times.clear();
    }
    public static List<Integer> sort(List<Integer> arr) {
        Long time = System.nanoTime();
        arr.sort(Comparator.naturalOrder());
        times.add(System.nanoTime() - time);
        return arr;
    }
}
