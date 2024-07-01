package org.brayancaro.enums.menu;

import org.brayancaro.exceptions.menu.InvalidOptionException;

public enum Option {
    START,
    SHOW_HISTORY,
    DELETE_HISTORY,
    QUIT;

    public static Option fromIndex(int index) throws InvalidOptionException {
        return switch (index) {
            case 1 -> START;
            case 2 -> SHOW_HISTORY;
            case 3 -> DELETE_HISTORY;
            case 4 -> QUIT;
            default -> throw new InvalidOptionException();
        };
    }

    public static String getPrintOptionsText() {
        return """
            ⧄------------------------------------⧅
            |          ¿Que quieres hacer?       |
            | 1. Jugar una partida personalizada |
            | 2. Ver registros                   |
            | 3. Borrar registros                |
            | 4. Salir                           |
            ⧅------------------------------------⧄""";
    }

    public static void printOptionsText() {
        System.out.println(getPrintOptionsText());
    }
}
