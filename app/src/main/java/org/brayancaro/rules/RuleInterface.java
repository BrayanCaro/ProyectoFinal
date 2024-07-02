package org.brayancaro.rules;

/**
 * RuleInterface
 */
public interface RuleInterface {
    public RuleInterface withValue(int value);

    public boolean passes();

    public String getErrorMessage();
}
