package game.elements;

import game.Dungeon;
import game.properties.Direction;

public abstract class InteractiveDungeonElement extends DungeonElement {
    protected Dungeon parent;
    protected int xpos;
    protected int ypos;

    public InteractiveDungeonElement(Dungeon parent, int xpos, int ypos) {
        this.parent = parent;
        this.xpos = xpos;
        this.ypos = ypos;
    }


    public boolean isMovable(Direction direction) {
        int[] newPos = getNextPosition(direction, xpos, ypos);
        int newXpos = newPos[0];
        int newYpos = newPos[1];


        if (checkOutOfBoundary(newXpos, newYpos)) {
            return false;
        }
        DungeonElement element = parent.getElement(newXpos, newYpos);
        if (element == null){
            return true;
        }
        // check if we can move the player/monster to the new position
        return element.isAccessible(this);
    }

    // return an array of length 2 to contain info of postion

    // Note: (0,0) coordinate starts from top left of the dungeon. Moving down would be y+1
    // X: xth column
    // Y: yth row
    public int[] getNextPosition(Direction direction, int oldX, int oldY) {
        int newX = oldX;
        int newY = oldY;

        switch (direction) {
            case UP:
                newY -= 1;
                break;
            case DOWN:
                newY += 1;
                break;
            case LEFT:
                newX -= 1;
                break;
            case RIGHT:
                newX += 1;
                break;
        }

        int[] pos = new int[2];
        pos[0] = newX;
        pos[1] = newY;

        return pos;
    }

    public void setDungeon(Dungeon dungeon) {
        this.parent = dungeon;
    }

    public Dungeon getDungeon() {
        return this.parent;
    }

    public boolean checkOutOfBoundary(int x, int y) {
        // 0 <= x <= rows - 1
        // 0 <= y <= columns - 1
        // check
        return x < 0 || y < 0 || x >= parent.getColumns() || y >= parent.getRows();
    }

    public void setX(int xpos) {
        this.xpos = xpos;
    }

    public void setY(int ypos) {
        this.ypos = ypos;
    }

    public int getX() {
        return this.xpos;
    }

    public int getY() {
        return this.ypos;
    }
}
