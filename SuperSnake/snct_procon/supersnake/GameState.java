package snct_procon.supersnake;

import java.util.*;

/**
 * SuperSnake のゲームの状態を表す.
 */
public class GameState {
    
    /**
     * フィールドの状態を取得する.
     * @return フィールドの状態
     */
    public FieldState getFieldState() {
        return field;
    }
    
    /**
     * プレイヤーの状態を取得する.
     * @param playerIndex プレイヤー番号[0, プレイヤー数 - 1]
     * @return プレイヤーの状態
     */
    public PlayerState getPlayerState(int playerIndex) {
        return players.get(playerIndex);
    }
    
    /**
     * プレイヤーの人数を取得する.
     * @return プレイヤーの人数
     */
    public int getPlayersCount() {
        return players.size();
    }
    
    /**
     * ゲームの状態を初期化する.
     * @param field フィールドの状態
     * @param players プレイヤーの状態
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
