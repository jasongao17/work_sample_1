package tokenizer;

public interface Tokenizer {
    String checkNext();

    String checkNextN(int n);

    String getNext();

    String getNextN(int n);

    boolean checkToken(String regexp);

    boolean checkNToken(String regexp, int n);

    String getAndCheckNext(String regexp);

    String getAndCheckNextN(String regexp, int n);

    boolean moreTokens();
}
