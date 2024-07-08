/*
 * This source file was generated by the Gradle 'init' task
 */
package org.brayancaro;

import static org.junit.Assert.*;

import java.util.Random;
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
    public void playerCanWinAGame() throws Exception {
        new Menu()
            .random(new Random(120))
            .setScanner(new Scanner("""
                        1 # start game
                        8 # rows
                        8 # columns
                        1 # bombs
                        1 1
                        v
                        n
                        4 # exit
                        """))
            .play();

        assertTrue(true);
    }

    @Test
    public void playerCanWinAndSaveGame() throws Exception {
        new Menu()
            .random(new Random(120))
            .setScanner(new Scanner("""
                        1 # start game
                        8 # rows
                        8 # columns
                        1 # bombs
                        1 1
                        v
                        s
                        name-for-saving-game

                        2 # see game

                        4
                        """))
            .play();

        assertTrue(true);
    }

    @Test
    public void playerCanQuitGameAfterStarting() throws Exception {
        new Menu()
            .random(new Random(120))
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

    @Test
    public void playerCanWinAfterMarkingCell() throws Exception {
        new Menu()
            .random(new Random(120))
            .setScanner(new Scanner("""
                        1 # start game
                        8 # rows
                        8 # columns
                        63 # bombs
                        6 1
                        m
                        1 1
                        v
                        4 # exit
                        """))
            .play();

        assertTrue(true);
    }

    @Test
    public void playerCanWinAfterTogglingCell() throws Exception {
        new Menu()
            .random(new Random(120))
            .setScanner(new Scanner("""
                        1 # start game
                        8 # rows
                        8 # columns
                        63 # bombs
                        6 1
                        m
                        6 1
                        m
                        1 1
                        v
                        4 # exit
                        """))
            .play();

        assertTrue(true);
    }
}
