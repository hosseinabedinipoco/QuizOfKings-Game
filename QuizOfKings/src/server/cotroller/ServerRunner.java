package server.cotroller;

import client.RegisterMenu;
import exception.GameFullBank;
import exception.IsRepeated;
import exception.NoFoundName;
import exception.UnVerifyPass;
import server.model.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ServerRunner implements Runnable {

    private ObjectInputStream in;

    private ObjectOutputStream out;

    public ServerRunner(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                String request = (String) in.readObject();
                if (request.equals("exit")) {
                    return;
                }
                requestHandler(request);
            } catch (IOException e) {

            } catch (ClassNotFoundException | InterruptedException e) {

            }
        }
    }

    private void requestHandler(String request) throws IOException, ClassNotFoundException, InterruptedException {
        switch (request) {
            case "sign in":
                signIn();
                break;
            case "sign up":
                signUp();
                break;
            case "show info":
                showInfo();
                break;
            case "change name":
                changeName();
                break;
            case "change pass":
                changePass();
                break;
            case "show":
                show();
                break;
            case "remove":
                remove();
                break;
            case "add question":
                addQuestion();
                break;
            case "friend request":
                handleFriendRequest();
                break;
            case "game request":
                handleGameRequest();
                break;
            case "rank user":
                returnRankUser();
                break;
            case "random game":
                randomGame();
                break;
            case "search":
                search();
                break;
            case "send friend request":
                sendFriendRequest();
                break;
            case "show friend":
                showFriend();
                break;
            case "send game request":
                sendGameRequest();
                break;
            case "now game":
                nowGame();
                break;
            case "game info":
                gameInfo();
                break;
            case "play game":
                playGame();
                break;
            case "update game":
                updateGame();
                break;
            case "result":
                result();
                break;
            case "select question":
                selectQuestion();
                break;
        }
    }

    private void selectQuestion() throws IOException, ClassNotFoundException {
        User you = (User) getPersonWithName((String)in.readObject());
        User enemy = (User) getPersonWithName((String) in.readObject());
        Game game = getGame(you, enemy);
        Collections.shuffle(game.getTypes());
        out.writeObject((ArrayList)game.getTypes().stream().limit(3).collect(Collectors.toList()));
        out.flush();
        String type = (String) in.readObject();
        game.removeTypes(type);
        Collections.shuffle(Database.getQuestions());
        ArrayList<Question> questions = (ArrayList<Question>) Database.getQuestions().stream().filter(q -> q.getType().equals(type)).limit(3).collect(Collectors.toList());
        game.setQuestions(questions);
        out.writeObject(setQuestion(questions));
        out.flush();
    }

    private void result() throws IOException, ClassNotFoundException {
        User you = (User) getPersonWithName((String)in.readObject());
        User enemy = (User) getPersonWithName((String) in.readObject());
        Game game = getGame(you, enemy);
        if(game.getScore(you) > game.getScore(enemy)) {
            out.writeObject(true);
            you.extraCoin(50);
            you.addExperience(50);
        }else if(game.getScore(you) == game.getScore(enemy)) {
            out.writeObject(null);
            you.addExperience(25);
            you.extraCoin(25);
        }else {
            out.writeObject(false);
            you.addExperience(10);
        }
        you.removeGame(game);
        out.flush();
    }

    private void updateGame() throws IOException, ClassNotFoundException {
        User you = (User) getPersonWithName((String)in.readObject());
        User enemy = (User) getPersonWithName((String) in.readObject());
        Game game = getGame(you, enemy);
        int score = (int) in.readObject();
        game.addScore(you, score);
        game.addTurn();
    }

    private void playGame() throws IOException, ClassNotFoundException {
        User you = (User) getPersonWithName((String)in.readObject());
        User enemy = (User) getPersonWithName((String) in.readObject());
        Game game = getGame(you, enemy);
        if(game.getQuestions().size() != 0) {
            out.writeObject(false);
            out.writeObject(setQuestion(game.getQuestions()));
            game.clearQuestion();
        }else {
            out.writeObject(true);
        }
        out.flush();
    }

    private ArrayList<ArrayList> setQuestion(ArrayList<Question> questions) {
        ArrayList<ArrayList> send = new ArrayList<>();
        ArrayList a;
        for(Question question : questions) {
            a = new ArrayList<>();
            a.add(question.getType());
            a.add(question.getQuestion());
            Collections.shuffle(question.getTests());
            a.add(question.getTests());
            a.add(question.getAnswer());
            send.add(a);
        }
        return send;
    }

    private void gameInfo() throws IOException, ClassNotFoundException {
        User you = (User) getPersonWithName((String)in.readObject());
        User enemy = (User) getPersonWithName((String) in.readObject());
        Game game = getGame(you, enemy);
        out.writeObject(game.getScore(you));
        out.writeObject(game.getScore(enemy));
        if(game.turn() == null) {
            out.writeObject(null);
        }else if (game.turn().equals(you)){
            out.writeObject(true);
        } else {
            out.writeObject(false);
        }
        out.flush();
    }

    private Game getGame(User you, User enemy) {
        for(Game game : you.getGames()) {
            if(game.getGuestUser().equals(enemy) || game.getHostUser().equals(enemy)) {
                return game;
            }
        }
        return null;
    }

    private void nowGame() throws IOException, ClassNotFoundException {
        User user = (User) getPersonWithName((String) in.readObject());
        ArrayList<String> list = new ArrayList<>();
        for (Game game : user.getGames()) {
            if (game.getGuestUser().equals(user)) {
                list.add(game.getHostUser().getName());
            } else {
                list.add(game.getGuestUser().getName());
            }
        }
        out.writeObject(list);
        out.flush();
    }

    private void sendGameRequest() throws IOException, ClassNotFoundException {
        User senderUser = (User) getPersonWithName((String) in.readObject());
        User receiverUser = (User) getPersonWithName((String) in.readObject());
        if (senderUser.getGames().size() == 5) {
            out.writeObject(new GameFullBank("you cannot play game more than 5"));
            out.flush();
        } else {
            receiverUser.addGameRequest(senderUser);
            out.writeObject("okay");
            out.flush();
        }
    }

    private void showFriend() throws IOException, ClassNotFoundException {
        User user = (User) getPersonWithName((String) in.readObject());
        out.writeObject(user.getFriends().stream().map(f -> f.getName()).collect(Collectors.toList()));
        out.flush();
    }

    private void sendFriendRequest() throws IOException, ClassNotFoundException {
        String sender = (String) in.readObject();
        String receiver = (String) in.readObject();
        User senderUser = (User) getPersonWithName(sender);
        User receiverUser = (User) getPersonWithName(receiver);
        receiverUser.addFriendRequest(senderUser);
    }

    private void search() throws IOException, ClassNotFoundException {
        String search = (String) in.readObject();
        ArrayList<String> list = (ArrayList<String>) Database.getPeople().stream().filter(p -> p instanceof User && p.getName().contains(search)).
                map(p -> p.getName()).collect(Collectors.toList());
        out.writeObject(list);
        out.flush();
    }

    private void randomGame() throws IOException, ClassNotFoundException, InterruptedException {
        User user = (User) getPersonWithName((String) in.readObject());
        if (user.getGames().size() == 5) {
            out.writeObject(new GameFullBank("you cannot play game more than 5"));
            out.flush();
        } else {
            Database.addExceptedUser(user);
            synchronized (user) {
                user.wait(10000);
            }
            if(user.enemy != null) {
                out.writeObject("you ply with " + user.enemy);
                user.enemy = "";
            }else {
                out.writeObject("no found user");
            }
            Database.removeExceptedUser(user);
            out.flush();
        }
    }

    private void returnRankUser() throws IOException {
        out.writeObject((ArrayList) Database.getRankUser().stream().limit(100).map(u -> u.getName() + "\tlevel: " + ((User) u).getLevel()).collect(Collectors.toList()));
        out.flush();
    }

    private void handleFriendRequest() throws IOException, ClassNotFoundException {
        User user = (User) getPersonWithName((String) in.readObject());
        out.writeObject(user.getFriendsRequests().stream().map(u -> u.getName()).collect(Collectors.toList()));
        out.flush();
        for (User u : user.getFriendsRequests()) {
            if ((boolean) in.readObject()) {
                user.addFriend(u);
                u.addFriend(user);
            }
        }
        user.clearFriendRequest();
    }

    private void handleGameRequest() throws IOException, ClassNotFoundException {
        User user = (User) getPersonWithName((String) in.readObject());
        out.writeObject(user.getGameRequests().stream().map(u -> u.getName()).collect(Collectors.toList()));
        out.flush();
        for (User u : user.getGameRequests()) {
            out.writeObject(u.getName());
            out.flush();
            if ((boolean) in.readObject()) {
                if (user.getGames().size() >= 5) {
                    out.writeObject(new GameFullBank("you cannot play game more than 5"));
                    u.removeGameRequest(user);
                } else {
                    Game game = new Game(u, user);
                    u.addGame(game);
                    user.addGame(game);
                    out.writeObject("okay");
                }
            } else {
                out.writeObject("okay");
            }
            out.flush();
        }
        user.clearFriendRequest();
    }

    private void addQuestion() throws IOException, ClassNotFoundException {
        HashMap info = (HashMap) in.readObject();
        String question = (String) info.get("question");
        ArrayList<String> test = (ArrayList<String>) info.get("tests");
        String type = (String) info.get("type");
        String answer = (String) info.get("answer");
        Database.addQuestion(new Question(type, question, test, answer));
    }

    private void remove() throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        Person person = getPersonWithName(name);
        if (!(person instanceof User)) {
            out.writeObject(new NoFoundName("no user with this name"));
        } else {
            Database.removeUser((User) person);
            out.writeObject("removed");
        }
        out.flush();
    }

    private void show() throws IOException, ClassNotFoundException {
        String apply = (String) in.readObject();
        List list;
        if (apply.equals("user")) {
            list = Database.getPeople().stream().filter(p -> p instanceof User).collect(Collectors.toList());
        } else {
            list = Database.getQuestions();
        }
        out.writeObject(list);
        out.flush();
    }

    private void changePass() throws IOException, ClassNotFoundException {
        Person person = getPersonWithName((String) in.readObject());
        person.setPassword((String) in.readObject());
        out.writeObject("password changed");
        out.flush();
    }

    private void changeName() throws IOException, ClassNotFoundException {
        HashMap<String, String> info = (HashMap<String, String>) in.readObject();
        Person person = getPersonWithName(info.get("old name"));
        String newName = info.get("new name");
        if (isAvialableEmail(newName)) {
            out.writeObject(new IsRepeated("this name is avialable"));
        } else {
            out.writeObject("name is changed");
            person.setName(info.get("new name"));
        }
        out.flush();
    }

    private void showInfo() throws IOException, ClassNotFoundException {
        Person person = getPersonWithName((String) in.readObject());
        out.writeObject(person.toString());
        out.flush();
    }

    private void signUp() throws IOException, ClassNotFoundException {
        HashMap<String, String> info = (HashMap<String, String>) in.readObject();
        String name = info.get("name");
        String pass = info.get("pass");
        String verifyPass = info.get("verify pass");
        String email = info.get("email");
        if (!pass.equals(verifyPass)) {
            out.writeObject(new UnVerifyPass("unConfirm password"));
        } else if (isAvialableName(name)) {
            out.writeObject(new IsRepeated("this name is aveilable"));
        } else if (isAvialableEmail(email)) {
            out.writeObject(new IsRepeated("the email is aviealable"));
        } else {
            User user = new User(name, pass, email);
            Database.addUser(user);
            out.writeObject("signed up");
        }
        out.flush();
    }

    private boolean isAvialableEmail(String email) {
        return Database.getPeople().stream().filter(p -> p instanceof User).map(p -> ((User) p).getEmail()).anyMatch(e -> e.equals(email));
    }

    private boolean isAvialableName(String name) {
        return Database.getPeople().stream().map(p -> p.getName()).anyMatch(n -> n.equals(name));
    }

    private void signIn() throws IOException, ClassNotFoundException {
        HashMap<String, String> info = (HashMap<String, String>) in.readObject();
        String name = info.get("name");
        String pass = info.get("pass");
        Person person = getPersonWithName(name);
        if (person == null) {
            out.writeObject(new NoFoundName("there is no admin or user with this name"));
        } else if (!person.getPassword().equals(pass)) {
            out.writeObject(new UnVerifyPass("incorrect password"));
        } else if (person instanceof User) {
            out.writeObject("user");
        } else {
            out.writeObject("admin");
        }
        out.flush();
    }

    private Person getPersonWithName(String name) {
        for (Person person : Database.getPeople()) {
            if (name.equals(person.getName())) {
                return person;
            }
        }
        return null;
    }
}
