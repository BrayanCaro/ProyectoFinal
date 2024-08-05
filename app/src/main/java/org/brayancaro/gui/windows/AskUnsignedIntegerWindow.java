package org.brayancaro.gui.windows;

import java.util.regex.Pattern;

import org.brayancaro.records.menu.Configuration;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

public class AskUnsignedIntegerWindow extends Window {

    protected Configuration configuration;

    public AskUnsignedIntegerWindow() {

        Pattern unsigned = Pattern.compile("\\d+");

        TextBox rowsTextBox = new TextBox().setValidationPattern(unsigned);
        TextBox columnsTextBox = new TextBox().setValidationPattern(unsigned);
        TextBox bombCountTextBox = new TextBox().setValidationPattern(unsigned);

        setComponent(
                new Panel()
                        .addComponent(rowsTextBox)
                        .addComponent(columnsTextBox)
                        .addComponent(bombCountTextBox)
                        .addComponent(new Button("done", () -> {
                            configuration = new Configuration(
                                    Integer.parseUnsignedInt(rowsTextBox.getText()),
                                    Integer.parseUnsignedInt(columnsTextBox.getText()),
                                    Integer.parseUnsignedInt(bombCountTextBox.getText()));

                            var validatorResults = validator.validate(configuration);

                            if (validatorResults.isEmpty()) {
                                close();
                                return;
                            }

                            MessageDialog.showMessageDialog(getTextGUI(), "Error",
                                    validatorResults.iterator().next().getMessage());

                        })));
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
