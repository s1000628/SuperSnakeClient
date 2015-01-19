
/**
 * RGBで色を表す.
 */
public class Color {
    
    /**
     * R成分を取得する.
     * @return R成分
     */
    public int getR() {
        return r;
    }
    
    /**
     * G成分を取得する.
     * @return G成分
     */
    public int getG() {
        return g;
    }
    
    /**
     * B成分を取得する.
     * @return B成分
     */
    public int getB() {
        return b;
    }
    
    /**
     * 色を(r, g, b)で初期化する.
     * @param r R成分
     * @param g G成分
     * @param b B成分
     */
    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    private int r;
    private int g;
    private int b;
    
    @Override
    public Color clone() {
        return new Color(r, g, b);
    }

    @Override
    public String toString() {
        return "Color [r=" + r + ", g=" + g + ", b=" + b + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + b;
        result = prime * result + g;
        result = prime * result + r;
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
        Color other = (Color) obj;
        if (b != other.b)
            return false;
        if (g != other.g)
            return false;
        if (r != other.r)
            return false;
        return true;
    }
}
