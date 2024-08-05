package org.brayancaro.records.menu;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record Configuration(
                @Positive @Max(100) int rows,
                @Positive @Max(100) int columns,
                @Positive @Max(1000) int bombCount) implements Serializable {
}
