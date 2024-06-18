/**
 * Clase para representar el menu
 * Objetivo. Simular un menu para un usuario
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 */

import java.io.*;
import java.lang.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private static Scanner io;
    private static TableroPersonalizado tableroEstatico;
    private static String[][] datos = new String[20][4];
    private static boolean ayuda = true;
    static MiHilo hola = new MiHilo("hola");
    static Thread cronometro = new Thread(hola);

    public static void main(String[] args) {
        int opcion;
        io = new Scanner(System.in);
        try {
            do {
                menu();
                opcion = Integer.parseInt(new Scanner(System.in).next());
                if (ayuda) {
                    realizarAccion(opcion);
                }
            } while (ayuda && opcion != 4);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            System.out.println("Escribe solo un numero del 1 al 3\n");
            main(args);
        } catch (TocasteUnaBombaExcepcion e) {
            tableroEstatico.mostrarTodasLasBombas();
            System.out.println("\033[33m" + tableroEstatico + "\033[0m");
            System.out.println(
                tableroEstatico.centrar() +
                " 🚫🚫🚫🚫🚫🚫 Perdiste 🚫🚫🚫🚫🚫🚫\n"
            );
            try {
                cronometro.interrupt();
            } catch (IllegalThreadStateException ee) {}
            main(args);
        } catch (InputMismatchException e) {
            System.out.println("Escribe solo un numero del 1 al 3\n");
            main(args);
        } catch (Exception e) {
            System.out.println(e);
            main(args);
        }
    }

    /**
     * Metodo estatico que solo imprime el menu
     */
    private static void menu() {
        System.out.println(" ⧄------------------------------------⧅");
        System.out.println(" |          ¿Que quieres hacer?       |");
        System.out.println(" | 1. Jugar una partida personalizada |");
        System.out.println(" | 2. Ver registros                   |");
        System.out.println(" | 3. Borrar registros                |");
        System.out.println(" | 4. Salir                           |");
        System.out.println(" ⧅------------------------------------⧄");
    }

    /**
     * Metodo que realiza una accion de acuerdo a las dichas en el metodo menu.
     * @param opcion -- Indica la opcion a realizar
     */
    public static void realizarAccion(int opciones)
        throws TocasteUnaBombaExcepcion, ComandoErroneoExcepcion, Exception {
        switch (opciones) {
            case 1:
                System.out.print("¿Con cuantas filas? ");
                int filas = Integer.parseInt(new Scanner(System.in).next());

                try {
                    if (filas < 8) {
                        throw new IllegalArgumentException(
                            "Necesitas poner 8 o mas filas"
                        );
                    }
                    if (filas > 29) {
                        throw new IllegalArgumentException(
                            "Necesitas poner 29 o menos filas"
                        );
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                    realizarAccion(1);
                } catch (InputMismatchException e) {
                    System.out.println(
                        "Las columas solo se escriben con numeros\n"
                    );
                }

                System.out.print("¿Con cuantas columnas? ");
                int columnas = Integer.parseInt(new Scanner(System.in).next());
                try {
                    if (columnas < 8) {
                        throw new IllegalArgumentException(
                            "Necesitas poner 8 o mas columnas"
                        );
                    }
                    if (columnas > 29) {
                        throw new IllegalArgumentException(
                            "Necesitas poner 29 0 menos columnas"
                        );
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                    realizarAccion(1);
                }

                System.out.print("¿Con cuantas bombas? ");
                int bombas = Integer.parseInt(new Scanner(System.in).next());

                TableroPersonalizado tableroDelUsuario;

                try {
                    if (bombas <= 0) {
                        throw new IllegalArgumentException(
                            "Necesitas poner mas bombas"
                        );
                    }
                    if (bombas >= (filas * columnas)) {
                        throw new IllegalArgumentException(
                            "Necesitas poner menos bombas"
                        );
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                    realizarAccion(1);
                }

                tableroDelUsuario = new TableroPersonalizado(
                    filas,
                    columnas,
                    bombas
                );
                tableroEstatico = tableroDelUsuario;
                System.out.println(tableroDelUsuario);
                System.out.println("EMPECEMOS");
                System.out.println("Hay " + bombas + " bombas en el mapa");
                try {
                    cronometro.start();
                } catch (Exception e) {}
                do {
                    System.out.print("Introduce la cordenada > ");
                    String comando = new Scanner(System.in).nextLine();
                    comando.trim();
                    if (comando.contains(" ")) {
                        try {
                            String[] resultados = comando.split(" ");
                            if (!comando.contains(" ")) {
                                throw new ComandoErroneoExcepcion(
                                    "Comando erroneo, pon solo 2 numeros separados por un espacio"
                                );
                            }
                            if (resultados.length != 2) {
                                throw new ComandoErroneoExcepcion(
                                    "Comando erroneo, pon solo 2 numeros separados por un espacio"
                                );
                            }

                            int cordenadaX = Integer.parseInt(resultados[0]);
                            if (cordenadaX < 0) {
                                throw new IndexOutOfBoundsException(
                                    "Introduce numeros positivos"
                                );
                            }
                            int cordenadaY = Integer.parseInt(resultados[1]);
                            if (cordenadaY < 0) {
                                throw new IndexOutOfBoundsException(
                                    "Introduce numeros positivos"
                                );
                            }

                            System.out.print(
                                "¿Quieres marcar o ver esa celda? (m/v) "
                            );
                            comando = new Scanner(System.in).nextLine();
                            comando.trim().toLowerCase();

                            if (comando.contains("m")) {
                                System.out.println(comando.contains("m"));
                                tableroDelUsuario.marcarCelda(
                                    cordenadaY - 1,
                                    cordenadaX - 1
                                );
                            } else if (comando.contains("v")) {
                                tableroDelUsuario.elegirCelda(
                                    cordenadaY - 1,
                                    cordenadaX - 1
                                );
                            } else {
                                throw new InputMismatchException(
                                    "Por favor Introduce \"m\" o \"v\""
                                );
                            }
                            System.out.println(
                                "Quedan " +
                                tableroDelUsuario.jugadorGanoSinMarcas() +
                                " casillas sin ver."
                            );
                            System.out.println(
                                "Hay " + bombas + " bombas en el mapa"
                            );
                            System.out.println(tableroDelUsuario);
                        } catch (NumberFormatException e) {
                            System.out.println(
                                "El comando solo puede tener 2 numeros separados por un espacio"
                            );
                        } catch (InputMismatchException e) {
                            System.out.println(e);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(e);
                        } catch (IllegalAccessException e) {
                            System.out.println(e);
                        } catch (ComandoErroneoExcepcion e) {
                            System.out.println(e);
                        }
                    } else {
                        try {
                            throw new InputMismatchException("Comando erroneo");
                        } catch (InputMismatchException e) {
                            System.out.println(e);
                        }
                    }

                    if (tableroDelUsuario.jugadorGanoSinMarcas() == bombas) {
                        tableroDelUsuario.ganador();
                        System.out.println("\n" + tableroDelUsuario);
                        System.out.println(
                            tableroEstatico.centrar() +
                            " 🎉🎊🎉🎊🎉🎊 !GANASTE! 🎉🎊🎉🎊🎉🎊\n"
                        );
                        try {
                            cronometro.interrupt();
                        } catch (Exception e) {}
                        boolean aux = true;
                        do {
                            System.out.print(
                                "¿Quieres guardar tu partida? (s/n) "
                            );
                            String guardarPartida = new Scanner(
                                System.in
                            ).nextLine();
                            guardarPartida.toLowerCase();

                            if (guardarPartida.contains("s")) {
                                System.out.print("¿Cual es tu nombre? ");
                                String usuario = new Scanner(
                                    System.in
                                ).nextLine();
                                grabar(
                                    tableroDelUsuario,
                                    usuario,
                                    bombas,
                                    hola.tiempo
                                );
                                guardarDatos();
                                System.out.print(
                                    "¡Listo!, tu partida se ha guardado\n"
                                );
                                System.out.print(
                                    "\n(Presiona la tecla \"↵\" para salir al menu)"
                                );
                                String enter = new Scanner(
                                    System.in
                                ).nextLine();
                                for (int i = 0; i < 45; i++) {
                                    System.out.println();
                                }
                                aux = false;
                                opciones = 4;
                            } else if (guardarPartida.contains("n")) {
                                System.out.println();
                                aux = false;
                                opciones = 4;
                                break;
                            } else {
                                System.out.println("Elije una opcion");
                            }
                        } while (aux);

                        opciones = 4;
                    }
                } while (
                    tableroDelUsuario.jugadorGanoSinMarcas() != bombas &&
                    opciones != 4
                );
                break;
            case 2:
                try {
                    tabla(cargarDatosDeUnArchivo());
                    System.out.print(
                        "(Presiona la tecla \"↵\" para salir al menu)"
                    );
                    String enter = new Scanner(System.in).nextLine();
                    for (int i = 0; i < 45; i++) {
                        System.out.println();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(
                        "¡Ups!, parece que no has jugado una partida."
                    );
                    System.out.println(
                        "Pero si ya jugaste asegurate de que el archivo \"listaDeTablas.minas\" esta en la misma carpeta.\n"
                    );
                } catch (IllegalArgumentException e) {
                    System.out.println(
                        "No hay nada que mostrar, juega y guarda una partida\n"
                    );
                }
                break;
            case 3:
                try {
                    borrarDatos();
                    System.out.println("Listo!, datos borrados\n");
                } catch (NullPointerException e) {
                    System.out.println("No hay nada que borrar\n");
                }
                break;
            case 4:
                System.out.println("Adios 👋");
                ayuda = false;
                break;
            default:
                System.out.println("Esa opcion no se puede elegir\n");
                break;
        }
    }

    /**
     * Metodo para guardar una partida
     * @param nombreDelArchivo -- Refiere al nombre del archivo que centendra la partida
     * @throws FileNotFoundException -- Si el archio no es encontrado
     * @throws RuntimeException -- Si el archivo no puede ser leido, o si el archivo no puede ser escrito
     */
    public static void guardarDatos() throws IOException {
        ObjectOutputStream guardarTabla = new ObjectOutputStream(
            new FileOutputStream("listaDeTablas.minas")
        );
        guardarTabla.writeObject(Menu.datos);
        guardarTabla.close();
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

        ObjectOutputStream guardarTabla = new ObjectOutputStream(
            new FileOutputStream("listaDeTablas.minas")
        );
        guardarTabla.writeObject(listaVacia);
        guardarTabla.close();
    }

    /**
     * Metodo para cargar una partida de un archivo
     * @param nombreDelArchivo -- Refiere al nombre del archivo que contiene las partidas
     */
    public static String[][] cargarDatosDeUnArchivo() throws Exception {
        ObjectInputStream celdasParaCargar = new ObjectInputStream(
            new FileInputStream("listaDeTablas.minas")
        );
        Menu.datos = (String[][]) celdasParaCargar.readObject();
        return Menu.datos;
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
}
