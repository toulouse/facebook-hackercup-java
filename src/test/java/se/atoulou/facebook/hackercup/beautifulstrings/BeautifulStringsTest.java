package se.atoulou.facebook.hackercup.beautifulstrings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.common.io.CharStreams;

public class BeautifulStringsTest {

    @Test
    public void testBeautifulStringsSampleInput() throws IOException {
        String inputPath = "se/atoulou/facebook/hackercup/beautifulstrings/input.txt";
        String resultPath = "se/atoulou/facebook/hackercup/beautifulstrings/result.txt";

        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(inputPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HackApp app = new BeautifulStringsApp();
        app.actualRun(in, out);

        String expectedResult = CharStreams.toString(new InputStreamReader(ClassLoader.getSystemClassLoader()
                .getResourceAsStream(resultPath)));
        String actualResult = out.toString("utf-8");

        Assert.assertEquals("Output does not match", expectedResult, actualResult);
    }
}
