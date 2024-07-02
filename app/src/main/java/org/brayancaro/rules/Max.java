package org.brayancaro.rules;

public class Max implements RuleInterface {
    protected int value;

    protected int maxValue;

    public RuleInterface withValue(int value) {
        this.value = value;

        return this;
    }

    public RuleInterface withMaxValue(int maxValue) {
        this.maxValue = maxValue;

        return this;
    }

    @Override
    public boolean passes() {
        return value <= maxValue;
    }

    @Override
    public String getErrorMessage() {
        return "Value " + value + " must be less or equals than " + maxValue;
    }
}
