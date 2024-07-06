package org.brayancaro.rules;

public class Min implements RuleInterface {
    protected int value;

    protected int minValue;

    public RuleInterface withValue(int value) {
        this.value = value;

        return this;
    }

    public RuleInterface withMinValue(int minValue) {
        this.minValue = minValue;

        return this;
    }

    @Override
    public boolean passes() {
        return value >= minValue;
    }

    @Override
    public String getErrorMessage() {
        return "Value " + value + " must be grater or equals than " + minValue;
    }
}
