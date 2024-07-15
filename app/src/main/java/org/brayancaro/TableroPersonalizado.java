/**
 * Clase para representar tableros que ya estan representados.
 * Objetivo. Ser una opcion para quienes quieran personalizar un juego.
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 * @see Tablero
 */
package org.brayancaro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.brayancaro.enums.cell.State;
import org.brayancaro.records.Coordinate;

public class TableroPersonalizado extends Tablero {

    private static final long serialVersionUID = 42l;

    protected int rows;

    protected int columns;

    /**
     * Constructor de un tablero con celdas a partir de un numero de bombas a poner
     * @param filas - Indica la cantidad de filas del tablero
     * @param columnas - Indica la cantidad de columnas del tablero
     * @param numeroDeBombasParaPoner - Indica la cantidad de bombas que tendra el tablero
     */
    public TableroPersonalizado(
        int filas,
        int columnas,
        int numeroDeBombasParaPoner,
        Random random
    ) throws IllegalArgumentException {
        rows = filas;
        columns = columnas;

        random(random);

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

            var coordinate = new Coordinate(numeroAleratorioFilas, numeroAleratorioColumnas);

            if (
                !celdas[numeroAleratorioFilas][numeroAleratorioColumnas].obtenerEstaCeldaTieneUnaBomba()
            ) {
                celdas[numeroAleratorioFilas][numeroAleratorioColumnas].ponerBomba();
                setupBombCountForNeigbours(coordinate);
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

        var coordinate = new Coordinate(cordenadaX, cordenadaY);

        celdas[cordenadaX][cordenadaY].verCelda();

        if (!celdas[cordenadaX][cordenadaY].obtenerEstaCeldaTieneUnaBomba()) {
            if (
                celdas[cordenadaX][cordenadaY].obtenerBombasAlRededorDeEstaCelda() ==
                0
            ) {
                visitCellsWihoutBombs(coordinate);
            }
        } else {
            celdas[cordenadaX][cordenadaY].explotar();
            throw new TocasteUnaBombaExcepcion("Perdiste :(");
        }
    }

    public void elegirCelda(Coordinate coordinate) throws IndexOutOfBoundsException, IllegalAccessException, TocasteUnaBombaExcepcion, Exception {
        // FIXME: resolve wrong parameters, affects only non quadratic boards
        elegirCelda(coordinate.y(), coordinate.x());
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

    /**
     * Uses BFS to visit hidden cells without bombs.
     *
     * This method performs a breadth-first search (BFS) to reveal all connected cells
     * that do not contain bombs. At the end of the search, only cells that are
     * neighbors of a bomb-containing cell will also be revealed.
     *
     * @param coordinate the starting coordinate from which to begin the search
     */
    private void visitCellsWihoutBombs(Coordinate coordinate) {
        celdas[coordinate.x()][coordinate.y()].verCelda();

        var neighbours = neighbours(coordinate);

        neighbours.removeIf((Coordinate coord)  ->  celdas[coord.x()][coord.y()].haSidoVista());

        for (var neighbourCoordinate : neighbours) {
            Celdas cell = celdas[neighbourCoordinate.x()][neighbourCoordinate.y()];
            if (cell.obtenerBombasAlRededorDeEstaCelda() == 0) {
                visitCellsWihoutBombs(neighbourCoordinate);
            } else {
                cell.verCelda();
            }
        }
    }

    /**
     * Adjusts the bomb count for the neighboring cells.
     *
     * This method updates the bomb count for each cell adjacent to the given coordinate.
     * It is intended to be used during the setup of the board to correctly reflect
     * the number of bombs surrounding each cell.
     *
     * @param coordinate the coordinate of the cell for which to adjust the bomb count
     */
    private void setupBombCountForNeigbours(Coordinate coordinate) {
        for (var neighbourCoordinate : neighbours(coordinate)) {
            Celdas cell = celdas[neighbourCoordinate.x()][neighbourCoordinate.y()];
            if (!cell.obtenerEstaCeldaTieneUnaBomba()) {
                cell.aumentarBombasAlRededorDeEstaCelda();
            }
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

    public void marcarCelda(Coordinate coordinate) throws IndexOutOfBoundsException, IllegalAccessException {
        // FIXME: resolve wrong parameters, affects only non quadratic boards
        marcarCelda(coordinate.y(), coordinate.x());
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

    /**
     * TODO improve error handling
     */
    public void execute(Coordinate coordinate, State state) throws Exception {
        switch (state) {
            case MARKED  -> marcarCelda(coordinate);
            case REVEALED -> elegirCelda(coordinate);
        }
    }

    public List<Coordinate> neighbours(Coordinate coordinate) {
        int x = coordinate.x();
        int y = coordinate.y();

        var coordinates = new ArrayList<Coordinate>();

        for (int i = Math.max(0, x - 1); i <= Math.min(rows - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(columns - 1, y + 1); j++) {
                if (i == x && j == y) {
                    continue;
                }
                coordinates.add(new Coordinate(i, j));
            }
        }

        return coordinates;
    }
}
