/**
 * Clase para representar el tablero.
 * Objetivo. Servir como tablero para el juego buscaminas
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 */
package org.brayancaro;

import java.io.Serializable;

public abstract class Tablero implements Serializable {

    private static final long serialVersionUID = 42l;
    protected Celdas[][] celdas;

    /**
     * Metodo para calcular un numero aleatorio entre un intervalo maximo (dado por el parametro) y el 0
     * @param valorMaximo -- El valor maximo del intervalo
     * @return int -- El numero aleatorio
     */
    public int numeroEnteroAleatorio(int valorMaximo) {
        return (int) (Math.random() * (valorMaximo + 1));
    }

    /**
     * Metodo para convertir una tablero a una cadena de caracteres
     * @return String -- El tablero en formato de cadena
     */
    public String toString() {
        String renglones = " ";
        for (int w = 0; w < celdas[0].length; w++) {
            if (w < 10) {
                renglones += " |" + (w + 1) + "  ";
            } else {
                renglones += " |" + (w + 1) + " ";
            }
        }

        renglones += "\n ";
        for (int p = 0; p < celdas[0].length; p++) {
            renglones += " --- ";
        }
        renglones += "\n";

        for (int i = 0; i < celdas.length; i++) {
            for (int j = 0; j < celdas[0].length; j++) {
                renglones += " | " + celdas[i][j];
            }
            renglones += " | -" + (i + 1) + "\n ";
            for (int k = 0; k < celdas[0].length; k++) {
                renglones += " --- ";
            }
            if (i == (celdas.length - 1)) {} else {
                renglones += "\n";
            }
        }

        return renglones + "\b\b ";
    }
}
