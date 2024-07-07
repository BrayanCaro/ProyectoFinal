/*
 * This source file was generated by the Gradle 'init' task
 */
package org.brayancaro;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

public class MenuTest {

    @Test
    public void menuFailsWithInvalidInput() {
        var classUnderTest = new Menu()
            .setScanner(new Scanner("foo"));

        assertThrows(
            Exception.class,
                () -> classUnderTest.play()
        );
    }

    @Test
    public void playerCanQuitGameAfterStarting() throws Exception {
        new Menu()
            .setScanner(new Scanner("""
                        1 # start game
                        8 # rows
                        8 # columns
                        63 # bombs
                        1 1
                        v
                        4 # exit
                        """))
            .play();

        assertTrue(true);
    }
}
