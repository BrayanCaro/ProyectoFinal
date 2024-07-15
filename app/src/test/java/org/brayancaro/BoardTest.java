package org.brayancaro;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.brayancaro.records.Coordinate;
import org.junit.Before;
import org.junit.Test;

/**
 * BoardTest
 */
public class BoardTest {
    protected TableroPersonalizado board;

    @Before
    public void init() {
        int rows = 5;
        int colums = 6;
        int bombsCount = 1;
        board = new TableroPersonalizado(rows, colums, bombsCount, new Random(120));
    }

    @Test
    public void neighboursListIsValidForSimpleCase() {
        var expected = Arrays.asList(
                new Coordinate(1, 1),
                new Coordinate(1, 2),
                new Coordinate(1, 3),
                new Coordinate(2, 1),
                new Coordinate(2, 3),
                new Coordinate(3, 1),
                new Coordinate(3, 2),
                new Coordinate(3, 3)
        );
        var current = board.neighbours(new Coordinate(2, 2));

        assertEquals("Coordinate neighbours is wrong", new HashSet<>(expected), new HashSet<>(current));
    }

    @Test
    public void neighbourListIsValidEvenNearbyBorders() {
        var expected = Arrays.asList(
                new Coordinate(0, 1),
                new Coordinate(1, 0),
                new Coordinate(1, 1)
        );
        var current = board.neighbours(new Coordinate(0, 0));

        assertEquals("Coordinate neighbours is wrong", new HashSet<>(expected), new HashSet<>(current));
    }

    @Test
    public void neighbourListIsValidEvenNearbyBordersLimitBoard() {
        var expected = Arrays.asList(
                new Coordinate(4, 4),
                new Coordinate(3, 4),
                new Coordinate(3, 5)
        );
        var current = board.neighbours(new Coordinate(4, 5));

        assertEquals("Coordinate neighbours is wrong", new HashSet<>(expected), new HashSet<>(current));
    }
}
