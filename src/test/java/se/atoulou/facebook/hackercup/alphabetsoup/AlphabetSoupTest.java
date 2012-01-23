package se.atoulou.facebook.hackercup.alphabetsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Provider;

import org.junit.Assert;
import org.junit.Test;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.io.CharStreams;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class AlphabetSoupTest {
    @Test
    public void testAlphabetSoupSampleInput() throws IOException {
        Injector injector = Guice.createInjector(new AlphabetSoupTestModule());

        Key<String> inputPathKey = Key.get(String.class, Names.named("InputPath"));
        Provider<String> inputPathProvider = injector.getBinding(inputPathKey).getProvider();
        String inputPath = inputPathProvider.get();

        Key<String> resultPathKey = Key.get(String.class, Names.named("ResultPath"));
        Provider<String> resultPathProvider = injector.getBinding(resultPathKey).getProvider();
        String resultPath = resultPathProvider.get();

        String expectedResult = CharStreams.toString(new InputStreamReader(ClassLoader.getSystemClassLoader()
                .getResourceAsStream(resultPath)));

        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(inputPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HackApp hackApp = injector.getInstance(HackApp.class);
        hackApp.actualRun(in, out);

        String actualResult = out.toString("utf-8");

        Assert.assertEquals("Output does not match", expectedResult, actualResult);
    }
}
