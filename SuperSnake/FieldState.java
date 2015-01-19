import java.util.*;

/**
 * �ʍs�\�ȃZ���Ȃǂ́A�t�B�[���h�̏�Ԃ�\��.
 */
public class FieldState {
    
    /**
     * �t�B�[���h�̖��O���擾����.
     * @return �t�B�[���h�̖��O
     */
    public String getName() {
        return new String(name);
    }
    
    /**
     * �t�B�[���h�̑傫�����擾����.
     * @return �t�B�[���h�̑傫��
     */
    public Size getSize() {
        return size;
    }
    
    /**
     * �Z���̏�Ԃ��擾����.
     * @param x �擾����Z���� X ���W
     * @param y �擾����Z���� Y ���W
     * @return �Z���̏��
     */
    public CellState getCellState(int x, int y) {
        return cells.get(x).get(y);
    }
    
    /**
     * �t�B�[���h�̏�Ԃ�����������.
     * @param name �t�B�[���h�̖��O
     * @param size �t�B�[���h�̑傫��
     * @param cells �Z���̏��
     */
    public FieldState(String name, Size size, List<List<CellState>> cells) {
        this.name = new String(name);
        this.size = size.clone();
        this.cells = new ArrayList<List<CellState>>();
        for (List<CellState> row : cells) {
            List<CellState> newRow = new ArrayList<CellState>();
            for (CellState cell : row) {
                newRow.add(cell.clone());
            }
            this.cells.add(newRow);
        }
    }
    
    private String name;
    private Size size;
    private List<List<CellState>> cells = new ArrayList<List<CellState>>();
    
    @Override
    public FieldState clone() {
        return new FieldState(name, size, cells);
    }

    @Override
    public String toString() {
        return "FieldState [name=" + name + ", size=" + size + ", cells="
                + cells + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cells == null) ? 0 : cells.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
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
        FieldState other = (FieldState) obj;
        if (cells == null) {
            if (other.cells != null)
                return false;
        } else if (!cells.equals(other.cells))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        return true;
    }
}
