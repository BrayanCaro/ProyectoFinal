package org.brayancaro.records.menu;

import java.io.Serializable;

public record Configuration(int rows, int columns, int bombCount) implements Serializable {
}
