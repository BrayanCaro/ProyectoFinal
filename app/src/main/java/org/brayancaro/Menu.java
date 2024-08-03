/**
 * Clase para representar el menu
 * Objetivo. Simular un menu para un usuario
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 */
package org.brayancaro;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import org.brayancaro.enums.menu.Option;
import org.brayancaro.gui.windows.AskOptionWindow;
import org.brayancaro.gui.windows.AskSaveStatsWindow;
import org.brayancaro.gui.windows.AskUnsignedIntegerWindow;
import org.brayancaro.gui.windows.GameWindow;
import org.brayancaro.gui.windows.ListGamesWindow;
import org.brayancaro.records.menu.Configuration;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class Menu {
    public static final String SAVED_FILE_PATH = "listaDeTablas.minas";

    private static String[][] datos = new String[20][4];

    protected Random random;

    private Screen screen;

    private MultiWindowTextGUI gui;

    public static void main(String[] args) throws Exception {
        var defaultTerminalFactory = new DefaultTerminalFactory();

        try (var terminal = defaultTerminalFactory.createTerminal();
                var screen = new TerminalScreen(terminal)) {

            (new Menu())
                    .random(new SecureRandom())
                    .screen(screen)
                    .play();
        }

    }

    public void play() throws Exception {
        screen.startScreen();

        Option option = null;
        do {
            option = askOption();
            if (option instanceof Option) {
                realizarAccion(option);
            }
        } while (option != Option.QUIT);

        screen.stopScreen();
    }

    /*
     * Metodo que realiza una accion de acuerdo a las dichas en el metodo menu.
     */
    public void realizarAccion(Option option) throws Exception {
        switch (option) {
            case Option.START -> startGame();
            case Option.SHOW_HISTORY -> showHistory();
            case Option.DELETE_HISTORY -> deleteGameStats();
            case Option.QUIT -> System.out.println("Adios 👋");
        }
    }

    private void startGame() throws IOException {
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

    private void showHistory() throws ClassNotFoundException, IOException {
        try {
            gui.addWindowAndWait(new ListGamesWindow(cargarDatosDeUnArchivo()));
        } catch (FileNotFoundException e) {
            new MessageDialogBuilder()
                    .setTitle("¡Ups!, parece que no has jugado una partida.")
                    .setText(
                            "Pero si ya jugaste asegurate de que el archivo \"listaDeTablas.minas\" esta en la misma carpeta.")
                    .build()
                    .showDialog(gui);
        }
    }

    private void handleWinningState(TableroPersonalizado board) throws IOException {
        if (board.jugadorGanoSinMarcas() == board.getConfiguration().bombCount()) {
            new MessageDialogBuilder()
                    .setTitle("GANASTE")
                    .setText("")
                    .build()
                    .showDialog(gui);
            executeSaveGame(board);
        }
    }

    private void executeSaveGame(TableroPersonalizado tableroDelUsuario) throws IOException {
        var name = askShouldSaveGame();
        if (name.isPresent()) {
            saveGame(name.get(), tableroDelUsuario);
        }
    }

    private void saveGame(String name, TableroPersonalizado tableroDelUsuario) throws IOException {
        grabar(tableroDelUsuario, name);

        guardarDatos();

        new MessageDialogBuilder()
                .setTitle("¡Listo!")
                .setText("Tu partida se ha guardado")
                .build()
                .showDialog(gui);
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
        gui.addWindowAndWait(askConfigWindow);
        return askConfigWindow.getConfiguration();
    }

    /**
     * Metodo para guardar una partida
     *
     * @param nombreDelArchivo -- Refiere al nombre del archivo que centendra la
     *                         partida
     * @throws FileNotFoundException -- Si el archio no es encontrado
     * @throws RuntimeException      -- Si el archivo no puede ser leido, o si el
     *                               archivo no puede ser escrito
     */
    public static void guardarDatos() throws IOException {
        try (var guardarTabla = new ObjectOutputStream(
                new FileOutputStream(SAVED_FILE_PATH))) {
            guardarTabla.writeObject(Menu.datos);
        }
    }

    protected void deleteGameStats() {

        var messageBuilder = new MessageDialogBuilder()
                .setTitle("Acción no requerida")
                .setText("No hay archivos a borrar");

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

    /**
     * Metodo para cargar una partida de un archivo
     *
     * @param nombreDelArchivo -- Refiere al nombre del archivo que contiene las
     *                         partidas
     */
    public static String[][] cargarDatosDeUnArchivo() throws IOException, ClassNotFoundException {
        try (var stream = new ObjectInputStream(
                new FileInputStream(SAVED_FILE_PATH))) {
            Menu.datos = (String[][]) stream.readObject();
            return Menu.datos;
        }
    }

    public static void grabar(
            TableroPersonalizado tablero,
            String nombre) {
        if (datos[19][0] != null) {
            throw new IllegalArgumentException("El tablero esta lleno");
        }
        int i = 0;
        boolean aux = false;
        do {
            if (datos[i][0] == null) {
                datos[i][0] = nombre;
                datos[i][1] = tablero.dimension();
                datos[i][2] = tablero.getConfiguration().bombCount() + "";
                datos[i][3] = tablero.getEndedAt().format(DateTimeFormatter.RFC_1123_DATE_TIME);
                aux = true;
            } else {
                i++;
            }
        } while (i < 20 && !aux);
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
}
