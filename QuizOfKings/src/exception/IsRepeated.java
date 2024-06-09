package exception;

import java.io.Serializable;

public class IsRepeated extends RuntimeException implements Serializable {
    public IsRepeated(String message) {
        super(message);
    }
}
