package hu.stepintomeetups.commands;

import hu.stepintomeetups.configuration.BotConfiguration;
import org.javacord.api.event.message.CertainMessageEvent;

import javax.inject.Inject;

/**
 * Abstract class which needs to be extended when a new command is being created.
 */
public abstract class GuruBotCommand {

    @Inject
    BotConfiguration botConfiguration;

    /**
     * Returns the name/identifier of the command.
     */
    public abstract String getCommand();

    /**
     * Returns the command in a common prefixed manner.
     */
    protected String getPrefixedCommand() {
        String commandPrefix = botConfiguration.commandPrefix();
        return commandPrefix != null && !commandPrefix.isBlank() ? "/" + commandPrefix + "-" + getCommand() : "/" + getCommand();
    }

    /**
     * Handles the incoming event.
     */
    public abstract void handleMessage(CertainMessageEvent event);


}
