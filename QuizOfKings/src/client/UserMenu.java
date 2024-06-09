package client;

import exception.BackException;
import exception.GameFullBank;
import exception.IsRepeated;
import exception.validExecption;
import server.model.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class UserMenu {

    public static void run() throws IOException, ClassNotFoundException {
        System.out.println("1.game");
        System.out.println("2.profile");
        System.out.println("3.requests");
        System.out.println("4.logout");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), UserMenu::run, UserMenu::game, UserMenu::profile, UserMenu::requests,
                    UserMenu::logOut);
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("invalid command");
            run();
        }
    }

    public static void requests() throws IOException, ClassNotFoundException {
        System.out.println("1.friend request");
        System.out.println("2.game request");
        System.out.println("3.back");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), UserMenu::requests, UserMenu::friendRequest, UserMenu::gameRequest,
                    UserMenu::run);
        }catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            requests();
        }
    }

    public static void friendRequest() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("friend request");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.flush();
        ArrayList<String> usersName = (ArrayList<String>) RegisterMenu.in.readObject();
        for(int i = 0; i < usersName.size(); i++) {
            System.out.println(usersName.get(i)+" has friend request for you");
            RegisterMenu.out.writeObject(acceptOrReject());
            RegisterMenu.out.flush();
        }
        Utility.excepted();
        requests();
    }

    private static boolean acceptOrReject() {
        System.out.println("if you want accept press 0");
        return ScannerWrapper.getInstance().nextLine().equals("0");
    }

    public static void gameRequest() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("game request");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.flush();
        ArrayList<String> usersName = (ArrayList<String>) RegisterMenu.in.readObject();
        for(String s : usersName) {
            System.out.println(s+" has game request for you");
            RegisterMenu.out.writeObject(acceptOrReject());
            RegisterMenu.out.flush();
            try {
                throw (RuntimeException) RegisterMenu.in.readObject();
            }catch (GameFullBank e) {
                System.out.println(e.getMessage());
            }catch (ClassCastException e) {

            }
        }
        Utility.excepted();
        requests();
    }

    public static void game() throws IOException, ClassNotFoundException {
        System.out.println("1.random new game");
        System.out.println("2.search user");
        System.out.println("3.game with friends");
        System.out.println("4.now game");
        System.out.println("5.back");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), UserMenu::game, UserMenu::randomGame, UserMenu::searchUser,
                    UserMenu::showFriends, UserMenu::nowGame, UserMenu::run);
        }catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            e.printStackTrace();
            game();
        }
    }

    public static void randomGame() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("random game");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.flush();
        try {
            Object o = RegisterMenu.in.readObject();
            if(o instanceof RuntimeException) {
                throw (RuntimeException)o;
            }
            System.out.println(o);
            game();
        }catch (GameFullBank e) {
            e.getMessage();
            game();
        }
    }

    public static void searchUser() throws IOException, ClassNotFoundException {
        System.out.print("search:");
        RegisterMenu.out.writeObject("search");
        RegisterMenu.out.writeObject(ScannerWrapper.getInstance().nextLine());
        RegisterMenu.out.flush();
        ArrayList<String> list = (ArrayList<String>) RegisterMenu.in.readObject();
        System.out.println("for send request press on user's name");
        for(int i = 0; i < list.size(); i++) {
            System.out.println((i + 1)+". "+list.get(i));
        }
        try {
            String name = list.get(Integer.parseInt(ScannerWrapper.getInstance().nextLine()) - 1);
            RegisterMenu.out.writeObject("send friend request");
            RegisterMenu.out.writeObject(RegisterMenu.registeredName);
            RegisterMenu.out.writeObject(name);
            RegisterMenu.out.flush();
            game();
        }catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("invalid command");
            game();
        }
    }

    public static void showFriends() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("show friend");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.flush();
        ArrayList<String> list = (ArrayList<String>) RegisterMenu.in.readObject();
        System.out.println("for game press on user's name");
        for(int i = 0; i < list.size(); i++) {
            System.out.println((i + 1)+". "+list.get(i));
        }
        try {
            String name = list.get(Integer.parseInt(ScannerWrapper.getInstance().nextLine()) - 1);
            RegisterMenu.out.writeObject("send game request");
            RegisterMenu.out.writeObject(RegisterMenu.registeredName);
            RegisterMenu.out.writeObject(name);
            Object o = RegisterMenu.in.readObject();
            if(o instanceof RuntimeException) {
                throw (RuntimeException) o;
            }
            game();
        }catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("invalid command");
            game();
        }catch (GameFullBank e) {
            System.out.println(e.getMessage());
            game();
        }
    }

    public static void nowGame() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("now game");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.flush();
        ArrayList<String> list = (ArrayList<String>) RegisterMenu.in.readObject();
        System.out.println("for game press");
        for(int i = 0; i < list.size(); i++) {
            System.out.println((i + 1)+". "+list.get(i));
        }
        try {
            String name = list.get(Integer.parseInt(ScannerWrapper.getInstance().nextLine()) - 1);
            GameMenu.name = name;
            GameMenu.run();
        }catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("invalid command");
            game();
        }
    }

    public static void profile() throws IOException, ClassNotFoundException {
        System.out.println("1.show info");
        System.out.println("2.change");
        System.out.println("3.show rank user");
        System.out.println("4.back");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), UserMenu::profile, UserMenu::showUserInfo, UserMenu::change,
                    UserMenu::showRankUser, UserMenu::run);
        }catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            profile();
        }
    }

    public static void showRankUser() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("rank user");
        RegisterMenu.out.flush();
        ArrayList<String> list = (ArrayList<String>) RegisterMenu.in.readObject();
        for(int i = 0; i < list.size(); i++) {
            System.out.println((i + 1)+". "+list.get(i));
        }
        Utility.excepted();
        profile();
    }

    public static void showUserInfo() throws IOException, ClassNotFoundException {
        Utility.showInfo();
        Utility.excepted();
        profile();
    }

    public static void change() throws IOException, ClassNotFoundException {
        System.out.println("1.change name");
        System.out.println("2.change password");
        System.out.println("3.back");
        String input = ScannerWrapper.getInstance().nextLine();
        try {
            EventHandler.eventHandle(Integer.parseInt(input), UserMenu::change, UserMenu::changeName, UserMenu::changePass,
                    UserMenu::profile);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("invalid command");
            change();
        }
    }

    public static void changeName() throws IOException, ClassNotFoundException {
        try {
            Utility.changeName();
            change();
        }catch (BackException e) {
            change();
        }catch (IsRepeated | validExecption e) {
            System.out.println(e.getMessage());
            changeName();
        }
    }

    public static void changePass() throws IOException, ClassNotFoundException {
        try {
            Utility.changePass();
            change();
        }catch (BackException e) {
            change();
        }catch (validExecption e) {
            changePass();
        }
    }

    public static void logOut() throws IOException, ClassNotFoundException {
        System.out.println();
        System.out.println("logged out");
        System.out.println();
        RegisterMenu.run();
    }
}
