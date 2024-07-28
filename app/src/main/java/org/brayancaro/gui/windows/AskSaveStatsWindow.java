package org.brayancaro.gui.windows;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;

public class AskSaveStatsWindow extends BasicWindow {
    private RadioBoxList<String> booleanBoxList;

    private int selectedIndex;

    public AskSaveStatsWindow() {
        booleanBoxList = new RadioBoxList<>();
        booleanBoxList.addItem("SI");
        booleanBoxList.addItem("NO");
        booleanBoxList.getSelectedIndex();
        booleanBoxList.addListener((selectedIndex, previousSelection) -> this.selectedIndex = selectedIndex);

        setComponent(
                new Panel()
                        .addComponent(new Label("¿Quieres guardar tu partida?"))
                        .addComponent(booleanBoxList)
                        .addComponent(new Button("Confirmar", () -> {
                            selectedIndex = booleanBoxList.getSelectedIndex();
                            close();
                        })));
    }

    public boolean canSaveStats() {
        return selectedIndex == 0;
    }
}
