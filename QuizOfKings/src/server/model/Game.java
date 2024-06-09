package server.model;

import java.util.ArrayList;

public class Game {
    private int hostScore = 0;

    private int guestScore = 0;

    private User hostUser;

    private User guestUser;

    private ArrayList<Question> questions = new ArrayList<>();

    private float turn = 1;

    private ArrayList<String> types = new ArrayList<>();
    {
        types.add("SPORT"); types.add("FOOTBALL"); types.add("SCIENCE"); types.add("RELIGIOUS"); types.add("PUBLIC_INFORMATION");
                types.add("GEOGRAPHY"); types.add("HISTORY"); types.add("MATH"); types.add("ENGLISH"); types.add("TECHNOLOGY");
                types.add("CINEMA"); types.add("MUSIC");
    }

    public Game(User hostUser, User guestUser) {
        this.guestUser = guestUser;
        this.hostUser = hostUser;
    }

    public void addScore(User user, int score) {
        if(hostUser.equals(user)) {
            hostScore += score;
        } else {
            guestScore += score;
        }
    }

    public void addTurn() {
        turn += 0.5;
    }

    public void removeTypes(String type) {
        types.remove(type);
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public User turn() {
        if(turn == 1 || turn == 2.5 || turn ==3) {
            return hostUser;
        }else if(turn == 1.5 || turn == 2 || turn == 3.5) {
            return guestUser;
        }else {
            return null;
        }
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getScore(User user) {
        if(user.equals(hostUser)) {
            return hostScore;
        }else {
            return guestScore;
        }
    }

    public User getGuestUser() {
        return guestUser;
    }

    public User getHostUser() {
        return hostUser;
    }

    public void clearQuestion() {
        questions = new ArrayList<>();
    }
}
