package hu.stepintomeetups.skills.listeners;

import hu.stepintomeetups.commands.GuruBotCommand;
import hu.stepintomeetups.configuration.BotConfiguration;
import hu.stepintomeetups.listeners.DiscordMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;

/**
 * Listener which listens to the 'commands', 'list-skills', 'i-know' and 'i-dont-know' commands.
 */
@Slf4j
@ApplicationScoped
@DiscordMessageListener
@RequiredArgsConstructor
public class SkillCenterListener implements MessageCreateListener {

    private final BotConfiguration botConfiguration;
    private final Instance<GuruBotCommand> guruBotCommands;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String messageContent = event.getMessageContent();
        if (!messageContent.startsWith("/")) {
            log.trace("Incoming message is not slash based, not checking against any of the available commands.");
            return;
        }
        String command = commandize(messageContent);
        guruBotCommands.stream()
                .filter(guruBotCommand -> guruBotCommand.getCommand().equalsIgnoreCase(command))
                .findFirst()
                .ifPresent(guruBotCommand -> guruBotCommand.handleMessage(event));
    }

    private String commandize(String content) {
        String commandPrefix = botConfiguration.commandPrefix();
        return commandPrefix != null && !commandPrefix.isBlank() && content.startsWith("/" + commandPrefix)
                ? content.substring(("/" + commandPrefix).length()).split(" ")[0]
                : content.substring("/".length()).split(" ")[0];
    }

}
