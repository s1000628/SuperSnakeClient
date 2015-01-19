
/**
 * 2次元の大きさを表す.
 */
public class Size {
    
    /**
     * 幅を取得する.
     * @return 幅
     */
    public int getWidth() {
        return w;
    }
    
    /**
     * 高さを取得する.
     * @return 高さ
     */
    public int getHeight() {
        return h;
    }

    /**
     * 大きさを初期化する.
     * @param width 幅
     * @param height 高さ
     */
    public Size(int width, int height) {
        this.w = width;
        this.h = height;
    }
    
    private int w;
    private int h;
    
    @Override
    public Size clone() {
        return new Size(w, h);
    }

    @Override
    public String toString() {
        return "Size [w=" + w + ", h=" + h + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + h;
        result = prime * result + w;
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
        Size other = (Size) obj;
        if (h != other.h)
            return false;
        if (w != other.w)
            return false;
        return true;
    }
}
