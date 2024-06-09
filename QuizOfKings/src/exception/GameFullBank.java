package exception;

public class GameFullBank extends RuntimeException{
    public GameFullBank(String message) {
        super(message);
    }
}
