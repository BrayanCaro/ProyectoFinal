package org.brayancaro.gui.components;

import org.brayancaro.records.Coordinate;

import com.googlecode.lanterna.gui2.Button;

/**
 * Button
 */
public class MinesweeperButton extends Button {
    private Coordinate coordinate;

    public MinesweeperButton coordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public MinesweeperButton(String label) {
        super(label);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
