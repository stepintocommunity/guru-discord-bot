package hu.stepintomeetups.welcome.action;

import hu.stepintomeetups.ContentProvider;
import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Action which welcomes new users on the server.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class WelcomeUserAction {

    private static final String MESSAGE_KEY = "welcome";
    private final BotConfiguration botConfiguration;
    private final ContentProvider contentProvider;

    /**
     * Welcomes the incoming user.
     */
    public void welcomeUser(User user) {
        sendWelcomeMessage(user);
    }

    /**
     * Welcome user based on {@link MessageCreateEvent} object.
     */
    public void welcomeUser(MessageCreateEvent event) {
        if (event.getMessageAuthor().isBotUser()) {
            return;
        }
        sendWelcomeMessage(event.getChannel());
    }

    private void sendWelcomeMessage(Messageable messageable) {
        EmbedBuilder embed = contentProvider.getByContentKey(MESSAGE_KEY);
        Map<String, BotConfiguration.SkillConfig> emojis = botConfiguration.skills();
        emojis.forEach((key, skillConfig) -> embed.addInlineField(skillConfig.name(), skillConfig.getMessagizedTag()));
        String[] unicodeEmojis = emojis.values().stream().map(BotConfiguration.SkillConfig::emoji).collect(Collectors.toList()).toArray(new String[]{});
        messageable.sendMessage(embed)
                .thenAccept(message -> message.addReactions(unicodeEmojis)
                        .exceptionally(throwable -> {
                            log.error("Error during adding emojis to message", throwable);
                            return null;
                        }))
                .exceptionally(throwable -> {
                    log.error("Error during sending message to the channel", throwable);
                    return null;
                });
    }
}
