package org.blockworldeditor.dagger;

import java.util.Scanner;

public class CommandLineAtm {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        CommandRouterFactory commandRouterFactory = DaggerCommandRouterFactory.create();
        CommandRouter commandRouter = commandRouterFactory.router();

        while (scanner.hasNextLine()) {
            commandRouter.route(scanner.nextLine());
        }
    }
}
