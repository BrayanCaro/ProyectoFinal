package org.brayancaro.records.board;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.brayancaro.records.menu.Configuration;

public record GameStat(String alias, Configuration boardConfiguration, ZonedDateTime endedAt) implements Serializable {
    public String[] toRow() {
        return new String[] {
                alias,
                boardConfiguration.columns() + "",
                boardConfiguration.rows() + "",
                boardConfiguration.bombCount() + "",
                endedAt.format(DateTimeFormatter.RFC_1123_DATE_TIME),
        };
    }

    public static String[] headers() {
        return new String[] {
                "Nombre",
                "Filas",
                "Columnas",
                "No.Bombas",
                "Tiempo"
        };
    }
}
