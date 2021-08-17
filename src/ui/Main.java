package ui;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import exception.GameException;
import exception.TokenizationException;
import exception.ValidationException;
import game.Game;
import parser.GameParser;
import tokenizer.Tokenizer;
import validator.GameValidator;
import tokenizer.SimpleTokenizer;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, GameException,
            ValidationException, TokenizationException {
        List<String> fixedLiterals = Arrays.asList("DEFINE","PLAYER","HEALTH","ATTACK","DEFENCE","START","GOAL","MONSTER","WALL","COIN","DOOR","KEY","DELETE","GREEN","RED","BLUE","SLIME","SKELETON","BOSS",
        "DUNGEON","LEFT","RIGHT","UP","DOWN","HP_MAX","ATK_MAX","DEF_MAX","HEAL_POT","PATH",",",";","(",")","[","]","{","}");
        Tokenizer tokenizer = SimpleTokenizer.createSimpleTokenizer("input.tcts", fixedLiterals);
        System.out.println("Done tokenizing");

        GameParser p = GameParser.getParser(tokenizer);
        Game game = p.parseGame();
        System.out.println("Done parsing");

        GameValidator validator = new GameValidator();
        String errorMsg = game.accept(validator);
        if (!errorMsg.isEmpty()) {
            String e = "\nPlease make sure these errors are fixed:\n";
            e += errorMsg;
            throw new ValidationException(e);
        }
        System.out.println("Done validating");

        game.play();
    }
}