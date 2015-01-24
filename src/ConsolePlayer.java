
import java.io.*;
import snct_procon.supersnake.*;

public class ConsolePlayer extends Player {
    
    public ConsolePlayer() {
        super("Java太郎", new Color(0, 255, 0));
    }

    @Override
    public Action think(GameState state) {
        // 行動を入力
        System.out.println("[ Action ]");
        Action action = Action.STRAIGHT;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("straight(s) | left(l) | right(r) > ");
                switch (in.readLine()) {
                case "straight":
                case "s":
                    action = Action.STRAIGHT;
                    break;
                case "left":
                case "l":
                    action = Action.LEFT;
                    break;
                case "right":
                case "r":
                    action = Action.RIGHT;
                    break;
                default:
                    System.out.println("入力エラー");
                    continue;
                }
                break;
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        return action;
    }

}
