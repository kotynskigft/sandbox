package myproject;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller implements ControllerViewApi {
    private DbAccess dbAccess;
    private DefaultTableModel model;

    private final AtomicInteger maxId;

    Controller() {
        dbAccess = new DbAccess();

        initListModel();
        maxId = new AtomicInteger(0);
    }

    void createWindow() {
        new View(this, model);
    }

    private void initListModel() {
        model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Termin");
        model.addColumn("Opis");
        model.addColumn("Miejsce");
    }

    public void removeListElement(int selectedRow) {
        if (selectedRow < 0) return;

        model.removeRow(selectedRow);
    }

    public void saveList() {
        dbAccess.saveList(model);
    }

    public void addListElement() {
        int newId = maxId.addAndGet(1);
        model.addRow(new Object[]{newId, Date.valueOf(LocalDate.now()), "Opis (" + newId + ")", "Miejsce (" + newId + ")"});
    }

    public void loadList() {
        dbAccess.loadList(model, maxId);
    }
}
