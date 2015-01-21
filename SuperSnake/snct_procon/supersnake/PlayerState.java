package snct_procon.supersnake;

/**
 * プレイヤーの状態を表す.
 */
public class PlayerState {
    
    /**
     * プレイヤーの名前を取得する.
     * @return プレイヤーの名前
     */
    public String getName() {
        return new String(name);
    }
    
    /**
     * プレイヤーの位置を取得する.
     * @return プレイヤーの位置
     */
    public Point getPosition() {
        return pos;
    }
    
    /**
     * プレイヤーの色を取得する.
     * @return プレイヤーの色
     */
    public Color getColor() {
        return col;
    }
    
    /**
     * プレイヤーの向きを取得する.
     * @return プレイヤーの向き
     */
    public Direction getDirection() {
        return dir;
    }
    
    /**
     * プレイヤーが生きているか否かを取得する.
     * @return プレイヤーが生きていれば true 、そうでなければ false
     */
    public boolean isAlive() {
        return alive;
    }
    
    /**
     * プレイヤーが死んでいるか否かを取得する.
     * @return プレイヤーが死んでいれば true 、そうでなければ false
     */
    public boolean isDead() {
        return !alive;
    }
    
    /**
     * プレイヤーの状態を初期化する.
     * @param name プレイヤーの名前
     * @param color プレイヤーの色
     * @param position プレイヤーの位置
     * @param direction プレイヤーの向き
     * @param alive プレイヤーが生きているか否か
     */
    public PlayerState(String name, Color color, Point position, Direction direction, boolean alive) {
        this.name = new String(name);
        this.col = color.clone();
        this.pos = position.clone();
        this.dir = direction;
        this.alive = alive;
    }

    private String name;
    private Color col;
    private Point pos;
    private Direction dir;
    private boolean alive;
    
    @Override
    public PlayerState clone() {
        return new PlayerState(name, col, pos, dir, alive);
    }

    @Override
    public String toString() {
        return "PlayerState [name=" + name + ", col=" + col + ", pos=" + pos
                + ", dir=" + dir + ", alive=" + alive + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (alive ? 1231 : 1237);
        result = prime * result + ((col == null) ? 0 : col.hashCode());
        result = prime * result + ((dir == null) ? 0 : dir.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
        PlayerState other = (PlayerState) obj;
        if (alive != other.alive)
            return false;
        if (col == null) {
            if (other.col != null)
                return false;
        } else if (!col.equals(other.col))
            return false;
        if (dir != other.dir)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        return true;
    }
}
