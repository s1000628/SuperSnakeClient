
import java.io.*;
import snct_procon.supersnake.*;

public class ConsolePlayer extends Player {
    
    public ConsolePlayer() {
        super("Java太郎", new Color(0, 255, 0));
    }

    @Override
    public Action think(GameState state) {
        // フィールドの状態を表示
        System.out.println("[ Field ]");

        FieldState field = state.getFieldState();
        int width = field.getSize().getWidth();
        int height = field.getSize().getHeight();
        int playersCount = state.getPlayersCount();
        
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int p = -1;
                
                // 位置(x, y)にプレイヤーがいるか確認
                for (int i = 0; i < playersCount; ++i) {
                    Point pos = state.getPlayerState(i).getPosition();
                    if (pos.getX() == x && pos.getY() == y) {
                        p = i;
                        break;
                    }
                }
                
                if (p >= 0) {
                    // プレイヤーがいたらプレイヤー番号を出力
                    System.out.print(p);
                } else {
                    // プレイヤーがいなかったら通行可能か否かを出力
                    if (field.getCellState(x, y).isPassable()) {
                        System.out.print("_");
                    } else {
                        System.out.print("X");
                    }
                }
            }
            
            System.out.println();
        }
        
        // 各プレイヤーの状態を表示
        System.out.println("[ Players State ]");
        for (int i = 0; i < playersCount; ++i) {
            PlayerState player = state.getPlayerState(i);
            System.out.println(
                player.getName() + "(player" + i + "):"
                + (player.isDead() ? " (死亡)" : "")
                + " (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")"
                + " " + player.getDirection().name()
                );
        }
        
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
