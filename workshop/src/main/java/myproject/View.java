package myproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class View {
    private final JFrame frame;
    private final JTable table;

    public View(ControllerViewApi controllerViewApi, DefaultTableModel model) {
        frame = initFrame();
        table = initTable(model);
        initButtons(controllerViewApi);
        initMenuBar();

        frame.setVisible(true);
    }

    private JFrame initFrame() {
        JFrame localFrame = new JFrame("JTable Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(588, 272);
        frame.getContentPane().setLayout(null);

        return localFrame;
    }

    private JTable initTable(DefaultTableModel model) {
        JTable localTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(localTable);
        scrollPane.setBounds(0, 0, 422, 172);
        localTable.setFillsViewportHeight(true);
        frame.getContentPane().add(scrollPane);

        return localTable;
    }

    private void initButtons(ControllerViewApi controllerViewApi) {
        JButton button = new JButton("Usun Rekord");
        button.setBounds(432, 113, 130, 23);
        frame.getContentPane().add(button);
        button.addActionListener((event) ->
                controllerViewApi.removeListElement(table.getSelectedRow())
        );

        JButton button_2 = new JButton("Dodaj Rekord");
        button_2.setBounds(432, 79, 130, 23);
        frame.getContentPane().add(button_2);
        button_2.addActionListener((event) -> controllerViewApi.addListElement());

        JButton button_3 = new JButton("SQL-Save");
        button_3.setBounds(432, 45, 130, 23);
        frame.getContentPane().add(button_3);
        button_3.addActionListener((event) -> controllerViewApi.saveList());

        JButton button_4 = new JButton("SQL-Reload");
        button_4.setBounds(432, 11, 130, 23);
        frame.getContentPane().add(button_4);
        button_4.addActionListener((event) -> controllerViewApi.loadList());
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem menuItem_1 = new JMenuItem("Open");
        menu.add(menuItem_1);

        JMenuItem menuItem_2 = new JMenuItem("Save");
        menu.add(menuItem_2);

        JMenu menu_1 = new JMenu("Edit");
        menuBar.add(menu_1);
    }
}
