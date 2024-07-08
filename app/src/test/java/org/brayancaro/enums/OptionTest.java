package org.brayancaro.enums;

import static org.junit.Assert.assertThrows;

import org.brayancaro.enums.menu.Option;
import org.brayancaro.exceptions.menu.InvalidOptionException;
import org.junit.Test;

/**
 * OptionTest
 */
public class OptionTest {
    @Test
    public void menuFailsWithInvalidInput() {
        assertThrows(
            InvalidOptionException.class,
            () -> Option.fromIndex(-999)
        );
    }
}
