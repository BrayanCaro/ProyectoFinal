package org.brayancaro.prompts;

import org.brayancaro.rules.strings.Pattern;

/**
 * Prompt
 */
public class Prompt extends PromptBase<String> {
    @Override
    public String askOne() {
        return scanner.nextLine();
    }

    public Prompt pattern(java.util.regex.Pattern  pattern) {
        addRule(new Pattern().withPattern(pattern));
        return this;
    }
}
