package server.cotroller;

import server.model.Database;
import server.model.Game;
import server.model.User;

import java.util.Random;

public class RandomHandler implements Runnable {
    @Override
    public void run() {
        int rand1, rand2;
        rand1 = rand2 = 0;
        Random random = new Random();
        int size;
        boolean flag = true;
        do {
            size = Database.getExceptedUsers().size();
            try {
                rand1 = random.nextInt(0, size);
                rand2 = random.nextInt(0, size);
            }catch (IllegalArgumentException e) {

            }
        } while (rand1 == rand2);
        User user1 = Database.getExceptedUsers().get(rand1);
        User user2 = Database.getExceptedUsers().get(rand2);
        Game game = new Game(user1, user2);
        user1.addGame(game);
        user2.addGame(game);
        user1.enemy = user2.getName();
        user2.enemy = user1.getName();
        synchronized (user1) {
            user1.notify();
        }
        synchronized (user2) {
            user2.notify();
        }
    }
}
