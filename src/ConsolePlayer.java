
import java.io.*;
import snct_procon.supersnake.*;

/**
 * �W�����͂ɂ���đ���\�ȃv���C���[.
 */
public class ConsolePlayer extends Player {
    
    /**
     * ConsolePlayer ������������.
     */
    public ConsolePlayer() {
        super("Java���Y", new Color(0, 255, 0));
    }

    /**
     * �W�����͂���̓��͂ɂ���čs�������肷��.
     */
    @Override
    public Action think(GameState state) {
        // �s�������
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
                    System.out.println("���̓G���[");
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
