package org.brayancaro.gui.windows;

import org.brayancaro.enums.menu.Option;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;

/**
 * AskOptionWindow
 */
public class AskOptionWindow extends BasicWindow {

    protected Option optionSelected;

    public AskOptionWindow() {
        RadioBoxList<Option> radioBoxList = getRadioBoxList();

        setComponent(new Panel()
                .addComponent(radioBoxList)
                .addComponent(new Button("Confirm", () -> {
                    this.optionSelected = radioBoxList.getCheckedItem();
                    this.close();
                })));
    }

    private RadioBoxList<Option> getRadioBoxList() {
        RadioBoxList<Option> radioBoxList = new RadioBoxList<>();
        for (var option : Option.values()) {
            radioBoxList.addItem(option);
        }
        return radioBoxList;
    }

    public Option getOptionSelected() {
        return optionSelected;
    }
}
