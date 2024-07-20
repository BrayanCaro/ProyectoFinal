package org.brayancaro.enums.menu;

public enum Option {
    START,
    SHOW_HISTORY,
    DELETE_HISTORY,
    QUIT;

    @Override
    public String toString() {
        return switch (this) {
            case START -> "Jugar una partida personalizada";
            case SHOW_HISTORY -> "Ver registros";
            case DELETE_HISTORY -> "Borrar registros";
            case QUIT -> "Salir";
        };
    }
}
