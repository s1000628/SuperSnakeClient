package snct_procon.supersnake;

import java.util.*;

/**
 * SuperSnake �̃Q�[���̏�Ԃ�\��.
 */
public class GameState {
    
    /**
     * �t�B�[���h�̏�Ԃ��擾����.
     * @return �t�B�[���h�̏��
     */
    public FieldState getFieldState() {
        return field;
    }
    
    /**
     * �v���C���[�̏�Ԃ��擾����.
     * @param playerIndex �v���C���[�ԍ�[0, �v���C���[�� - 1]
     * @return �v���C���[�̏��
     */
    public PlayerState getPlayerState(int playerIndex) {
        return players.get(playerIndex);
    }
    
    /**
     * �v���C���[�̐l�����擾����.
     * @return �v���C���[�̐l��
     */
    public int getPlayersCount() {
        return players.size();
    }
    
    /**
     * �Q�[���̏�Ԃ�����������.
     * @param field �t�B�[���h�̏��
     * @param players �v���C���[�̏��
     */
    public GameState(FieldState field, List<PlayerState> players) {
        this.field = field.clone();
        this.players = new ArrayList<PlayerState>();
        for (PlayerState player : players) {
            this.players.add(player.clone());
        }
    }
    
    private FieldState field;
    private List<PlayerState> players;
    
    @Override
    public GameState clone() {
        return new GameState(field, players);
    }

    @Override
    public String toString() {
        return "GameState [field=" + field + ", players=" + players + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((field == null) ? 0 : field.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameState other = (GameState) obj;
        if (field == null) {
            if (other.field != null)
                return false;
        } else if (!field.equals(other.field))
            return false;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        return true;
    }
}
