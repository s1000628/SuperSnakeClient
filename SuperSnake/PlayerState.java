
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
     * �v���C���[�̏�Ԃ�����������.
     * @param name �v���C���[�̖��O
     * @param position �v���C���[�̈ʒu
     * @param color �v���C���[�̐F
     * @param direction �v���C���[�̌���
     */
    public PlayerState(String name, Point position, Color color, Direction direction) {
        this.name = new String(name);
        this.pos = position.clone();
        this.col = color.clone();
        this.dir = direction;
    }

    private String name;
    private Point pos;
    private Color col;
    private Direction dir;
    
    @Override
    public PlayerState clone() {
        return new PlayerState(name, pos, col, dir);
    }

    @Override
    public String toString() {
        return "PlayerState [name=" + name + ", pos=" + pos + ", col=" + col
                + ", dir=" + dir + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
