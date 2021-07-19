package hu.stepintomeetups.skills.action;

import hu.stepintomeetups.ContentProvider;
import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Action which sends all the known commands.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class AvailableCommandsAction {

    private static final String MESSAGE_KEY = "commands";
    private final BotConfiguration botConfiguration;
    private final ContentProvider contentProvider;

    /**
     * Sends all supported commands to the user.
     */
    public void sendAllCommandsToUser(User user) {
        EmbedBuilder embedBuilder = contentProvider.getByContentKey(MESSAGE_KEY);
        botConfiguration.commands().forEach(botCommand -> embedBuilder.addField(botCommand.description(), botCommand.command()));
        user.sendMessage(embedBuilder);
    }

}
