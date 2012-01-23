package se.atoulou.facebook.hackercup.auction;

import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import se.atoulou.facebook.hackercup.auction.typesafety.AuctionEvaluation;
import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.inject.Inject;

/**
 * The Auction hack app.
 * 
 * @author toulouse
 */
public class AuctionApp extends HackApp {
    private static final Logger logger = LoggerFactory.getLogger(AuctionApp.class);
    @Inject
    private Marker logMarker;

    @Override
    protected void run(PrintWriter writer, List<String> inputLines) {
        int i = 1;
        for (String inputLine : inputLines) {
            logger.trace(logMarker, "Evaluating Auction {}", String.valueOf(i));
            AuctionEvaluation answer = evaluateAuction(inputLine);
            writer.println("Case #" + i + ": " + answer);
            i++;
        }
        writer.flush();

    }

    @Override
    protected void validateConstraints(PrintWriter output, List<String> inputLines) {
        // TODO Auto-generated method stub

    }

    protected AuctionEvaluation evaluateAuction(String inputLine) {
        // TODO Auto-generated method stub
        return null;
    }

}
