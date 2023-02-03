package org.blockworldeditor.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SystemOutModule {
    @Provides
    static Outputter texOutputter() {
        return System.out::println;
    }
}
