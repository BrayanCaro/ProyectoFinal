package org.brayancaro.prompts;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import org.brayancaro.rules.Max;
import org.brayancaro.rules.Min;
import org.brayancaro.rules.RuleInterface;

/**
 * PromptInt
 */
public class PromptInt {
    protected Scanner scanner;

    protected Optional<String> title = Optional.empty();

    protected LinkedList<RuleInterface> rules = new LinkedList<>();

    private Consumer<? super String> printTitleUsing = System.out::println;

    public int ask() {
        do {
            try {
                printTitleWhenPresent();

                var value = askOne();

                Optional<RuleInterface> rule = getFailedRule(value);

                if (rule.isPresent()) {
                    System.out.println(rule.get().getErrorMessage());
                    continue;
                }

                return value;
            } catch (InputMismatchException e) {
                continue;
            }
        } while (true);
    }

    private void printTitleWhenPresent() {
        title.ifPresent(printTitleUsing);
    }

    private Optional<RuleInterface> getFailedRule(int value) {
        return rules.stream()
            .filter((RuleInterface rule) -> !rule.withValue(value).passes())
            .findFirst();
    }

    public int askOne() throws InputMismatchException {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next();
            System.out.println("The value must be an integer");
            throw e;
        }
    }

    public PromptInt scanner(Scanner scanner) {
        this.scanner = scanner;

        return this;
    }

    public PromptInt title(Optional<String> title) {
        this.title = title;

        return this;
    }

    public PromptInt title(String title) {
        return title(Optional.of(title));
    }

    public PromptInt printTitleUsing(Consumer<? super String> strategy) {
        this.printTitleUsing = strategy;

        return this;
    }

    public PromptInt addRules(RuleInterface... rules) {
        for (var rule : rules) {
            this.rules.add(rule);
        }
        return this;
    }

    public PromptInt min(int value) {
        addRules(new Min().withMinValue(value));
        return this;
    }

    public PromptInt max(int value) {
        addRules(new Max().withMaxValue(value));
        return this;
    }
}
