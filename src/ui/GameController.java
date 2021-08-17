package ui;

import game.Dungeon;
import game.elements.Monster;
import game.elements.Player;
import game.properties.PlayerMoveResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameController {
    private static JFrame frame = new JFrame();
    private static StatsPanel statsPanel;
    private static Player player;
    private static List<Dungeon> dungeons;
    private static List<Monster> monsters;
    private static int currDungeonCounter = 0;
    private static DungeonBoard currentBoard;

    public GameController(Player p, List<Monster> ml, List<Dungeon> dl) {
        player = p;
        monsters = ml;
        dungeons = dl;

        initializePanels();
    }

    private void initializePanels() {
        // Add status panel
        statsPanel = new StatsPanel(player);
        frame.getContentPane().add(statsPanel, BorderLayout.WEST);
        // Add dungeon panel
        addDungeonBoard();

        frame.setTitle("Dungeon Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(GameConstants.MAX_FRAME_WIDTH + GameConstants.SIDE_PANEL_WIDTH, GameConstants.MAX_FRAME_HEIGHT));
        frame.setMaximumSize(new Dimension(GameConstants.MAX_FRAME_WIDTH + GameConstants.SIDE_PANEL_WIDTH, GameConstants.MAX_FRAME_HEIGHT));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static void changeDungeon() {
        if (currDungeonCounter < dungeons.size()) {
            frame.getContentPane().remove(currentBoard);
            frame.validate();
            addDungeonBoard();
            currentBoard.requestFocusInWindow();
            frame.pack();
        } else {
            showGameClearScreen();
        }
    }

    private static void showGameClearScreen() {
        frame.getContentPane().remove(currentBoard);
        frame.getContentPane().add(new GoodGamePanel(player,true));
        frame.validate();
        frame.pack();
    }

    private static void showGameOverScreen() {
        frame.getContentPane().remove(currentBoard);
        frame.getContentPane().add(new GoodGamePanel(player,false));
        frame.validate();
        frame.pack();
    }

    // Callback function
    private static Void processMoveResult(PlayerMoveResult result) {
        switch (result) {
            case GOAL:
                changeDungeon();
                break;
            case DEAD:
                statsPanel.updatePanel(result);
                showGameOverScreen();
                break;
            case HEAL:
            case HP_MAX:
            case DEF_MAX:
            case ATK_MAX:
            case COIN:
            case MONSTER:
            case KEY:
            case DOOR:
                statsPanel.updatePanel(result);
                break;
            case NONE:
            default:
                break;
        }
        return null;
    }

    private static void addDungeonBoard() {
        // Add dungeon board
        currentBoard = new DungeonBoard(player, monsters, dungeons.get(currDungeonCounter), GameController::processMoveResult);
        currDungeonCounter++;
        frame.getContentPane().add(currentBoard, BorderLayout.CENTER);
    }
}
