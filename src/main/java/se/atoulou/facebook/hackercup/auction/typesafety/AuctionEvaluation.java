package se.atoulou.facebook.hackercup.auction.typesafety;

public class AuctionEvaluation {
    private final Integer terribleDeals;
    private final Integer bargains;

    /**
     * @param terribleDeals
     * @param bargains
     */
    public AuctionEvaluation(Integer terribleDeals, Integer bargains) {
        this.terribleDeals = terribleDeals;
        this.bargains = bargains;
    }

    public Integer getTerribleDeals() {
        return terribleDeals;
    }

    public Integer getBargains() {
        return bargains;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bargains == null) ? 0 : bargains.hashCode());
        result = prime * result + ((terribleDeals == null) ? 0 : terribleDeals.hashCode());
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
        AuctionEvaluation other = (AuctionEvaluation) obj;
        if (bargains == null) {
            if (other.bargains != null)
                return false;
        } else if (!bargains.equals(other.bargains))
            return false;
        if (terribleDeals == null) {
            if (other.terribleDeals != null)
                return false;
        } else if (!terribleDeals.equals(other.terribleDeals))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Result [terribleDeals=" + terribleDeals + ", bargains=" + bargains + "]";
    }
}
