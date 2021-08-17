package tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import exception.TokenizationException;

public class SimpleTokenizer implements Tokenizer {

    public static Tokenizer createSimpleTokenizer(String filename, List<String> fixedLiteralsList)
            throws TokenizationException
    {
        return new SimpleTokenizer(filename, fixedLiteralsList);
    }
    private static final String SEPARATOR = "(?<=;)|(?=;)";

    private String inputProgram;
    private List<String> fixedLiterals;
    private ArrayList<String> tokens = new ArrayList<String>();
    private int currentToken = 0;

    private SimpleTokenizer(String filename, List<String> fixedLiteralsList) throws TokenizationException {
        fixedLiterals = fixedLiteralsList;
        try {
            inputProgram = Files.readString(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Didn't find file");
            System.exit(0);
        }
        tokenize();
    }

    //modifies: this.tokens
    //effects: will result in a list of tokens (sitting at this.tokens) that has no spaces around tokens.
    private void tokenize () throws TokenizationException {
        String[] tokenizedProgram = this.inputProgram.replace("\n", "").trim().split(SEPARATOR);

        for (int i = 0; i < tokenizedProgram.length; i++) {
            tokenizedProgram[i] = tokenizedProgram[i].trim();
        }

        for(String token: tokenizedProgram) {
            splitTokens(token, tokens);
        }

        System.out.println("FINAL TOKENS: "+ tokens);
    }

    // Splits the given string into multiple substrings that represent individual tokens and save them all into the list
    // The user-defined string can contain any alphabets, numbers, '_' symbols and spaces
    private void splitTokens(String token, ArrayList<String> list) throws TokenizationException {
        if (token.isEmpty()) {
            return;
        }

        // If the substring starts with a fixed literal token then split the substring into the literal token and the remainder 
        // then call splitTokens again on the remainder
        for (String s: fixedLiterals) {
            if (token.startsWith(s)) {
                list.add(token.substring(0, s.length()).trim());
                splitTokens(token.substring(s.length()).trim(), list);
                return;
            }
        }

        // If the substring starts with some string that matches a valid user-defined string token then split the substring 
        // into the token and the remainder then call splitTokens again on the remainder
        int index = 0;
        char[] tokenCharacter = token.toCharArray();
        for (char c: tokenCharacter) {
            if (isUserDefinedToken(c)) {
                index++;
            } else if (isStartOfLiteralToken(c)) {
                break;
            } else {
                throw new TokenizationException("Error while tokenizing input: " + token + ", unknown value found: " + tokenCharacter[index]);
            }
        }
        list.add(token.substring(0, index).trim());
        splitTokens(token.substring(index).trim(), list);
        return;
    }

    // The user defined variable will always end with one of these symbols
    private boolean isStartOfLiteralToken(char c) {
        return c == '(' || c == ')' || c == '{' || c == ',';
    }

    private boolean isUserDefinedToken(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || Character.isWhitespace(c));
    }

    @Override
    public String checkNext() {
        String token = "";
        if (currentToken < tokens.size()) {
            token = tokens.get(currentToken);
        } else {
            token = "NO_MORE_TOKENS";
        }
        return token;
    }

    // Check the next n tokens and add them together with spaces in between
    @Override
    public String checkNextN(int n) {
        int count = currentToken;
        String token = "";
        if ((count + n) < tokens.size()) {
            for (int i = 0; i < n; i++) {
                token += tokens.get(count + i);
                token += " ";
            }
        } else {
            token = "NO_MORE_TOKENS";
        }
        return token.trim();
    }

    @Override
    public String getNext() {
        String token = "";
        if (currentToken < tokens.size()) {
            token = tokens.get(currentToken);
            currentToken++;
        } else {
            token = "NULLTOKEN";
        }
        return token;
    }

    @Override
    public String getNextN(int n) {
        String token = "";
        if ((currentToken + n) < tokens.size()) {
            for (int i = 0; i < n; i++) {
                token += tokens.get(currentToken + i);
                token += " ";
            }
            currentToken += n;
        } else {
            token = "NULLTOKEN";
        }
        return token.trim();
    }

    @Override
    public boolean checkToken(String regexp) {
        String s = checkNext();
        System.out.println("comparing: |"+s+"|  to  |"+regexp+"|");
        return (s.matches(regexp));
    }

    @Override
    public boolean checkNToken(String regexp, int n) {
        String s = checkNextN(n);
        System.out.println("comparing: |"+s+"|  to  |"+regexp+"|");
        return (s.matches(regexp));
    }


    @Override
    public String getAndCheckNext(String regexp) {
        String s = getNext();
        if (!s.matches(regexp)) {
            throw new RuntimeException("Unexpected next token for Parsing! Expected something matching: " + regexp + " but got: " + s);
        }
        System.out.println("matched: "+s+"  to  "+regexp);
        return s;
    }

    @Override
    public String getAndCheckNextN(String regexp, int n) {
        String s = getNextN(n);
        if (!s.matches(regexp)) {
            throw new RuntimeException("Unexpected next token for Parsing! Expected something matching: " + regexp + " but got: " + s);
        }
        System.out.println("matched: "+s+"  to  "+regexp);
        return s;
    }

    @Override
    public boolean moreTokens() {
        return currentToken < tokens.size();
    }
}
