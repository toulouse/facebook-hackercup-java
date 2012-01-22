package se.atoulou.facebook.hackercup.alphabetsoup;

import java.io.OutputStream;

import com.google.inject.AbstractModule;

public class AlphabetInjectModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OutputStream.class).annotatedWith(StandardOutOutput.class).toInstance(System.out);
    }
}
