package se.atoulou.facebook.hackercup.beautifulstrings;

import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

public class BeautifulStringsApp extends HackApp {
    private static final Logger logger = LoggerFactory.getLogger(BeautifulStringsApp.class);

    @Override
    protected void run(PrintWriter writer, List<String> inputLines) {
        int i = 1;
        for (String inputLine : inputLines) {
            int answer = calculateBeauty(inputLine);
            writer.println("Case #" + i + ": " + answer);
            i++;
        }
        writer.flush();
    }

    private int calculateBeauty(String inputLine) {
        // We know we're only dealing with ASCII letters, so let's remove everything else
        String strippedString = inputLine.replaceAll("[^a-zA-Z]", "");
        String lowercaseString = strippedString.toLowerCase(Locale.ENGLISH);

        ImmutableMultiset.Builder<String> setBuilder = ImmutableMultiset.builder();
        for (int i = 0; i < lowercaseString.length(); i++) {
            setBuilder.add(lowercaseString.substring(i, i + 1));
        }
        ImmutableMultiset<String> letters = setBuilder.build();
        ImmutableMultiset<String> lettersDescending = Multisets.copyHighestCountFirst(letters);

        int total = 0;
        int value = 26;
        for (Multiset.Entry<String> stringEntry : lettersDescending.entrySet()) {
            total += value * stringEntry.getCount();
            value--;
        }

        logger.debug("Characters (descending): {}", lettersDescending.toString());
        logger.debug("Total: {}", total);
        return total;
    }
}
