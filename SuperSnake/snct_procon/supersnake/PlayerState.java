package snct_procon.supersnake;

/**
 * �v���C���[�̏�Ԃ�\��.
 */
public class PlayerState {
    
    /**
     * �v���C���[�̖��O���擾����.
     * @return �v���C���[�̖��O
     */
    public String getName() {
        return new String(name);
    }
    
    /**
     * �v���C���[�̈ʒu���擾����.
     * @return �v���C���[�̈ʒu
     */
    public Point getPosition() {
        return pos;
    }
    
    /**
     * �v���C���[�̐F���擾����.
     * @return �v���C���[�̐F
     */
    public Color getColor() {
        return col;
    }
    
    /**
     * �v���C���[�̌������擾����.
     * @return �v���C���[�̌���
     */
    public Direction getDirection() {
        return dir;
    }
    
    /**
     * �v���C���[�������Ă��邩�ۂ����擾����.
     * @return �v���C���[�������Ă���� true �A�����łȂ���� false
     */
    public boolean isAlive() {
        return alive;
    }
    
    /**
     * �v���C���[������ł��邩�ۂ����擾����.
     * @return �v���C���[������ł���� true �A�����łȂ���� false
     */
    public boolean isDead() {
        return !alive;
    }
    
    /**
     * �v���C���[�̏�Ԃ�����������.
     * @param name �v���C���[�̖��O
     * @param color �v���C���[�̐F
     * @param position �v���C���[�̈ʒu
     * @param direction �v���C���[�̌���
     * @param alive �v���C���[�������Ă��邩�ۂ�
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
