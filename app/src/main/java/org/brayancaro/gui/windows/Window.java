package org.brayancaro.gui.windows;

import com.googlecode.lanterna.gui2.BasicWindow;

import jakarta.validation.Validator;

public class Window extends BasicWindow {
    protected Validator validator;

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
