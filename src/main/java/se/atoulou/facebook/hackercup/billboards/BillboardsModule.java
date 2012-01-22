package se.atoulou.facebook.hackercup.billboards;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import se.atoulou.facebook.hackercup.common.HackApp;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class BillboardsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HackApp.class).to(BillboardsApp.class);
        bindConstant().annotatedWith(Names.named("InputPath")).to("se/atoulou/facebook/hackercup/billboards/input.txt");
        bind(Marker.class).toInstance(MarkerFactory.getMarker("Billboards"));
    }
}
