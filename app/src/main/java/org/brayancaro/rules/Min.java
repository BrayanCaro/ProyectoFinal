package org.brayancaro.rules;

public class Min implements RuleInterface<Integer> {
    protected int value;

    protected int minValue;

    public RuleInterface<Integer> withValue(Integer value) {
        this.value = value;

        return this;
    }

    public RuleInterface<Integer> withMinValue(Integer minValue) {
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
