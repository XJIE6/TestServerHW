package ru.spbau.mit.kravchenkoyura.testserver.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//Интерфейс подсчёта времени для всех сокетов

public interface TimeCounter {
    public static List<Long> times = Collections.synchronizedList(new LinkedList<>());;
    public static List<Long> getTimes() {
        return times;
    }
    public static void resetTime() {
        times.clear();
    }
}