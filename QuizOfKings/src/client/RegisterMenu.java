package client;

import exception.*;
import server.cotroller.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class RegisterMenu {

    static String registeredName;

    static ObjectInputStream in;

    static ObjectOutputStream out;

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        new Thread(new Server()).start();
        Thread.sleep(1000);
        try (Socket socket = new Socket("127.0.0.1", 80);
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            in = objectInputStream;
            out = objectOutputStream;
            run();
        }
    }

    public static void run() throws IOException, ClassNotFoundException {
        System.out.println("1.sign in");
        System.out.println("2.sign up");
        System.out.println("3.exit");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), RegisterMenu::run, RegisterMenu::signIn, () -> signUp(RegisterMenu::run),
                    RegisterMenu::exit);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            run();
        }
    }

    public static void signIn() throws IOException, ClassNotFoundException {
        try {
            String name = get("name", RegisterMenu::isvalidName);
            String pass = get("password", RegisterMenu::isValidPassword);
            HashMap<String, String> userInfo = new HashMap<>();
            userInfo.put("name", name);
            userInfo.put("pass", pass);
            out.writeObject("sign in");
            out.writeObject(userInfo);
            out.flush();
            Object o = in.readObject();
            if (o instanceof RuntimeException) {
                throw (RuntimeException) o;
            } else if (((String) o).equals("admin")) {
                registeredName = name;
                AdminMenu.run();
            } else {
                registeredName = name;
                UserMenu.run();
            }
        } catch (BackException e) {
            run();
        } catch (validExecption | NoFoundName | UnVerifyPass e) {
            System.out.println(e.getMessage());
            signIn();
        }
    }

    public static String get(String apply, Predicate<String> validation) throws IOException, ClassNotFoundException {
        System.out.println();
        System.out.println("if you want go back enter 0");
        System.out.print(apply+":");
        String input = ScannerWrapper.getInstance().nextLine();
        if(input.equals("0")) {
            throw new BackException();
        } else if(!validation.test(input)) {
            throw new validExecption(apply+" invalid");
        }
        return input;
    }

    public static boolean isvalidName(String name) {
        return name.matches("[a-zA-Z0-9_]+");
    }

    public static boolean isValidPassword(String pass) {
        return pass.matches("[[a-z]+[A-Z]+[0-9]+[!@#$%^&*?]+]{6,}");
    }

    public static void signUp(EventHandlerInterface backMethodReference) throws IOException, ClassNotFoundException {
        try {
            String name = get("name", RegisterMenu::isvalidName);
            String pass = get("password", RegisterMenu::isValidPassword);
            String verifyPass = get("confirm password", RegisterMenu::isValidPassword);
            String email = get("email", RegisterMenu::isValidEmail);
            HashMap<String, String> userInfo = new HashMap<>();
            userInfo.put("name", name);
            userInfo.put("pass", pass);
            userInfo.put("verify pass", verifyPass);
            userInfo.put("email", email);
            out.writeObject("sign up");
            out.writeObject(userInfo);
            out.flush();
            throw (RuntimeException) in.readObject();
        } catch (BackException e) {
            backMethodReference.run();
        } catch (validExecption | IsRepeated | UnVerifyPass e) {
            System.out.println(e.getMessage());
            signUp(backMethodReference);
        }catch (ClassCastException e) {
            System.out.println("signed up");
            backMethodReference.run();
        }
    }

    private static boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9_.]+@[a-zA-Z0-9_.]+.[a-zA-Z0-9_.]+");
    }

    public static void exit() throws IOException {
        System.out.println("good bye");
        out.writeObject("exit");
        out.flush();
    }
}
