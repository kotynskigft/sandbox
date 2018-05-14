package myproject;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

class DbAccess {
    private static final String DB_URL = "jdbc:ucanaccess://MyDatabase.accdb";

    void saveList(DefaultTableModel model) {
        // Zapis do bazy danych
        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement s = conn.createStatement()
        ) {
            // usunmy elemeny z bazy, ktore nie istnieja w modelu
            removeFromDbNotExisistingElements(model, s);

            // przejrzymy wszystkie lokalne rekordy szukając kandydatów do aktualizacji
            updateDbExistingElements(model, s);

            // przejrzymy wszystkie lokalne rekordy szukając kandydatów do dodania do bazy danych
            insertToDbNewElements(model, s);

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void insertToDbNewElements(DefaultTableModel model, Statement s) throws SQLException {
        for (int row = 0; row < model.getRowCount(); row++) {
            int local_id = (int) model.getValueAt(row, 0);
            ResultSet single = s.executeQuery("SELECT Id from Kalendarz WHERE Id=" + local_id);

            if (single.next()) continue;

            Date local_date = (Date) model.getValueAt(row, 1);
            String local_opis = (String) model.getValueAt(row, 2);
            String local_miejsce = (String) model.getValueAt(row, 3);
            s.execute("INSERT INTO Kalendarz (Id, Termin, Opis, Miejsce) VALUES (" + local_id + ",#" + local_date + "#, '" + local_opis + "', '" + local_miejsce + "')");

        }
    }

    private void updateDbExistingElements(DefaultTableModel model, Statement s) throws SQLException {
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
    }

    private void removeFromDbNotExisistingElements(DefaultTableModel model, Statement s) throws SQLException {
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
//                        continue;
                    }
                }

                if (!znaleziono) {
                    // lokalnie nie znaleziono wydarzeniea - usuwamy rekord
                    s.execute("DELETE FROM Kalendarz WHERE Id=" + database_id);
                }

            }
        }
    }

    void loadList(DefaultTableModel model, AtomicInteger maxId) {
        // usuwamy wszystkie rekordy aby uniknąć zduplikowania wczytywanioa
        int rows = model.getRowCount();
        for (int i = 0; i < rows; i++) {
            model.removeRow(0);
        }

        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM [Kalendarz]")) {

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
}
