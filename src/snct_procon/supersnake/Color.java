package snct_procon.supersnake;

/**
 * RGB‚ÅF‚ğ•\‚·.
 */
public class Color {
    
    /**
     * R¬•ª‚ğæ“¾‚·‚é.
     * @return R¬•ª
     */
    public int getR() {
        return r;
    }
    
    /**
     * G¬•ª‚ğæ“¾‚·‚é.
     * @return G¬•ª
     */
    public int getG() {
        return g;
    }
    
    /**
     * B¬•ª‚ğæ“¾‚·‚é.
     * @return B¬•ª
     */
    public int getB() {
        return b;
    }
    
    /**
     * F‚ğ(r, g, b)‚Å‰Šú‰»‚·‚é.
     * @param r R¬•ª
     * @param g G¬•ª
     * @param b B¬•ª
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
