package org.brayancaro.gui.windows;

import org.brayancaro.records.board.GameStat;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;

public class ListGamesWindow extends BasicWindow {
    public ListGamesWindow(GameStat[] data) {
        var table = new Table<String>(GameStat.headers());

        TableModel<String> tableModel = table.getTableModel();

        for (var row : data) {
            tableModel.addRow(row.toRow());
        }

        var panel = new Panel()
                .addComponent(table)
                .addComponent(new Button("Cerrar", this::close));

        setComponent(panel);
    }
}
