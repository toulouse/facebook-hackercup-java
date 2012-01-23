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
    private static final boolean useMemoizingRecursive = true;

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
            writer.flush();
            i++;
        }
    }

    @Override
    protected void validateConstraints(PrintWriter output, List<String> inputLines) {
        // Skipping writing these for now
    }

    protected AuctionEntry entryFromStringList(List<String> stringList) {
        List<Long> longList = Lists.transform(stringList, new Function<String, Long>() {

            @Override
            public Long apply(String input) {
                return Long.valueOf(input);
            }
        });
        return new AuctionEntry(longList.get(0), longList.get(1), longList.get(2), longList.get(3), longList.get(4),
                longList.get(5), longList.get(6), longList.get(7), longList.get(8));
    }

    // Handwavey diagram explanation:
    // +-+-+
    // | |W| where W = strictly worse
    // +-*-+
    // |B| | where B = strictly better
    // +-+-+
    protected AuctionEvaluation evaluateAuctionEntry(AuctionEntry entry) {
        final long n = entry.getN();

        Map<Long, Long> lowestWeightForPrices = new TreeMap<Long, Long>();
        Map<Long, Long> highestWeightForPrices = new TreeMap<Long, Long>();

        for (long product_i = 1; product_i <= n; product_i++) {
            final long myPrice = getPrice(entry, product_i);
            final long myWeight = getWeight(entry, product_i);

            Long storedLowestWeight = lowestWeightForPrices.get(myPrice);
            Long storedHighestWeight = highestWeightForPrices.get(myPrice);

            if (storedLowestWeight == null || myWeight < storedLowestWeight) {
                lowestWeightForPrices.put(myPrice, myWeight);
            }

            if (storedHighestWeight == null || myWeight > storedHighestWeight) {
                highestWeightForPrices.put(myPrice, myWeight);
            }
        }

        int bargains = 0;
        int terribleDeals = 0;

        for (long product_k = 1; product_k <= n; product_k++) {
            long myPrice = getPrice(entry, product_k);
            long myWeight = getWeight(entry, product_k);

            boolean nothingIsStrictlyBetter = true;
            for (Long price : lowestWeightForPrices.keySet()) {
                Long lowestWeightForPrice = lowestWeightForPrices.get(price);

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
            for (Long price : highestWeightForPrices.keySet()) {
                Long highestWeightForPrice = highestWeightForPrices.get(price);

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

    protected long getPrice(AuctionEntry entry, long i) {
        if (useMemoizingRecursive) {
            return getPriceRecursive(entry, i);
        } else {
            return getPriceBuggy(entry, i);
        }
    }

    protected long getWeight(AuctionEntry entry, long i) {
        if (useMemoizingRecursive) {
            return getWeightRecursive(entry, i);
        } else {
            return getWeightBuggy(entry, i);
        }
    }

    Map<Long, Long> memoizedPrices = new HashMap<Long, Long>();

    protected long getPriceRecursive(AuctionEntry entry, long i) {
        Long memoizedResult = memoizedPrices.get(i);
        if (memoizedResult != null) {
            return memoizedResult;
        }

        final long a = entry.getA();
        final long b = entry.getB();
        final long m = entry.getM();

        if (i == 1) { // Base case
            return entry.getP_1();
        }

        final long result = 1 + (b + a * getPriceRecursive(entry, i - 1)) % m;
        memoizedPrices.put(i, result);
        return result;

    }

    Map<Long, Long> memoizedWeights = new HashMap<Long, Long>();

    protected long getWeightRecursive(AuctionEntry entry, long i) {
        Long memoizedResult = memoizedWeights.get(i);
        if (memoizedResult != null) {
            return memoizedResult;
        }

        final long c = entry.getC();
        final long d = entry.getD();
        final long k = entry.getK();

        if (i == 1) { // Base case
            return entry.getW_1();
        }

        final long result = 1 + (d + c * getWeightRecursive(entry, i - 1)) % k;
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
    protected long getPriceBuggy(AuctionEntry entry, long i) {
        final long a = entry.getA();
        final long b = entry.getB();
        final long m = entry.getM();
        final long p_1 = entry.getP_1();

        // Calculate A^i MOD M. Pray that I'm not fucking up because of primeness or something
        final long aimodm = modExp(a, i, m);

        long result = 1;
        result = result + (p_1 * aimodm * a) % m;
        result = result + (b * (aimodm + 1)) % m;

        return result;
    }

    /**
     * I recognize this is the same as getPrice, but I duped it for clarity to myself and I'm being a bit lazy.
     * 
     * @param entry
     * @param i
     * @return
     */
    protected long getWeightBuggy(AuctionEntry entry, long i) {
        final long c = entry.getC();
        final long d = entry.getD();
        final long k = entry.getK();
        final long w_1 = entry.getW_1();

        // Calculate C^i MOD K. Pray that I'm not fucking up because of primeness or something
        long cimodk = modExp(c, i, k);

        long result = 1;
        result = result + (w_1 * cimodk * c) % k;
        result = result + (d * (cimodk + 1)) % k;

        return result;
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
