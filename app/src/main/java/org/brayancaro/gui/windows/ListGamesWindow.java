package org.brayancaro.gui.windows;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;

public class ListGamesWindow extends BasicWindow {
    public ListGamesWindow(String[][] data) {
        var table = new Table<String>(
                "Nombre",
                "Dimensión",
                "No.Bombas",
                "Tiempo");

        TableModel<String> tableModel = table.getTableModel();

        for (var row : data) {
            tableModel.addRow(row);
        }

        var panel = new Panel()
                .addComponent(table)
                .addComponent(new Button("Cerrar", this::close));

        setComponent(panel);
    }
}
