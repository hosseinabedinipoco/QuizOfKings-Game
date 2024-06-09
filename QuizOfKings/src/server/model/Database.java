package server.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Database {

    private static ArrayList<Person> people = new ArrayList<>();
    static {
        people.add(new Admin("Hossein", "Abcd1234@"));
        people.add(new User("abol", "Abcd!1", "a@g.c"));
        people.add(new User("hossein", "Abcd!1", "h@g.c"));
    }

    private static ArrayList<Person> rankUser = new ArrayList<>();

    private static ArrayList<User> exceptedUsers = new ArrayList<>();

    private static ArrayList<Question> questions = new ArrayList<>();

    public static void addUser(User user) {
        people.add(user);
    }

    public static void removeUser(User user) {
        people.remove(user);
    }

    public static ArrayList<Person> getPeople() {
        return copyWithoutReference(people);
    }

    public static void sortUser() {
        rankUser = (ArrayList<Person>) people.stream().filter(p -> p instanceof User).sorted().collect(Collectors.toList());
    }

    public static ArrayList<Person> getRankUser() {
        return rankUser;
    }

    public static void addExceptedUser(User user) {
        exceptedUsers.add(user);
    }

    public static void removeExceptedUser(User user) {
        exceptedUsers.remove(user);
    }

    public static ArrayList<User> getExceptedUsers() {
        return exceptedUsers;
    }

    public static void addQuestion(Question question) {
        questions.add(question);
    }

    public static void removeQuestion(Question question) {
        questions.remove(question);
    }

    public static ArrayList<Question> getQuestions() {
        return copyWithoutReference(questions);
    }

    private static <T>ArrayList<T> copyWithoutReference(ArrayList<T> oldList) {
        ArrayList<T> newList = new ArrayList<>();
        for(T t : oldList) {
            newList.add(t);
        }
        return newList;
    }
}
