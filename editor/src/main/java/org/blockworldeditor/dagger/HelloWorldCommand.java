package org.blockworldeditor.dagger;

import java.util.List;

import javax.inject.Inject;

public class HelloWorldCommand implements Command {

    private final Outputter outputter;

    @Inject
    HelloWorldCommand(Outputter outputter) {
        this.outputter = outputter;
    }

    @Override
    public String key() {
        return "hello";
    }

    @Override
    public Status handleInput(List<String> input) {
        outputter.output("world!");
        return Status.HANDLED;
    }

}
