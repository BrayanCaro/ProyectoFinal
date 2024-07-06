/*
 * This source file was generated by the Gradle 'init' task
 */
package org.brayancaro;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.Test;

public class MenuTest {

    // @Test
    // public void menuFailsWithInvalidInput() {
    //     var classUnderTest = new Menu()
    //         .setScanner(new Scanner("foo"));

    //     assertThrows(
    //         Exception.class,
    //             () -> classUnderTest.play()
    //     );
    // }

    @Test
    public void valid_scanner_data() {
        try (var q = new Scanner("(1,2)\n")) {
            String next = q.next("\\(\\d+\\s*,\\s*\\d+\\)");

            assertEquals("(1,2)", next);
        }
    }

    @Test
    public void invalid_scanner_data() {
        try (var q = new Scanner("(1 , 1)")) {
                q.next("\\(\\d+\\s*,\\s*\\d+\\)");
            // assertThrows(NoSuchElementException.class, () -> {
            // });
        }
    }
}
