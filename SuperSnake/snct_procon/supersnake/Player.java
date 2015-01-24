package snct_procon.supersnake;

import java.util.*;

public abstract class Player {
    
    /**
     * �Q�[���̏�Ԃ���v���C���[�̍s�������肷��.
     * �T�u�N���X�Ŏ�������K�v������.
     * @param state �Q�[���̏��
     * @return �v���C���[�̍s��
     */
    public abstract Action think(GameState state);
    
    /**
     * �v���C���[������������.
     * @param name �v���C���[�̖��O
     * @param color �v���C���[�̐F
     */
    public Player(String name, Color color) {
        this.name = new String(name);
        this.color = color.clone();
    }

    /**
     * �Q�[���̏�Ԃ�\������.
     * �v���C���[�̐����Ɋւ�炸�A���^�[���̎n�߂ɌĂяo�����.
     * �T�u�N���X�ŃI�[�o�[���C�h����΁A���R�Ȍ`���ŕ\���ł���.
     * @param state
     */
    public void showGameState(GameState state) {
        // �t�B�[���h�̏��
        System.out.println("[ Field ]");
        showField(state);
        
        // �v���C���[�̏��
        System.out.println("[ Players ]");
        int playersCount = state.getPlayersCount();
        for (int i = 0; i < playersCount; ++i) {
            PlayerState player = state.getPlayerState(i);
            System.out.println(
                player.getName() + "(player" + i + "):"
                + (player.isDead() ? " (���S)" : "")
                + " (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")"
                + " " + player.getDirection().name()
                );
        }
    }
    
    /**
     * �Q�[���̌��ʂ�\������.
     * �T�u�N���X�ŃI�[�o�[���C�h����΁A���R�Ȍ`���ŕ\���ł���.
     * @param state �Q�[���̏��
     * @param rank �e�v���C���[�̏���
     */
    public void showResult(GameState state, List<Integer> rank) {
        // �t�B�[���h�̏��
        System.out.println("[ Field ]");
        showField(state);
        
        // ����
        System.out.println("[ Ranking ]");
        int playersCount = state.getPlayersCount();
        int maxVal = 0;
        for (int i = 0; i < playersCount; ++i) {
            if (rank.get(i) > maxVal) {
                maxVal = rank.get(i);
            }
        }
        List<List<Integer>> rankSorted = new ArrayList<List<Integer>>();
        for (int r = 0; r < maxVal; ++r) {
            rankSorted.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < playersCount; ++i) {
            rankSorted.get(rank.get(i) - 1).add(i);
        }
        for (int r = 0; r < maxVal; ++r) {
            System.out.println((r + 1) + "��:");
            for (Integer i : rankSorted.get(r)) {
                String name = state.getPlayerState(i).getName();
                System.out.println("  " + name + "(player" + i + ")");
            }
        }
    }

    private void showField(GameState state) {
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
    }
    
    /**
     * �v���C���[�̖��O���擾����.
     * @return �v���C���[�̖��O
     */
    public String getName() {
        return new String(name);
    }
    
    /**
     * �v���C���[�̐F���擾����.
     * @return �v���C���[�̐F
     */
    public Color getColor() {
        return color;
    }
    
    private String name;
    private Color color;
}
