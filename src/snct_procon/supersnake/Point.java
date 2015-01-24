package snct_procon.supersnake;

/**
 * 2次元の位置を表す.
 */
public class Point {
    
    /**
     * X座標を取得する.
     * @return X座標
     */
    public int getX() {
        return x;
    }
    
    /**
     * Y座標を取得する.
     * @return Y座標
     */
    public int getY() {
        return y;
    }
    
    /**
     * 位置を座標(x, y)で初期化する.
     * @param x X 座標
     * @param y Y 座標
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    private int x;
    private int y;

    @Override
    public Point clone() {
        return new Point(x, y);
    }
    
    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
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
        Point other = (Point) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
