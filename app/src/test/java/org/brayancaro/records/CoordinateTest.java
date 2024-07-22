package org.brayancaro.records;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


class CoordinateTest {
    @Test
    void createsValidRecord() {
        var coordinate = Coordinate.parse("1 2");

        assertEquals(0, coordinate.x());
        assertEquals(1, coordinate.y());
    }

    @Test
    void createsValidRecordWithLowerBond() {
        var coordinate = Coordinate.parse("0 0");

        assertEquals(0, coordinate.x());
        assertEquals(0, coordinate.y());
    }

    @Test
    void givenNegativeValues_createsValidRecord() {
        assertThrows(IllegalStateException.class, () -> {
            Coordinate.parse("-1 -1");
        });
    }
}
