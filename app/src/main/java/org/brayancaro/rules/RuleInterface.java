package org.brayancaro.rules;

/**
 * RuleInterface
 */
public interface RuleInterface<T> {
    public RuleInterface<T> withValue(T value);

    public boolean passes();

    public String getErrorMessage();
}
