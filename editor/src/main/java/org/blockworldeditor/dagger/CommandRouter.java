package org.blockworldeditor.dagger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.blockworldeditor.dagger.Command.Status;

import com.google.common.collect.Maps;

final public class CommandRouter {
    private final Map<String, Command> commands = Maps.newHashMap();

    @Inject
    CommandRouter(Command command) {
        commands.put(command.key(), command);
    }

    Status route(String input) {
        List<String> splitInput = Arrays.asList(input.split(" "));
        if (splitInput.isEmpty()) {
            return invalidCommand(input);
        }

        String commandKey = splitInput.get(0);
        Command command = commands.get(commandKey);
        if (command == null) {
            return invalidCommand(input);
        }

        Status status = command.handleInput(splitInput.subList(1, splitInput.size()));
        if (status == Status.INVALID) {
            System.out.println(commandKey + ": invalid arguments");
        }
        return status;
    }

    private Status invalidCommand(String input) {
        System.out.println(
                String.format("couldn't understand \"%s\". please try again.", input));
        return Status.INVALID;
    }
}
