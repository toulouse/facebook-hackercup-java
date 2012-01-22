package se.atoulou.facebook.hackercup.billboards;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import se.atoulou.facebook.hackercup.billboards.typesafety.CannotWrapException;
import se.atoulou.facebook.hackercup.billboards.typesafety.Height;
import se.atoulou.facebook.hackercup.billboards.typesafety.TextDimension;
import se.atoulou.facebook.hackercup.billboards.typesafety.Width;
import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.inject.Inject;

public class BillboardsApp extends HackApp {
    private static final Logger logger = LoggerFactory.getLogger(BillboardsApp.class);
    @Inject
    private Marker logMarker;

    @Override
    protected void run(PrintWriter writer, List<String> inputLines) {
        int i = 1;
        for (String inputLine : inputLines) {
            logger.trace(logMarker, "Constraining line {}", String.valueOf(i));
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

        // These constraints aren't necessarily complete, but are sufficient for now.
        // I may disable some when I submit if they're deemed to be wrong.
        validateConstraint(size, "The number of input lines violates the specification's constraints!");
        validateConstraint(format, "The input text is improperly formatted!");
        validateConstraint(!endSpaces, "Spaces at the end of the text!");
        validateConstraint(!adjacentSpaces, "Spaces adjacent in the text!");
        validateConstraint(validRanges, "The input numbers have illegal ranges!");
    }

    protected Integer constrainBillboard(String inputLine) {
        // Split W H T into their respective bits
        List<String> components = Lists.newArrayList(Splitter.on(' ').limit(3).trimResults().omitEmptyStrings()
                .split(inputLine));

        logger.trace(logMarker, "COMPONENTS: {}", components);

        final int w = Integer.parseInt(components.get(0));
        final int h = Integer.parseInt(components.get(1));
        final String text = components.get(2);

        // PSEUDOCODE:
        // for lettersPerLine := 1 up to text.length() {
        // wrap the text into lines
        // if a word cannot be wrapped, continue (more letters than the billboard is wide)
        // add lettersPerLine to list of candidates
        // }
        // return MAXIMUM(
        // foreach candidate {
        // return calculated font size for candidate, remember to round down
        // OR if wrapping exceeds allocated height, return zero
        // }
        // )

        List<Integer> wordLengths = getWordLengths(text);
        logger.trace(logMarker, "WORD LENGTHS: {}", wordLengths);

        List<TextDimension> dimensionCandidates = getViableDimensions(text.length(), wordLengths);
        logger.trace(logMarker, "VIABLE DIMENSIONS: {}", dimensionCandidates);

        List<Integer> fontSizes = Lists.transform(dimensionCandidates, new Function<TextDimension, Integer>() {

            @Override
            public Integer apply(TextDimension dimension) {
                return constrainFontSizeOnBoardToTextDimensions(w, h, dimension);
            }
        });
        logger.trace(logMarker, "VIABLE FONT SIZES: {}", fontSizes);

        return Collections.max(fontSizes);
    }

    protected List<Integer> getWordLengths(String text) {
        List<String> words = ImmutableList.copyOf(Splitter.on(' ').split(text));
        List<Integer> wordLengths = Lists.transform(words, new Function<String, Integer>() {

            @Override
            public Integer apply(String input) {
                return input.length();
            }
        });

        return wordLengths;
    }

    protected List<TextDimension> getViableDimensions(int textLength, List<Integer> wordLengths) {
        Table<Height, Width, Boolean> validPositions = TreeBasedTable.create(); // height, width, isCandidate

        for (int lettersPerLine = 1; lettersPerLine <= textLength; lettersPerLine++) {
            try {
                Integer minimumHeight = calculateMinimumHeight(lettersPerLine, wordLengths);
                validPositions.put(new Height(minimumHeight), new Width(lettersPerLine), Boolean.TRUE);
            } catch (CannotWrapException e) {
                continue;
            }
        }

        List<TextDimension> dimensionCandidates = Lists.newArrayListWithExpectedSize(validPositions.size());
        for (Height height : validPositions.rowKeySet()) {
            Map<Width, Boolean> validWidthsForHeight = validPositions.row(height);
            Width mostConstrainingWidth = Collections.min(validWidthsForHeight.keySet());
            dimensionCandidates.add(new TextDimension(mostConstrainingWidth, height));
        }

        return ImmutableList.copyOf(dimensionCandidates);
    }

    protected int calculateMinimumHeight(final int lettersPerLine, final List<Integer> wordLengths) {
        int numLines = 1;
        int spacesLeft = lettersPerLine;

        for (Integer wordLength : wordLengths) {
            if (lettersPerLine < wordLength) { // Word cannot fit on billboard
                throw new CannotWrapException();
            }

            if (wordLength <= spacesLeft) { // Space is available for the word.
                // Account for spaces.
                spacesLeft -= (wordLength + 1);
                // NOTE: This is OK even though a word that fits exactly on the end of a line doesn't have a trailing space. This is because
                // if spacesLeft == wordLength then wordFits still fails on the next loop and wraps.
            } else {
                numLines++;
                spacesLeft = lettersPerLine - wordLength;
            }
        }
        return numLines;
    };

    protected Integer constrainFontSizeOnBoardToTextDimensions(int w, int h, TextDimension textDimension) {
        // We don't expect any negative numbers so normal truncating division == flooring division
        int maxFontWidth = w / textDimension.getWidth().get();
        int maxFontHeight = h / textDimension.getHeight().get();

        // Since the font is square we've got to pick the smaller of the two dimensions, since the bigger one won't fit.
        return Math.min(maxFontWidth, maxFontHeight);
    }

}
