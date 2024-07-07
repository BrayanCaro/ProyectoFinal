package org.brayancaro.rules.strings;

import org.brayancaro.rules.RuleInterface;


/**
 * Pattern
 */
public class Pattern implements RuleInterface<String> {

    private String value;

    private java.util.regex.Pattern pattern;

    public Pattern withValue(String value) {
        this.value = value;

        return this;
    }

    public Pattern withPattern(java.util.regex.Pattern pattern) {
        this.pattern = pattern;

        return this;
    }

	@Override
	public boolean passes() {
        return this.pattern.matcher(value).matches();
	}

	@Override
	public String getErrorMessage() {
        return "Value " + value + " has an invalid format";
	}
}
