package client;

import java.io.IOException;
import java.util.ArrayList;

public class GameMenu {

    public static String name;

    public static void run() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("game info");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.writeObject(name);
        RegisterMenu.out.flush();
        System.out.println("you:"+RegisterMenu.in.readObject()+"\t\t"+name+":"+RegisterMenu.in.readObject());
        Object o = RegisterMenu.in.readObject();
        if(o == null) {
            gameStatus();
        } else {
            boolean turn = (boolean) o;
            if(turn) {
                System.out.println("iis your turn");
                playGame();
            }else{
                System.out.println("it is not your turn");
                Utility.excepted();
                UserMenu.game();
            }
        }
    }

    private static void gameStatus() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("result");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.writeObject(name);
        RegisterMenu.out.flush();
        Object o = RegisterMenu.in.readObject();
        if(o == null) {
            System.out.println("you draw");
        } else {
            Boolean result = (Boolean) o;
            if (result) {
                System.out.println("you won");
            } else if (!result) {
                System.out.println("you lose");
            }
        }
        UserMenu.game();
    }

    private static void playGame() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("play game");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.writeObject(name);
        RegisterMenu.out.flush();
        if((boolean) RegisterMenu.in.readObject()) {
            ArrayList<ArrayList> questions = selectQuestion();
            showQuestion(questions);
        }else {
            showQuestion((ArrayList<ArrayList>)RegisterMenu.in.readObject());
        }
    }

    private static void showQuestion(ArrayList<ArrayList> arrayLists) throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("update game");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.writeObject(name);
        int score = 0;
        String input;
        String yourAns;
        long start;
        for(int i = 0; i < 3; i++) {
            ArrayList a = arrayLists.get(i);
            start = System.currentTimeMillis();
            yourAns = "";
            System.out.println("type: "+a.get(0));
            System.out.println("question: "+a.get(1));
            System.out.println("1."+((ArrayList)a.get(2)).get(0));
            System.out.println("2."+((ArrayList)a.get(2)).get(1));
            System.out.println("3."+((ArrayList)a.get(2)).get(2));
            System.out.println("4."+((ArrayList)a.get(2)).get(3));
            while (System.currentTimeMillis() - start < 15000) {
                try {
                    input = ScannerWrapper.getInstance().nextLine();
                    yourAns = (String) ((ArrayList)a.get(2)).get(Integer.parseInt(input) - 1);
                    break;
                }catch (NumberFormatException | IndexOutOfBoundsException e) {

                }
            }
            if(((String)a.get(3)).equals(yourAns)) {
                System.out.println("correct answer");
                score++;
            }else {
                System.out.println("right answer: "+(String) a.get(3));
            }
        }
        RegisterMenu.out.writeObject(score);
        RegisterMenu.out.flush();
        run();
    }

    private static ArrayList<ArrayList> selectQuestion() throws IOException, ClassNotFoundException {
        RegisterMenu.out.writeObject("select question");
        RegisterMenu.out.writeObject(RegisterMenu.registeredName);
        RegisterMenu.out.writeObject(name);
        RegisterMenu.out.flush();
        ArrayList<String> types = (ArrayList<String>) RegisterMenu.in.readObject();
        for(int i = 0; i < types.size(); i++) {
            System.out.println((i+1)+". "+types.get(i));
        }
        String input = ScannerWrapper.getInstance().nextLine();
        RegisterMenu.out.writeObject(types.get(Integer.parseInt(input) - 1));
        RegisterMenu.out.flush();
        return (ArrayList<ArrayList>) RegisterMenu.in.readObject();
    }
}
