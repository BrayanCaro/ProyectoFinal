package org.brayancaro;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;

class DeleteFileTest {
    private Menu menu;

    private MultiWindowTextGUI mockGui;

    @BeforeEach
    public void setUp() {
        menu = Mockito.spy(new Menu());

        mockGui = Mockito.mock(MultiWindowTextGUI.class);

        menu.gui = mockGui;
    }

    @Test
    void testDeleteGameStats_noFile() throws IOException {
        // Arrange
        doThrow(NoSuchFileException.class).when(menu).deleteGameStatsFile();
        doNothing().when(mockGui).waitForWindowToClose(any());

        // Act
        Executable action = () -> menu.deleteGameStats();

        // Assert
        assertDoesNotThrow(action);
        verify(mockGui).addWindow(any());
    }

    @Test
    void testDeleteGameStats_ioException() throws IOException {
        // Arrange
        doThrow(IOException.class).when(menu).deleteGameStatsFile();
        doNothing().when(mockGui).waitForWindowToClose(any());

        // Act
        Executable action = () -> menu.deleteGameStats();

        // Assert
        assertDoesNotThrow(action);
        verify(mockGui).addWindow(any());
    }

    @Test
    void testDeleteGameStats_success() throws IOException {
        doNothing().when(menu).deleteGameStatsFile();
        doNothing().when(mockGui).waitForWindowToClose(any());

        // Act
        Executable action = () -> menu.deleteGameStats();

        // Assert
        assertDoesNotThrow(action);
        verify(mockGui).addWindow(any());
    }

}

