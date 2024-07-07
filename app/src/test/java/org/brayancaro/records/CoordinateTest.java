package org.brayancaro.records;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class CoordinateTest {
    @Test
    public void createsValidRecord() {
        var coordinate = Coordinate.parse("1 2");

        assertEquals(coordinate.x(), 0);
        assertEquals(coordinate.y(), 1);
    }

    @Test
    public void createsValidRecordWithLowerBond() {
        var coordinate = Coordinate.parse("0 0");

        assertEquals(coordinate.x(), 0);
        assertEquals(coordinate.y(), 0);
    }

    @Test
    public void givenNegativeValues_createsValidRecord() {
        assertThrows(IllegalStateException.class, () -> {
            Coordinate.parse("-1 -1");
        });
    }
}
