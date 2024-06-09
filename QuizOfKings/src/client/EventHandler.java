package client;

import java.io.IOException;

public class EventHandler {
    public static void eventHandle(int input, EventHandlerInterface... methodReference) throws IOException, ClassNotFoundException {
        methodReference[input].run();
    }
}
