package org.brayancaro;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;

class LoadStatsTest {

    private Menu menu;

    private MultiWindowTextGUI mockGui;

    @BeforeEach
    public void setUp() {
        menu = Mockito.spy(new Menu());

        mockGui = Mockito.mock(MultiWindowTextGUI.class);

        menu.gui = mockGui;
    }

    @ParameterizedTest
    @MethodSource("exceptions")
    void testShowHistory(Class<? extends Throwable> exceptionClass) throws IOException, ClassNotFoundException {
        // Arrange
        doThrow(exceptionClass).when(menu).loadStats();

        // Act
        Executable action = () -> menu.showHistory();

        // Assert
        assertDoesNotThrow(action);
    }

    static Stream<Class<? extends Throwable>> exceptions() {
        return Stream.of(
                IOException.class,
                FileNotFoundException.class,
                ClassNotFoundException.class);
    }
}
