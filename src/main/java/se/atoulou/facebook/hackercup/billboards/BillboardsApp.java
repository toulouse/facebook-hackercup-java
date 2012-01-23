package se.atoulou.facebook.hackercup.billboards;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import se.atoulou.facebook.hackercup.billboards.typesafety.LineCount;
import se.atoulou.facebook.hackercup.billboards.typesafety.TextDimension;
import se.atoulou.facebook.hackercup.billboards.typesafety.LineWidth;
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

    /**
     * The problem solution.
     * 
     * @param inputLine
     *            input line of text, including width and height
     * @return the best font size
     */
    protected Integer constrainBillboard(String inputLine) {
        // Split W H T into their respective bits
        List<String> components = Lists.newArrayList(Splitter.on(' ').limit(3).trimResults().omitEmptyStrings()
                .split(inputLine));

        logger.trace(logMarker, "COMPONENTS: {}", components);

        final int w = Integer.parseInt(components.get(0));
        final int h = Integer.parseInt(components.get(1));
        final String text = components.get(2);

        // GENERAL PSEUDOCODE:
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

    /**
     * Transform a list of words into a list of word lengths since the strings themselves are irrelevant
     * 
     * @param text
     *            input text
     * @return list of word lengths
     */
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

    /**
     * Calculate all the possible text dimensions that will fit wrappings of the text.
     * 
     * @param textLength
     *            length of the input text, i.e. the maximum possible line length
     * @param wordLengths
     *            list of word lengths to calculate the possible wrap dimensions for
     * @return A list of text dimensions
     */
    protected List<TextDimension> getViableDimensions(int textLength, List<Integer> wordLengths) {
        // Since this can be sparse there's no big deal doing this vs, say, a map.
        Table<LineCount, LineWidth, Boolean> viableDimensions = TreeBasedTable.create(); // height, width, isCandidate

        for (int lettersPerLine = 1; lettersPerLine <= textLength; lettersPerLine++) {
            // Short-circuit early if there's no way we'll be able to wrap this one
            Integer biggestWordLength = Collections.max(wordLengths);
            if (biggestWordLength > lettersPerLine) {
                continue;
            }

            Integer minimumLineCount = Integer.valueOf(getMinimumLineCount(lettersPerLine, wordLengths));
            viableDimensions.put(new LineCount(minimumLineCount), new LineWidth(lettersPerLine), Boolean.TRUE);
        }

        List<TextDimension> dimensionCandidates = Lists.newArrayListWithExpectedSize(viableDimensions.size());
        for (LineCount height : viableDimensions.rowKeySet()) {
            Map<LineWidth, Boolean> validWidthsForHeight = viableDimensions.row(height);
            LineWidth mostConstrainingWidth = Collections.min(validWidthsForHeight.keySet());
            dimensionCandidates.add(new TextDimension(height, mostConstrainingWidth));
        }

        return ImmutableList.copyOf(dimensionCandidates);
    }

    /**
     * Calculate the minimum possible number of lines we can display given a list of word lengths and allowable number of letters per line.
     * 
     * @param lettersPerLine
     *            number of letters allowed per line
     * @param wordLengths
     *            list of word lengths to wrap onto lines
     * @return An integer number of lines
     */
    protected int getMinimumLineCount(final int lettersPerLine, final List<Integer> wordLengths) {
        int numLines = 1;
        int spacesLeft = lettersPerLine;

        for (Integer wordLength : wordLengths) {
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

    /**
     * Calculate the largest font size that doesn't overflow horizontally or vertically.
     * 
     * NB: returns zero if the font cannot fit.
     * 
     * @param w
     *            board width
     * @param h
     *            board height
     * @param textDimension
     *            dimensions of the text we're constraining, i.e. 4 letters by 3 lines
     * @return An integer font size
     */
    protected int constrainFontSizeOnBoardToTextDimensions(int w, int h, TextDimension textDimension) {
        // We don't expect any negative numbers so normal truncating division == flooring division
        int maxFontWidth = w / textDimension.getLineWidth().get();
        int maxFontHeight = h / textDimension.getLineCount().get();

        // Since the font is square we've got to pick the smaller of the two dimensions, since the bigger one won't fit.
        return Math.min(maxFontWidth, maxFontHeight);
    }

}
