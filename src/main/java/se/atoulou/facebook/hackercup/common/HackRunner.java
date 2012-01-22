package se.atoulou.facebook.hackercup.common;

import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import se.atoulou.facebook.hackercup.alphabetsoup.AlphabetSoupInjectModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * To run, 'mvn clean compile' followed by 'mvn exec:java' should do the trick.
 * 
 * NOTE: THIS IS SUPER-ENTERPRISE READY LOL NOTE: While "InputPath" is a raw string it still avoid the service locator pattern on account of
 * it not actually identifying a service, but rather a key to some data.
 * 
 * @author toulouse
 */
public class HackRunner {
    private static final Logger logger = LoggerFactory.getLogger(HackRunner.class);
    private static final Marker logMarker = MarkerFactory.getMarker("HackRunner");

    public static void main(String[] args) {
        // Select the hack to run
        if (args.length != 1) {
            logger.error(logMarker, "HackRunner needs an argument to decide what to run!");

            // Exit with a nonzero code
            System.exit(1);
            return;

        }

        String hackAppName = args[0];
        Module module;

        if ("alphabetsoup".equalsIgnoreCase(hackAppName)) {
            module = new AlphabetSoupInjectModule();
        } else {
            logger.error(logMarker, "That problem is not recognized!");

            // Exit with a nonzero code
            System.exit(1);
            return;
        }

        // Let's get this party started
        logger.debug(logMarker, "Initializing Guice injector.");
        Injector injector = Guice.createInjector(module);

        logger.debug(logMarker, "Locating input file.");
        Key<String> inputPathKey = Key.get(String.class, Names.named("InputPath"));
        Provider<String> inputPathProvider = injector.getBinding(inputPathKey).getProvider();
        String inputPath = inputPathProvider.get();

        logger.debug(logMarker, "Instantiating HackApp.");
        HackApp hackApp = injector.getInstance(HackApp.class);

        logger.debug(logMarker, "Preparing input/output streams.");
        InputStream in = HackRunner.class.getClassLoader().getResourceAsStream(inputPath);
        OutputStream out = System.out;

        logger.debug(logMarker, "Running HackApp ({}).", hackApp.getClass().getSimpleName());
        hackApp.actualRun(in, out);
    }
}
