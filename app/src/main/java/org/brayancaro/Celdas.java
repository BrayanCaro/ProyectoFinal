/**
 * Clase para representar las celdas de un tablero.
 * Objetivo. Servir como base para la clase tablero
 * @author Brayan Martinez Santana
 * @version Primera version, Domingo 2 de Diciembre, 2018
 */
package org.brayancaro;

import java.io.Serializable;

public class Celdas implements Serializable {

    private static final long serialVersionUID = 42l;
    private int bombasAlRededorDeEstaCelda;
    private boolean estaCeldaTieneUnaBomba;
    private boolean estaCeldaEstaMarcada;
    private boolean estaCeldaPuedeVerse;
    private boolean explocion;
    private boolean ganador;

    /**
     * Constructor de una celda, sin bombas y sin marcar
     */
    public Celdas() {
        estaCeldaTieneUnaBomba = false;
        estaCeldaEstaMarcada = false;
        estaCeldaPuedeVerse = false;
    }

    /**
     * Metodo que cambia la celda a la de un ganador
     */
    public void hacerGanar() {
        ganador = true;
        estaCeldaEstaMarcada = false;
    }

    /**
     * Metodo para saber si una celda esta marcada
     */
    public boolean obtenerEstaEstaMarcada() {
        return estaCeldaEstaMarcada;
    }

    /**
     * Metodo para poner una bomba en una celda
     */
    public void ponerBomba() {
        estaCeldaTieneUnaBomba = true;
    }

    /**
     * Metodo para poner una bomba en una celda
     */
    public void marcarCelda() {
        estaCeldaEstaMarcada = !this.estaCeldaEstaMarcada;
    }

    /**
     * Metodo para elegir una celda
     */
    public void verCelda() {
        estaCeldaPuedeVerse = true;
        estaCeldaEstaMarcada = false;
    }

    /**
     * Metodo para conocer el estado de una celda (si tiene una bomba o no)
     * @return boolean -- Si tiene una bomba o no
     */
    public boolean obtenerEstaCeldaTieneUnaBomba() {
        return estaCeldaTieneUnaBomba;
    }

    /**
     * Metodo para cambiar el estado de una celda (aumentar el numero de bombas aledañas a la celda en 1)
     */
    public void aumentarBombasAlRededorDeEstaCelda() {
        bombasAlRededorDeEstaCelda += 1;
    }

    /**
     * Metodo para conocer el estado de la celda (si ya ha sido vista o no)
     * @return boolean -- Si esta celda ya ha sido vista
     */
    public boolean haSidoVista() {
        return estaCeldaPuedeVerse;
    }

    /**
     * Metodo para ver cuantas minas tiene una celda a su alrededor
     * @return int -- La cantidad de bombas que hay alrededor de una celda
     */
    public int obtenerBombasAlRededorDeEstaCelda() {
        return bombasAlRededorDeEstaCelda;
    }

    /*
     * Metodo privado para explotar una mina
     */
    public void explotar() {
        explocion = true;
    }

    /**
     * Metodo para convertir una celda a una pequeña cadena de caracteres
     * @return String -- La celda en formato de cadena
     */
    public String toString() {
        if (estaCeldaEstaMarcada) {
            return "🚩";
        } else if (estaCeldaPuedeVerse) {
            if (estaCeldaTieneUnaBomba) {
                if (explocion) {
                    return "💥";
                }
                return "💣";
            } else if (bombasAlRededorDeEstaCelda == 0) {
                return "  ";
            } else {
                return " " + bombasAlRededorDeEstaCelda;
            }
        } else if (ganador) {
            return "🏆";
        } else {
            return "❓";
        }
    }
}
