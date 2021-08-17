package ui;

import game.Dungeon;
import game.elements.*;
import game.properties.Attribute;
import game.properties.Direction;
import game.properties.MonsterType;
import game.properties.PlayerMoveResult;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Function;

public class DungeonBoard extends JPanel implements ActionListener {

    private boolean inGame = true;

    private final ImageIcon floorImage = new ImageIcon("src/images/misc/field.png");
    private Image slime, skeleton, boss; //monsters
    private Image coin, heal, heart, shield, sword;
    private Image playerd1, playerd2, playerd3, playerl1, playerl2, playerl3;
    private Image playeru1, playeru2, playeru3, playerr1, playerr2, playerr3;
    private Image wall, wall2, doorRed, doorBlue, doorGreen;
    private Image keyRed, keyGreen, keyBlue, flag, field, empty;

    private Timer timer;
    private Player player;
    private Dungeon dungeon;
    private List<Monster> monsters;
    private JLabel[][] grid;
    private TAdapter keyListener = new TAdapter();

    private Function<PlayerMoveResult, Void> callback;

    public DungeonBoard(Player player, List<Monster> monsterList, Dungeon dungeon, Function<PlayerMoveResult, Void> showGameOverScreen) {
        super();
        this.player = player;
        this.monsters = monsterList;
        this.dungeon = dungeon;
        this.callback = showGameOverScreen;

        loadImages();
        initVariables();
        initBoard();
    }

    private void loadImages() {
        slime = new ImageIcon("src/images/monsters/slime.png").getImage();
        skeleton = new ImageIcon("src/images/monsters/skelly.png").getImage();
        boss = new ImageIcon("src/images/monsters/boss.png").getImage();

        coin = new ImageIcon("src/images/items/coin.png").getImage();
        heal = new ImageIcon("src/images/items/heal.png").getImage();
        heart = new ImageIcon("src/images/items/heart.png").getImage();
        shield = new ImageIcon("src/images/items/shield.png").getImage();
        sword = new ImageIcon("src/images/items/sword.png").getImage();

        playerd1 = new ImageIcon("src/images/player/down1.png").getImage();
        playerd2 = new ImageIcon("src/images/player/down2.png").getImage();
        playerd3 = new ImageIcon("src/images/player/down3.png").getImage();
        playerl1 = new ImageIcon("src/images/player/left1.png").getImage();
        playerl2 = new ImageIcon("src/images/player/left2.png").getImage();
        playerl3 = new ImageIcon("src/images/player/left3.png").getImage();
        playerr1 = new ImageIcon("src/images/player/right1.png").getImage();
        playerr2 = new ImageIcon("src/images/player/right2.png").getImage();
        playerr3 = new ImageIcon("src/images/player/right3.png").getImage();
        playeru1 =  new ImageIcon("src/images/player/up1.png").getImage();
        playeru2 =  new ImageIcon("src/images/player/up2.png").getImage();
        playeru3 =  new ImageIcon("src/images/player/up3.png").getImage();

        wall = new ImageIcon("src/images/misc/wall.png").getImage();
        wall2 = new ImageIcon("src/images/misc/wall2.png").getImage();
        doorRed = new ImageIcon("src/images/misc/reddoor.png").getImage();
        doorBlue = new ImageIcon("src/images/misc/bluedoor.png").getImage();
        doorGreen = new ImageIcon("src/images/misc/greendoor.png").getImage();
        keyBlue = new ImageIcon("src/images/misc/bluekey.png").getImage();
        keyGreen = new ImageIcon("src/images/misc/greenkey.png").getImage();
        keyRed = new ImageIcon("src/images/misc/redkey.png").getImage();
        flag = new ImageIcon("src/images/items/dest.png").getImage();
        field = new ImageIcon("src/images/misc/field.png").getImage();
        empty = new ImageIcon("src/images/misc/empty.jpg").getImage();
    }

    private void initBoard() {
        drawDungeon();
        addKeyListener(keyListener);
        setFocusable(true);
        timer = new Timer(500, this);
        timer.start();
    }

    private void initVariables() {
        this.player.setDungeon(dungeon);
    }

    private void drawDungeon() {
        int top = 0;
        int left = 0;
        int bottom = 0;
        int right = 0;
        // Hack to make sure the cell in the grid is always a square, we essentially increase the border dimension to 
        // make the grid fit in the border and stay as squares
        top += (GameConstants.MAX_DUNGEON_WIDTH - dungeon.getRows()) * (GameConstants.GRID_CELL_RATIO_WIDTH/2);
        left += (GameConstants.MAX_DUNGEON_HEIGHT - dungeon.getColumns()) * (GameConstants.GRID_CELL_RATIO_HEIGHT/2);
        bottom += (GameConstants.MAX_DUNGEON_WIDTH - dungeon.getRows()) * (GameConstants.GRID_CELL_RATIO_WIDTH/2);
        right += (GameConstants.MAX_DUNGEON_HEIGHT - dungeon.getColumns()) * (GameConstants.GRID_CELL_RATIO_HEIGHT/2);
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));

        // Generate the grid 
        setLayout(new GridLayout(dungeon.getRows(), dungeon.getColumns()));
        grid = new JLabel[dungeon.getRows()][dungeon.getColumns()];
        for (int i = 0; i < dungeon.getRows(); i++) {
            for (int j = 0; j < dungeon.getColumns(); j++) {
                DungeonElement element = dungeon.getElement(j, i);
                if (element instanceof Monster) {
                    ((Monster) element).setX(j);
                    ((Monster) element).setY(i);
                    ((Monster) element).setDungeon(dungeon);
                    ((Monster) element).reset();
                } else if (element instanceof Start) {
                    this.player.setX(j);
                    this.player.setY(i);
                    dungeon.replaceElement(player, j, i);
                }
                addDungeonCell(i, j);
            }
        }
    }

    private void updateGrid() {
        for (int i = 0; i < dungeon.getRows(); i++) {
            for (int j = 0; j < dungeon.getColumns(); j++) {
                remove(grid[i][j]);
                addDungeonCell(i, j);
            }
        }
        revalidate();
        repaint();
    }

    private void addDungeonCell(int i, int j) {
        grid[i][j] = creatCell(dungeon.getElement(j, i));
        grid[i][j].setBorder(new LineBorder(Color.BLACK));
        grid[i][j].setOpaque(true);
        add(grid[i][j]);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private DungeonCell creatCell(DungeonElement ele) {
        if (ele instanceof Door) {
            game.properties.Color color = ((Door) ele).getColor();
            switch (color) {
                case RED:
                    return new DungeonCell(doorRed, floorImage);
                case BLUE:
                    return new DungeonCell(doorBlue, floorImage);
                case GREEN:
                    return new DungeonCell(doorGreen, floorImage);
                default:
                    return new DungeonCell(null, floorImage);
            }
        } else if (ele instanceof Goal) {
            return new DungeonCell(flag, floorImage);
        } else if (ele instanceof InaccessibleCell) {
            return new DungeonCell(empty, null);
        } else if (ele instanceof AttributeBooster) {
            Attribute attribute = ((AttributeBooster) ele).getAttribute();
            switch (attribute) {
                case ATTACK:
                    return new DungeonCell(sword, floorImage);
                case DEFENCE:
                    return new DungeonCell(shield, floorImage);
                case HEALTH:
                    if (((AttributeBooster) ele).isPermanent()) {
                        return new DungeonCell(heart, floorImage);
                    } else {
                        return new DungeonCell(heal, floorImage);
                    }
                default:
                    return new DungeonCell(null, floorImage);
            }
        } else if (ele instanceof Monster) {
            if (((Monster) ele).isDead()) {         // A hack to mitigate multithread problem
                return new DungeonCell(null, floorImage);
            }
            MonsterType type = ((Monster) ele).getType();
            switch (type) {
                case SLIME:
                    return new DungeonCell(slime, floorImage);
                case BOSS:
                    return new DungeonCell(boss, floorImage);
                case SKELETON:
                    return new DungeonCell(skeleton, floorImage);
                default:
                    return new DungeonCell(null, floorImage);
            }
        } else if (ele instanceof Player) {
            return new DungeonCell(playerd1, floorImage);
        } else if (ele instanceof Coin) {
            return new DungeonCell(coin, floorImage);
        } else if (ele instanceof Key) {
            game.properties.Color color = ((Key) ele).getColor();
            switch (color) {
                case RED:
                    return new DungeonCell(keyRed, floorImage);
                case BLUE:
                    return new DungeonCell(keyBlue, floorImage);
                case GREEN:
                    return new DungeonCell(keyGreen, floorImage);
                default:
                    return new DungeonCell(null, floorImage);
            }
        } else if (ele instanceof Start) {
            return new DungeonCell(playerd2, floorImage);
        } else if (ele instanceof Wall) {
            return new DungeonCell(wall, floorImage);
        } else {
            //null cases
            return new DungeonCell(null, floorImage);
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if(inGame) {
                PlayerMoveResult result = PlayerMoveResult.NONE;
                if (key == KeyEvent.VK_LEFT) {
                    result = player.move(Direction.LEFT);
                } else if (key == KeyEvent.VK_RIGHT) {
                    result = player.move(Direction.RIGHT);
                } else if (key == KeyEvent.VK_UP) {
                    result = player.move(Direction.UP);
                } else if (key == KeyEvent.VK_DOWN)  {
                    result = player.move(Direction.DOWN);
                }
                if (result != PlayerMoveResult.NONE) {
                    callback.apply(result);
                }
                updateGrid();
            }
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            PlayerMoveResult result = PlayerMoveResult.NONE;
            for (Monster m: monsters) {         // A hack to mitigate multithread problem
                if (m.getDungeon() != null && m.getDungeon().getName().equals(dungeon.getName()) && !m.isDead()) {
                    result = m.next();
                    if (result != PlayerMoveResult.NONE) {
                        callback.apply(result);
                    }
                }
            }
            updateGrid();
            revalidate();
            repaint();
        }
    }

    private class DungeonCell extends JLabel {
        private Image image;

        public DungeonCell(Image image, Icon floorImage) {
            super(floorImage);
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // This is a hack to place an image on top of the floor image and center the picture
            if (image != null) {
                int centerWidth = GameConstants.GRID_CELL_RATIO_WIDTH - (GameConstants.GRID_CELL_RATIO_WIDTH/2 + image.getWidth(null)/2);
                int centerHeight = GameConstants.GRID_CELL_RATIO_HEIGHT - (GameConstants.GRID_CELL_RATIO_HEIGHT/2 + image.getHeight(null)/2);
                g.drawImage(image, centerWidth, centerHeight, null);
            }
        }
    }
}
