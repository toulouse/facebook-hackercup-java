package se.atoulou.facebook.hackercup.auction;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class AuctionTestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HackApp.class).to(AuctionApp.class);
        bindConstant().annotatedWith(Names.named("InputPath")).to("se/atoulou/facebook/hackercup/auction/input.txt");
        bindConstant().annotatedWith(Names.named("ResultPath")).to("se/atoulou/facebook/hackercup/auction/result.txt");
        bind(Marker.class).toInstance(MarkerFactory.getMarker("AuctionTest"));
    }
}
