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

    @Override
    protected void checkConstraints(PrintWriter output, List<String> inputLines) {
        boolean size = inputLines.size() > 1 && inputLines.size() <= 20;

        boolean characters = Iterables.all(inputLines, new Predicate<String>() {

            @Override
            public boolean apply(String input) {
                return input.matches("^[a-zA-Z0-9 ]+$");
            }
        });

        boolean noStartingSpaces = Iterables.all(inputLines, new Predicate<String>() {

            @Override
            public boolean apply(String input) {
                return !input.startsWith(" ");
            }
        });

        boolean noEndingSpaces = Iterables.all(inputLines, new Predicate<String>() {

            @Override
            public boolean apply(String input) {
                return !input.endsWith(" ");
            }
        });

        boolean noAdjacentSpaces = Iterables.all(inputLines, new Predicate<String>() {

            @Override
            public boolean apply(String input) {
                return !input.matches("  ");
            }
        });

        if (assertionsEnabled()) {
            assert size;
            assert characters;
            assert noStartingSpaces;
            assert noEndingSpaces;
            assert noAdjacentSpaces;
        } else {
            if (!size) {
                RuntimeException e = new IllegalArgumentException(
                        "The number of input lines violates the specification's constraints!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }
            if (noStartingSpaces) {
                RuntimeException e = new IllegalArgumentException("Spaces at the start of the text!");
                logger.error(logMarker, "Bad input.", e);
                throw e;

            }
            if (noEndingSpaces) {
                RuntimeException e = new IllegalArgumentException("Spaces at the end of the text!");
                logger.error(logMarker, "Bad input.", e);
                throw e;

            }
            if (noAdjacentSpaces) {
                RuntimeException e = new IllegalArgumentException("Spaces adjacent in the text!");
                logger.error(logMarker, "Bad input.", e);
                throw e;

            }

            if (!characters) {
                RuntimeException e = new IllegalArgumentException("There are invalid characters in the input text!");
                logger.error(logMarker, "Bad input.", e);
                throw e;
            }
        }
    }

    @Override
    protected void run(PrintWriter writer, List<String> inputLines) {
        // TODO Auto-generated method stub

    }

}
