package org.brayancaro.exceptions.menu;

public class InvalidOptionException  extends Exception {
    public void reportToUser() {
        System.out.println("Escribe solo un valor del 1 al 3\n");
    }
}
