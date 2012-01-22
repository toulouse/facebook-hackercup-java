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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class BillboardsApp extends HackApp {
    private static final Logger logger = LoggerFactory.getLogger(BillboardsApp.class);
    @Inject
    private Marker logMarker;

    @Override
    protected void checkConstraints(PrintWriter output, List<String> inputLines) {
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

        assertTrue(size, "The number of input lines violates the specification's constraints!");
        assertTrue(format, "The input text is improperly formatted!");
        assertTrue(!endSpaces, "Spaces at the end of the text!");
        assertTrue(!adjacentSpaces, "Spaces adjacent in the text!");
        assertTrue(validRanges, "The input numbers have illegal ranges!");
    }

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

    private int constrainBillboard(String inputLine) {
        List<String> components = Lists.newArrayList(Splitter.on(' ').limit(3).trimResults().omitEmptyStrings()
                .split(inputLine));

        int w = Integer.parseInt(components.get(0));
        int h = Integer.parseInt(components.get(1));
        String text = components.get(2);

        List<String> words = Lists.newArrayList(Splitter.on(' ').split(text));

        logger.trace(logMarker, "W: {} H: {} TEXT: {}", new String[] { components.get(0), components.get(1), text });

        List<Integer> wordLengths = Lists.transform(words, new Function<String, Integer>() {

            @Override
            public Integer apply(String input) {
                return input.length();
            }
        });

        logger.trace(logMarker, "WORD LENGTHS: {}", wordLengths);

        return 0;
    }
}
