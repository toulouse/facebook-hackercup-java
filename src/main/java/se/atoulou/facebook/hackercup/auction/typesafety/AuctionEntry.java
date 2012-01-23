package se.atoulou.facebook.hackercup.auction.typesafety;

public class AuctionEntry {
    private final long n;
    private final long p_1;
    private final long w_1;
    private final long m;
    private final long k;
    private final long a;
    private final long b;
    private final long c;
    private final long d;

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
    public AuctionEntry(long n, long p_1, long w_1, long m, long k, long a, long b, long c, long d) {
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

    public long getN() {
        return n;
    }

    public long getP_1() {
        return p_1;
    }

    public long getW_1() {
        return w_1;
    }

    public long getM() {
        return m;
    }

    public long getK() {
        return k;
    }

    public long getA() {
        return a;
    }

    public long getB() {
        return b;
    }

    public long getC() {
        return c;
    }

    public long getD() {
        return d;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (a ^ (a >>> 32));
        result = prime * result + (int) (b ^ (b >>> 32));
        result = prime * result + (int) (c ^ (c >>> 32));
        result = prime * result + (int) (d ^ (d >>> 32));
        result = prime * result + (int) (k ^ (k >>> 32));
        result = prime * result + (int) (m ^ (m >>> 32));
        result = prime * result + (int) (n ^ (n >>> 32));
        result = prime * result + (int) (p_1 ^ (p_1 >>> 32));
        result = prime * result + (int) (w_1 ^ (w_1 >>> 32));
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
        if (a != other.a)
            return false;
        if (b != other.b)
            return false;
        if (c != other.c)
            return false;
        if (d != other.d)
            return false;
        if (k != other.k)
            return false;
        if (m != other.m)
            return false;
        if (n != other.n)
            return false;
        if (p_1 != other.p_1)
            return false;
        if (w_1 != other.w_1)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AuctionEntry [n=" + n + ", p_1=" + p_1 + ", w_1=" + w_1 + ", m=" + m + ", k=" + k + ", a=" + a + ", b="
                + b + ", c=" + c + ", d=" + d + "]";
    }

}
