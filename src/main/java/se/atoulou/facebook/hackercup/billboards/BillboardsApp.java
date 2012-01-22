package se.atoulou.facebook.hackercup.billboards;

import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class BillboardsApp extends HackApp {
    private static final Logger logger = LoggerFactory.getLogger(BillboardsApp.class);
    @Inject
    private Marker logMarker;

    @Override
    protected void run(PrintWriter writer, List<String> inputLines) {
        int i = 1;
        for (String inputLine : inputLines) {
            int answer = constrainBillboard(inputLine);
            writer.println("Case #" + i + ": " + answer);
            i++;
        }
        writer.flush();
    }

    @Override
    protected void validateConstraints(PrintWriter output, List<String> inputLines) {
        boolean size = 1 <= inputLines.size() && inputLines.size() <= 20;
    
        boolean format = Iterables.all(inputLines, new Predicate<String>() {
    
            @Override
            public boolean apply(String input) {
                return input.matches("^\\d+ \\d+ [a-zA-Z0-9&&[^ ]][a-zA-Z0-9 ]*$");
            }
        });
    
        boolean endSpaces = Iterables.any(inputLines, new Predicate<String>() {
    
            @Override
            public boolean apply(String input) {
                return input.endsWith(" ");
            }
        });
    
        boolean adjacentSpaces = Iterables.any(inputLines, new Predicate<String>() {
    
            @Override
            public boolean apply(String input) {
                return input.matches("  ");
            }
        });
    
        boolean validRanges = Iterables.all(inputLines, new Predicate<String>() {
    
            @Override
            public boolean apply(String input) {
                String[] components = input.split(" ");
                int w = Integer.parseInt(components[0]);
                int h = Integer.parseInt(components[1]);
                return (1 <= w && w <= 1000) && (1 <= h && h <= 1000);
            }
        });
    
        validateConstraint(size, "The number of input lines violates the specification's constraints!");
        validateConstraint(format, "The input text is improperly formatted!");
        validateConstraint(!endSpaces, "Spaces at the end of the text!");
        validateConstraint(!adjacentSpaces, "Spaces adjacent in the text!");
        validateConstraint(validRanges, "The input numbers have illegal ranges!");
    }

    private int constrainBillboard(String inputLine) {
        // Split W H T into their respective bits
        List<String> components = Lists.newArrayList(Splitter.on(' ').limit(3).trimResults().omitEmptyStrings()
                .split(inputLine));

        logger.trace(logMarker, "COMPONENTS: {}", components);

        int w = Integer.parseInt(components.get(0));
        int h = Integer.parseInt(components.get(1));
        String text = components.get(2);

        List<String> words = ImmutableList.copyOf(Splitter.on(' ').split(text));
        List<Integer> wordLengths = Lists.transform(words, new Function<String, Integer>() {

            @Override
            public Integer apply(String input) {
                return input.length();
            }
        });

        logger.trace(logMarker, "WORD LENGTHS: {}", wordLengths);

        // PSEUDOCODE:
        // for lettersPerLine := 1 up to text.length() {
        // wrap the text into lines (involves popping and incrementing)
        // if a word cannot be wrapped, continue (more letters than the billboard is wide)
        // add lettersPerLine to list of candidates
        // }
        // return MAXIMUM(
        // foreach candidate {
        // return calculated font size for candidate, remember to round down
        // OR if wrapping exceeds allocated height, return zero
        // }
        // )

        List<Integer> dimensionCandidates = Lists.newArrayListWithExpectedSize(text.length());

        for (int lettersPerLine = 1; lettersPerLine <= text.length(); lettersPerLine++) {
            try {
                dimensionCandidates.add(calculateMinimumHeight(lettersPerLine, wordLengths));
            } catch (CannotWrapException e) {
                continue;
            }
        }
        return 0;
    }

    private int calculateMinimumHeight(final int lettersPerLine, final List<Integer> wordLengths) {
        int numLines = 0;
        int spacesLeft = lettersPerLine;

        for (Integer wordLength : wordLengths) {
            if (lettersPerLine < wordLength) {
                throw new CannotWrapException();
            }

            if (spacesLeft < wordLength) {
                numLines++;
                spacesLeft = lettersPerLine;
                continue;
            }

            spacesLeft -= wordLength;
        }
        return numLines;
    }

    private static class CannotWrapException extends RuntimeException {
        private static final long serialVersionUID = -2772246466214119704L;
    }
}
