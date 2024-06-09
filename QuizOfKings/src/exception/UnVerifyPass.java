package exception;

import java.io.Serializable;

public class UnVerifyPass extends RuntimeException implements Serializable {
    public UnVerifyPass(String message) {
        super(message);
    }
}
