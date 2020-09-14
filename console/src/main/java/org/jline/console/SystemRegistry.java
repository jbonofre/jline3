/*
 * Copyright (c) 2002-2020, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.console;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jline.builtins.ConsoleOptionGetter;
import org.jline.reader.Completer;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;

/**
 * Aggregate command registries and dispatch command executions.
 *
 * @author <a href="mailto:matti.rintanikkola@gmail.com">Matti Rinta-Nikkola</a>
 */
public interface SystemRegistry extends CommandRegistry, ConsoleOptionGetter {

    /**
     * Set command registeries
     * @param commandRegistries command registeries used by the application
     */
    void setCommandRegistries(CommandRegistry... commandRegistries);

    /**
     * Register subcommand registry
     * @param command main command
     * @param subcommandRegistry subcommand registry
     */
    void register(String command, CommandRegistry subcommandRegistry);

    /**
     * Initialize consoleEngine environment by executing console script
     * @param script initialization script
     */
    void initialize(File script);

    /**
     *
     * @return pipe names defined in systemRegistry
     */
    Collection<String> getPipeNames();

    /**
     * Returns command completer that includes also console variable and script completion.
     * @return command completer
     */
    Completer completer();

    /**
     * Returns a command, method or syntax description for use in the JLine Widgets framework.
     * @param line command line whose description to return
     * @return command description for JLine TailTipWidgets to be displayed
     *         in the terminal status bar.
     */
    CmdDesc commandDescription(CmdLine line);

   /**
     * Execute a command, script or evaluate scriptEngine statement
     * @param line command line to be executed
     * @return execution result
     * @throws Exception in case of error
     */
    Object execute(String line) throws Exception;

    /**
     * Delete temporary console variables and reset output streams
     */
    void cleanUp();

    /**
     * Print exception on terminal
     * @param exception exception to print on terminal
     */
    void trace(Exception exception);

    /**
     * Print exception on terminal
     * @param stack print stack trace if stack true otherwise message
     * @param exception exception to be printed
     */
    void trace(boolean stack, Exception exception);

    /**
     * Return console option value
     * @param name the option name
     * @return option value
     */
    Object consoleOption(String name);

    /**
     * @return terminal
     */
    Terminal terminal();

    /**
     * Execute command with arguments
     * @param command command to be executed
     * @param args arguments of the command
     * @return command execution result
     * @throws Exception in case of error
     */
    Object invoke(String command, Object... args) throws Exception;

    /**
     * Returns whether a line contains command/script that is known to this registry.
     * @param line the parsed command line to test
     * @return true if the specified line has a command registered
     */
    boolean isCommandOrScript(ParsedLine line);

    /**
     * Orderly close SystemRegistry.
     */
    void close();
    /**
     * @return systemRegistry of the current thread
     */
    static SystemRegistry get() {
        return Registeries.getInstance().getSystemRegistry();
    }

    /**
     * Add systemRegistry to the thread map
     * @param systemRegistry the systemRegistry
     */
    static void add(SystemRegistry systemRegistry) {
        Registeries.getInstance().addRegistry(systemRegistry);
    }

    /**
     * Remove systemRegistry of the current thread from the thread map
     */
    static void remove() {
        Registeries.getInstance().removeRegistry();
    }

    /**
     * Manage systemRegistry store
     */
    class Registeries {
        private static final Registeries instance = new Registeries();
        private final Map<Long, SystemRegistry> systemRegisteries = new HashMap<>();

        private Registeries () {}

        protected static Registeries getInstance() {
            return instance;
        }

        protected void addRegistry(SystemRegistry systemRegistry) {
            systemRegisteries.put(Thread.currentThread().getId(), systemRegistry);
        }

        protected SystemRegistry getSystemRegistry() {
            return systemRegisteries.getOrDefault(Thread.currentThread().getId(), null);
        }

        protected void removeRegistry() {
            systemRegisteries.remove(Thread.currentThread().getId());
        }

    }

}
