
import java.io.*;
import snct_procon.supersnake.*;

public class ConsolePlayer extends Player {
    
    public ConsolePlayer() {
        super("Java���Y", new Color(0, 255, 0));
    }

    @Override
    public Action think(GameState state) {
        // �t�B�[���h�̏�Ԃ�\��
        System.out.println("[ Field ]");

        FieldState field = state.getFieldState();
        int width = field.getSize().getWidth();
        int height = field.getSize().getHeight();
        int playersCount = state.getPlayersCount();
        
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int p = -1;
                
                // �ʒu(x, y)�Ƀv���C���[�����邩�m�F
                for (int i = 0; i < playersCount; ++i) {
                    Point pos = state.getPlayerState(i).getPosition();
                    if (pos.getX() == x && pos.getY() == y) {
                        p = i;
                        break;
                    }
                }
                
                if (p >= 0) {
                    // �v���C���[��������v���C���[�ԍ����o��
                    System.out.print(p);
                } else {
                    // �v���C���[�����Ȃ�������ʍs�\���ۂ����o��
                    if (field.getCellState(x, y).isPassable()) {
                        System.out.print("_");
                    } else {
                        System.out.print("X");
                    }
                }
            }
            
            System.out.println();
        }
        
        // �e�v���C���[�̏�Ԃ�\��
        System.out.println("[ Players State ]");
        for (int i = 0; i < playersCount; ++i) {
            PlayerState player = state.getPlayerState(i);
            System.out.println(
                player.getName() + "(player" + i + "):"
                + (player.isDead() ? " (���S)" : "")
                + " (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")"
                + " " + player.getDirection().name()
                );
        }
        
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
