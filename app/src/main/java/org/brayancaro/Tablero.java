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
     * Random unsigned integer between 0 and bound (exclusive)
     * @param int bound Exclusive
     */
    protected int randomUnsignedInt(int bound) {
        return random.nextInt(0, bound);
    }


    public Tablero random(Random random) {
        this.random = random;

        return this;
    }
}
