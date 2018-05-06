package myproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MyApplication {

    public static void main(String[] args) throws Exception {

        final JFrame frame = new JFrame("JTable Demo");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Termin");
        model.addColumn("Opis");
        model.addColumn("Miejsce");

        frame.getContentPane().setLayout(null);

        final JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, 422, 172);
        table.setFillsViewportHeight(true);
        frame.getContentPane().add(scrollPane);

        JButton button = new JButton("Usun Rekord");
        button.setBounds(432, 113, 130, 23);
        frame.getContentPane().add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow < 0) return;

                model.removeRow(selectedRow);
            }
        });

        final AtomicInteger maxId = new AtomicInteger(0);

        JButton button_2 = new JButton("Dodaj Rekord");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int newId = maxId.addAndGet(1);
                model.addRow(new Object[]{newId, Date.valueOf(LocalDate.now()), "Opis (" + newId + ")", "Miejsce (" + newId + ")"});
            }
        });
        button_2.setBounds(432, 79, 130, 23);
        frame.getContentPane().add(button_2);

        JButton button_3 = new JButton("SQL-Save");
        button_3.setBounds(432, 45, 130, 23);
        frame.getContentPane().add(button_3);
        button_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                // Zapis do bazy danych
                try (
                        Connection conn = DriverManager.getConnection("jdbc:ucanaccess://MyDatabase.accdb");
                        Statement s = conn.createStatement();
                ) {


                    try (ResultSet rs = s.executeQuery("SELECT * FROM [Kalendarz]")) {

                        // przejrzymy wszystkie rekordy bazy danych szukając kandydatów
                        while (rs.next()) {
                            int database_id = rs.getInt("Id");
                            // teraz szukamy czy istnieje dla niego odpowiednik w tabeli
                            boolean znaleziono = false;
                            for (int row = 0; row < model.getRowCount(); row++) {
                                Integer local_id = (Integer) model.getValueAt(row, 0);
                                if (database_id == local_id) {
                                    znaleziono = true;
                                    continue;
                                }
                            }

                            if (!znaleziono) {
                                // lokalnie nie znaleziono wydarzeniea - usuwamy rekord
                                s.execute("DELETE FROM Kalendarz WHERE Id=" + database_id);
                            }

                        }
                    }


                    // przejrzymy wszystkie lokalne rekordy szukając kandydatów do aktualizacji
                    for (int row = 0; row < model.getRowCount(); row++) {
                        int local_id = (int) model.getValueAt(row, 0);
                        ResultSet single = s.executeQuery("SELECT * from Kalendarz WHERE Id=" + local_id);

                        if (!single.next()) continue;

                        Date local_date = (Date) model.getValueAt(row, 1);
                        String local_opis = (String) model.getValueAt(row, 2);
                        String local_miejsce = (String) model.getValueAt(row, 3);

                        Date db_date = single.getDate("Termin");
                        String db_opis = single.getString("Opis");
                        String db_miejsce = single.getString("Miejsce");

                        if (Objects.equals(local_date, db_date) && Objects.equals(local_opis, db_opis) && Objects.equals(local_miejsce, db_miejsce))
                            continue;

                        s.execute("UPDATE Kalendarz SET Termin=#" + local_date + "#, Opis='" + local_opis + "', Miejsce='" + local_miejsce + "' WHERE Id=" + local_id);

                    }

                    // przejrzymy wszystkie lokalne rekordy szukając kandydatów do dodania do bazy danych
                    for (int row = 0; row < model.getRowCount(); row++) {
                        int local_id = (int) model.getValueAt(row, 0);
                        ResultSet single = s.executeQuery("SELECT Id from Kalendarz WHERE Id=" + local_id);

                        if (single.next()) continue;

                        Date local_date = (Date) model.getValueAt(row, 1);
                        String local_opis = (String) model.getValueAt(row, 2);
                        String local_miejsce = (String) model.getValueAt(row, 3);
                        s.execute("INSERT INTO Kalendarz (Id, Termin, Opis, Miejsce) VALUES (" + local_id + ",#" + local_date + "#, '" + local_opis + "', '" + local_miejsce + "')");

                    }


                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        JButton button_4 = new JButton("SQL-Reload");
        button_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // usuwamy wszystkie rekordy aby uniknąć zduplikowania wczytywanioa
                int rows = model.getRowCount();
                for (int i = 0; i < rows; i++) {
                    model.removeRow(0);
                }

                try (
                        Connection conn = DriverManager.getConnection("jdbc:ucanaccess://MyDatabase.accdb");
                        Statement s = conn.createStatement();
                        ResultSet rs = s.executeQuery("SELECT * FROM [Kalendarz]");) {

                    while (rs.next()) {
                        Integer id = rs.getInt("Id");
                        if (id > maxId.intValue()) maxId.set(id);

                        Date termin = rs.getDate("Termin");
                        String opis = rs.getString("Opis");
                        String miejsce = rs.getString("Miejsce");

                        model.addRow(new Object[]{id, termin, opis, miejsce});
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        button_4.setBounds(432, 11, 130, 23);
        frame.getContentPane().add(button_4);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(588, 272);

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
        frame.setVisible(true);
    }
}