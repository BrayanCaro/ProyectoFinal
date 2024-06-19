/**
 * Clase para representar tableros que ya estan representados.
 * Objetivo. Ser una opcion para quienes quieran personalizar un juego.
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 * @see Tablero
 */
package org.example;

import java.io.*;

public class TableroPersonalizado extends Tablero {

    private static final long serialVersionUID = 42l;

    /**
     * Constructor de un tablero con celdas a partir de un numero de bombas a poner
     * @param filas - Indica la cantidad de filas del tablero
     * @param columnas - Indica la cantidad de columnas del tablero
     * @param numeroDeBombasParaPoner - Indica la cantidad de bombas que tendra el tablero
     */
    public TableroPersonalizado(
        int filas,
        int columnas,
        int numeroDeBombasParaPoner
    ) throws IllegalArgumentException {
        if (numeroDeBombasParaPoner <= 0) {
            throw new IllegalArgumentException("Necesitas poner mas bombas");
        }
        if (numeroDeBombasParaPoner >= filas * columnas) {
            throw new IllegalArgumentException("Necesitas poner menos bombas");
        }
        celdas = new Celdas[filas][columnas];

        for (int i = 0; i < celdas.length; i++) {
            for (int j = 0; j < celdas[0].length; j++) {
                celdas[i][j] = new Celdas();
            }
        }

        for (int i = 0; i < numeroDeBombasParaPoner; i++) {
            int numeroAleratorioFilas = super.numeroEnteroAleatorio(
                this.celdas.length - 1
            );
            int numeroAleratorioColumnas = super.numeroEnteroAleatorio(
                this.celdas[0].length - 1
            );

            if (
                !celdas[numeroAleratorioFilas][numeroAleratorioColumnas].obtenerEstaCeldaTieneUnaBomba()
            ) {
                celdas[numeroAleratorioFilas][numeroAleratorioColumnas].ponerBomba();
                asignarNumeroDeBombasAledañas(
                    numeroAleratorioFilas,
                    numeroAleratorioColumnas
                );
            } else {
                if (i <= (celdas.length + 1) * (celdas[0].length + 1)) {
                    i--;
                }
            }
        }
    }

    /**
     * Metodo para revelar una o mas casillas segun sea el caso
     * @param cordenadaX -- Hace referencia a la posicion que queremos revelar en el eje x
     * @param cordenadaY -- Hace referencia a la posicion que queremos revelar en el eje y
     */
    public void elegirCelda(int cordenadaX, int cordenadaY)
        throws Exception, IndexOutOfBoundsException, IllegalAccessException, TocasteUnaBombaExcepcion {
        if (cordenadaX < 0 || cordenadaY < 0) {
            throw new IndexOutOfBoundsException(
                "El tablero no puede tener cordenadas negativas"
            );
        }

        if (cordenadaX >= celdas.length || cordenadaY >= celdas[0].length) {
            throw new IndexOutOfBoundsException(
                "El tablero no puede tener cordenadas tan grandes"
            );
        }

        if (celdas[cordenadaX][cordenadaY].haSidoVista()) {
            throw new IllegalAccessException("Esta celda ya es visible");
        }

        if (celdas[cordenadaX][cordenadaY].obtenerEstaEstaMarcada()) {
            throw new IllegalAccessException(
                "No puedes ver una mina si ya la marcaste, para hacer eso vuelve a marcar la celda"
            );
        }

        celdas[cordenadaX][cordenadaY].verCelda();

        if (!celdas[cordenadaX][cordenadaY].obtenerEstaCeldaTieneUnaBomba()) {
            if (
                celdas[cordenadaX][cordenadaY].obtenerBombasAlRededorDeEstaCelda() ==
                0
            ) {
                buscarTodosLosCerosEncerrados(
                    this.celdas,
                    cordenadaX,
                    cordenadaY
                );
            }
        } else {
            celdas[cordenadaX][cordenadaY].explotar();
            throw new TocasteUnaBombaExcepcion("Perdiste :(");
        }
    }

    /**
     * Metodo para mostrar todas las bombas si un jugador pierde
     */
    public void mostrarTodasLasBombas() {
        for (int i = 0; i < celdas.length; i++) {
            for (int j = 0; j < celdas[0].length; j++) {
                if (celdas[i][j].obtenerEstaCeldaTieneUnaBomba()) {
                    celdas[i][j].verCelda();
                }
            }
        }
    }

    /*
     * Metodo para calcular todos las casillas que tengan un 0
     * @param cordenadaX -- Hace referencia a la posicion que queremos mover en el eje x
     * @param cordenadaY -- Hace referencia a la posicion que necesito mover en el eje y
     */
    private static void buscarTodosLosCerosEncerrados(
        Celdas[][] celdas,
        int cordenadaX,
        int cordenadaY
    ) {
        celdas[cordenadaX][cordenadaY].verCelda();
        int filas = celdas.length - 1;
        int columnas = celdas[0].length - 1;
        boolean xEsValidoArriba = (cordenadaX + 1) < celdas.length;
        boolean xEsValidoAbajo = (cordenadaX - 1) >= 0;
        boolean yEsValidoIzquierda = 0 <= (cordenadaY - 1);
        boolean yEsValidoDerecha = (cordenadaY + 1) < celdas[0].length;
        boolean xyEsValidoArribaIzquierda =
            xEsValidoArriba && yEsValidoIzquierda;
        boolean xyEsValidoArribaDerecha = xEsValidoArriba && yEsValidoDerecha;
        boolean xyEsValidoAbajoIzquierda = xEsValidoAbajo && yEsValidoIzquierda;
        boolean xyEsValidoAbajoDerecha = xEsValidoAbajo && yEsValidoDerecha;

        if (
            xEsValidoArriba &&
            !celdas[cordenadaX + 1][cordenadaY].haSidoVista() &&
            celdas[cordenadaX +
                1][cordenadaY].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(celdas, cordenadaX + 1, cordenadaY);
        }
        if (
            xEsValidoArriba &&
            !celdas[cordenadaX + 1][cordenadaY].haSidoVista() &&
            celdas[cordenadaX +
                1][cordenadaY].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX + 1][cordenadaY].verCelda();
        }

        if (
            xEsValidoAbajo &&
            !celdas[cordenadaX - 1][cordenadaY].haSidoVista() &&
            celdas[cordenadaX -
                1][cordenadaY].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(celdas, cordenadaX - 1, cordenadaY);
        }
        if (
            xEsValidoAbajo &&
            !celdas[cordenadaX - 1][cordenadaY].haSidoVista() &&
            celdas[cordenadaX -
                1][cordenadaY].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX - 1][cordenadaY].verCelda();
        }

        if (
            yEsValidoIzquierda &&
            !celdas[cordenadaX][cordenadaY - 1].haSidoVista() &&
            celdas[cordenadaX][cordenadaY -
                    1].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(celdas, cordenadaX, cordenadaY - 1);
        }
        if (
            yEsValidoIzquierda &&
            !celdas[cordenadaX][cordenadaY - 1].haSidoVista() &&
            celdas[cordenadaX][cordenadaY -
                    1].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX][cordenadaY - 1].verCelda();
        }

        if (
            yEsValidoDerecha &&
            !celdas[cordenadaX][cordenadaY + 1].haSidoVista() &&
            celdas[cordenadaX][cordenadaY +
                    1].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(celdas, cordenadaX, cordenadaY + 1);
        }
        if (
            yEsValidoDerecha &&
            !celdas[cordenadaX][cordenadaY + 1].haSidoVista() &&
            celdas[cordenadaX][cordenadaY +
                    1].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX][cordenadaY + 1].verCelda();
        }

        if (
            xyEsValidoArribaIzquierda &&
            !celdas[cordenadaX + 1][cordenadaY - 1].haSidoVista() &&
            celdas[cordenadaX + 1][cordenadaY -
                    1].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(
                celdas,
                cordenadaX + 1,
                cordenadaY - 1
            );
        }
        if (
            xyEsValidoArribaIzquierda &&
            !celdas[cordenadaX + 1][cordenadaY - 1].haSidoVista() &&
            celdas[cordenadaX + 1][cordenadaY -
                    1].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX + 1][cordenadaY - 1].verCelda();
        }

        if (
            xyEsValidoArribaDerecha &&
            !celdas[cordenadaX + 1][cordenadaY + 1].haSidoVista() &&
            celdas[cordenadaX + 1][cordenadaY +
                    1].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(
                celdas,
                cordenadaX + 1,
                cordenadaY + 1
            );
        }
        if (
            xyEsValidoArribaDerecha &&
            !celdas[cordenadaX + 1][cordenadaY + 1].haSidoVista() &&
            celdas[cordenadaX + 1][cordenadaY +
                    1].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX + 1][cordenadaY + 1].verCelda();
        }

        if (
            xyEsValidoAbajoIzquierda &&
            !celdas[cordenadaX - 1][cordenadaY - 1].haSidoVista() &&
            celdas[cordenadaX - 1][cordenadaY -
                    1].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(
                celdas,
                cordenadaX - 1,
                cordenadaY - 1
            );
        }
        if (
            xyEsValidoAbajoIzquierda &&
            !celdas[cordenadaX - 1][cordenadaY - 1].haSidoVista() &&
            celdas[cordenadaX - 1][cordenadaY -
                    1].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX - 1][cordenadaY - 1].verCelda();
        }

        if (
            xyEsValidoAbajoDerecha &&
            !celdas[cordenadaX - 1][cordenadaY + 1].haSidoVista() &&
            celdas[cordenadaX - 1][cordenadaY +
                    1].obtenerBombasAlRededorDeEstaCelda() ==
                0
        ) {
            buscarTodosLosCerosEncerrados(
                celdas,
                cordenadaX - 1,
                cordenadaY + 1
            );
        }
        if (
            xyEsValidoAbajoDerecha &&
            !celdas[cordenadaX - 1][cordenadaY + 1].haSidoVista() &&
            celdas[cordenadaX - 1][cordenadaY +
                    1].obtenerBombasAlRededorDeEstaCelda() !=
                0
        ) {
            celdas[cordenadaX - 1][cordenadaY + 1].verCelda();
        }
    }

    /*
     * Metodo para calcular y asignar la candiad de bombas aledañas a una celda
     * @param cordenadaX -- Hace referencia a la posicion que queremos mover en el eje x
     * @param cordenadaY -- Hace referencia a la posicion que necesito mover en el eje y
     */
    private void asignarNumeroDeBombasAledañas(int cordenadaX, int cordenadaY) {
        int filas = celdas.length - 1;
        int columnas = celdas[0].length - 1;
        boolean xEsValidoArriba = (cordenadaX + 1) < celdas.length;
        boolean xEsValidoAbajo = (cordenadaX - 1) >= 0;
        boolean yEsValidoIzquierda = 0 <= (cordenadaY - 1);
        boolean yEsValidoDerecha = (cordenadaY + 1) < celdas[0].length;
        boolean xyEsValidoArribaIzquierda =
            xEsValidoArriba && yEsValidoIzquierda;
        boolean xyEsValidoArribaDerecha = xEsValidoArriba && yEsValidoDerecha;
        boolean xyEsValidoAbajoIzquierda = xEsValidoAbajo && yEsValidoIzquierda;
        boolean xyEsValidoAbajoDerecha = xEsValidoAbajo && yEsValidoDerecha;

        if (
            xEsValidoArriba &&
            !celdas[cordenadaX + 1][cordenadaY].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX +
            1][cordenadaY].aumentarBombasAlRededorDeEstaCelda();
        }
        if (
            xEsValidoAbajo &&
            !celdas[cordenadaX - 1][cordenadaY].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX -
            1][cordenadaY].aumentarBombasAlRededorDeEstaCelda();
        }
        if (
            yEsValidoIzquierda &&
            !celdas[cordenadaX][cordenadaY - 1].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX][cordenadaY -
                1].aumentarBombasAlRededorDeEstaCelda();
        }
        if (
            yEsValidoDerecha &&
            !celdas[cordenadaX][cordenadaY + 1].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX][cordenadaY +
                1].aumentarBombasAlRededorDeEstaCelda();
        }
        if (
            xyEsValidoArribaIzquierda &&
            !celdas[cordenadaX + 1][cordenadaY -
                1].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX + 1][cordenadaY -
                1].aumentarBombasAlRededorDeEstaCelda();
        }
        if (
            xyEsValidoArribaDerecha &&
            !celdas[cordenadaX + 1][cordenadaY +
                1].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX + 1][cordenadaY +
                1].aumentarBombasAlRededorDeEstaCelda();
        }
        if (
            xyEsValidoAbajoIzquierda &&
            !celdas[cordenadaX - 1][cordenadaY -
                1].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX - 1][cordenadaY -
                1].aumentarBombasAlRededorDeEstaCelda();
        }
        if (
            xyEsValidoAbajoDerecha &&
            !celdas[cordenadaX - 1][cordenadaY +
                1].obtenerEstaCeldaTieneUnaBomba()
        ) {
            celdas[cordenadaX - 1][cordenadaY +
                1].aumentarBombasAlRededorDeEstaCelda();
        }
    }

    /**
     * Metodo para marcar una celda durante el juego
     * @param cordenadaX -- Hace referencia a la posicion que queremos mover en el eje x
     * @param cordenadaY -- Hace referencia a la posicion que necesito mover en el eje y
     */
    public void marcarCelda(int cordenadaX, int cordenadaY)
        throws IndexOutOfBoundsException, IllegalAccessException {
        if (cordenadaX < 0 || cordenadaY < 0) {
            throw new IndexOutOfBoundsException(
                "El tablero no puede tener cordenadas negativas"
            );
        }

        if (cordenadaX >= celdas.length || cordenadaY >= celdas[0].length) {
            throw new IndexOutOfBoundsException(
                "El tablero no puede tener cordenadas tan grandes"
            );
        }

        if (celdas[cordenadaX][cordenadaY].haSidoVista()) {
            throw new IllegalAccessException(
                "No puedes marcar una celda que ya es visible"
            );
        }
        celdas[cordenadaX][cordenadaY].marcarCelda();
    }

    /**
     * Metodo para guardar una partida
     * @param nombreDelArchivo -- Refiere al nombre del archivo que centendra la partida
     * @throws FileNotFoundException -- Si el archio no es encontrado
     * @throws RuntimeException -- Si el archivo no puede ser leido, o si el archivo no puede ser escrito
     */
    public void guardarPartidaEnUnArchivo(String nombreDelArchivo)
        throws IOException {
        ObjectOutputStream celdasParaGrabar = new ObjectOutputStream(
            new FileOutputStream(nombreDelArchivo)
        );
        celdasParaGrabar.writeObject(this.celdas);
        celdasParaGrabar.close();
    }

    /**
     * Metodo para cargar una partida de un archivo
     * @param nombreDelArchivo -- Refiere al nombre del archivo que contiene las partidas
     */
    public Celdas[][] cargarPartidaDeUnArchivo(String nombreDelArchivo)
        throws Exception {
        ObjectInputStream celdasParaCargar = new ObjectInputStream(
            new FileInputStream(nombreDelArchivo)
        );
        this.celdas = (Celdas[][]) celdasParaCargar.readObject();
        return this.celdas;
    }

    /**
     * Metodo para preparar a guardar los datos de una partida
     * @param nombreDelArchivo -- Refiere al nombre del archivo que contiene las partidas
     */
    public String dimension() {
        return celdas.length + "×" + celdas[0].length;
    }

    /**
     * Metodo que evalua el tablero y determina si el jugador ya hago
     * @return boolean -- Regresa falso y el jugador no ha ganado, true si el jugador ya gano
     */
    public boolean elJugadorYaGano() {
        return true;
    }

    /**
     * Metodo que dice cuantas celdas hay sin ver (es utilizado para determinar si un jugador gana anque no marca nada)
     * @return int -- Dice la cantidad de casillas que faltan por descubrir
     */
    public int jugadorGanoSinMarcas() {
        int a = 0;

        for (int i = 0; i < celdas.length; i++) {
            for (int j = 0; j < celdas[0].length; j++) {
                if (!celdas[i][j].haSidoVista()) {
                    a++;
                }
            }
        }
        return a;
    }

    /**
     * Metodo que modifica el mapa para cuando el usuario gana
     */
    public void ganador() {
        for (int i = 0; i < celdas.length; i++) {
            for (int j = 0; j < celdas[0].length; j++) {
                if (celdas[i][j].obtenerEstaCeldaTieneUnaBomba()) {
                    celdas[i][j].hacerGanar();
                }
            }
        }
    }

    /**
     * Metodo auxilar para centrar textos
     * @return String -- Los espacios en blanco centrados
     */
    public String centrar() {
        int largo = (celdas.length * 5 - 35) / 2;
        String salida = "";
        for (int i = 0; i < largo; i++) {
            salida += " ";
        }
        return (salida);
    }
}
