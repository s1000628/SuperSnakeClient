import java.util.*;

/**
 * 通行可能なセルなどの、フィールドの状態を表す.
 */
public class FieldState {
    
    /**
     * フィールドの名前を取得する.
     * @return フィールドの名前
     */
    public String getName() {
        return new String(name);
    }
    
    /**
     * フィールドの大きさを取得する.
     * @return フィールドの大きさ
     */
    public Size getSize() {
        return size;
    }
    
    /**
     * セルの状態を取得する.
     * @param x 取得するセルの X 座標
     * @param y 取得するセルの Y 座標
     * @return セルの状態
     */
    public CellState getCellState(int x, int y) {
        return cells.get(x).get(y);
    }
    
    /**
     * フィールドの状態を初期化する.
     * @param name フィールドの名前
     * @param size フィールドの大きさ
     * @param cells セルの状態
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
