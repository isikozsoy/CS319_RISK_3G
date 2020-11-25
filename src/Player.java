import java.util.Random;

public class Player {
    final Random random=new Random();

    private String color;

    Player() {
        int nextInt = random.nextInt(0xffffff + 1);
        color = String.format("#%06x", nextInt);
    }

    public String getColor() {
        return color;
    }
}