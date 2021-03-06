package ru.spbau.mit.kravchenkoyura.testserver.client;

import javax.swing.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ClientUI {
    private final static String[] SERVERS_LIST = {"TCP1", "TCP2", "TCP3", "TCP4", "UDP1", "UDP2"};
    private final static int HEIGHT = 1250;
    private final static int WEIGHT = 600;
    private final static int SETTINGS_PANEL_HEIGHT = 450;
    private final static int GRAPHIC_PANEL_HEIGHT = 800;

    private static JFrame frame;

    private static JTextField serverIP;
    private static JComboBox serversList;
    private static JButton buttonRun;
    private static Graphic graphic;

    private static JTable table;

    private static MainClient client = null;

    public static void main(String[] args) {
        buildClientUI();
    }

    private static void buildClientUI() {
        frame = new JFrame("Benchmark client");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(HEIGHT, WEIGHT);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(buildSettingsPanel(), BorderLayout.WEST);
        frame.add(buildGraphicPanel(), BorderLayout.EAST);
        frame.setVisible(true);
    }

    private static JToolBar buildToolbar() {
        final JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
        toolbar.setFloatable(false);
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

        final JLabel labelServerIP = new JLabel("IP: ");
        labelServerIP.setVerticalAlignment(SwingConstants.CENTER);
        toolbar.add(labelServerIP);

        serverIP = new JTextField(9);
        serverIP.setText("127.0.0.1");
        toolbar.add(serverIP);

        final JLabel labelServerType = new JLabel("Type: ");
        labelServerType.setVerticalAlignment(SwingConstants.CENTER);
        toolbar.add(labelServerType);

        serversList = new JComboBox(SERVERS_LIST);
        toolbar.add(serversList);

        buttonRun = new JButton("Run");
        buttonRun.setMargin(new Insets(3, 5, 3, 5));
        buttonRun.addActionListener(runBenchmark);
        toolbar.add(buttonRun);

        return toolbar;
    }

    private static boolean checkTestParameters() {
        try {
            InetAddress.getByName(serverIP.getText()).getHostName();
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(frame, "Incorrect sever IP address!");
            return false;
        }

        int changingValuesCount = 0;
        for (int i = 1; i < 4; ++i) {
            final Integer from = (Integer) table.getValueAt(i, 1);
            final Integer to = (Integer) table.getValueAt(i, 2);
            final Integer step = (Integer) table.getValueAt(i, 3);

            if (from > to) {
                JOptionPane.showMessageDialog(frame, "'From' value is bigger than 'To' value.");
                return false;
            }

            if ((from.equals(to) && step != 0) || (!from.equals(to) && step == 0)) {
                JOptionPane.showMessageDialog(frame, "Incorrect 'Step' value");
                return false;
            }

            if (!from.equals(to)) {
                ++changingValuesCount;
            }
        }

        if (changingValuesCount > 1) {
            JOptionPane.showMessageDialog(frame, "It's possible to change just 1 parameter!");
            return false;
        }

        return true;
    }

    private static JPanel buildSettingsPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(SETTINGS_PANEL_HEIGHT, frame.getHeight()));
        panel.setLayout(new BorderLayout());
        panel.add(buildToolbar(), BorderLayout.NORTH);
        table = buildSettingGrid();
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private static JTable buildSettingGrid() {
        final int MIN_WIGHT = 130;
        final int ROW_HEIGHT = 20;
        final JTable table = new JTable(new AbstractTableModel() {
            private final String[] columnNames = new String[]{"Parameter", "From", "To", "Step"};
            private final String[] parametersNames = new String[]{"Requests count:", "Clients count:", "Array length:", "Delay:"};

            public final Integer[][] parameters = new Integer[][]{
                    {0, 10, 0, 0},
                    {0, 5, 5, 0},
                    {0, 100, 4000, 400},
                    {0, 5, 5, 0}
            };

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public int getRowCount() {
                return 4;
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex > 0) {
                    try {
                        final Integer value = Integer.parseInt(aValue.toString());
                        if (value >= 0) {
                            parameters[rowIndex][columnIndex] = value;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Enter a non-negative integer!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Enter a non-negative integer!");
                    }
                }
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return parametersNames[rowIndex];
                    default:
                        return parameters[rowIndex][columnIndex];
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex != 0 && ((rowIndex == 0 && columnIndex == 1) || rowIndex > 0);
            }
        });

        final Font font = new Font("Trebuchet MS", 0, 14);

        table.getTableHeader().setFont(font);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setMinWidth(MIN_WIGHT);
        table.setRowHeight(ROW_HEIGHT);
        table.setFont(font);

        table.getColumnModel().getColumn(1).setCellRenderer(new EditTextRender());
        table.getColumnModel().getColumn(2).setCellRenderer(new EditTextRender());
        table.getColumnModel().getColumn(3).setCellRenderer(new EditTextRender());

        table.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() );
        return table;
    }

    private static JPanel buildGraphicPanel() {
        graphic = new Graphic();
        graphic.setPreferredSize(new Dimension(GRAPHIC_PANEL_HEIGHT, frame.getHeight()));
        return graphic;
    }


    private static class EditTextRender extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row2, int column) {
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.getDefault());
            decimalFormat.setGroupingUsed(false);

            final JTextField field = new JFormattedTextField(decimalFormat);
            field.setEditable(true);
            field.setText(table.getModel().getValueAt(row2, column).toString());
            field.addActionListener((e) -> table.getModel().setValueAt(((JTextField) e.getSource()).getText(), row2, column));

            if (row2 == 0 && column > 1) {
                return null;
            }

            return field;
        }
    }

    private static ActionListener runBenchmark = (e) -> {
        if (!checkTestParameters()) {
            return;
        }
        if (client == null) {
            try {
                client = new MainClient(serverIP.getText());
            } catch (IOException e1) {
                return;
            }
        }

        final Integer requestsCount = (Integer) table.getValueAt(0, 1);
        new Thread(() -> {
            int point = 0;
            graphic.clear();
            SwingUtilities.invokeLater(() -> buttonRun.setEnabled(false));
            List<Integer> firstParam = new ArrayList<>();
            List<Integer> secondParam = new ArrayList<>();
            List<Integer> thirdParam = new ArrayList<>();
            for (int i = (Integer) table.getValueAt(1, 1); i <= (Integer) table.getValueAt(1, 2); i += Math.max((Integer) table.getValueAt(1, 3), 1)) {
                for (int j = (Integer) table.getValueAt(2, 1); j <= (Integer) table.getValueAt(2, 2); j += Math.max((Integer) table.getValueAt(2, 3), 1)) {
                    for (int k = (Integer) table.getValueAt(3, 1); k <= (Integer) table.getValueAt(3, 2); k += Math.max((Integer) table.getValueAt(3, 3), 1)) {
                        double[] res = client.runServer(serversList.getSelectedIndex(), i, j, requestsCount, k);
                        firstParam.add((int)res[0]);
                        secondParam.add((int)res[1]);
                        thirdParam.add((int)res[2]);
                        graphic.addPoint(point, (long) res[0], (long) res[1], (long) res[2]);
                        point += 1;
                    }
                }
            }
            try (FileWriter file = new FileWriter("data.txt")) {
                for (Integer i : firstParam) {
                    file.write(i.toString());
                    file.write('\n');
                }
                file.write('\n');
                for (Integer i : secondParam) {
                    file.write(i.toString());
                    file.write('\n');
                }
                file.write('\n');
                for (Integer i : thirdParam) {
                    file.write(i.toString());
                    file.write('\n');
                }
            } catch (IOException e1) {
                //data writing failed
                SwingUtilities.invokeLater(() -> {
                    buttonRun.setEnabled(true);
                    JOptionPane.showMessageDialog(frame, "Error in result writing");
                });
            }
            SwingUtilities.invokeLater(() -> {
                buttonRun.setEnabled(true);
                JOptionPane.showMessageDialog(frame, "Benchmark has finished!");
            });
        }).start();
    };
}