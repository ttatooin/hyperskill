package life;

import javax.swing.*;
import java.awt.*;

public class GameOfLife extends JFrame {

    Game game;

    JPanel controllersPanel = new JPanel();
    JButton buttonPlay = new JButton("Play");
    JButton buttonNext = new JButton("Next");
    JButton buttonRestart = new JButton("Restart");

    JPanel statePanel = new JPanel();
    JPanel[][] cells;

    public GameOfLife() {
        super("Game of Life");

        game = new Game(10);
        game.fillRandom(20);

        // Creating of main window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setVisible(true);
        setSize(200, 200);

        // Creating GUI elements.
        // Controllers panel.
        //controllersPanel.setSize(100, 200);
        controllersPanel.add(buttonPlay);
        controllersPanel.add(buttonNext);
        controllersPanel.add(buttonRestart);

        buttonNext.addActionListener(e -> next());

        // State panel.
        statePanel.setLayout(new GridLayout(game.getSize(), game.getSize(), 1, 1));

        cells = new JPanel[game.getSize()][game.getSize()];
        for (int i = 0; i < game.getSize(); ++i) {
            for (int j = 0; j < game.getSize(); ++j) {
                cells[i][j] = new JPanel();
                cells[i][j].setBackground(Color.LIGHT_GRAY);
                cells[i][j].setSize(20, 20);
                cells[i][j].setMinimumSize(new Dimension(20, 20));
                cells[i][j].setMaximumSize(new Dimension(20, 20));
                cells[i][j].setToolTipText(Integer.toString(game.countLiveCellsAround(j, i)));
                statePanel.add(cells[i][j]);
            }
        }
        updateStatePanel();

        add(controllersPanel);
        add(statePanel);
    }

    private void updateStatePanel() {
        for (int i = 0; i < game.getSize(); ++i) {
            for (int j = 0; j < game.getSize(); ++j) {
                cells[i][j].setBackground(game.isAliveAt(j, i) ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            }
        }
    }

    private void next() {
        game.nextGeneration();
        updateStatePanel();
    }

    public static void main(String[] args) {
        GameOfLife app = new GameOfLife();
        /*
        try {
            while (true) {
                Thread.sleep(2000);
                app.game.nextGeneration();
                app.updateStatePanel();
            }
        } catch (Exception e) {
        }
         */
    }
}
