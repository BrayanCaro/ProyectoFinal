package org.brayancaro.gui.components;

import org.brayancaro.TableroPersonalizado;
import org.brayancaro.enums.cell.State;
import org.brayancaro.records.Coordinate;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class MinesweeperButton extends Button {

    public static final char MARK_CELL_CHARACTER = 'm';

    private Coordinate coordinate;

    private TableroPersonalizado board;

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

    @Override
    public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == MARK_CELL_CHARACTER) {
            try {
                board.execute(coordinate, State.MARKED);
                this.setLabel(board.getCell(coordinate).toString());
            } catch (Exception e) {
                return Result.UNHANDLED;
            }
            return Result.HANDLED;
        }
        return super.handleKeyStroke(keyStroke);
    }

    public MinesweeperButton board(TableroPersonalizado board) {
        this.board = board;
        return this;
    }
}
