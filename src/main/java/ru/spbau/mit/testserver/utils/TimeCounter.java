package ru.spbau.mit.testserver.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public interface TimeCounter {
    static List<Long> times = Collections.synchronizedList(new LinkedList<>());;
    public static List<Long> getTimes() {
        return times;
    }
    public static void resetTime() {
        times.clear();
    }
}
