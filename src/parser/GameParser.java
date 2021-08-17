package parser;

import java.util.ArrayList;
import java.util.List;

import exception.GameException;
import exception.ParserException;
import game.*;
import game.elements.*;
import game.properties.*;
import tokenizer.Tokenizer;

public class GameParser {
    private final Tokenizer tokenizer;
    private List<Monster> monsters; // Keep this reference for validation on dungeon monster references

    public static GameParser getParser(Tokenizer tokenizer) {
        return new GameParser(tokenizer);
    }

    private GameParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    // PROGRAM ::= PLAYER MONSTER* DUNGEON+
    public Game parseGame() throws GameException {
        Player player = parsePlayer();
        monsters = new ArrayList<Monster>();
        while (isMonsterToken()) {
            monsters.add(parseMonster());
        }
        List<Dungeon> dungeons = new ArrayList<Dungeon>();
        while (isDungeonToken()) {
            dungeons.add(parseDungeon());
        }
        if (tokenizer.moreTokens()) {
            throw new ParserException("Unknown token " + tokenizer.getNext()
                    + " detected while parsing, please ensure you have the correct syntax");
        }
        if (dungeons.isEmpty()) {
            throw new ParserException("At least 1 dungeon must be defined");
        }
        return new Game(player, monsters, dungeons);
    }

    // PLAYER ::= “DEFINE PLAYER” “{” HP ATK DEF “}”
    private Player parsePlayer() throws GameException {
        tokenizer.getAndCheckNextN("DEFINE PLAYER", 2);
        tokenizer.getAndCheckNext("\\{");
        int hp = parseHp();
        int atk = parseAtk();
        int def = parseDef();
        tokenizer.getAndCheckNext("\\}");
        return new Player(null, 0, 0)
                   .setMaxHealth(hp)
                   .setCurrentHealth(hp)
                   .setAttack(atk)
                   .setDefence(def);
    }

    // MONSTER ::= “DEFINE” MONSTER_TYPE REFERENCE_NAME “{” HP ATK DEF PATH “}”
    private Monster parseMonster() throws GameException {
        tokenizer.getAndCheckNext("DEFINE");
        MonsterType mType = MonsterType.valueOf(tokenizer.getNext());
        String name = tokenizer.getNext();
        for (Monster monster: monsters) {
            if (monster.getName().equals(name)) {
                throw new ParserException("There is already a monster named: " + name + ", please use a different name");
            }
        }
        tokenizer.getAndCheckNext("\\{");
        int hp = parseHp();
        int atk = parseAtk();
        int def = parseDef();
        List<Direction> path = parsePath();
        tokenizer.getAndCheckNext("\\}");
        return new Monster(mType, name, null, 0, 0)
                   .setHealth(hp)
                   .setAttack(atk)
                   .setDefence(def)
                   .setPath(path);
    }

    // PATH ::= “PATH” “[“ (DIRECTION (“,” DIRECTION )*)* ”]”
    private List<Direction> parsePath() throws GameException {
        List<Direction> path = new ArrayList<Direction>();
        tokenizer.getAndCheckNext("PATH");
        tokenizer.getAndCheckNext("\\[");
        while (!tokenizer.checkToken("\\]")) {
            Direction dir = Direction.valueOf(tokenizer.getNext());
            path.add(dir);
            if (tokenizer.checkNextN(2).equals(",]")) { // The last direction in the path cannot end with ","
                throw new ParserException("Invalid syntax " + tokenizer.checkNextN(2) + "while parsing PATH");
            } else if (tokenizer.checkNext().equals("]")) { // End of the list, break before we try to get and check "," token
                break;
            } else {
                tokenizer.getAndCheckNext(",");
            }
        }
        tokenizer.getNext(); // Consume ]
        return path;
    }

    // DUNGEON ::= “DEFINE DUNGEON” REFERENCE_NAME POSITION “{“ START GOAL DUNGEON_OBJECT* “}”
    private Dungeon parseDungeon() throws GameException {
        tokenizer.getAndCheckNextN("DEFINE DUNGEON", 2);
        String name = tokenizer.getNext();
        Position size = parsePosition();
        tokenizer.getAndCheckNext("\\{");
        Dungeon dungeon = new Dungeon(name, size.getX(), size.getY());
        parseStart(dungeon);
        parseGoal(dungeon);
        while (!tokenizer.checkToken("\\}")) {
            parseDungeonElement(dungeon);
        }
        tokenizer.getAndCheckNext("\\}");
        return dungeon;
    }

    // START ::= "START" POSITION “;”
    private void parseStart(Dungeon dungeon) throws GameException {
        DungeonElementName eName = DungeonElementName.START;
        tokenizer.getAndCheckNext(eName.name());
        Position pos = parsePosition();
        tokenizer.getAndCheckNext(";");
        dungeon.addElement(new Start(), eName, pos.getX(), pos.getY());
    }

    // GOAL ::= “GOAL” POSITION “;”
    private void parseGoal(Dungeon dungeon) throws GameException {
        DungeonElementName eName = DungeonElementName.GOAL;
        tokenizer.getAndCheckNext(eName.name());
        Position pos = parsePosition();
        tokenizer.getAndCheckNext(";");
        dungeon.addElement(new Goal(), eName, pos.getX(), pos.getY());
    }

    // DUNGEON_OBJECT ::= REFERENCE_MONSTER | COIN | DOOR | KEY | WALL | ITEM | SET | DELETE
    private void parseDungeonElement(Dungeon dungeon) throws GameException {
        DungeonElementName token;
        try {
            token = DungeonElementName.valueOf(tokenizer.checkNext());
        } catch(IllegalArgumentException | NullPointerException e) {
            throw new ParserException(getAddElementFailedMessage() + ", element" + tokenizer.checkNext() + "is not recognized");
        }
        switch (token) {
            case MONSTER:
                parseDungeonMonster(dungeon, token);
                break;
            case DOOR:
                parseDoor(dungeon, token);
                break;
            case COIN:
                parseCoin(dungeon, token);
                break;
            case KEY:
                parseKey(dungeon, token);
                break;
            case WALL:
                parseWall(dungeon, token);
                break;
            case DELETE:
                parseDelete(dungeon, token);
                break;
            case HP_MAX:
                parseItem(dungeon, token);
                break;
            case ATK_MAX:
                parseItem(dungeon, token);
                break;
            case DEF_MAX:
                parseItem(dungeon, token);
                break;
            case HEAL_POT:
                parseItem(dungeon, token);
                break;
            default:
                break;
        }
    }

    // REFERENCE_MONSTER ::= “MONSTER” REFERENCE_NAME POSITION “;”
    private void parseDungeonMonster(Dungeon dungeon, DungeonElementName eName) throws GameException {
        tokenizer.getAndCheckNext(eName.name());
        String name = tokenizer.getNext();
        Position pos = parsePosition();
        tokenizer.getAndCheckNext(";");
        for (Monster m : monsters) {
            if (m.getName().equals(name)) {
                dungeon.addElement(m, eName, pos.getX(), pos.getY());
                return;
            }
        }
        throw new ParserException(getAddElementFailedMessage() + ", monster " + name + " is not found, please be sure to define the monster first");
    }

    /* 
      COIN ::= “COIN” POSITION “;” 
      COIN_SET ::= “COIN” DIRECTION POSITION NUM “;”
    */
    private void parseCoin(Dungeon dungeon, DungeonElementName eName) throws GameException {
        tokenizer.getAndCheckNext(eName.name());
        if (tokenizer.checkToken("\\(")) {  // COIN case
            Position pos = parsePosition();
            dungeon.addElement(new Coin(), eName, pos.getX(), pos.getY());
        } else {    // COIN_SET case
            Direction dir = Direction.valueOf(tokenizer.getNext());
            Position pos = parsePosition();
            String sNum = tokenizer.getNext();
            if (isNumeric(sNum)) {
                int num = Integer.parseInt(sNum);
                addElements(dungeon, DungeonElementName.COIN_SET, dir, pos, num);
            } else {
                throw new ParserException(getInvalidNumberMessage(sNum, DungeonElementName.COIN_SET.name()));
            }
        }
        tokenizer.getAndCheckNext(";");
    }

    // DOOR ::= “DOOR” COLOR POSITION “;”
    private void parseDoor(Dungeon dungeon, DungeonElementName eName) throws GameException {
        tokenizer.getAndCheckNext(eName.name());
        Color color = Color.valueOf(tokenizer.getNext());
        Position pos = parsePosition();
        tokenizer.getAndCheckNext(";");
        dungeon.addElement(new Door(color), eName, pos.getX(), pos.getY());
    }

    // KEY ::= “KEY” COLOR POSITION “;”
    private void parseKey(Dungeon dungeon, DungeonElementName eName) throws GameException {
        tokenizer.getAndCheckNext(eName.name());
        Color color = Color.valueOf(tokenizer.getNext());
        Position pos = parsePosition();
        tokenizer.getAndCheckNext(";");
        dungeon.addElement(new Key(color), eName, pos.getX(), pos.getY());
    }

    /*     
      WALL ::= “WALL” POSITION “;”
      WALL_SET ::= “WALL” DIRECTION POSITION NUM “;”
    */
    private void parseWall(Dungeon dungeon, DungeonElementName eName) throws GameException {
        tokenizer.getAndCheckNext(eName.name());
        if (tokenizer.checkToken("\\(")) {  // WALL case
            Position pos = parsePosition();
            dungeon.addElement(new Wall(), eName, pos.getX(), pos.getY());
        } else {    // WALL_SET case
            Direction dir = Direction.valueOf(tokenizer.getNext());
            Position pos = parsePosition();
            String sNum = tokenizer.getNext();
            if (isNumeric(sNum)) {
                int num = Integer.parseInt(sNum);
                addElements(dungeon, DungeonElementName.WALL_SET, dir, pos, num);
            } else {
                throw new ParserException(getInvalidNumberMessage(sNum, DungeonElementName.WALL_SET.name()));
            }
        }
        tokenizer.getAndCheckNext(";");
    }

    /* 
      DELETE ::= "DELETE" POSITION “;” 
      DELETE_SET ::= “DELETE” DIRECTION POSITION NUM “;”
    */
    private void parseDelete(Dungeon dungeon, DungeonElementName eName) throws GameException {
        tokenizer.getAndCheckNext(eName.name());
        if (tokenizer.checkToken("\\(")) {  // DELETE case
            Position pos = parsePosition();
            dungeon.addElement(new InaccessibleCell(), eName, pos.getX(), pos.getY());
        } else {    // DELETE_SET case
            Direction dir = Direction.valueOf(tokenizer.getNext());
            Position pos = parsePosition();
            String sNum = tokenizer.getNext();
            if (isNumeric(sNum)) {
                int num = Integer.parseInt(sNum);
                addElements(dungeon, DungeonElementName.DELETE_SET, dir, pos, num);
            } else {
                throw new ParserException(getInvalidNumberMessage(sNum, DungeonElementName.DELETE_SET.name()));
            }
        }
        tokenizer.getAndCheckNext(";");
    }

    // For adding a set/list of element (Currently for coin, delete and wall only)
    private void addElements(Dungeon dungeon, DungeonElementName name, Direction dir, Position pos, int num) throws GameException {
        for (int i = 0; i < num; i++) {
            int xpos = pos.getX();
            int ypos = pos.getY();
            switch (dir) {
                case UP:
                    ypos -= i;
                    break;
                case DOWN:
                    ypos += i;
                    break;
                case RIGHT:
                    xpos += i;
                    break;
                case LEFT:
                    xpos -= i;
                    break;
                default:
                    break;
            }
            DungeonElement de = null;
            switch (name) {
                case WALL_SET:
                    de = new Wall();
                    break;
                case DELETE_SET:
                    de = new InaccessibleCell();
                    break;
                case COIN_SET:
                    de = new Coin();
                    break;
                default:
                    break;
            }
            dungeon.addElement(de, name, xpos, ypos);
        }
    }

    /*     
      ITEM ::= HP_MAX | ATK_MAX | DEF_MAX | HEAL_POT
      HP_MAX ::= “HP_MAX” POSITION NUM “;”
      ATK_MAX ::= “ATK_MAX” POSITION NUM “;”
      DEF_MAX ::= “DEF_MAX” POSITION NUM “;”
      HEAL_POT ::= “HEAL_POT” POSITION NUM “;” 
    */
    private void parseItem(Dungeon dungeon, DungeonElementName eName) throws GameException {
        tokenizer.getAndCheckNext(eName.name());
        Attribute attribute = null;
        boolean permanent = true;
        switch (eName) {
            case HP_MAX:
                attribute = Attribute.HEALTH;
                break;
            case ATK_MAX:
                attribute = Attribute.ATTACK;
                break;
            case DEF_MAX:
                attribute = Attribute.DEFENCE;
                break;
            case HEAL_POT:
                attribute = Attribute.HEALTH;
                permanent = false;
                break;
            default:
                break;
        }
        Position pos = parsePosition();
        String sAmount = tokenizer.getNext();
        if (isNumeric(sAmount)) {
            int amount = Integer.parseInt(sAmount);
            tokenizer.getAndCheckNext(";");
            dungeon.addElement(new AttributeBooster(attribute, amount, permanent), eName, pos.getX(), pos.getY());
        } else {
            throw new ParserException(getInvalidNumberMessage(sAmount, "ITEM"));
        }
    }

    // POSITION ::= “(” NUM “,” NUM “)”
    private Position parsePosition() throws GameException {
        tokenizer.getAndCheckNext("\\(");
        int x;
        String sX = tokenizer.getNext();
        if (isNumeric(sX)) {
            x = Integer.parseInt(sX);
        } else {
            throw new ParserException(getInvalidNumberMessage(sX, "position") + ", please ensure the format is correct e.g (1,1)");
        }
        tokenizer.getAndCheckNext(",");
        int y;
        String sY = tokenizer.getNext();
        if (isNumeric(sY)) {
            y = Integer.parseInt(sY);
        } else {
            throw new ParserException(getInvalidNumberMessage(sY, "position") + ", please ensure the format is correct e.g (1,1)");
        }
        tokenizer.getAndCheckNext("\\)");
        return new Position(x, y);
    }

    // HP ::= “HEALTH” NUM “;”
    private int parseHp() throws GameException {
        tokenizer.getAndCheckNext("HEALTH");
        String sNum = tokenizer.getNext();
        if (isNumeric(sNum)) {
            int num = Integer.parseInt(sNum);
            tokenizer.getAndCheckNext(";");
            return num;
        } else {
            throw new ParserException(getInvalidNumberMessage(sNum, "HEALTH"));
        }
    }

    // ATK ::= “ATTACK” NUM “;”
    private int parseAtk() throws GameException {
        tokenizer.getAndCheckNext("ATTACK");
        String sNum = tokenizer.getNext();
        if (isNumeric(sNum)) {
            int num = Integer.parseInt(sNum);
            tokenizer.getAndCheckNext(";");
            return num;
        } else {
            throw new ParserException(getInvalidNumberMessage(sNum, "ATTACK"));
        }
    }

    // DEF ::= “DEFENCE” NUM “;”
    private int parseDef() throws GameException {
        tokenizer.getAndCheckNext("DEFENCE");
        String sNum = tokenizer.getNext();
        if (isNumeric(sNum)) {
            int num = Integer.parseInt(sNum);
            tokenizer.getAndCheckNext(";");
            return num;
        } else {
            throw new ParserException(getInvalidNumberMessage(sNum, "DEFENCE"));
        }
    }

    // Check if current tokens will be a define monster statement
    private boolean isMonsterToken() {
        for (MonsterType mType: MonsterType.values()) {
            if (tokenizer.checkNToken("DEFINE " + mType.name(), 2)) {
                return true;
            }
        }
        return false;
    }

    // Check if current tokens will be a define dungeon statement
    private boolean isDungeonToken() {
        return tokenizer.checkNToken("DEFINE DUNGEON", 2);
    }

    private boolean isNumeric(String sNum) {
        if (sNum == null) {
            return false;
        }
        try {
            Integer.parseInt(sNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String getInvalidNumberMessage(String sNum, String input) {
        return "Invalid number " + sNum + " while parsing " + input;
    }

    private String getAddElementFailedMessage() {
        return "Adding element to dungeon failed";
    }

    private class Position {
        private int x;
        private int y;
    
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        public int getX() {
            return x;
        }
    
        public int getY() {
            return y;
        }
    }
}
