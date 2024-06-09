package exception;

import java.io.Serializable;

public class NoFoundName extends RuntimeException implements Serializable{
    public NoFoundName(String message) {
        super(message);
    }
}
