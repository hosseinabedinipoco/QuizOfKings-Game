package client;

import exception.BackException;
import exception.IsRepeated;
import exception.NoFoundName;
import exception.validExecption;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

public class AdminMenu {

    public static void run() throws IOException, ClassNotFoundException {
        System.out.println("1.show user");
        System.out.println("2.add user");
        System.out.println("3.remove user");
        System.out.println("4.show question");
        System.out.println("5.add question");
        System.out.println("6.profile");
        System.out.println("7.logg out");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), AdminMenu::run, () -> show("user"),
                    () -> RegisterMenu.signUp(AdminMenu::run), AdminMenu::removeUser, () -> show("question"),
                    AdminMenu::addQuestion, AdminMenu::profile, AdminMenu::loggOut);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            run();
        }
    }

    public static void show(String apply) throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("show");
        RegisterMenu.out.writeObject(apply);
        RegisterMenu.out.flush();
        List list = (ArrayList) RegisterMenu.in.readObject();
        list.stream().forEach(l -> System.out.println(l));
        Utility.excepted();
        run();
    }

    public static void removeUser() throws IOException, ClassNotFoundException {
        try {
            String name = RegisterMenu.get("name", RegisterMenu::isvalidName);
            RegisterMenu.out.writeObject("remove");
            RegisterMenu.out.writeObject(name);
            RegisterMenu.out.flush();
            throw (RuntimeException) RegisterMenu.in.readObject();
        } catch (BackException e) {
            run();
        } catch (validExecption | NoFoundName e) {
            System.out.println(e.getMessage());
            removeUser();
        } catch (ClassCastException e) {
            System.out.println("this user removed");
            run();
        }
    }

    public static void addQuestion() throws IOException, ClassNotFoundException {
        try {
            String type = getType();
            String question = get("Question");
            ArrayList tests = new ArrayList();
            tests.add(get("test1"));
            tests.add(get("test2"));
            tests.add(get("test3"));
            tests.add(get("test4"));
            String answer = getAnswer(tests);
            RegisterMenu.out.writeObject("add question");
            HashMap info = new HashMap<>();
            info.put("type", type);
            info.put("question", question);
            info.put("answer", answer);
            info.put("tests", tests);
            RegisterMenu.out.writeObject(info);
            RegisterMenu.out.flush();
            System.out.println("question added");
            run();
        } catch (BackException e) {
            run();
        }
    }

    private static String getAnswer(ArrayList<String> tests) {
        for (int i = 0; i < 4; i++) {
            System.out.println((i + 1) + ". " + tests.get(i));
        }
        String input;
        try {
            input = ScannerWrapper.getInstance().nextLine();
            return tests.get(Integer.parseInt(input) - 1);
        } catch (InputMismatchException e) {
            System.out.println("invalid");
            return getAnswer(tests);
        }
    }

    private static String get(String apply) {
        System.out.println("if you want back press 0");
        System.out.print(apply + ":");
        String input = ScannerWrapper.getInstance().nextLine();
        if (input.equals("0")) {
            throw new BackException();
        }
        return input;
    }

    private static String getType() {
        System.out.println("if you want back enter 0");
        String[] types = {"SPORT", "FOOTBALL", "SCIENCE", "RELIGIOUS", "PUBLIC_INFORMATION", "GEOGRAPHY", "HISTORY",
                "MATH", "ENGLISH", "TECHNOLOGY", "CINEMA", "MUSIC"};
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        String input;
        try {
            input = ScannerWrapper.getInstance().nextLine();
            if (input.equals("0")) {
                throw new BackException();
            }
            return types[Integer.parseInt(input) - 1];
        } catch (InputMismatchException | ArrayIndexOutOfBoundsException e) {
            System.out.println("invalid");
            return getType();
        }
    }

    public static void profile() throws IOException, ClassNotFoundException {
        System.out.println("1.show info");
        System.out.println("2.change");
        System.out.println("3.back");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), AdminMenu::profile, AdminMenu::showInfo, AdminMenu::change,
                    AdminMenu::run);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            profile();
        }
    }

    public static void showInfo() throws IOException, ClassNotFoundException {
        Utility.showInfo();
        Utility.excepted();
        profile();
    }

    public static void change() throws IOException, ClassNotFoundException {
        System.out.println("1.change name");
        System.out.println("2.change pass");
        System.out.println("3.back");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), AdminMenu::change, AdminMenu::changeName, AdminMenu::changePass,
                    AdminMenu::profile);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            change();
        }
    }

    public static void changeName() throws IOException, ClassNotFoundException {
        try {
            Utility.changeName();
            change();
        } catch (BackException e) {
            change();
        } catch (IsRepeated | validExecption e) {
            System.out.println(e.getMessage());
            changeName();
        }
    }

    public static void changePass() throws IOException, ClassNotFoundException {
        try {
            Utility.changePass();
            change();
        } catch (BackException e) {
            change();
        } catch (validExecption e) {
            changePass();
        }
    }

    public static void loggOut() throws IOException, ClassNotFoundException {
        System.out.println("you logged out");
        RegisterMenu.run();
    }
}
