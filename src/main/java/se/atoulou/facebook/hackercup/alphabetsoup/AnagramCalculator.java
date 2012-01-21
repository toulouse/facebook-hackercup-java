package se.atoulou.facebook.hackercup.alphabetsoup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * To run, 'mvn clean compile' followed by 'mvn exec:java' should do the trick.
 * 
 * @author toulouse
 * 
 */
public class AnagramCalculator {

    // BOILERPLATE

    // Initialize injector
    private static final Module module = new AlphabetInjectModule();
    private static final Injector injector = Guice.createInjector(new Module[] { module });

    // Initialize logging
    private static final Logger LOG = LoggerFactory.getLogger(AnagramCalculator.class);

    public static void main(String[] args) {
        AnagramCalculator calculator = injector.getInstance(AnagramCalculator.class);
        calculator.run();
    }

    public static Injector getInjector() {
        return injector;
    }

    // SLIGHTLY MORE INTERESTING BOILERPLATE

    // Some app-specific configuration
    private static final String INPUT_FILE_PATH = "/se/atoulou/facebook/hackercup/alphabetsoup/input.txt";

    private final List<String> inputLines;

    public AnagramCalculator() {
        final InputStream stream = getClass().getResourceAsStream(INPUT_FILE_PATH);
        String inputString;
        try {
            inputString = CharStreams.toString(new InputStreamReader(stream));
            LOG.trace("Input String:" + inputString);
        } catch (IOException e) {
            LOG.error("Error loading input file into string", e);
            throw new RuntimeException(e);
        }

        String[] rawLines = inputString.split("\\r?\\n"); // Untested, alternatively, "[\\r\\n]+" to skip empty lines
        int expectedInputLength = Integer.parseInt(rawLines[0]);
        inputLines = Lists.newArrayListWithExpectedSize(expectedInputLength);

        // Not using String line : lines because I treat line #1 differently
        for (int i = 1; i < rawLines.length; i++) {
            // What if the input ends with an empty line?
            String line = rawLines[i];
            if (!line.isEmpty()) {
                inputLines.add(line);
            }

            if (i > expectedInputLength) {
                RuntimeException e = new IllegalArgumentException("The number of input lines read doesn't match the expected number given in the data file!");
                LOG.error("Bad input.", e);
                throw e;
            }
        }
    }

    // THE GOOD STUFF

    private void run() {
        LOG.info(inputLines.toString());
    }

}
