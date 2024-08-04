package org.brayancaro.gui.windows;

import org.brayancaro.Celdas;
import org.brayancaro.TableroPersonalizado;
import org.brayancaro.TocasteUnaBombaExcepcion;
import org.brayancaro.enums.cell.State;
import org.brayancaro.gui.components.MinesweeperButton;
import org.brayancaro.records.Coordinate;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

public class GameWindow extends BasicWindow {
    private TableroPersonalizado board;

    public GameWindow(TableroPersonalizado board) {
        this.board = board;

        var mainPanel = new Panel();

        var configuration = this.board.getConfiguration();
        var panel = new Panel(new GridLayout(configuration.columns()));

        Label labelComponent = new Label(getLabelText());

        mainPanel.addComponent(labelComponent);

        for (int i = 0; i < configuration.columns(); i++) {
            for (int j = 0; j < configuration.rows(); j++) {
                var coordinate = new Coordinate(i, j);
                var cell = board.getCell(coordinate);
                var button = new MinesweeperButton(cell.toString())
                    .coordinate(coordinate)
                    .board(board);

                button.addListener(btn -> {
                    try {
                        board.execute(coordinate, State.REVEALED);
                    } catch (TocasteUnaBombaExcepcion e) {
                        board.mostrarTodasLasBombas();
                        close();
                    } catch (Exception e) {
                        close();
                    } finally {
                        if (board.jugadorGanoSinMarcas() == configuration.bombCount()) {
                            board.ganador();
                            close();
                        }

                        updateUi(panel, labelComponent, cell, btn);
                    }
                });

                panel.addComponent(button);
            }
        }

        mainPanel.addComponent(panel);

        setComponent(mainPanel);
    }

    private void updateUi(Panel panel, Label labelComponent, Celdas cell, Button btn) {
        labelComponent.setText(getLabelText());

        btn.setLabel(cell.toString());
        panel.getChildrenList().forEach(component -> {
            var minesweeperBtn = (MinesweeperButton) component;
            minesweeperBtn.setLabel(
                    board.getCell(minesweeperBtn.getCoordinate())
                            .toString());
        });
    }

    protected String getLabelText() {
        return String.format("""
                Quedan %d casillas sin ver.
                Hay %s bombas en el mapa
                """, board.jugadorGanoSinMarcas(), board.getConfiguration().bombCount());
    }
}
