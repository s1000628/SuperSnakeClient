package snct_procon.supersnake;

/**
 * SuperSnake�̃Z��(�t�B�[���h���1�}�X)�̏�Ԃ�\��.
 */
public class CellState {
    
    /**
     * �Z�����ʍs�\���ۂ����擾����.
     * @return �Z�����ʍs�\�Ȃ�true�A�����łȂ��Ȃ�false
     */
    public boolean isPassable() {
        return passable;
    }
    
    /**
     * �Z���̐F���擾����.
     * @return �Z���̐F
     */
    public Color getColor() {
        return col;
    }
    
    /**
     * �Z���̏�Ԃ�����������.
     * @param passable �Z�����ʍs�\���ۂ�
     * @param color �Z���̐F
     */
    public CellState(boolean passable, Color color) {
        this.passable = passable;
        this.col = color.clone();
    }
    
    private boolean passable;
    private Color col;
    
    @Override
    public CellState clone() {
        return new CellState(passable, col);
    }

    @Override
    public String toString() {
        return "Cell [passable=" + passable + ", color=" + col + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((col == null) ? 0 : col.hashCode());
        result = prime * result + (passable ? 1231 : 1237);
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
        CellState other = (CellState) obj;
        if (col == null) {
            if (other.col != null)
                return false;
        } else if (!col.equals(other.col))
            return false;
        if (passable != other.passable)
            return false;
        return true;
    }
}
