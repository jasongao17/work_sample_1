package game;

import java.util.ArrayList;
import java.util.List;

import exception.DungeonObjectConflictException;
import exception.DungeonOutOfBoundsException;
import game.elements.*;
import game.properties.*;
import validator.SimpleVisitor;

public class Dungeon {
    private DungeonElement[][] playground;          // This represent the dungeon itself each square containing nothing or an element
    private String name;
    private int rows;
    private int columns;
    private List<String> monstersAdded = new ArrayList<String>();

    public Dungeon(String name, int x, int y) {
        this.name = name;
        this.rows = y;           // rows is y (increasing rows will make y coordinate higher)
        this.columns = x;        // columns is x (increasing columns will make x coordinate longer)
        this.playground = new DungeonElement[rows][columns];
    }

    public void addElement(DungeonElement e, DungeonElementName eName, int x, int y) throws DungeonOutOfBoundsException, DungeonObjectConflictException {
        if (x >= columns || y >= rows || x < 0 || y < 0) {
            throw new DungeonOutOfBoundsException("The position x: " + x + " y: " + y + " for adding " + eName.name() + " to " + name + 
                                                  " is outside of the dungeon, please check the position you've stated in the input");
        }
        if (playground[y][x] != null) {
            throw new DungeonObjectConflictException("The position x: " + x + " y: " + y + " for adding " + eName.name() + " to " + name + 
                                                     " is already occupied by another element, please choose a different position");
        }
        if (e instanceof Monster) {
           if (monstersAdded.contains(((Monster) e).getName())) {
                throw new DungeonObjectConflictException("The monster " + ((Monster) e).getName() + " has already been added to dungeon " + name + ".");
           } else {
                monstersAdded.add(((Monster) e).getName());
           }
        }
        playground[y][x] = e;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

    // Return the element on the given position
    public DungeonElement getElement(int x, int y) {
        return playground[y][x];
    }

    // Replace the element at position to a new element
    public void replaceElement(DungeonElement e, int x, int y) {
        playground[y][x] = e;
    }

    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}