/**
 * Clase para representar el menu
 * Objetivo. Simular un menu para un usuario
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 */
package org.brayancaro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.brayancaro.enums.menu.Option;
import org.brayancaro.gui.windows.AskOptionWindow;
import org.brayancaro.gui.windows.AskSaveStatsWindow;
import org.brayancaro.gui.windows.AskUnsignedIntegerWindow;
import org.brayancaro.gui.windows.GameWindow;
import org.brayancaro.gui.windows.ListGamesWindow;
import org.brayancaro.records.board.GameStat;
import org.brayancaro.records.menu.Configuration;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.screen.Screen;

import jakarta.validation.Validator;

public class Menu {
    public static final String SAVED_FILE_PATH = "listaDeTablas.minas";

    protected Random random;

    protected Screen screen;

    protected MultiWindowTextGUI gui;

    protected Validator validator;

    public void play() throws IOException {
        screen.startScreen();

        Option option = null;
        do {
            option = askOption();
            realizarAccion(option);
        } while (option != Option.QUIT);

        screen.stopScreen();
    }

    /*
     * Metodo que realiza una accion de acuerdo a las dichas en el metodo menu.
     */
    public void realizarAccion(Option option) {
        switch (option) {
            case Option.START -> startGame();
            case Option.SHOW_HISTORY -> showHistory();
            case Option.DELETE_HISTORY -> deleteGameStats();
            case Option.QUIT -> {/* do nothing, because the user is going to quit the app */}
            case null -> {/* do nothing, the option is invalid */}
        }
    }

    private void startGame() {
        var config = askConfiguration();
        var board = new TableroPersonalizado(config, random);
        new MessageDialogBuilder()
                .setTitle("EMPECEMOS")
                .setText("")
                .build()
                .showDialog(gui);
        var gameWindow = new GameWindow(board);
        gui.addWindowAndWait(gameWindow);
        handleWinningState(board);
    }

    protected void showHistory() {
        try {
            gui.addWindowAndWait(new ListGamesWindow(loadStats()));
        } catch (ClassNotFoundException e) {
            new MessageDialogBuilder()
                    .setTitle("Error al cargar el archivo")
                    .setText("El historial es invalido, favor de eliminarlo")
                    .build()
                    .showDialog(gui);
        } catch (FileNotFoundException e) {
            new MessageDialogBuilder()
                    .setTitle("¡Ups!, parece que no has jugado una partida.")
                    .setText("Pero si ya jugaste asegurate de que el archivo \"listaDeTablas.minas\" esta en la misma carpeta.")
                    .build()
                    .showDialog(gui);
        } catch (IOException e) {
            new MessageDialogBuilder()
                    .setTitle("Error")
                    .setText("Hubo un error al cargar el historial")
                    .build()
                    .showDialog(gui);
        }
    }

    private void handleWinningState(TableroPersonalizado board) {
        if (board.jugadorGanoSinMarcas() == board.getConfiguration().bombCount()) {
            new MessageDialogBuilder()
                    .setTitle("GANASTE")
                    .setText("")
                    .build()
                    .showDialog(gui);
            executeSaveGame(board);
        }
    }

    private void executeSaveGame(TableroPersonalizado tableroDelUsuario) {
        var name = askShouldSaveGame();
        if (name.isPresent()) {
            saveGame(new GameStat(name.get(), tableroDelUsuario.configuration, tableroDelUsuario.getEndedAt()));
        }
    }

    private void saveGame(GameStat gameStat) {
        var message = new MessageDialogBuilder()
            .setTitle("¡Listo!")
            .setText("Tu partida se ha guardado");

        try {
            saveGameStatsIntoFile(gameStat);
        } catch (IOException e) {
            message.setTitle("Hubo un error al guardar la partica")
                .setText("");
        } finally {
            message
                .build()
                .showDialog(gui);
        }
    }

    private Optional<String> askShouldSaveGame() {
        var window = new AskSaveStatsWindow();
        gui.addWindowAndWait(window);
        return window.getName();
    }

    protected Option askOption() {
        AskOptionWindow optionWindow = new AskOptionWindow();
        gui.addWindowAndWait(optionWindow);
        return optionWindow.getOptionSelected();
    }

    protected Configuration askConfiguration() {
        var askConfigWindow = new AskUnsignedIntegerWindow();
        askConfigWindow.setValidator(validator);
        gui.addWindowAndWait(askConfigWindow);
        return askConfigWindow.getConfiguration();
    }

    protected void saveGameStatsIntoFile(GameStat gameStat) throws IOException {
        var previousStats = safeLoadStats();
        var stats = Arrays.copyOf(previousStats, previousStats.length + 1);
        stats[previousStats.length] = gameStat;

        try (var outputStream = new ObjectOutputStream(
                new FileOutputStream(SAVED_FILE_PATH))) {
            outputStream.writeObject(stats);
        }
    }

    protected void deleteGameStats() {

        var messageBuilder = new MessageDialogBuilder()
                .setTitle("Archivos eliminados")
                .setText("");

        try {
            deleteGameStatsFile();
        } catch (NoSuchFileException e) {
            messageBuilder
                    .setTitle("Acción no requerida")
                    .setText("No hay archivos a borrar");
        } catch (IOException e) {
            messageBuilder.setTitle("Error")
                    .setText("Hubo un error al borrar los archivos");
        } finally {
            messageBuilder.build().showDialog(gui);
        }
    }

    protected void deleteGameStatsFile() throws IOException {
        Files.delete(Paths.get(SAVED_FILE_PATH));
    }

    protected GameStat[] loadStats() throws IOException, ClassNotFoundException {
        try (var fis = new FileInputStream(SAVED_FILE_PATH);
                var stream = new ObjectInputStream(fis)) {
            return (GameStat[]) stream.readObject();
        }
    }

    protected GameStat[] safeLoadStats() {
        try {
            return loadStats();
        } catch (ClassNotFoundException | IOException e) {
            return new GameStat[0];
        }
    }


    public Menu random(Random random) {
        this.random = random;

        return this;
    }

    public Menu screen(Screen screen) {
        this.screen = screen;
        this.gui = new MultiWindowTextGUI(screen);
        this.gui.setTheme(SimpleTheme.makeTheme(true,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.BLACK));

        return this;
    }

    public Menu validator(Validator validator) {
        this.validator = validator;
        return this;
    }
}
