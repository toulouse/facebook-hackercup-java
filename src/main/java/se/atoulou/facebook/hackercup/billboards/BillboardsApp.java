package se.atoulou.facebook.hackercup.billboards;

import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class BillboardsApp extends HackApp {
    private static final Logger logger = LoggerFactory.getLogger(BillboardsApp.class);
    @Inject
    private Marker logMarker;

    // TODO: make this not so big and ugly
    @Override
    protected void checkConstraints(PrintWriter output, List<String> inputLines) {
        boolean size = inputLines.size() > 1 && inputLines.size() <= 20;

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

        if (assertionsEnabled()) {
            assert size;
            assert format;
            assert !endSpaces;
            assert !adjacentSpaces;
        } else {
            if (!size) {
                RuntimeException e = new IllegalArgumentException(
                        "The number of input lines violates the specification's constraints!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }

            if (!format) {
                RuntimeException e = new IllegalArgumentException("The input text is improperly formatted!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }

            if (endSpaces) {
                RuntimeException e = new IllegalArgumentException("Spaces at the end of the text!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }

            if (adjacentSpaces) {
                RuntimeException e = new IllegalArgumentException("Spaces adjacent in the text!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }

        }
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
        // TODO Auto-generated method stub
        return 0;
    }

}
