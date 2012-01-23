package se.atoulou.facebook.hackercup.auction;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class AuctionModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HackApp.class).to(AuctionApp.class);
        bindConstant().annotatedWith(Names.named("InputPath")).to("se/atoulou/facebook/hackercup/auction/input.txt");
        bind(Marker.class).toInstance(MarkerFactory.getMarker("Auction"));
    }
}
