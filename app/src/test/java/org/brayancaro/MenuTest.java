/*
 * This source file was generated by the Gradle 'init' task
 */
package org.brayancaro;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.IOSafeTerminal;

class MenuTest {
    private Menu menu;

    private IOSafeTerminal terminal;

    @BeforeEach
    public void init() throws IOException {
        new File(Menu.SAVED_FILE_PATH).delete();

        terminal = Mockito.mock(IOSafeTerminal.class);

        Mockito.when(terminal.getTerminalSize()).thenReturn(TerminalSize.ONE);

        var screen = new TerminalScreen(terminal);
        menu = new Menu()
                .random(new Random(120))
                .screen(screen);
    }

    @ParameterizedTest
    @MethodSource
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void playerCanWinAGame(KeyStroke[][] groupKeyStrokes, String scannerSource) {
        var keyStrokesIterator = Stream.of(groupKeyStrokes)
                .flatMap(Stream::of)
                .iterator();

        Mockito.when(terminal.pollInput()).then(
                new Answer<KeyStroke>() {
                    @Override
                    public KeyStroke answer(InvocationOnMock invocation) {
                        return keyStrokesIterator.hasNext()
                                ? keyStrokesIterator.next()
                                // skip flush when closing the screen
                                // https://github.com/mabe02/lanterna/blob/5b839cc52ccfff7ba56a7da40a753037af802893/src/main/java/com/googlecode/lanterna/screen/TerminalScreen.java#L120-L127
                                : new KeyStroke(KeyType.EOF);
                    }
                });

        assertDoesNotThrow(() -> menu.setScanner(new Scanner(scannerSource)).play());
    }

    static Stream<Arguments> playerCanWinAGame() {
        return Stream.of(
                Arguments.arguments(
                        Named.named("start a game, win, and quit",
                                new KeyStroke[][] {
                                        enterGameKeyStrokes(),
                                        simulateExitStrokes(),
                                }),
                        """
                                8 # rows
                                8 # columns
                                1 # bombs
                                1 1
                                v
                                n
                                """),
                Arguments.arguments(
                        Named.named("start, play, reveal cell, see stats and quit",
                                new KeyStroke[][] {
                                        enterGameKeyStrokes(),
                                        viewGameStartsStrokes(),
                                        simulateExitStrokes(),
                                }),
                        """
                                1 # start game
                                8 # rows
                                8 # columns
                                1 # bombs
                                1 1
                                v
                                s
                                name-for-saving-game

                                <white space for confirmation stats>
                                    """),
                Arguments.arguments(
                        Named.named(
                                "start, play, mark cell, reveal cell and quit",
                                new KeyStroke[][] {
                                        enterGameKeyStrokes(),
                                        simulateExitStrokes(),
                                }),
                        """
                                8 # rows
                                8 # columns
                                63 # bombs
                                6 1
                                m
                                1 1
                                v
                                """),
                Arguments.arguments(
                        Named.named(
                                "start, play, toggle mark cell, reveal cell and quit",
                                new KeyStroke[][] {
                                        enterGameKeyStrokes(),
                                        simulateExitStrokes(),
                                }),
                        """
                                8 # rows
                                8 # columns
                                63 # bombs
                                6 1
                                m
                                6 1
                                m
                                1 1
                                v
                                """),
                Arguments.arguments(
                        Named.named(
                                "start, play, toggle mark cell, reveal cell and quit",
                                new KeyStroke[][] {
                                        enterGameKeyStrokes(),
                                        deleteGameStatsStrokes(),
                                        simulateExitStrokes(),
                                }),
                        """
                                8 # rows
                                8 # columns
                                1 # bombs
                                1 1
                                v
                                s
                                name-for-saving-game
                                <white space for confirm file saved>
                                """),
                Arguments.arguments(
                        Named.named(
                                "try delete stats (that isn't present) without exceptions",
                                new KeyStroke[][] {
                                        deleteGameStatsStrokes(),
                                        simulateExitStrokes(),
                                }),
                        "<no-scan-data-required"),
                Arguments.arguments(
                        Named.named(
                                "try see stats (that isn't present) without exceptions",
                                new KeyStroke[][] {
                                        viewGameStartsStrokes(),
                                        simulateExitStrokes(),
                                }),
                        "<no-scan-data-required"));
    }

    private static KeyStroke[] deleteGameStatsStrokes() {
        return new KeyStroke[] {
                // erase data
                new KeyStroke(KeyType.ArrowDown), // focus 2nd option
                new KeyStroke(KeyType.ArrowDown), // focus 3rd option
                new KeyStroke(KeyType.Enter), // select
                new KeyStroke(KeyType.Tab), // submit
                new KeyStroke(KeyType.Enter),
                null,
        };
    }

    private static KeyStroke[] viewGameStartsStrokes() {
        return new KeyStroke[] {
                new KeyStroke(KeyType.ArrowDown), // focus 2nd option
                new KeyStroke(KeyType.Enter), // select
                new KeyStroke(KeyType.Tab), // submit
                new KeyStroke(KeyType.Enter),
                null // required to indicate no more input
        };
    }

    /**
     * Mock user keys: press first option to start a game and then submit
     */
    private static KeyStroke[] enterGameKeyStrokes() {
        return new KeyStroke[] {
                new KeyStroke(KeyType.Enter),
                new KeyStroke(KeyType.Tab),
                new KeyStroke(KeyType.Enter),
                null // required to indicate no more input
        };
    }

    private static KeyStroke[] simulateExitStrokes() {
        return new KeyStroke[] {
                new KeyStroke(KeyType.ArrowDown), // focus 2nd option
                new KeyStroke(KeyType.ArrowDown), // focus 3rd option
                new KeyStroke(KeyType.ArrowDown), // focus 4rd option
                new KeyStroke(KeyType.Enter), // select
                new KeyStroke(KeyType.Tab), // submit
                new KeyStroke(KeyType.Enter),
                null, // required to indicate no more input
        };
    }
}
