package hu.stepintomeetups.action;

import hu.stepintomeetups.ContentProvider;
import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Action which sends the all available skills to the users.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SkillsAction {

    private static final String MESSAGE_KEY = "skills";
    private final BotConfiguration botConfiguration;
    private final ContentProvider contentProvider;

    /**
     * Send knows skills/permissions to the incoming user.
     */
    public void sendKnownSkillsToUser(User user) {
        EmbedBuilder embed = contentProvider.getByContentKey(MESSAGE_KEY);
        Map<String, BotConfiguration.SkillConfig> emojis = botConfiguration.skills();
        emojis.forEach((key, skillConfig) -> embed.addInlineField(skillConfig.name(), skillConfig.getMessagizedTag()));
        user.sendMessage(embed)
                .exceptionally(throwable -> {
                    log.error("Error during sending message to user:[" + user + "]");
                    return null;
                });
    }
}
