package hu.stepintomeetups.action;

import com.vdurmont.emoji.EmojiParser;
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

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class WelcomeUserAction {

    private final DiscordApi discordApi;
    private final BotConfiguration botConfiguration;

    /**
     * @param user
     */
    public void welcomeUser(User user) {
        sendWelcomeMessage(user);
    }

    /**
     *
     */
    public void welcomeUser(MessageCreateEvent event) {
        if (event.getMessageAuthor().isBotUser()) {
            return;
        }
        sendWelcomeMessage(event.getChannel());
    }

    private void sendWelcomeMessage(Messageable messageable) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Üdvözlünk a Step Into Meetup szerverén")
                .setDescription("Ahhoz, hogy a maximumot tudd kihozni a szereveren eltöltött idődből, kérlek olvasd végig a következőket és teljesítsd az első kihívásodat.")
                .setImage("https://avatars.githubusercontent.com/u/43297388?s=200&v=4")
                .addField("Programozási nyelv ismeretek", "Kérlek az emoticonok segítségével válaszd ki az általad ismert vagy ismerni kívánt nyelveket, hogy felruházhassunk a hozzájuk tartozó szerepkörökkel")
                .setFooter("https://stepintomeetups.hu");
        Map<String, BotConfiguration.SkillConfig> emojis = botConfiguration.skills();
        emojis.forEach((key, skillConfig) -> embed.addInlineField(skillConfig.name(), skillConfig.getMessagizedTag()));
        String[] unicodeEmojis = emojis.values().stream().map(BotConfiguration.SkillConfig::emoji).collect(Collectors.toList()).toArray(new String[]{});
        messageable.sendMessage(embed)
                .thenAccept(message -> {
                    message.addReactions(unicodeEmojis)
                            .exceptionally(throwable -> {
                                log.error("Error during adding emojis to message", throwable);
                                return null;
                            });
                })
                .exceptionally(throwable -> {
                    log.error("Error during sending message to the channel");
                    return null;
                });
    }
}
