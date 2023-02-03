package org.blockworldeditor.dagger;

import dagger.Component;

@Component(modules = { HelloWorldModule.class, SystemOutModule.class })
interface CommandRouterFactory {
    CommandRouter router();
}
