package snct_procon.supersnake;

/**
 * SuperSnakeのセル(フィールド上の1マス)の状態を表す.
 */
public class CellState {
    
    /**
     * セルが通行可能か否かを取得する.
     * @return セルが通行可能ならtrue、そうでないならfalse
     */
    public boolean isPassable() {
        return passable;
    }
    
    /**
     * セルの色を取得する.
     * @return セルの色
     */
    public Color getColor() {
        return col;
    }
    
    /**
     * セルの状態を初期化する.
     * @param passable セルが通行可能か否か
     * @param color セルの色
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
