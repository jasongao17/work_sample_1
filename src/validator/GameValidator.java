package validator;

import java.util.ArrayList;
import java.util.List;

import game.Dungeon;
import game.Game;
import game.elements.AttributeBooster;
import game.elements.Coin;
import game.elements.Door;
import game.elements.DungeonElement;
import game.elements.Goal;
import game.elements.InaccessibleCell;
import game.elements.Key;
import game.elements.Monster;
import game.elements.Player;
import game.elements.Start;
import game.elements.Wall;
import game.properties.Attribute;

public class GameValidator implements SimpleVisitor<String> {
    private final int MAX_DUNGEON_WIDTH = 20;
    private final int MAX_DUNGEON_HEIGHT = 20;
    private List<String> monsterNames = new ArrayList<String>();
    private List<String> dungeonNames = new ArrayList<String>();

    @Override
    // Don't need to call accept for list of monsters since the dungeon will contain it
    public String visit(Game g) {
        List<Dungeon> dungeons = g.getDungeons();
        String errorMsg = "";
        errorMsg += g.getPlayer().accept(this);
        for (Dungeon d: dungeons) {
            errorMsg += d.accept(this);
        }
        return errorMsg;
    }

    @Override
    public String visit(Dungeon d) {
        int height = d.getRows();
        int width = d.getColumns();
        String errorMsg = "";
        if (dungeonNames.contains(d.getName())) {
            errorMsg += "- There is already a dungeon named " + d.getName() + ", please use a different name \n";
        } 
        dungeonNames.add(d.getName());
        if (height > MAX_DUNGEON_HEIGHT) {
            errorMsg += "- The maximum height allowed for dungeon creation is " + MAX_DUNGEON_HEIGHT + "\n";
        }
        if (width > MAX_DUNGEON_WIDTH) {
            errorMsg += "- The maximum width allowed for dungeon creation is " + MAX_DUNGEON_WIDTH + "\n";
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (d.getElement(x, y) != null) {
                    errorMsg += d.getElement(x, y).accept(this);
                }
            }
        }
        return errorMsg;
    }

    @Override
    public String visit(AttributeBooster ab) {
        String errorMsg = "";
        if (ab.getAmount() <= 0) {  // negative values should have been handled by tokenizer/parser
            Attribute attribute = ab.getAttribute();
            switch (attribute) {
                case HEALTH:
                    if (ab.isPermanent()) {
                        errorMsg += "- The hp max ";
                    } else {
                        errorMsg += "- The heal pot ";
                    }
                    break;
                case ATTACK:
                    errorMsg += "- The atk max ";
                    break;
                case DEFENCE:
                    errorMsg += "- The def max ";
                    break;
                default:
                    break;
            }
            errorMsg += "item must have at least 1 amount\n";
        }
        return errorMsg;
    }

    @Override
    public String visit(Coin c) {
        return "";
    }

    @Override
    public String visit(Door d) {
        return "";
    }

    @Override
    public String visit(Goal n) {
        return "";
    }

    @Override
    public String visit(InaccessibleCell ic) {
        return "";
    }

    @Override
    public String visit(Key k) {
        return "";
    }

    @Override
    public String visit(Monster m) {
        String errorMsg = "";
        if (m.getMaxHealth() <= 0) {   // negative attributes should have been handled by tokenizer/parser
            errorMsg += "- The monster " + m.getName() + " must have at least 1 health\n";
        }
        return errorMsg;
    }

    @Override
    public String visit(Player p) {
        String errorMsg = "";
        if (p.getMaxHealth() <= 0) {    // negative attributes should have been handled by tokenizer/parser
            errorMsg += "- The player must have at least 1 health\n";
        }
        return errorMsg;
    }

    @Override
    public String visit(Start s) {
        return "";
    }

    @Override
    public String visit(Wall w) {
        return "";
    }
    
}
