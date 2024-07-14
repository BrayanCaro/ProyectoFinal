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
import java.security.SecureRandom;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.brayancaro.enums.menu.Option;
import org.brayancaro.enums.cell.State;
import org.brayancaro.exceptions.menu.InvalidOptionException;
import org.brayancaro.prompts.Prompt;
import org.brayancaro.prompts.PromptInt;
import org.brayancaro.records.Coordinate;

public class Menu {
    public static final String SAVED_FILE_PATH = "listaDeTablas.minas";

    private static String[][] datos = new String[20][4];
    static MiHilo hola = new MiHilo("hola");
    static Thread cronometro = new Thread(hola);

    protected Scanner scanner;

    protected Random random;

    public static void main(String[] args) throws Exception {
        try (var scanner = new Scanner(System.in)) {
            new Menu()
                .setScanner(scanner)
                .random(new SecureRandom())
                .play();
        }
    }

    public void play() throws Exception {
        Option option = null;

        do {
            option = askOption();
            realizarAccion(option);
        } while (option != Option.QUIT);
    }

    public Menu setScanner(Scanner scanner) {
        this.scanner = scanner;

        return this;
    }

    /**
     * Metodo que realiza una accion de acuerdo a las dichas en el metodo menu.
     */
    public void realizarAccion(Option option) throws Exception {
        switch (option) {
            case Option.START -> startGame(option);
            case Option.SHOW_HISTORY -> {
                try {
                    tabla(cargarDatosDeUnArchivo());
                    System.out.print("(Presiona la tecla \"↵\" para salir al menu)");
                    scanner.nextLine();
                    for (int i = 0; i < 45; i++) {
                        System.out.println();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(
                        """
                        ¡Ups!, parece que no has jugado una partida.
                        Pero si ya jugaste asegurate de que el archivo "listaDeTablas.minas" esta en la misma carpeta."""
                    );
                }
            }
            case Option.DELETE_HISTORY -> {
                try {
                    borrarDatos();
                    System.out.println("Listo!, datos borrados\n");
                } catch (NullPointerException e) {
                    System.out.println("No hay nada que borrar\n");
                }
            }
            case Option.QUIT -> System.out.println("Adios 👋");
        }
    }

    private void startGame(Option option) throws Exception, IOException {
        var filas = new PromptInt()
            .min(8)
            .max(29)
            .scanner(scanner)
            .title("¿Con cuantas filas? ")
            .printTitleUsing(System.out::print)
            .ask();

        var columnas = new PromptInt()
            .min(8)
            .max(29)
            .scanner(scanner)
            .title("¿Con cuantas columnas? ")
            .printTitleUsing(System.out::print)
            .ask();

        var bombas = new PromptInt()
            .min(1)
            .max((filas * columnas) - 1)
            .scanner(scanner)
            .title("¿Con cuantas bombas? ")
            .printTitleUsing(System.out::print)
            .ask();

        var board = new TableroPersonalizado(
                filas,
                columnas,
                bombas,
                random
        );

        System.out.println(board);
        System.out.println("EMPECEMOS");
        System.out.println("Hay " + bombas + " bombas en el mapa");
        try {
            cronometro.start();
        } catch (Exception e) {}
        do {
            try {
                executeChangeCellState(bombas, board);
            } catch (TocasteUnaBombaExcepcion e) {
                board.mostrarTodasLasBombas();
                System.out.println("\033[33m" + board + "\033[0m");
                System.out.println(board.centrar() + " 🚫🚫🚫🚫🚫🚫 Perdiste 🚫🚫🚫🚫🚫🚫\n");
                try {
                    cronometro.interrupt();
                } catch (IllegalThreadStateException ee) {}

                break;
            }
            option = handleWinningState(option, bombas, board);
        } while (
            board.jugadorGanoSinMarcas() != bombas &&
            option != Option.QUIT
        );
    }

    private Option handleWinningState(Option option, Integer bombas, TableroPersonalizado board) throws IOException {
        if (board.jugadorGanoSinMarcas() == bombas) {
            board.ganador();
            System.out.println("\n" + board);
            System.out.println(
                board.centrar() +
                " 🎉🎊🎉🎊🎉🎊 !GANASTE! 🎉🎊🎉🎊🎉🎊\n"
            );
            try {
                cronometro.interrupt();
            } catch (Exception e) {}

            executeSaveGame(bombas, board);
            option = Option.QUIT;
        }
        return option;
    }

    private void executeSaveGame(Integer bombas, TableroPersonalizado tableroDelUsuario) throws IOException {
        if (askShouldSaveGame()) {
            saveGame(bombas, tableroDelUsuario);
        } else {
            System.out.println();
        }
    }

    private void saveGame(Integer bombas, TableroPersonalizado tableroDelUsuario) throws IOException {
        var username = new Prompt()
            .scanner(scanner)
            .title("¿Cual es tu nombre? ")
            .printTitleUsing(System.out::print)
            .ask()
            .toLowerCase()
            .trim();

        grabar(
                tableroDelUsuario,
                username,
                bombas,
                hola.tiempo
              );

        guardarDatos();

        System.out.println( "¡Listo!, tu partida se ha guardado");
        System.out.print("(Presiona la tecla \"↵\" para salir al menu)");

        scanner.nextLine();
        for (int i = 0; i < 45; i++) {
            System.out.println();
        }
    }

    private void executeChangeCellState(Integer bombas, TableroPersonalizado tableroDelUsuario) throws Exception {
        var coordinate = askCoordinate();
        var stateAction = askShouldReveal();
        changeCellState(bombas, tableroDelUsuario, coordinate, stateAction);
    }

    private void changeCellState(Integer bombas, TableroPersonalizado tableroDelUsuario, Coordinate coordinate,
            State stateAction) throws Exception {
        try {
            tableroDelUsuario.execute(coordinate, stateAction);
            System.out.printf("""
                    Quedan %d casillas sin ver.
                    Hay %s bombas en el mapa
                    """, tableroDelUsuario.jugadorGanoSinMarcas(), bombas);
            System.out.println(tableroDelUsuario);
        } catch (NumberFormatException e) {
            System.out.println("El comando solo puede tener 2 numeros separados por un espacio");
        } catch (InputMismatchException e) {
            System.out.println(e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        }
    }

    private boolean askShouldSaveGame() {
        Pattern pattern = Pattern.compile("\\s*[sn]\\s*", Pattern.CASE_INSENSITIVE);

        return new Prompt()
            .pattern(pattern)
            .scanner(scanner)
            .title("¿Quieres guardar tu partida? (s/n) ")
            .printTitleUsing(System.out::print)
            .ask()
            .toLowerCase()
            .contains("s");
    }

    private State askShouldReveal() {
        Pattern pattern = Pattern.compile("\\s*[mv]\\s*", Pattern.CASE_INSENSITIVE);

        var isRevealed = new Prompt()
            .pattern(pattern)
            .scanner(scanner)
            .title( "¿Quieres marcar o ver esa celda? (m/v) ")
            .printTitleUsing(System.out::print)
            .ask()
            .trim()
            .toLowerCase()
            .contains("v");

        return isRevealed ? State.REVEALED : State.MARKED;
    }

    private Coordinate askCoordinate() {
        Pattern pattern = Pattern.compile(Coordinate.PATTERN);

        var value = new Prompt()
            .pattern(pattern)
            .scanner(scanner)
            .title("Introduce la cordenada > ")
            .printTitleUsing(System.out::print)
            .ask();

        return Coordinate.parse(value);
    }

    protected Option askOption() throws InvalidOptionException {
        return Option.fromIndex(new PromptInt()
            .min(1)
            .max(4)
            .scanner(scanner)
            .title(Option.getPrintOptionsText())
            .ask());
    }

    /**
     * Metodo para guardar una partida
     * @param nombreDelArchivo -- Refiere al nombre del archivo que centendra la partida
     * @throws FileNotFoundException -- Si el archio no es encontrado
     * @throws RuntimeException -- Si el archivo no puede ser leido, o si el archivo no puede ser escrito
     */
    public static void guardarDatos() throws IOException {
        try (var guardarTabla = new ObjectOutputStream(
            new FileOutputStream(SAVED_FILE_PATH)
        )) {
            guardarTabla.writeObject(Menu.datos);
        }
    }

    /**
     * Metodo para guardar una partida
     * @param nombreDelArchivo -- Refiere al nombre del archivo que centendra la partida
     * @throws FileNotFoundException -- Si el archio no es encontrado
     * @throws RuntimeException -- Si el archivo no puede ser leido, o si el archivo no puede ser escrito
     */
    public static void borrarDatos() throws IOException, NullPointerException {
        if (datos[0][0] == null) {
            throw new NullPointerException();
        }
        String[][] listaVacia = datos;
        for (int i = 0; i < 20; i++) {
            if (datos[i][0] != null) {
                listaVacia[i][0] = null;
                listaVacia[i][1] = null;
                listaVacia[i][2] = null;
                listaVacia[i][3] = null;
            }
        }

        try (var guardarTabla = new ObjectOutputStream(
            new FileOutputStream(SAVED_FILE_PATH)
        )) {
            guardarTabla.writeObject(listaVacia);
        }
    }

    /**
     * Metodo para cargar una partida de un archivo
     * @param nombreDelArchivo -- Refiere al nombre del archivo que contiene las partidas
     */
    public static String[][] cargarDatosDeUnArchivo() throws IOException, ClassNotFoundException {
        try (var stream = new ObjectInputStream(
            new FileInputStream(SAVED_FILE_PATH)
        )) {
            Menu.datos = (String[][]) stream.readObject();
            return Menu.datos;
        }
    }

    /**
     * Metodo que imprime el menu
     * @param arreglo -- Indica el arreglo de arreglos de Strings que va a imprimir
     */
    public static void tabla(String[][] arreglo) {
        if (arreglo[0][0] == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < 45; i++) {
            System.out.println();
        }
        for (int i = 0; i < 45; i++) {
            System.out.print("=");
        }
        System.out.print("\n");
        System.out.print("| Nombre | Dimension | No.Bombas |  Tiempo  |\n");
        for (int i = 0; i < 45; i++) {
            System.out.print("=");
        }
        System.out.print("\n");
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i][0] != null) {
                System.out.print(
                    "| " +
                    arreglo[i][0].concat("      ").substring(0, 6) +
                    " |   " +
                    arreglo[i][1].concat("   ").substring(0, 6) +
                    "  |    " +
                    arreglo[i][2].concat("  ").substring(0, 3) +
                    "    | " +
                    arreglo[i][3] +
                    " |\n"
                );
            }
        }
        for (int i = 0; i < 45; i++) {
            System.out.print("=");
        }
        System.out.print("\n");
    }

    public static void grabar(
        TableroPersonalizado tablero,
        String nombre,
        int bombas,
        String tiempo
    ) {
        if (datos[19][0] != null) {
            throw new IllegalArgumentException("El tablero esta lleno");
        }
        int i = 0;
        boolean aux = false;
        do {
            if (datos[i][0] == null) {
                datos[i][0] = nombre;
                datos[i][1] = tablero.dimension();
                datos[i][2] = bombas + "";
                datos[i][3] = tiempo;
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
}
