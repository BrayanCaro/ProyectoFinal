package org.brayancaro.gui.windows;

import java.util.Optional;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Border;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.TextBox;

public class AskSaveStatsWindow extends BasicWindow {
    private RadioBoxList<String> booleanBoxList;

    private int selectedIndex;

    private Optional<String> name = Optional.empty();

    public AskSaveStatsWindow() {
        var nameTextBox = new TextBox();

        Border nameContainer = (Border) nameTextBox.withBorder(Borders.singleLine("¿Cual es tu nombre?"))
            .setVisible(false);

        booleanBoxList = new RadioBoxList<String>()
                .addItem("SI")
                .addItem("NO")
                .addListener((selectedIndex, previousSelection) -> {
                    this.selectedIndex = selectedIndex;
                    nameContainer.setVisible(canSaveStats());
                });

        var formPanel = new Panel(new LinearLayout(Direction.HORIZONTAL))
                .addComponent(booleanBoxList.withBorder(Borders.singleLine("¿Quieres guardar tu partida?")))
                .addComponent(nameContainer);

        setComponent(new Panel()
                .addComponent(formPanel)
                .addComponent(new Button("Confirmar", () -> {
                    if (nameContainer.isVisible()) {
                        name = Optional.of(nameTextBox.getText());
                    }
                    close();
                })));
    }

    public boolean canSaveStats() {
        return selectedIndex == 0;
    }

    public Optional<String> getName() {
        return name;
    }
}
