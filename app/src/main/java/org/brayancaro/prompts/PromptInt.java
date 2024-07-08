package org.brayancaro.prompts;

import java.util.InputMismatchException;

import org.brayancaro.rules.Max;
import org.brayancaro.rules.Min;

/**
 * PromptInt
 */
public class PromptInt extends PromptBase<Integer> {
    public Integer askOne() throws InputMismatchException {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("The value must be an integer");
            throw e;
        } finally {
            scanner.nextLine();
        }
    }

    public PromptInt min(int value) {
        addRule(new Min().withMinValue(value));
        return this;
    }

    public PromptInt max(int value) {
        addRule(new Max().withMaxValue(value));
        return this;
    }
}
