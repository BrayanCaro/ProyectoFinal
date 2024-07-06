package org.brayancaro.prompts;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import org.brayancaro.rules.RuleInterface;

/**
 * PromptBase
 */
public abstract class PromptBase<T> {
    protected Scanner scanner;

    protected Optional<String> title = Optional.empty();

    protected LinkedList<RuleInterface<T>> rules = new LinkedList<>();

    protected Consumer<? super String> printTitleUsing = System.out::println;

    public abstract T askOne();

    public T ask() {
        do {
            try {
                printTitleWhenPresent();

                var value = askOne();

                Optional<RuleInterface<T>> rule = getFailedRule(value);

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

    protected void printTitleWhenPresent() {
        title.ifPresent(printTitleUsing);
    }

    protected Optional<RuleInterface<T>> getFailedRule(T value) {
        return rules.stream()
            .filter((RuleInterface<T> rule) -> !rule.withValue(value).passes())
            .findFirst();
    }

    public PromptBase<T> addRule(RuleInterface<T> rule) {
        this.rules.add(rule);

        return this;
    }

    public PromptBase<T> printTitleUsing(Consumer<? super String> strategy) {
        this.printTitleUsing = strategy;

        return this;
    }

    public PromptBase<T> scanner(Scanner scanner) {
        this.scanner = scanner;

        return this;
    }

    public PromptBase<T> title(Optional<String> title) {
        this.title = title;

        return this;
    }

    public PromptBase<T> title(String title) {
        return title(Optional.of(title));
    }
}
