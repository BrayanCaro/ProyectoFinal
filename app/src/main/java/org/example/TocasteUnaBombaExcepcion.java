/**
 * Excepcion para el manejo de bombas en una celda
 * @author Brayan Martinez Santana
 * @version @version Primera version, Lunes 3 de Diciembre, 2018
 */
package org.example;

public class TocasteUnaBombaExcepcion extends Exception {

    private static final long serialVersionUID = 42l;

    public TocasteUnaBombaExcepcion(String a) {
        super(a);
    }
}
