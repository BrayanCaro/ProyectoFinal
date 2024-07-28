/**
 * Clase para representar el tablero.
 * Objetivo. Servir como tablero para el juego buscaminas
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 */
package org.brayancaro;

import java.io.Serializable;
import java.util.Random;

public abstract class Tablero implements Serializable {

    private static final long serialVersionUID = 42l;
    protected Celdas[][] celdas;

    protected Random random;

    /**
     * Metodo para calcular un numero aleatorio entre un intervalo maximo (dado por el parametro) y el 0
     * @param valorMaximo -- El valor maximo del intervalo
     * @return int -- El numero aleatorio
     */
    public int numeroEnteroAleatorio(int valorMaximo) {
        return random.nextInt(0, valorMaximo + 1);
    }

    public Tablero random(Random random) {
        this.random = random;

        return this;
    }
}
