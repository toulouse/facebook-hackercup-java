package se.atoulou.facebook.hackercup.auction.typesafety;

public class AuctionEntry {
    private final Integer n;
    private final Integer p_1;
    private final Integer w_1;
    private final Integer m;
    private final Integer k;
    private final Integer a;
    private final Integer b;
    private final Integer c;
    private final Integer d;

    /**
     * @param n
     * @param p_1
     * @param w_1
     * @param m
     * @param k
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public AuctionEntry(Integer n, Integer p_1, Integer w_1, Integer m, Integer k, Integer a, Integer b, Integer c,
            Integer d) {
        this.n = n;
        this.p_1 = p_1;
        this.w_1 = w_1;
        this.m = m;
        this.k = k;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Integer getN() {
        return n;
    }

    public Integer getP_1() {
        return p_1;
    }

    public Integer getW_1() {
        return w_1;
    }

    public Integer getM() {
        return m;
    }

    public Integer getK() {
        return k;
    }

    public Integer getA() {
        return a;
    }

    public Integer getB() {
        return b;
    }

    public Integer getC() {
        return c;
    }

    public Integer getD() {
        return d;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        result = prime * result + ((c == null) ? 0 : c.hashCode());
        result = prime * result + ((d == null) ? 0 : d.hashCode());
        result = prime * result + ((k == null) ? 0 : k.hashCode());
        result = prime * result + ((m == null) ? 0 : m.hashCode());
        result = prime * result + ((n == null) ? 0 : n.hashCode());
        result = prime * result + ((p_1 == null) ? 0 : p_1.hashCode());
        result = prime * result + ((w_1 == null) ? 0 : w_1.hashCode());
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
        AuctionEntry other = (AuctionEntry) obj;
        if (a == null) {
            if (other.a != null)
                return false;
        } else if (!a.equals(other.a))
            return false;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;
        if (c == null) {
            if (other.c != null)
                return false;
        } else if (!c.equals(other.c))
            return false;
        if (d == null) {
            if (other.d != null)
                return false;
        } else if (!d.equals(other.d))
            return false;
        if (k == null) {
            if (other.k != null)
                return false;
        } else if (!k.equals(other.k))
            return false;
        if (m == null) {
            if (other.m != null)
                return false;
        } else if (!m.equals(other.m))
            return false;
        if (n == null) {
            if (other.n != null)
                return false;
        } else if (!n.equals(other.n))
            return false;
        if (p_1 == null) {
            if (other.p_1 != null)
                return false;
        } else if (!p_1.equals(other.p_1))
            return false;
        if (w_1 == null) {
            if (other.w_1 != null)
                return false;
        } else if (!w_1.equals(other.w_1))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AuctionEntry [n=" + n + ", p_1=" + p_1 + ", w_1=" + w_1 + ", m=" + m + ", k=" + k + ", a=" + a + ", b="
                + b + ", c=" + c + ", d=" + d + "]";
    }

}
