package server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class User extends Person implements Serializable, Comparable<User> {

    private String email;

    private int coin = 100;

    private int level = 1;

    private int rank;

    private int experience = 0;

    private ArrayList<User> friends = new ArrayList<>();

    private ArrayList<User> friendsRequests = new ArrayList<>();

    private ArrayList<User> gameRequests = new ArrayList<>();

    private ArrayList<Game> games = new ArrayList<>();

    public String enemy;

    public User(String name, String password, String email) {
        super(name, password);
        this.email = email;
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        level++;
    }

    public void addExperience(int add) {
        experience += add;
        if(experience > 160 * level * level) {
            increaseLevel();
        }
    }

    public int getRank() {
        return rank;
    }

    public void addFriendRequest(User user) {
        friendsRequests.add(user);
    }

    public void clearFriendRequest() {
        friendsRequests = new ArrayList<>();
    }

    public ArrayList<User> getFriendsRequests() {
        return copyWithoutReference(friendsRequests);
    }

    public void addGameRequest(User user) {
        gameRequests.add(user);
    }

    public void removeGameRequest(User user) {
        gameRequests.remove(user);
    }

    public ArrayList<User> getGameRequests() {
        return copyWithoutReference(gameRequests);
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public ArrayList<Game> getGames() {
        return copyWithoutReference(games);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public int getCoin() {
        return coin;
    }

    public void addFriend(User user) {
        friends.add(user);
    }

    public void removeFriend(User user) {
        friends.remove(user);
    }

    public ArrayList<User> getFriends() {
        return copyWithoutReference(friends);
    }

    private <T>ArrayList<T> copyWithoutReference(ArrayList<T> oldList) {
        ArrayList<T> newList = new ArrayList<>();
        for(T t : oldList) {
            newList.add(t);
        }
        return newList;
    }

    public void extraCoin(int extra) {
        coin += extra;
    }

    public void lessCoin(int less) {
        coin -= less;
    }

    @Override
    public String toString() {
        return "*********************\n"+
                super.toString()+
                "email: "+email+"\n"+
                "coin: "+coin+"\n"+
                "level: "+level+"\n"+
                "rank: "+rank+"\n"+
                "********************";
    }

    @Override
    public int compareTo(User user) {
        return this.experience - user.experience;
    }
}
