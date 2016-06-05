package ru.spbau.mit.testserver.client;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graphic extends JPanel {
    private static final int TIME_COUNT = 4;
    private XChartPanel<XYChart> graphic;

    private List<Integer>[] times;
    private String[] names = {"client time", "request time", "sortTime"};

    public Graphic() {
        graphic = new XChartPanel<>(new XYChart(400, 400, Styler.ChartTheme.GGPlot2));
        times = new LinkedList[TIME_COUNT];
        for (int i = 0; i < TIME_COUNT; i++) {
            times[i] = new LinkedList<>();
            times[i].add(0);
        }
        graphic.getChart().addSeries(names[0], times[3], times[0], null);
        graphic.getChart().addSeries(names[1], times[3], times[1], null);
        graphic.getChart().addSeries(names[2], times[3], times[2], null);
        List<Integer> list = new LinkedList<Integer>();
        list.add(0);
        graphic.getChart().addSeries("(0.0)", list, list, null);

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        add(graphic, c);
    }

    public void clear() {
        for (int i = 0; i < times.length; i++) {
            times[i].clear();
        }
        updateTimes();
    }

    public void addPoint(int value, long clientTime, long requestTime, long serverTime) {
        times[0].add((int) (clientTime));
        times[1].add((int) (requestTime));
        times[2].add((int) (serverTime));
        times[3].add(value);
        System.out.print("added");
        updateTimes();
    }

    private void updateTimes() {
        for (int i = 0; i < TIME_COUNT - 1; i++) {
            graphic.updateSeries(names[i], times[TIME_COUNT - 1], times[i], null);
        }

        System.out.print("updated");
    }

}
