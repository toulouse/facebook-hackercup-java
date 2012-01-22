package se.atoulou.facebook.hackercup.alphabetsoup;

import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;
import com.google.inject.Inject;

/**
 * The AlphabetSoup hack app.
 * 
 * @author toulouse
 */
public final class AlphabetSoupApp extends HackApp {
    private static final Logger logger = LoggerFactory.getLogger(AlphabetSoupApp.class);
    @Inject
    private Marker logMarker;

    @Override
    protected void checkConstraints(PrintWriter writer, List<String> inputLines) {
        boolean size = inputLines.size() > 1 && inputLines.size() <= 20;
        boolean uppercase = Iterables.all(inputLines, new Predicate<String>() {

            @Override
            public boolean apply(String input) {
                return input.matches("^[A-Z ]+$");
            }
        });

        if (assertionsEnabled()) {
            assert size;
            assert uppercase;
        } else {
            if (!size) {
                RuntimeException e = new IllegalArgumentException(
                        "The number of input lines violates the specification's constraints!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }
            if (!uppercase) {
                RuntimeException e = new IllegalArgumentException("There are invalid characters in the input text!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }
        }
    }

    @Override
    protected void run(PrintWriter writer, List<String> inputLines) {
        int i = 1;
        for (String inputLine : inputLines) {
            int answer = calculateAnagram(inputLine);
            writer.println("Case #" + i + ": " + answer);
            i++;
        }
        writer.flush();
    }

    private int calculateAnagram(String inputLine) {
        // Tree multiset used for prettiness so the debug output alphabetizes the log.
        final Multiset<Character> charactersMultiset = TreeMultiset.create(Lists.charactersOf(inputLine));
        final Multiset<Character> hackerCupMultiset = TreeMultiset.create(Lists.charactersOf("HACKERCUP"));

        int occurrences = 0;
        // Guava is sweet and so cheaty-face for this problem
        while (Multisets.intersection(charactersMultiset, hackerCupMultiset).containsAll(hackerCupMultiset)) {
            Multisets.removeOccurrences(charactersMultiset, hackerCupMultiset);
            occurrences++;
        }

        logger.trace(logMarker, "\"{}\"({}): {}", new String[] { inputLine, Integer.toString(occurrences),
                charactersMultiset.toString() });

        return occurrences;
    }
}
