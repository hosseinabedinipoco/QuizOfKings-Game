package client;

import java.io.IOException;
import java.util.HashMap;

public class Utility {
    public static void showInfo() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("show info");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.flush();
        String info = (String) RegisterMenu.in.readObject();
        System.out.println(info);
    }

    public static void excepted() {
        System.out.println();
        do {
            System.out.println("if you want go back enter 0");
        }while (!ScannerWrapper.getInstance().nextLine().equals("0"));
    }

    public static void changeName() throws IOException, ClassNotFoundException {
        String name = RegisterMenu.get("name", RegisterMenu::isvalidName);
        RegisterMenu.out.writeObject("change name");
        HashMap<String, String> info = new HashMap<>();
        info.put("old name", RegisterMenu.registeredName);
        info.put("new name", name);
        RegisterMenu.out.writeObject(info);
        RegisterMenu.out.flush();
        Object o = RegisterMenu.in.readObject();
        if(o instanceof RuntimeException) {
            throw (RuntimeException) o;
        }
        System.out.println(o);
        RegisterMenu.registeredName = name;
    }

    public static void changePass() throws IOException, ClassNotFoundException {
        String pass = RegisterMenu.get("password", RegisterMenu::isValidPassword);
        RegisterMenu.out.writeObject("change pass");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.writeObject(pass);
        RegisterMenu.out.flush();
        Object o = RegisterMenu.in.readObject();
        System.out.println(o);
    }
}
