package se.atoulou.facebook.hackercup.auction;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import se.atoulou.facebook.hackercup.auction.typesafety.AuctionEntry;
import se.atoulou.facebook.hackercup.auction.typesafety.AuctionEvaluation;
import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * The Auction hack app. A lot of autoboxing/unboxing going on here. Mainly because I was lazy, and preferred working with primitives but
 * collections need objects.
 * 
 * @author toulouse
 */
public class AuctionApp extends HackApp {
    private static final boolean useHackyMemoizingRecursive = true;

    private static final Logger logger = LoggerFactory.getLogger(AuctionApp.class);
    @Inject
    private Marker logMarker;

    @Override
    protected void run(PrintWriter writer, List<String> inputLines) {
        int i = 1;
        for (String inputLine : inputLines) {
            memoizedPrices.clear();
            memoizedWeights.clear();

            List<String> entryComponents = Lists.newArrayList(Splitter.on(' ').trimResults().omitEmptyStrings()
                    .split(inputLine));
            AuctionEntry entry = entryFromStringList(entryComponents);
            logger.trace(logMarker, "Evaluating Auction {}: {}", new String[] { String.valueOf(i), entry.toString() });
            AuctionEvaluation answer = evaluateAuctionEntry(entry);
            writer.println("Case #" + i + ": " + answer.getTerribleDeals() + " " + answer.getBargains());
            i++;
        }
        writer.flush();

    }

    @Override
    protected void validateConstraints(PrintWriter output, List<String> inputLines) {
        // Skipping writing these for now
    }

    protected AuctionEntry entryFromStringList(List<String> stringList) {
        List<Integer> intList = Lists.transform(stringList, new Function<String, Integer>() {

            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }
        });
        return new AuctionEntry(intList.get(0), intList.get(1), intList.get(2), intList.get(3), intList.get(4),
                intList.get(5), intList.get(6), intList.get(7), intList.get(8));
    }

    // Handwavey diagram explanation:
    // +-+-+
    // | |W| where W = strictly worse
    // +-*-+
    // |B| | where B = strictly better
    // +-+-+
    protected AuctionEvaluation evaluateAuctionEntry(AuctionEntry entry) {
        final int n = entry.getN();

        TreeMap<Integer, Integer> lowestWeightForPrices = new TreeMap<Integer, Integer>();
        TreeMap<Integer, Integer> highestWeightForPrices = new TreeMap<Integer, Integer>();

        // DEBUG
        // Table<Integer, Integer, Integer> products = TreeBasedTable.create();

        for (int product_i = 1; product_i <= n; product_i++) {
            final int myPrice = getPrice(entry, product_i);
            final int myWeight = getWeight(entry, product_i);

            // products.put(myPrice, myWeight, product_i);

            Integer storedLowestWeight = lowestWeightForPrices.get(myPrice);
            Integer storedHighestWeight = highestWeightForPrices.get(myPrice);

            if (storedLowestWeight == null || myWeight < storedLowestWeight) {
                lowestWeightForPrices.put(myPrice, myWeight);
            }

            if (storedHighestWeight == null || myWeight > storedHighestWeight) {
                highestWeightForPrices.put(myPrice, myWeight);
            }
        }

        int bargains = 0;
        int terribleDeals = 0;

        // logger.info("products {}", products);
        // System.out.println("/");
        // for (Table.Cell<Integer, Integer, Integer> cell : products.cellSet()) {
        // System.out.println(cell.getRowKey() + "," + cell.getColumnKey() + "," + cell.getValue());
        // }

        for (int product_k = 1; product_k <= n; product_k++) {
            int myPrice = getPrice(entry, product_k);
            int myWeight = getWeight(entry, product_k);

            boolean nothingIsStrictlyBetter = true;
            for (Integer price : lowestWeightForPrices.keySet()) {
                Integer lowestWeightForPrice = lowestWeightForPrices.get(price);

                if (price > myPrice) { // Only checking things with a LESSER OR EQUAL price.
                    continue;
                }

                if (lowestWeightForPrice < myWeight) { // LESSER WEIGHT
                    if (price <= myPrice) { // LESSER OR EQUAL PRICE?
                        nothingIsStrictlyBetter = false;
                        break;
                    }
                } else if (lowestWeightForPrice == myWeight) { // EQUAL WEIGHT
                    if (price < myPrice) { // LESSER PRICE?
                        nothingIsStrictlyBetter = false;
                        break;
                    }
                }
            }

            boolean nothingIsStrictlyWorse = true;
            for (Integer price : highestWeightForPrices.keySet()) {
                Integer highestWeightForPrice = highestWeightForPrices.get(price);

                if (price < myPrice) { // Only check things with a EQUAL OR GREATER price
                    continue;
                }

                if (highestWeightForPrice == myWeight) { // EQUAL WEIGHT
                    if (price > myPrice) { // GREATER PRICE?
                        nothingIsStrictlyWorse = false;
                        break;
                    }
                } else if (highestWeightForPrice > myWeight) { // GREATER WEIGHT
                    if (price >= myPrice) { // GREATER OR EQUAL PRICE?
                        nothingIsStrictlyWorse = false;
                        break;
                    }
                }
            }

            if (nothingIsStrictlyBetter) {
                bargains++;
            }

            if (nothingIsStrictlyWorse) {
                terribleDeals++;
            }
        }

        logger.trace(logMarker, "entry: {}", entry);

        return new AuctionEvaluation(terribleDeals, bargains);
    }

    protected int getPrice(AuctionEntry entry, int i) {
        if (useHackyMemoizingRecursive) {
            return getPriceRecursive(entry, i);
        } else {
            return getPriceBuggy(entry, i);
        }
    }

    protected int getWeight(AuctionEntry entry, int i) {
        if (useHackyMemoizingRecursive) {
            return getWeightRecursive(entry, i);
        } else {
            return getWeightBuggy(entry, i);
        }
    }

    Map<Integer, Integer> memoizedPrices = new HashMap<Integer, Integer>();

    protected int getPriceRecursive(AuctionEntry entry, int i) {
        Integer memoizedResult = memoizedPrices.get(i);
        if (memoizedResult != null) {
            return memoizedResult;
        }

        final int a = entry.getA();
        final int b = entry.getB();
        final int m = entry.getM();

        if (i == 1) { // Base case
            return entry.getP_1();
        }

        final int result = 1 + (b + a * getPriceRecursive(entry, i - 1)) % m;
        memoizedPrices.put(i, result);
        return result;

    }

    Map<Integer, Integer> memoizedWeights = new HashMap<Integer, Integer>();

    protected int getWeightRecursive(AuctionEntry entry, int i) {
        Integer memoizedResult = memoizedWeights.get(i);
        if (memoizedResult != null) {
            return memoizedResult;
        }

        final int c = entry.getC();
        final int d = entry.getD();
        final int k = entry.getK();

        if (i == 1) { // Base case
            return entry.getW_1();
        }

        final int result = 1 + (d + c * getWeightRecursive(entry, i - 1)) % k;
        memoizedWeights.put(i, result);
        return result;
    }

    /**
     * Use the solved recurrence relation to return P(i) *without* having to iterate i times over P(i) = ((A*P(i-1) + B) mod M) + 1
     * 
     * BUG: This is probably wrong, though.
     * 
     * NOTE: I think I'm generally not the best at solving complex math problems, but it looked like a recurrence relation. Hopefully I got
     * this right; I was really unsure.
     * 
     * P(i) = P(1) * A^(i+1) + B * (A^i + 1) MOD M + 1
     * 
     * @param entry
     * @param i
     * @return
     */
    protected int getPriceBuggy(AuctionEntry entry, int i) {
        final long a = entry.getA().longValue();
        final long b = entry.getB().longValue();
        final long m = entry.getM().longValue();
        final long p_1 = entry.getP_1().longValue();

        // Calculate A^i MOD M. Pray that I'm not fucking up because of primeness or something
        long aimodm = modExp(a, i, m);

        long result = 1;
        result = result + (p_1 * aimodm * a) % m;
        result = result + (b * (aimodm + 1)) % m;

        // Loss of precision not a problem since we know the input of m is actually int-sized.
        int realResult = (int) result;

        return realResult;
    }

    /**
     * I recognize this is the same as getPrice, but I duped it for clarity to myself and I'm being a bit lazy.
     * 
     * @param entry
     * @param i
     * @return
     */
    protected int getWeightBuggy(AuctionEntry entry, int i) {
        final long c = entry.getC().longValue();
        final long d = entry.getD().longValue();
        final long k = entry.getK().longValue();
        final long w_1 = entry.getW_1().longValue();

        // Calculate C^i MOD K. Pray that I'm not fucking up because of primeness or something
        long cimodk = modExp(c, i, k);

        long result = 1;
        result = result + (w_1 * cimodk * c) % k;
        result = result + (d * (cimodk + 1)) % k;

        // Loss of precision not a problem since we know the input of m is actually int-sized.
        int realResult = (int) result;

        return realResult;
    }

    protected long modExp(final long baseNumber, final long exponent, final long modulus) {
        // Dealing with longs just in case we have to multiply big.

        if (baseNumber == 0L || baseNumber == 1L) {
            return baseNumber;
        }
        if (exponent == 0L) {
            return 1L;
        }
        if (exponent == 1L) {
            return baseNumber % modulus;
        }

        long result = 1L;
        long exp = exponent;
        long baseLong = baseNumber;

        while (exp > 0) {
            if ((exp & 1L) == 1L) {
                result = (result * baseLong) % modulus;
            }
            exp >>= 1;
            baseLong = (baseLong * baseLong) % modulus;
        }

        return result;
    }
}
