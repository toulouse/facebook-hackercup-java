package se.atoulou.facebook.hackercup.alphabetsoup;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class AlphabetSoupInjectModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HackApp.class).to(AlphabetSoupApp.class);
        bindConstant().annotatedWith(Names.named("InputPath")).to(
                "se/atoulou/facebook/hackercup/alphabetsoup/input.txt");
        bind(Marker.class).toInstance(MarkerFactory.getMarker("AlphabetSoup"));
    }
}
